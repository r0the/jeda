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

import ch.jeda.platform.BitmapImp;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

class JavaBitmapImp implements BitmapImp {

    private BufferedImage bitmap;
    private Graphics2D graphics;

    JavaBitmapImp(final BufferedImage bitmap) {
        this.bitmap = bitmap;
        this.graphics = bitmap.createGraphics();
    }

    JavaBitmapImp(final int width, final int height) {
        reset(width, height);
    }

    @Override
    public BitmapImp createCopy() {
        final ColorModel cm = bitmap.getColorModel();
        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        final WritableRaster raster = bitmap.copyData(null);
        return new JavaBitmapImp(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
    }

    @Override
    public void copyFrom(final BitmapImp source) {
        assert source instanceof JavaBitmapImp;

        ((JavaBitmapImp) source).bitmap.copyData(bitmap.getRaster());
    }

    @Override
    public void flipHorizontally() {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final BufferedImage source = bitmap;
        reset(width, height);
        graphics.drawImage(source, width, 0, 0, height, 0, 0, width, height, null);
    }

    @Override
    public void flipVertically() {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final BufferedImage source = bitmap;
        reset(width, height);
        graphics.drawImage(source, 0, height, width, 0, 0, 0, width, height, null);
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bitmap.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public void load(final String path) {
        bitmap = ResourceManager.loadImage(path);
    }

    @Override
    public void scale(float factor) {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final BufferedImage source = bitmap;
        final int newHeight = (int) (height * factor);
        final int newWidth = (int) (width * factor);
        reset(newWidth, newHeight);
        graphics.drawImage(source, 0, 0, newWidth, newHeight, null);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        bitmap.setRGB(x, y, color);
    }

    private void reset(final int width, final int height) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration();
        this.bitmap = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        this.graphics = bitmap.createGraphics();
    }

}
