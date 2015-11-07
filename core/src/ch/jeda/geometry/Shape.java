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

import ch.jeda.ui.Canvas;

/**
 * Represents a geometric shape.
 *
 * @since 2.0
 */
public abstract class Shape {

    /**
     * Checks if this shape contains a point.
     *
     * @param x the horizontal coordinate of the point
     * @param y the vertical coordinate of the point
     * @return <code>true</code>, if the shape contains the point, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public final boolean contains(double x, double y) {
        return contains((float) x, (float) y);
    }

    /**
     * Checks if this shape contains a point.
     *
     * @param x the horizontal coordinate of the point
     * @param y the vertical coordinate of the point
     * @return <code>true</code>, if the shape contains the point, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public abstract boolean contains(float x, float y);

    /**
     * Draws the border of this shape on a canvas.
     *
     * @param canvas the canvas to draw on
     *
     * @since 2.0
     */
    public abstract void draw(Canvas canvas);

    /**
     * Draws the area of this shape on a canvas.
     *
     * @param canvas the canvas to draw on
     *
     * @since 2.0
     */
    public abstract void fill(Canvas canvas);

    /**
     * Returns a horizontally flipped copy of this shape.
     *
     * @return a horizontally flipped copy of this shape
     *
     * @since 2.4
     */
    public abstract Shape flipHorizontally();

    /**
     * Returns a vertically flipped copy of this shape.
     *
     * @return a vertically flipped copy of this shape
     *
     * @since 2.4
     */
    public abstract Shape flipVertically();

    public abstract Shape translate(float dx, float dy);
}
