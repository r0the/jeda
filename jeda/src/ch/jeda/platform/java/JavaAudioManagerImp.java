/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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

import ch.jeda.JedaInternal;
import ch.jeda.Log;
import ch.jeda.platform.AudioManagerImp;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class JavaAudioManagerImp implements AudioManagerImp, LineListener {

    private Callback callback;
    private AudioPlayer musicPlayer;
    private final Map<String, SoundData> soundMap;
    private final List<Clip> soundStreams;

    JavaAudioManagerImp() {
        soundMap = new HashMap<String, SoundData>();
        soundStreams = new ArrayList<Clip>();
    }

    @Override
    public boolean isSoundAvailable(final String path) {
        return soundMap.containsKey(path);
    }

    @Override
    public void loadSound(final String path) {
        if (soundMap.containsKey(path)) {
            return;
        }

        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            final AudioInputStream in = AudioSystem.getAudioInputStream(
                new BufferedInputStream(JedaInternal.openResource(path)));
            AudioSystem.write(in, AudioFileFormat.Type.WAVE, buffer);
            soundMap.put(path, new SoundData(buffer.toByteArray()));
        }
        catch (final UnsupportedAudioFileException ex) {
            Log.e("Detected unsupported audio format in file '", path, "'.");
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading audio file '", path, "'.");
        }
    }

    @Override
    public void playSound(final String path) {
        final SoundData soundData = soundMap.get(path);
        if (soundData == null) {
            return;
        }

        try {
            final AudioInputStream in = AudioSystem.getAudioInputStream(soundData.openStream());
            final AudioFormat format = in.getFormat();
            final DataLine.Info info = new DataLine.Info(Clip.class, format);
            final Clip clip = startClip(info, in);
            if (clip != null) {
                soundStreams.add(clip);
            }
        }
        catch (final UnsupportedAudioFileException ex) {
            Log.e("Detected unsupported audio format in file '", path, "'.");
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading audio file '", path, "'.");
        }
    }

    @Override
    public void pausePlayback() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    @Override
    public void resumePlayback() {
        if (musicPlayer != null) {
            musicPlayer.resume();
        }
    }

    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean startPlayback(final String path) {
        if (musicPlayer != null) {
            return false;
        }

        musicPlayer = createAudioPlayer(path);
        if (musicPlayer == null) {
            return false;
        }
        else {
            musicPlayer.start();
            return true;
        }
    }

    @Override
    public void stopPlayback() {
        musicPlayer.stop();
        musicPlayer = null;
    }

    @Override
    public void update(final LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    event.getLine().close();
                }
            }).start();
            // TODO: Fix
            soundStreams.remove(event.getLine());
        }
    }

    void playbackStopped() {
        musicPlayer = null;
        callback.playbackStopped();
    }

    private AudioPlayer createAudioPlayer(final String path) {
        if (path.endsWith(".mp3")) {
            return new Mp3AudioPlayer(this, path);
        }
        else {
            return null;
        }
    }

    private Clip startClip(final DataLine.Info info, final AudioInputStream in) throws IOException {
        int tries = 0;
        Clip result = null;
        do {
            ++tries;
            try {
                result = (Clip) AudioSystem.getLine(info);
                result.addLineListener(this);
                result.open(in);
                result.start();
            }
            catch (final LineUnavailableException ex) {
                // close oldest clip
                if (!soundStreams.isEmpty()) {
                    final Clip clip = soundStreams.get(0);
                    soundStreams.remove(0);
                    clip.stop();
                    clip.close();
                }
            }
        }
        while (result == null && tries < 10);

        return result;
    }

}
