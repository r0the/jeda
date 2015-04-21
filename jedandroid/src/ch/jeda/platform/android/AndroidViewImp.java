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
package ch.jeda.platform.android;

import ch.jeda.event.EventQueue;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.ViewFeature;
import ch.jeda.ui.WindowFeature;
import java.util.EnumSet;

class AndroidViewImp extends AndroidCanvasImp implements ViewImp {

    private final CanvasFragment canvasView;

    @Override
    public void close() {
    }

    @Override
    public EnumSet<ViewFeature> getFeatures() {
        return this.canvasView.getFeatures();
    }

    @Override
    public boolean isVisible() {
        return this.canvasView.isVisible();
    }

    @Override
    public void setEventQueue(final EventQueue eventQueue) {
        this.canvasView.setEventQueue(eventQueue);
    }

    @Override
    public void setFeature(final ViewFeature feature, final boolean enabled) {
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

    static AndroidViewImp create(final CanvasFragment canvasView, final int width, final int height) {
        return new AndroidViewImp(canvasView, width, height);
    }

    private AndroidViewImp(final CanvasFragment canvasView, final int width, final int height) {
        this.canvasView = canvasView;
        this.init(width, height);
    }
}
