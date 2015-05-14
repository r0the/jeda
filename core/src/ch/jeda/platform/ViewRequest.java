/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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

import ch.jeda.ui.ViewFeature;
import java.util.EnumSet;

/**
 * <b>Internal</b>. Do not use this class.
 */
public final class ViewRequest {

    private final EnumSet<ViewFeature> features;
    private final int height;
    private final Object lock;
    private final int width;
    private ViewImp result;

    public ViewRequest(final int width, final int height, final EnumSet<ViewFeature> features) {
        this.width = width;
        this.height = height;
        this.features = features;
        lock = new Object();
        result = null;
    }

    public ViewImp getResult() {
        return result;
    }

    public EnumSet<ViewFeature> getFeatures() {
        return features;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setResult(final ViewImp result) {
        synchronized (lock) {
            if (this.result == null) {
                this.result = result;
                lock.notify();
            }
        }
    }

    public void waitForResult() {
        synchronized (lock) {
            while (result == null) {
                try {
                    lock.wait();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }
}
