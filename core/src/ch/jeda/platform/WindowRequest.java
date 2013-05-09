/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.platform;

import ch.jeda.ui.Window;
import java.util.EnumSet;

/**
 * <b>Internal</b>. Do not use this class.
 */
public final class WindowRequest {

    private final EnumSet<Window.Feature> features;
    private final int height;
    private final Object lock;
    private final int width;
    private WindowImp result;

    public WindowRequest(final int width, final int height,
                         final EnumSet<Window.Feature> features) {
        this.width = width;
        this.height = height;
        this.features = features;
        this.lock = new Object();
    }

    public WindowImp getResult() {
        return this.result;
    }

    public EnumSet<Window.Feature> getFeatures() {
        return this.features;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setResult(final WindowImp result) {
        synchronized (this.lock) {
            this.result = result;
            this.lock.notify();
        }
    }

    public void waitForResult() {
        synchronized (this.lock) {
            while (this.result == null) {
                try {
                    this.lock.wait();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }
}
