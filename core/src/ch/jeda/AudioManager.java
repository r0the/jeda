/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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

final class AudioManager implements AudioManagerImp.Callback {

    private final AudioManagerImp imp;
    private final Object lock;
    private Music currentMusic;

    AudioManager(final AudioManagerImp imp) {
        lock = new Object();
        this.imp = imp;
        this.imp.setCallback(this);
    }

    @Override
    public void playbackStopped() {
        synchronized (lock) {
            currentMusic.setPlaybackState(PlaybackState.STOPPED);
            currentMusic = null;
        }
    }

    boolean isSoundAvailable(final Sound sound) {
        return imp.isSoundAvailable(sound.getPath());
    }

    void loadSound(final Sound sound) {
        imp.loadSound(sound.getPath());
    }

    void pauseMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case PLAYING:
                if (getCurrentMusic() == music) {
                    synchronized (lock) {
                        imp.pausePlayback();
                        currentMusic.setPlaybackState(PlaybackState.PAUSED);
                    }
                }
        }
    }

    void playMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case STOPPED:
                if (getCurrentMusic() == null) {
                    synchronized (lock) {
                        if (imp.startPlayback(music.getPath())) {
                            currentMusic = music;
                            currentMusic.setPlaybackState(PlaybackState.PLAYING);
                        }
                    }
                }
            case PAUSED:
                if (getCurrentMusic() == music) {
                    synchronized (lock) {
                        imp.resumePlayback();
                        currentMusic.setPlaybackState(PlaybackState.PLAYING);
                    }
                }
        }
    }

    void playSound(final Sound sound) {
        imp.playSound(sound.getPath());
    }

    void stopMusic(final Music music) {
        switch (music.getPlaybackState()) {
            case PLAYING:
            case PAUSED:
                if (getCurrentMusic() == music) {
                    imp.stopPlayback();
                }
        }
    }

    private Music getCurrentMusic() {
        synchronized (lock) {
            return currentMusic;
        }
    }
}
