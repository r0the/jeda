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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public final class Size implements Serializable {

    /**
     * The width of this size.
     * 
     * @since 1
     */
    @XmlElement
    public final int width;
    /**
     * The height of this size.
     * 
     * @since 1
     */
    @XmlElement
    public final int height;
    private static final char SEPARATOR = 'x';

    /**
     * Creates a new empty size object. The object has a width and height of 0.
     *
     * @since 1
     */
    public Size() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * Creates a new size object with the specified width and height.
     *
     * @param width the width
     * @param height the height
     * @throws IllegalArgumentException if width or height are smaller than 0
     * 
     * @since 1
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
     * Parses a size from a string. The string must contain two positivie
     * integers separated by an 'x', e.g. "800x600". Returns <code>null</code>
     * if <code>text</code> does not represent a valid size.
     * 
     * @param text the string to parse
     * @throws NullPointerException if <code>text</code> has the value
     *         <code>null</code>
     * 
     * @since 1
     */
    public Size(String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }

        int w = 0;
        int h = 0;
        final int pos = text.indexOf(SEPARATOR);
        if (pos >= 0) {
            try {
                w = Integer.parseInt(text.substring(0, pos));
                h = Integer.parseInt(text.substring(pos + 1));
            }
            catch (NumberFormatException ex) {
                w = 0;
                h = 0;
            }
        }

        this.width = w;
        this.height = h;
    }

    /**
     * Returns the area of a rectangle described by this size. More specific,
     * returns the product of <code>width</code> and <code>height</code>.
     * 
     * @return area
     * 
     * @since 1
     */
    public int area() {
        return this.width * this.height;
    }

    /**
     * Checks whether the specified location lies within a rectangle described
     * by this size.
     * 
     * @param location the location to check
     * @return <code>true</code> if location lies within, otherwise
     *         <code>false</code>
     * 
     * @since 1
     */
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
     * Checks if this size is empty. A size is empty if it's area is zero.
     * 
     * @return <code>true</code> if size is empty, otherwise
     *         <code>false</code>
     * 
     * @since 1
     */
    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    /**
     * Returns a scaled variant of this size. Both width and height of this
     * size are scaled and rounded. If both width and height are positive,
     * a resulting size is returned. Otherwise, <code>null</code> is returned.
     * 
     * @param factor the factor by which to scale this size.
     * @return the scaled size or <code>null</code>
     * 
     * @since 1
     */
    public Size scaled(double factor) {
        return new Size((int) Math.round(this.width * factor),
                        (int) Math.round(this.height * factor));
    }

    /**
     * Returns a string representation of this size. The string representation
     * has the form "WxH" where W is replaced by the width and H by the height
     * of this size.
     * 
     * @return string representation of this size
     * 
     * @since 1
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.width);
        result.append('x');
        result.append(this.height);
        return result.toString();
    }
}
