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
package ch.jeda.platform.android;

import ch.jeda.platform.WindowImp;
import ch.jeda.ui.Event;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.Window;
import ch.jeda.ui.Window.Feature;
import java.util.EnumSet;

class AndroidWindowImp extends AndroidCanvasImp implements WindowImp {

    private final CanvasView canvasView;

    @Override
    public void close() {
    }

    @Override
    public Event[] fetchEvents() {
        return this.canvasView.fetchEvents();
    }

    @Override
    public EnumSet<Window.Feature> getFeatures() {
        return this.canvasView.getFeatures();
    }

    @Override
    public void setFeature(final Feature feature, final boolean enabled) {
        this.canvasView.setFeature(feature, enabled);
    }

    @Override
    public void setMouseCursor(final MouseCursor mouseCursor) {
        // ignore
    }

    @Override
    public void setTitle(final String title) {
        this.canvasView.setTitle(title);
    }

    @Override
    public void update() {
        this.canvasView.setBitmap(this.getBitmap());
    }

    static AndroidWindowImp create(final CanvasView canvasView, final int width, final int height) {
        if (canvasView.getFeatures().contains(Window.Feature.DoubleBuffered)) {
            return new AndroidWindowImp(canvasView, width, height);
        }
        else {
            return new SingleBufferedAndroidViewImp(canvasView, width, height);
        }
    }

    private AndroidWindowImp(final CanvasView canvasView, final int width, final int height) {
        this.canvasView = canvasView;
        this.init(width, height);
    }

    private static class SingleBufferedAndroidViewImp extends AndroidWindowImp {

        SingleBufferedAndroidViewImp(final CanvasView displayView, final int width, final int height) {
            super(displayView, width, height);
        }

        @Override
        void modified() {
            this.update();
        }
    }
}
