/*
 * Copyright (C) 2012 by Stefan Rothe
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

import ch.jeda.Size;
import ch.jeda.ui.Window;
import java.util.EnumSet;

public final class WindowRequest {

    private final Size size;
    private final EnumSet<Window.Feature> features;
    private final Object lock;
    private WindowImp result;

    public WindowRequest(Size size, EnumSet<Window.Feature> features) {
        this.size = size;
        this.features = features;
        this.lock = new Object();
    }

    public WindowImp getResult() {
        return this.result;
    }

    public EnumSet<Window.Feature> getFeatures() {
        return this.features;
    }

    public Size getSize() {
        return this.size;
    }

    public void setResult(WindowImp result) {
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
