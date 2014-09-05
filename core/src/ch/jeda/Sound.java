/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
package ch.jeda;

/**
 * This class represents short sampled sound data. The sound data can be retrieved from a file or a resource.
 *
 * <p>
 * <img src="../../windows.png"> <img src="../../linux.png"> Supported audio formats are:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Audio_Interchange_File_Format" target="_blank">AIFF</a>
 * <li><a href="http://en.wikipedia.org/wiki/Au_file_format" target="_blank">AU</a>
 * <li><a href="http://en.wikipedia.org/wiki/Ogg" target="_blank">OGG</a> (Codec: Vorbis)
 * <li><a href="http://en.wikipedia.org/wiki/Wav" target="_blank">WAV</a> (Codec: PCM)
 * </ul>
 * <p>
 * <img src="../../android.png"> Supported audio formats are:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Mp3" target="_blank">MP3</a> (Codec: MP3)
 * <li><a href="http://en.wikipedia.org/wiki/Mp4" target="_blank">MP4</a> (Codecs: AAC LC, HE-AACv1, HE-AACv2)
 * <li><a href="http://en.wikipedia.org/wiki/Ogg" target="_blank">OGG</a> (Codec: Vorbis)
 * <li><a href="http://en.wikipedia.org/wiki/Wav" target="_blank">WAV</a> (Codec: PCM)
 * </ul>
 *
 * @since 1.0
 */
public final class Sound {

    private final String path;

    /**
     * Constructs a new Sound object representing the contents of the specified audio file.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param path path to the audio file
     *
     * @since 1.0
     */
    public Sound(final String path) {
        this.path = path;
        Jeda.getAudioManager().loadSound(this);
    }

    /**
     * Checks if the sound is available. Returns <tt>true</tt> if the sound is available to be played. Returns
     * <tt>false</tt> if the sound is not available (e.g. because the specified file is not present or has the wrong
     * format.
     *
     * @return <tt>true</tt> if the sound is available, otherwise <tt>false</tt>
     *
     * @since 1.0
     */
    public boolean isAvailable() {
        return Jeda.getAudioManager().isSoundAvailable(this);
    }

    /**
     * Starts the playback of the sound. Multiple playbacks of the same sound can be started simultaneously.
     *
     * @since 1.0
     */
    public void play() {
        Jeda.getAudioManager().playSound(this);
    }

    String getPath() {
        return this.path;
    }
}
