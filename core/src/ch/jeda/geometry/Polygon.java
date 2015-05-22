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
 * Represents a closed polygon shape.
 *
 * @since 2.0
 */
public final class Polygon extends Shape {

    private final float[] points;

    /**
     * Constructs a polygon shape. The polygon is defined by a sequence of coordinate pairs specifiying the vertices of
     * the polygon. For example, the code
     * <pre><code>new Polygon(x1, y1, x2, y2, x3, y3);</code></pre> will define a triangle with the vertices (x1, y2),
     * (x2, y2), and (x3, y3).
     *
     * @param points the vertices of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public Polygon(final double... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = new float[points.length];
        for (int i = 0; i < points.length; ++i) {
            this.points[i] = (float) points[i];
        }
    }

    /**
     * Constructs a polygon shape. The polygon is defined by a sequence of coordinate pairs specifiying the vertices of
     * the polygon. For example, the code
     * <pre><code>new Polygon(x1, y1, x2, y2, x3, y3);</code></pre> will define a triangle with the vertices (x1, y2),
     * (x2, y2), and (x3, y3).
     *
     * @param points the vertices of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public Polygon(final float... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = Arrays.copyOf(points, points.length);
    }

    @Override
    public boolean contains(final float x, final float y) {
        // Ray casting algorithm
        // Sources: http://stackoverflow.com/a/218081 (General idea, but intersection test too complicated)
        //          http://geomalgorithms.com/a03-_inclusion.html (C++ code)
        //          http://web.archive.org/web/20120322002749/http://local.wasp.uwa.edu.au/~pbourke/geometry/
        int intersections = 0;
        for (int i = 0; i < points.length; i = i + 2) {
            final float v1y = points[i + 1];
            final float v2y = points[(i + 3) % points.length];
            // Check if polygon edge crosses the line of ray upwards or downwards
            if ((v1y <= y && v2y > y) || (v1y >= y && v2y < y)) {
                // compute x coordinate of intersection
                final float vt = (y - v1y) / (v2y - v1y);
                final float v1x = points[i];
                final float v2x = points[(i + 2) % points.length];
                final float ix = v1x + vt * (v2x - v1x);
                // x coordinate must be on ray and on edge
                if (x < ix) {
                    ++intersections;
                }
            }
        }

        return intersections % 2 == 1;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawPolygon(points);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.fillPolygon(points);
    }

    /**
     * Returns the number of vertices of this polygon.
     *
     * @return the number of vertices of this polygon
     *
     * @since 2.0
     */
    public int getPointCount() {
        return points.length / 2;
    }

    /**
     * Returns the x coordinate of the <code>i</code>-th vertex of this polygon.
     *
     * @param i the index of the vertex
     * @return the x coordinate of the <code>i</code>-th vertex of this polygon
     * @throws IndexOutOfBoundsException if <code>i</code> is not a valid vertex index
     *
     * @since 2.0
     */
    public float getPointX(int i) {
        return points[2 * i];
    }

    /**
     * Returns the y coordinate of the <code>i</code>-th vertex of this polygon.
     *
     * @param i the index of the vertex
     * @return the y coordinate of the <code>i</code>-th vertex of this polygon
     * @throws IndexOutOfBoundsException if <code>i</code> is not a valid vertex index
     *
     * @since 2.0
     */
    public float getPointY(int i) {
        return points[2 * i + 1];
    }
}
