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
package ch.jeda.platform.android;

import ch.jeda.PlaybackState;
import ch.jeda.platform.MusicImp;

final class AndroidMusicImp implements MusicImp {

    private final AudioManager audioManager;
    private final Object lock;
    private final String path;
    private PlaybackState playbackState;

    AndroidMusicImp(final AudioManager audioManager, final String path) {
        this.audioManager = audioManager;
        this.lock = new Object();
        this.path = path;
        this.playbackState = PlaybackState.STOPPED;
    }

    @Override
    public PlaybackState getPlaybackState() {
        synchronized (this.lock) {
            return this.playbackState;
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void pause() {
        this.audioManager.pauseMusic(this);
    }

    @Override
    public void play() {
        this.audioManager.playMusic(this);
    }

    @Override
    public void stop() {
        this.audioManager.stopMusic(this);

    }

    void setPlaybackState(final PlaybackState playbackState) {
        synchronized (this.lock) {
            this.playbackState = playbackState;
        }
    }

    String getPath() {
        return this.path;
    }
}
