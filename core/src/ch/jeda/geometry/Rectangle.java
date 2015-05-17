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
package ch.jeda.geometry;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

/**
 * Represents a filled rectangle.
 *
 * @since 2.0
 */
public final class Rectangle extends Shape {

    private final double height;
    private final double width;

    /**
     * Constructs a new rectangle shape. With and height must be positive. The center of the rectangle is the origin of
     * the local coordinate system.
     *
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws IllegalArgumentException if <code>width</code> or <code>height</code> are not positive
     *
     * @since 2.0
     */
    public Rectangle(final double width, final double height) {
        if (width <= 0.0) {
            throw new IllegalArgumentException("width");
        }

        if (height <= 0.0) {
            throw new IllegalArgumentException("height");
        }

        this.height = height;
        this.width = width;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return Math.abs(x) <= width / 2.0 && Math.abs(y) <= height / 2.0;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawRectangle(0, 0, width, height, Alignment.CENTER);
    }

    /**
     * Returns the area of this rectangle.
     *
     * @return the area of this rectangle
     *
     * @since 2.0
     */
    public double getArea() {
        return width * height;
    }

    /**
     * Returns the height of this rectangle.
     *
     * @return the height of this rectangle
     *
     * @since 2.0
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the width of this rectangle.
     *
     * @return the width of this rectangle
     *
     * @since 2.0
     */
    public double getWidth() {
        return width;
    }
}
