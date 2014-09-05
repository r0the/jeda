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
package ch.jeda.platform.java;

abstract class AudioPlayer implements Runnable {

    private final JavaAudioManagerImp audioManager;
    private final Object lock;
    private final Thread thread;
    private boolean paused;
    private boolean stopRequested;

    AudioPlayer(final JavaAudioManagerImp audioManager) {
        this.audioManager = audioManager;
        this.lock = new Object();
        this.thread = new Thread(this);
        this.thread.setPriority(Thread.MAX_PRIORITY);

        this.paused = false;
        this.stopRequested = false;
    }

    @Override
    public final void run() {
        this.playLoop();
        this.audioManager.playbackStopped();
    }

    boolean isPaused() {
        synchronized (this.lock) {
            return this.paused;
        }
    }

    void pause() {
        synchronized (this.lock) {
            this.paused = true;
        }
    }

    void resume() {
        synchronized (this.lock) {
            this.paused = false;
        }
    }

    final void start() {
        this.thread.start();
    }

    void stop() {
        synchronized (this.lock) {
            this.stopRequested = true;
        }
    }

    protected final boolean isStopRequested() {
        synchronized (this.lock) {
            return this.stopRequested;
        }
    }

    protected abstract void playLoop();
}
