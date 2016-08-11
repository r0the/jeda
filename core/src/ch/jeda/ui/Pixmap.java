/*
 * Copyright (C) 2016 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.JedaInternal;
import ch.jeda.platform.CanvasImp;

public class Pixmap {

    private final CanvasImp imp;

    public Pixmap(int width, int height) {
        imp = JedaInternal.createCanvasImp(width, height);
    }

    public int getHeight() {
        return imp.getHeight();
    }

    public Color getPixel(int x, int y) {
        return imp.getPixel(x, y);
    }

    public int getWidth() {
        return imp.getWidth();
    }

    public void setPixel(int x, int y, Color color) {
        imp.setPixel(x, y, color);
    }

    public Image toImage() {
        return new Image(imp.takeSnapshot(0, 0, getWidth(), getHeight()));
    }
}
