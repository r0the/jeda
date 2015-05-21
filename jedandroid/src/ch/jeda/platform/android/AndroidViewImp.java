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

import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.ViewFeature;
import java.util.EnumSet;

class AndroidViewImp implements ViewImp {

    private final AndroidCanvasImp background;
    private final AndroidCanvasImp foreground;
    private final int height;
    private final int width;
    private final SurfaceFragment surfaceFragment;

    AndroidViewImp(final SurfaceFragment canvasView, final int width, final int height) {
        this.height = height;
        this.width = width;
        background = new AndroidCanvasImp();
        background.init(width, height);
        foreground = new AndroidCanvasImp();
        foreground.init(width, height);
        surfaceFragment = canvasView;
    }

    @Override
    public void close() {
    }

    @Override
    public CanvasImp getBackground() {
        return background;
    }

    @Override
    public EnumSet<ViewFeature> getFeatures() {
        return surfaceFragment.getFeatures();
    }

    @Override
    public CanvasImp getForeground() {
        return foreground;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean isVisible() {
        return surfaceFragment.isVisible();
    }

    @Override
    public void setFeature(final ViewFeature feature, final boolean enabled) {
        surfaceFragment.setFeature(feature, enabled);
    }

    @Override
    public void setMouseCursor(final MouseCursor mouseCursor) {
        // ignore
    }

    @Override
    public void setTitle(final String title) {
        surfaceFragment.setTitle(title);
    }

    @Override
    public void update() {
        surfaceFragment.setBitmap(foreground.getBitmap());
    }
}
