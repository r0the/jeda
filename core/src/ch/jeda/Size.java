/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

public class Size implements Serializable {

    /**
     * An empty <code>Size</code>. Both width and height of this objects are
     * 0.
     */
    public static final Size EMPTY = new Size();
    /**
     * The width of this size.
     */
    public final int width;
    /**
     * The height of this size.
     */
    public final int height;
    private static final char SEPARATOR = 'x';

    public static Size max(Size a, Size b) {
        return new Size(Math.max(a.width, b.width), Math.max(a.height, b.height));
    }

    /**
     * Parses a size from a string. The string must contain two positivie
     * integers separated by an 'x', e.g. "800x600". Returns <code>null</code>
     * if the string does not represent a valid size.
     * 
     * @param text the string to parse
     * @return the parsed size or <code>null</code>
     */
    public static Size parse(String text) {
        int pos = text.indexOf(SEPARATOR);
        if (pos == -1) {
            return null;
        }

        try {
            int width = Integer.parseInt(text.substring(0, pos));
            int height = Integer.parseInt(text.substring(pos + 1));

            if (width > 0 && height > 0) {
                return new Size(width, height);
            }
            else {
                return null;
            }
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Creates a new Size object.
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

    private Size() {
        this(0, 0);
    }

    public int area() {
        return this.width * this.height;
    }

    public boolean contains(Location location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        return 0 <= location.x && location.x < this.width &&
               0 <= location.y && location.y < this.height;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Size) {
            Size other = (Size) object;
            return this.width == other.width && this.height == other.height;
        }
        else {
            return super.equals(object);
        }
    }

    @Override
    public int hashCode() {
        return 13 * this.width + this.height;
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public Size scaled(double factor) {
        return new Size((int) (this.width * factor), (int) (this.height * factor));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.width);
        result.append('x');
        result.append(this.height);
        return result.toString();
    }
}
