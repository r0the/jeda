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
package ch.jeda.platform.android;

import android.media.MediaPlayer;
import ch.jeda.platform.MusicImp;

public class AndroidMusicImp implements MusicImp {

    private final String path;
    private final MediaPlayer mediaPlayer;

    public AndroidMusicImp(final String path, final MediaPlayer mediaPlayer) {
        this.path = path;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void pause() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        }
    }

    @Override
    public void play() {
        if (!this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.start();
        }
    }

    @Override
    public void stop() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
    }
}
