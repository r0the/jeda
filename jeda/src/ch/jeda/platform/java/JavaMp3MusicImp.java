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

import ch.jeda.Log;
import ch.jeda.PlaybackState;
import ch.jeda.platform.MusicImp;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.mp3transform.Decoder;

public class JavaMp3MusicImp implements MusicImp, Runnable {

    private static final int STREAM_BUFFER_SIZE = 128 * 1024;
    private final Object lock;
    private final String path;
    private Decoder decoder;
    private Thread thread;

    public JavaMp3MusicImp(final String path) {
        this.lock = new Object();
        this.path = path;
        this.thread = null;
    }

    @Override
    public PlaybackState getPlaybackState() {
        synchronized (this.lock) {
            if (this.thread == null) {
                return PlaybackState.STOPPED;
            }
            else if (this.decoder.isPaused()) {
                return PlaybackState.PAUSED;
            }
            else {
                return PlaybackState.PLAYING;
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void pause() {
        switch (this.getPlaybackState()) {
            case PLAYING:
                synchronized (this.lock) {
                    this.decoder.setPaused(true);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void play() {
        switch (this.getPlaybackState()) {
            case PAUSED:
                synchronized (this.lock) {
                    this.decoder.setPaused(false);
                }

                break;
            case STOPPED:
                synchronized (this.lock) {
                    this.decoder = new Decoder();
                    this.thread = new Thread(this);
                    this.thread.setPriority(Thread.MAX_PRIORITY);
                    this.thread.start();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        try {
            final BufferedInputStream bin = new BufferedInputStream(ResourceManager.openInputStream(this.path), STREAM_BUFFER_SIZE);
            this.decoder.play(this.path, bin);
            synchronized (this.lock) {
                this.decoder = null;
                this.thread = null;
            }
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.audio.error.read", this.path);
        }
    }

    @Override
    public void stop() {
        switch (this.getPlaybackState()) {
            case PAUSED:
            case PLAYING:
                synchronized (this.lock) {
                    this.decoder.requestStop();
                }

                break;
            default:
                break;
        }
    }
}
