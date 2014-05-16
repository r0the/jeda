/*
 * Copyright (C) 2011 - 2014 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.platform.java;

import ch.jeda.Log;
import ch.jeda.platform.SoundImp;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.platform.AudioManagerImp;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioManager implements AudioManagerImp, TickListener {

    private final Mixer mixer;
    private final List<AudioPlayback> pendingDeletions;
    private final List<AudioPlayback> pendingInsertions;
    private final List<AudioPlayback> playing;
    private final List<AudioFormat> supportedFormats;
    private Callback callback;
    private AudioPlayer musicPlayer;

    AudioManager() {
        this.mixer = findMixer();
        this.pendingDeletions = new ArrayList<AudioPlayback>();
        this.pendingInsertions = new ArrayList<AudioPlayback>();
        this.playing = new ArrayList<AudioPlayback>();
        this.supportedFormats = new ArrayList<AudioFormat>();
        final Line.Info[] lineInfos = this.mixer.getSourceLineInfo();
        for (int i = 0; i < lineInfos.length; ++i) {
            if (lineInfos[i] instanceof SourceDataLine.Info) {
                this.supportedFormats.addAll(Arrays.asList(((SourceDataLine.Info) lineInfos[i]).getFormats()));
            }
        }
    }

    @Override
    public void onTick(final TickEvent event) {
        synchronized (this.pendingInsertions) {
            this.playing.addAll(this.pendingInsertions);
            this.pendingInsertions.clear();
        }

        for (int i = 0; i < this.playing.size(); ++i) {
            final AudioPlayback playback = this.playing.get(i);
            playback.step();
            if (playback.hasFinished()) {
                playback.stop();
                this.pendingDeletions.add(playback);
            }
        }

        this.playing.removeAll(this.pendingDeletions);
        this.pendingDeletions.clear();
    }

    SoundImp createSoundImp(final String path) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            final AudioInputStream in = javax.sound.sampled.AudioSystem.getAudioInputStream(
                new BufferedInputStream(ResourceManager.openInputStream(path)));
            javax.sound.sampled.AudioSystem.write(in, AudioFileFormat.Type.WAVE, buffer);
            return new JavaSoundImp(this, buffer.toByteArray());
        }
        catch (final UnsupportedAudioFileException ex) {
            Log.err("jeda.audio.error.unsupported-file-format", path);
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.audio.error.read", path);
        }

        return null;
    }

    boolean isSupported(final AudioFormat audioFormat) {
        for (final AudioFormat format : this.supportedFormats) {
            if (audioFormat.matches(format)) {
                return true;
            }
        }

        return false;
    }

    AudioPlayback playSound(final AudioDataSource source) {
        if (source == null) {
            throw new NullPointerException("audioData");
        }

        final AudioFormat fileFormat = source.getFormat();
        if (!isSupported(fileFormat)) {
            Log.err("jeda.audio.error.unsupported-format", fileFormat);
            return null;
        }

        final DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, fileFormat,
                                                      javax.sound.sampled.AudioSystem.NOT_SPECIFIED);
        SourceDataLine line;
        try {
            line = (SourceDataLine) this.mixer.getLine(dinfo);
        }
        catch (final LineUnavailableException ex) {
            try {
                line = (SourceDataLine) javax.sound.sampled.AudioSystem.getLine(dinfo);
            }
            catch (final LineUnavailableException ex2) {
                Log.err(ex2, "jeda.audio.error.line-unavailable");
                return null;
            }
        }

        try {
            line.open(source.getFormat());
        }
        catch (final LineUnavailableException ex) {
            Log.err(ex, "jeda.audio.error.line-unavailable");
            return null;
        }

        final AudioPlayback playback = new AudioPlayback(source, line);
        synchronized (this.pendingInsertions) {
            this.pendingInsertions.add(playback);
        }

        return playback;
    }

    private static Mixer findMixer() {
        final Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfos.length; ++i) {
            final Mixer result = AudioSystem.getMixer(mixerInfos[i]);
            if (result.getSourceLineInfo().length > 0) {
                return result;
            }
        }

        return null;
    }

    @Override
    public void pausePlayback() {
        if (this.musicPlayer != null) {
            this.musicPlayer.pause();
        }
    }

    @Override
    public void resumePlayback() {
        if (this.musicPlayer != null) {
            this.musicPlayer.resume();
        }
    }

    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean startPlayback(final String path) {
        if (this.musicPlayer != null) {
            return false;
        }

        this.musicPlayer = this.createAudioPlayer(path);
        if (this.musicPlayer == null) {
            return false;
        }
        else {
            this.musicPlayer.start();
            return true;
        }
    }

    @Override
    public void stopPlayback() {
        this.musicPlayer.stop();
        this.musicPlayer = null;
    }

    void playbackStopped() {
        this.musicPlayer = null;
        this.callback.playbackStopped();
    }

    private AudioPlayer createAudioPlayer(final String path) {
        if (path.endsWith(".mp3")) {
            return new Mp3AudioPlayer(this, path);
        }
        else {
            return null;
        }
    }
}
