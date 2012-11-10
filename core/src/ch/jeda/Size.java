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

    public final int width;
    public final int height;

    /**
     * Creates a new Size object with a width and height of 0.
     */
    public Size() {
        this(0, 0);
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
        result.append("Size(");
        result.append(this.width);
        result.append(", ");
        result.append(this.height);
        result.append(')');
        return result.toString();
    }
}
