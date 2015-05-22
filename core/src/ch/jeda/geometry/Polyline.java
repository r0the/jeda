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
import java.util.Arrays;

/**
 * Represents an open polygonal chain shape.
 *
 * @since 2.0
 */
public class Polyline extends Shape {

    private final float[] points;

    /**
     * Constructs a polyline shape. The polyline is defined by a sequence of coordinate pairs specifiying it's vertices.
     * For example, the code
     * <pre><code>new Polyline(x1, y1, x2, y2, x3, y3);</code></pre> will define a polyline consisting of the two line
     * segments (x1, y2) to (x2, y2) and (x2, y2) to (x3, y3).
     *
     * @param points the points of the polyline as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 4 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public Polyline(final double... points) {
        if (points.length < 4 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = new float[points.length];
        for (int i = 0; i < points.length; ++i) {
            this.points[i] = (float) points[i];
        }
    }

    /**
     * Constructs a polyline shape. The polyline is defined by a sequence of coordinate pairs specifiying it's vertices.
     * For example, the code
     * <pre><code>new Polyline(x1, y1, x2, y2, x3, y3);</code></pre> will define a polyline consisting of the two line
     * segments (x1, y2) to (x2, y2) and (x2, y2) to (x3, y3).
     *
     * @param points the points of the polyline as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 4 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public Polyline(final float... points) {
        if (points.length < 4 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = Arrays.copyOf(points, points.length);
    }

    @Override
    public boolean contains(final float x, final float y) {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawPolyline(points);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.drawPolyline(points);
    }

    /**
     * Returns the number of vertices of this polygonal chain.
     *
     * @return the number of vertices of this polygonal chain
     *
     * @since 2.0
     */
    public int getPointCount() {
        return points.length / 2;
    }

    /**
     * Returns the x coordinate of the <code>i</code>-th vertex of this polygonal chain.
     *
     * @param i the index of the vertex
     * @return the x coordinate of the <code>i</code>-th vertex of this polygonal chain
     * @throws IndexOutOfBoundsException if <code>i</code> is not a valid vertex index
     *
     * @since 2.0
     */
    public float getPointX(int i) {
        return points[2 * i];
    }

    /**
     * Returns the y coordinate of the <code>i</code>-th vertex of this polygonal chain.
     *
     * @param i the index of the vertex
     * @return the y coordinate of the <code>i</code>-th vertex of this polygonal chain
     * @throws IndexOutOfBoundsException if <code>i</code> is not a valid vertex index
     *
     * @since 2.0
     */
    public float getPointY(int i) {
        return points[2 * i + 1];
    }
}
