/*
 * Copyright (C) 2014 by Stefan Rothe
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

import ch.jeda.platform.AudioManagerImp;

class AudioManager implements AudioManagerImp.Callback {

    private final AudioManagerImp imp;
    private final Object lock;
    private Music currentMusic;

    AudioManager(final AudioManagerImp imp) {
        this.imp = imp;
        this.lock = new Object();
        this.imp.setCallback(this);
    }

    @Override
    public void playbackStopped() {
        synchronized (this.lock) {
            this.currentMusic.setPlaybackState(PlaybackState.STOPPED);
            this.currentMusic = null;
        }
    }

    void pauseMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case PLAYING:
                if (this.getCurrentMusic() == music) {
                    synchronized (this.lock) {
                        this.imp.pausePlayback();
                        this.currentMusic.setPlaybackState(PlaybackState.PAUSED);
                    }
                }
        }
    }

    void playMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case STOPPED:
                if (this.getCurrentMusic() == null) {
                    synchronized (this.lock) {
                        if (this.imp.startPlayback(music.getPath())) {
                            this.currentMusic = music;
                            this.currentMusic.setPlaybackState(PlaybackState.PLAYING);
                        }
                    }
                }
            case PAUSED:
                if (this.getCurrentMusic() == music) {
                    synchronized (this.lock) {
                        this.imp.resumePlayback();
                        this.currentMusic.setPlaybackState(PlaybackState.PLAYING);
                    }
                }
        }
    }

    void stopMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case PLAYING:
            case PAUSED:
                if (this.getCurrentMusic() == music) {
                    this.imp.stopPlayback();
                }
        }
    }

    private Music getCurrentMusic() {
        synchronized (this.lock) {
            return this.currentMusic;
        }
    }
}
