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

    private final float x;
    private final float y;
    private final float height;
    private final float width;

    /**
     * Constructs a new rectangle shape. With and height must be positive.
     *
     * @param x the horizontal coordinate of the rectangle's center
     * @param y the vertical coordinate of the rectangle's center
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws IllegalArgumentException if <code>width</code> or <code>height</code> are not positive
     *
     * @since 2.0
     */
    public Rectangle(final double x, final double y, final double width, final double height) {
        this((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Constructs a new rectangle shape. With and height must be positive. The center of the rectangle is the origin of
     * the local coordinate system.
     *
     * @param x the horizontal coordinate of the rectangle's center
     * @param y the vertical coordinate of the rectangle's center
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws IllegalArgumentException if <code>width</code> or <code>height</code> are not positive
     *
     * @since 2.0
     */
    public Rectangle(final float x, final float y, final float width, final float height) {
        if (width <= 0.0) {
            throw new IllegalArgumentException("width");
        }

        if (height <= 0.0) {
            throw new IllegalArgumentException("height");
        }

        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    @Override
    public boolean contains(final float x, final float y) {
        return Math.abs(this.x - x) <= width / 2.0 && Math.abs(this.y - y) <= height / 2.0;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawRectangle(x, y, width, height);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.fillRectangle(x, y, width, height);
    }

    @Override
    public Shape flipHorizontally() {
        return new Rectangle(-x, y, width, height);
    }

    @Override
    public Shape flipVertically() {
        return new Rectangle(x, -y, width, height);
    }

    /**
     * Returns the area of this rectangle.
     *
     * @return the area of this rectangle
     *
     * @since 2.0
     */
    public float getArea() {
        return width * height;
    }

    /**
     * Returns the height of this rectangle.
     *
     * @return the height of this rectangle
     *
     * @since 2.0
     */
    public float getHeight() {
        return height;
    }

    /**
     * Returns the width of this rectangle.
     *
     * @return the width of this rectangle
     *
     * @since 2.0
     */
    public float getWidth() {
        return width;
    }

    /**
     * Creates and returns a polygon with the same shape as this rectangle.
     *
     * @return a polygon with the same shape as this rectangle
     *
     * @since 2.0
     */
    public Polygon toPolygon() {
        return new Polygon(x, y, x + width, y, x + width, y + height, x, y + height);
    }

    @Override
    public Shape translate(final float dx, final float dy) {
        return new Rectangle(x + dx, y + dy, width, height);
    }
}
