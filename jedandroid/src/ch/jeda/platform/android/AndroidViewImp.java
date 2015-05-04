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
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.ViewFeature;
import java.util.EnumSet;

class AndroidViewImp implements ViewImp {

    private final SurfaceFragment surfaceFragment;
    private final AndroidCanvasImp canvasImp;

    AndroidViewImp(final SurfaceFragment canvasView, final int width, final int height) {
        this.canvasImp = new AndroidCanvasImp();
        this.canvasImp.init(width, height);
        this.surfaceFragment = canvasView;
    }

    @Override
    public void close() {
    }

    public CanvasImp getCanvas() {
        return this.canvasImp;
    }

    @Override
    public EnumSet<ViewFeature> getFeatures() {
        return this.surfaceFragment.getFeatures();
    }

    @Override
    public boolean isVisible() {
        return this.surfaceFragment.isVisible();
    }

    @Override
    public void setEventQueue(final EventQueue eventQueue) {
        this.surfaceFragment.setEventQueue(eventQueue);
    }

    @Override
    public void setFeature(final ViewFeature feature, final boolean enabled) {
        this.surfaceFragment.setFeature(feature, enabled);
    }

    @Override
    public void setMouseCursor(final MouseCursor mouseCursor) {
        // ignore
    }

    @Override
    public void setTitle(final String title) {
        this.surfaceFragment.setTitle(title);
    }

    @Override
    public void update() {
        this.surfaceFragment.setBitmap(this.canvasImp.getBitmap());
    }
}
