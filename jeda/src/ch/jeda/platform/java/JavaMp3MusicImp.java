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
    public void pause() {
        synchronized (this.lock) {
            if (this.thread != null) {
                this.decoder.pause();
            }
        }
    }

    @Override
    public void play() {
        synchronized (this.lock) {
            if (this.thread == null) {
                this.decoder = new Decoder();
                this.thread = new Thread(this);
                this.thread.setPriority(Thread.MAX_PRIORITY);
                this.thread.start();
            }
            else {
                this.decoder.pause();
            }
        }
    }

    @Override
    public void run() {
        try {
            BufferedInputStream bin = new BufferedInputStream(ResourceManager.openInputStream(this.path), STREAM_BUFFER_SIZE);
            this.decoder.play(this.path, bin);
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.audio.error.read", this.path);
        }
    }

    @Override
    public void stop() {
        synchronized (this.lock) {
            if (this.thread != null) {
                this.decoder.stop();
                try {
                    this.thread.join();
                }
                catch (final InterruptedException ex) {
                    // ignore
                }

                this.decoder = null;
                this.thread = null;
            }
        }
    }
}
