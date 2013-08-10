/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda;

import java.io.Serializable;

/**
 * Represents a two-dimensional size. <tt>Size</tt> objects can for example be used to specify the size of a rectangle
 * to be drawn on a {@link ch.jeda.ui.Canvas}. <tt>Size</tt> objects are returned by some methods to inform about the
 * size of an object (for example {@link ch.jeda.ui.Image#getSize()}.
 * <p>
 * A size has a width and a height which cannot be negative. A size with both a width and a height of zero is called
 * empty.
 * <p>
 * <tt>Size</tt> objects are immutable. That means that their value cannot be changed.
 *
 * @since 1
 */
@Deprecated
public final class Size implements Serializable {

    private static final char SEPARATOR = 'x';
    /**
     * The empty size. This size has both a width and a height of zero.
     */
    public static final Size EMPTY = new Size();
    /**
     * The width of the size.
     */
    public final int width;
    /**
     * The height of the size.
     */
    public final int height;

    /**
     * Parses a size from a string. The string must consist of two positivie integers separated by an 'x', e.g.
     * "800x600". Returns <tt>Size.EMPTY</tt>
     * if <tt>text</tt> does not represent a valid size.
     *
     * @param text the string to parse
     */
    public static Size parse(String text) {
        if (text == null) {
            return Size.EMPTY;
        }

        final int pos = text.indexOf(SEPARATOR);
        if (pos == -1) {
            return Size.EMPTY;
        }

        try {
            int width = Integer.parseInt(text.substring(0, pos));
            int height = Integer.parseInt(text.substring(pos + 1));
            if (width > 0 && height > 0) {
                return new Size(width, height);
            }
            else {
                return Size.EMPTY;
            }
        }
        catch (NumberFormatException ex) {
            return Size.EMPTY;
        }
    }

    /**
     * @deprecated Use {@link Size#EMPTY} instead.
     */
    @Deprecated
    public Size() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * Constructs a size. The size has the specified width and height. The values of <tt>width</tt> and <tt>height</tt>
     * may not be negative.
     *
     * @param width the width
     * @param height the height
     * @throws IllegalArgumentException if width or height are smaller than 0
     */
    public Size(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("width");
        }

        if (height < 0) {
            throw new IllegalArgumentException("height");
        }

        this.width = width;
        this.height = height;
    }

    /**
     * Returns the area of a rectangle described by the size. More specific, returns the product of <tt>width</tt> and
     * <tt>height</tt>.
     *
     * @return product of <tt>width</tt> and <tt>height</tt>
     */
    public int area() {
        return this.width * this.height;
    }

    /**
     * Checks whether the specified location lies within a rectangle described by this size.
     *
     * @param location the location to check
     * @return <tt>true</tt> if location lies within, otherwise <tt>false</tt>
     * @throws NullPointerException if <tt>location</tt> is <tt>null</tt>
     */
    public boolean contains(Location location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        return 0 <= location.x && location.x < this.width &&
               0 <= location.y && location.y < this.height;
    }

    public boolean contains(int x, int y) {
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Size) {
            final Size other = (Size) object;
            return this.width == other.width && this.height == other.height;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 13 * this.width + this.height;
    }

    /**
     * Checks if the size is empty. A size is empty if it's area is zero.
     *
     * @return <tt>true</tt> if size is empty, otherwise <tt>false</tt>
     */
    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    /**
     * Returns a scaled variant of this size. Both width and height of this size are scaled and rounded. If both width
     * and height are positive, a resulting size is returned. Otherwise, <tt>null</tt> is returned.
     *
     * @param factor the factor by which to scale this size.
     * @return the scaled size or <tt>null</tt>
     */
    public Size scaled(double factor) {
        return new Size((int) Math.round(this.width * factor),
                        (int) Math.round(this.height * factor));
    }

    /**
     * Returns a string representation of this size. The string representation has the form <tt>"WxH"</tt> where
     * <tt>W</tt> is replaced by the width and
     * <tt>H</tt> by the height of this size.
     *
     * @return string representation of this size
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.width);
        result.append('x');
        result.append(this.height);
        return result.toString();
    }

    public Vector toVector() {
        return new Vector(this.width, this.height);
    }
}
