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
package ch.jeda.platform.android;

import ch.jeda.platform.Event;
import ch.jeda.platform.InputDeviceImp;
import ch.jeda.platform.WindowImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.Window;
import ch.jeda.ui.Window.Feature;
import java.util.ArrayList;
import java.util.EnumSet;

class AndroidWindowImp extends AndroidCanvasImp implements WindowImp {

    private final CanvasView canvasView;

    @Override
    public void close() {
    }

    @Override
    public Iterable<InputDeviceImp> detectInputDevices() {
        return new ArrayList();
    }

    @Override
    public EnumSet<Window.Feature> getFeatures() {
        return this.canvasView.getFeatures();
    }

    @Override
    public void setFeature(Feature feature, boolean enabled) {
        this.canvasView.setFeature(feature, enabled);
    }

    @Override
    public void setMouseCursor(MouseCursor mouseCursor) {
        // ignore
    }

    @Override
    public void setTitle(final String title) {
        this.canvasView.setTitle(title);
    }

    @Override
    public Iterable<Event> update() {
        this.canvasView.setBitmap(this.getBitmap());
        return this.canvasView.fetchEvents();
    }

    static AndroidWindowImp create(CanvasView canvasView) {
        if (canvasView.getFeatures().contains(Window.Feature.DoubleBuffered)) {
            return new AndroidWindowImp(canvasView);
        }
        else {
            return new SingleBufferedAndroidViewImp(canvasView);
        }
    }

    private AndroidWindowImp(CanvasView canvasView) {
        this.canvasView = canvasView;
        this.setSize(this.canvasView.getSize());
    }

    private static class SingleBufferedAndroidViewImp extends AndroidWindowImp {

        SingleBufferedAndroidViewImp(CanvasView displayView) {
            super(displayView);
        }

        @Override
        void modified() {
            this.update();
        }
    }
}
