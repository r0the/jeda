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
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

class BitmapCanvas extends java.awt.Canvas {

    private final BufferedImage bitmap;
    private final Graphics2D graphics;

    BitmapCanvas(final int width, final int height) {
        final Dimension d = new Dimension(width, height);
        setPreferredSize(d);
        setSize(d);
        bitmap = createBufferedImage(width, height);
        graphics = bitmap.createGraphics();
    }

    @Override
    public void paint(final Graphics graphics) {
        if (graphics != null) {
            graphics.drawImage(bitmap, 0, 0, this);
        }
    }

    @Override
    public void repaint() {
        paint(getGraphics());
    }

    void putImage(final BufferedImage image) {
        graphics.drawImage(image, 0, 0, this);
    }

    private static BufferedImage createBufferedImage(final int width, final int height) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().
            createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }
}
