/*
 * Copyright (C) 2015 by Stefan Rothe
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class BitmapCanvas extends java.awt.Canvas {

    private final JavaCanvasImp canvasImp;

    BitmapCanvas(final int width, final int height) {
        final Dimension d = new Dimension(width, height);
        this.setPreferredSize(d);
        this.setSize(d);
        this.canvasImp = new JavaCanvasImp(width, height);
    }

    @Override
    public void paint(final Graphics graphics) {
        if (graphics != null) {
            graphics.drawImage(this.canvasImp.getBitmap(), 0, 0, null);
        }
    }

    @Override
    public void repaint() {
        this.paint(this.getGraphics());
    }

    JavaCanvasImp getCanvasImp() {
        return this.canvasImp;
    }

    void setImage(final BufferedImage buffer) {
        buffer.copyData(this.canvasImp.getBitmap().getRaster());
    }
}
