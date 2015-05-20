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
package ch.jeda.ui;

import ch.jeda.JedaInternal;
import ch.jeda.platform.BitmapImp;
import java.util.Stack;

/**
 * Represents a raster graphics image.
 *
 * @since 2.0
 */
public final class Bitmap {

    private final BitmapImp imp;

    public Bitmap(final Bitmap source) {
        this.imp = source.imp.createCopy();
    }

    public Bitmap(final String path) {
        this.imp = JedaInternal.createBitmapImp(0, 0);
        imp.load(path);
    }

    public Bitmap(final int width, final int height) {
        this.imp = JedaInternal.createBitmapImp(width, height);
    }

    /**
     * Creates a filtered copy of the image. The new image has the same width and height as this image. The pixel colors
     * of the new image are determined by calling {@link ch.jeda.ui.ImageFilter#apply(ch.jeda.ui.Image, int, int)} for
     * each pixel.
     *
     * @param filter the image filter
     * @throws NullPointerException if <code>filter</code> is <code>null</code>
     *
     * @since 2.0
     */
    public void applyFilter(final ImageFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter");
        }

        final Bitmap result = new Bitmap(getWidth(), getHeight());
        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y < getHeight(); ++y) {
                result.setPixel(x, y, filter.apply(this, x, y));
            }
        }

        imp.copyFrom(result.imp);
    }

    /**
     * Flips this raster graphics image horizontally.
     *
     * @see #flipVertically()
     * @since 2.0
     */
    public void flipHorizontally() {
        imp.flipHorizontally();
    }

    /**
     * Flips this raster graphics image vertically.
     *
     * @see #flipHorizontally() ()
     * @since 2.0
     */
    public void flipVertically() {
        imp.flipVertically();
    }

    /**
     * Fills an area of a specified color with another color.
     *
     * @param x the x coordinate of the starting point
     * @param y the y coordinate of the starting point
     * @param oldColor the color to look for
     * @param newColor the color to replace it with
     *
     * @since 2.0
     */
    public void floodFill(int x, int y, final Color oldColor, final Color newColor) {
        if (oldColor == null) {
            throw new NullPointerException("oldColor");
        }

        if (newColor == null) {
            throw new NullPointerException("newColor");
        }

        final Stack<Integer> stackX = new Stack<Integer>();
        final Stack<Integer> stackY = new Stack<Integer>();
        stackX.push(x);
        stackY.push(y);
        while (!stackX.isEmpty()) {
            x = stackX.pop();
            y = stackY.pop();
            if (getPixel(x, y).equals(oldColor)) {
                setPixel(x, y, newColor);
                stackX.push(x);
                stackY.push(y + 1);
                stackX.push(x);
                stackY.push(y - 1);
                stackX.push(x + 1);
                stackY.push(y);
                stackX.push(x - 1);
                stackY.push(y);
            }
        }
    }

    /**
     * Returns the height of this raster graphics image.
     *
     * @return the height of this raster graphics image
     *
     * @see #getWidth()
     * @since 2.0
     */
    public int getHeight() {
        return imp.getHeight();
    }

    /**
     * Returns the pixel color at the specified coordinates. If the specified coordinates lay outside the image, the
     * color of the pixel closest to the coordinates is returned.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the pixel color
     *
     * @since 2.0
     */
    public Color getPixel(final int x, final int y) {
        return new Color(imp.getPixel(toRangeX(x), toRangeY(y)));
    }

    /**
     * Returns the width of this raster graphics image.
     *
     * @return the width of this raster graphics image
     *
     * @see #getHeight()
     * @since 2.0
     */
    public int getWidth() {
        return imp.getWidth();
    }

    /**
     * Scales this raster graphics image.
     *
     * @param factor the scale factor
     *
     * @since 2.0
     */
    public void scale(final double factor) {
        imp.scale((float) factor);
    }

    /**
     * Sets the color of a pixel. Sets the color of the pixel at the coordinates (<code>x</code>, <code>y</code>). Has
     * no effect if the coordinates reference a pixel outside the bitmap.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param color new color of the pixel
     * @throws NullPointerException if <code>color</code> is <code>null</code>
     *
     * @see #getPixel(int, int)
     * @since 2.0
     */
    public void setPixel(final int x, final int y, final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (contains(x, y)) {
            imp.setPixel(x, y, color.getValue());
        }
    }

    private int toRangeX(final int x) {
        return Math.max(0, Math.min(x, getWidth() - 1));
    }

    private int toRangeY(final int y) {
        return Math.max(0, Math.min(y, getHeight() - 1));
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < imp.getWidth() && 0 <= y && y < imp.getHeight();
    }
}
