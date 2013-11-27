/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
import ch.jeda.platform.MusicImp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioManager implements TickListener {

    private final Mixer mixer;
    private final List<AudioPlayback> pendingDeletions;
    private final List<AudioPlayback> pendingInsertions;
    private final List<AudioPlayback> playing;
    private final List<AudioFormat> supportedFormats;

    AudioManager() {
        this.mixer = findMixer();
        this.pendingDeletions = new ArrayList<AudioPlayback>();
        this.pendingInsertions = new ArrayList<AudioPlayback>();
        this.playing = new ArrayList<AudioPlayback>();
        this.supportedFormats = new ArrayList<AudioFormat>();
        Line.Info[] lineInfos = this.mixer.getSourceLineInfo();
        for (Line.Info lineInfo : lineInfos) {
            if (lineInfo instanceof SourceDataLine.Info) {
                this.supportedFormats.addAll(Arrays.asList(((DataLine.Info) lineInfo).getFormats()));
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

    MusicImp createMusicImp(final String path) {
        if (path != null & path.toLowerCase().endsWith(".mid")) {
            try {
                return new JavaMidiMusicImp(path, MidiSystem.getSequencer());
            }
            catch (final MidiUnavailableException ex) {
                Log.err("jeda.audio.error.midi-unavailable", path);
                return null;
            }
        }
        else {
            Log.err("jeda.audio.error.unsupported-file-format", path);
            return null;
        }
    }

    SoundImp createSoundImp(final String path) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            final AudioInputStream in = javax.sound.sampled.AudioSystem.getAudioInputStream(ResourceManager.openInputStream(path));
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

    AudioPlayback playSound(final AudioInputStream audioStream) {
        if (audioStream == null) {
            throw new NullPointerException("audioStream");
        }

        final AudioFormat fileFormat = audioStream.getFormat();
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
            line.open(audioStream.getFormat());
        }
        catch (final LineUnavailableException ex) {
            Log.err(ex, "jeda.audio.error.line-unavailable");
            return null;
        }

        final AudioPlayback playback = new AudioPlayback(audioStream, line);
        synchronized (this.pendingInsertions) {
            this.pendingInsertions.add(playback);
        }

        return playback;
    }

    private static Mixer findMixer() {
        final Mixer.Info[] mixerInfos = javax.sound.sampled.AudioSystem.getMixerInfo();
        for (final Mixer.Info mixerInfo : mixerInfos) {
            final Mixer result = javax.sound.sampled.AudioSystem.getMixer(mixerInfo);
            if (result.getSourceLineInfo().length > 0) {
                return result;
            }
        }

        return null;
    }
}
