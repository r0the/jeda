/*
 * Copyright (C) 2013 by Stefan Rothe
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

import ch.jeda.platform.SoundImp;

/**
 * This class represents short sampled sound data. The sound data can be retrieved from a file or a resource. Supported
 * audio formats are:
 * <ul>
 * <li><a href="http://de.wikipedia.org/wiki/AIFF">AIFF</a>
 * <li><a href="http://de.wikipedia.org/wiki/Au_%28Dateiformat%29">AU</a>
 * <li><a href="http://de.wikipedia.org/wiki/RIFF_WAVE">WAV</a>
 * </ul>
 *
 * @since 1
 */
public class Sound {

    private final SoundImp imp;

    /**
     * Constructs a new Sound object representing the contents of the specified sound file.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param path path to the sound file
     *
     * @since 1
     */
    public Sound(final String path) {
        this.imp = Jeda.createSoundImp(path);
    }

    /**
     * Starts the playback of the sound. Multiple playbacks of the same sound can be started simultaneously.
     *
     * @since 1
     */
    public void play() {
        this.imp.play();
    }
}
