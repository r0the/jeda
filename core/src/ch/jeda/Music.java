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

import ch.jeda.platform.MusicImp;

/**
 * This class represents music data. The music data can be loaded from a file or a resource. The music can be played
 * back. Only one music can be played at a time.
 *
 * <p>
 * <img src="../../windows.png"> <img src="../../linux.png"> Supported audio formats are:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Mp3" target="_blank">MP3</a> (Codec: MP3)
 * </ul>
 * <p>
 * <img src="../../android.png"> Supported audio formats are:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Mp3" target="_blank">MP3</a> (Codec: MP3)
 * <li><a href="http://en.wikipedia.org/wiki/Mp4" target="_blank">MP4</a> (Codecs: AAC LC, HE-AACv1, HE-AACv2)
 * <li><a href="http://en.wikipedia.org/wiki/Ogg" target="_blank">Ogg</a> (Codec: Vorbis)
 * <li><a href="http://en.wikipedia.org/wiki/Wav" target="_blank">WAV</a> (Codec: PCM)
 * </ul>
 *
 * @since 1
 */
public final class Music {

    private final MusicImp imp;

    /**
     * Constructs a new music object representing the contents of the specified audio file.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param path path to the audio file
     *
     * @since 1
     */
    public Music(final String path) {
        this.imp = Jeda.createMusicImp(path);
    }

    /**
     * Returns the current playback state of this Music object.
     *
     * @return the current playback state of this Music object.
     *
     * @since 1
     */
    public PlaybackState getPlaybackState() {
        return this.imp.getPlaybackState();
    }

    /**
     * Checks if the music is available. Returns <tt>true</tt> if the music is available to be played. Returns
     * <tt>false</tt> if the music is not available (e.g. because the specified file is not present or has the wrong
     * format.
     *
     * @return <tt>true</tt> if the music is available, otherwise <tt>false</tt>
     *
     * @since 1
     */
    public boolean isAvailable() {
        return this.imp.isAvailable();
    }

    /**
     * Pauses the playback of the music. Has not effect if the music is not playing.
     *
     * @since 1
     */
    public void pause() {
        this.imp.pause();
    }

    /**
     * Starts or resumes the playback of the music. Has no effect if another music is already playing.
     *
     * @since 1
     */
    public void play() {
        this.imp.play();
    }

    /**
     * Stops the playback of the music. Has no effect if the music is not playing or paused.
     *
     * @since 1
     */
    public void stop() {
        this.imp.stop();
    }
}
