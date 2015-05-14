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
package ch.jeda.platform.java;

abstract class AudioPlayer implements Runnable {

    private final JavaAudioManagerImp audioManager;
    private final Object lock;
    private final Thread thread;
    private boolean paused;
    private boolean stopRequested;

    AudioPlayer(final JavaAudioManagerImp audioManager) {
        this.audioManager = audioManager;
        lock = new Object();
        thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);

        paused = false;
        stopRequested = false;
    }

    @Override
    public final void run() {
        playLoop();
        audioManager.playbackStopped();
    }

    boolean isPaused() {
        synchronized (lock) {
            return paused;
        }
    }

    void pause() {
        synchronized (lock) {
            paused = true;
        }
    }

    void resume() {
        synchronized (lock) {
            paused = false;
        }
    }

    final void start() {
        thread.start();
    }

    void stop() {
        synchronized (lock) {
            stopRequested = true;
        }
    }

    protected final boolean isStopRequested() {
        synchronized (lock) {
            return stopRequested;
        }
    }

    protected abstract void playLoop();
}
