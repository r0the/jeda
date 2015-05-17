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

    private final int vertexCount;
    private final double[] vertexX;
    private final double[] vertexY;

    /**
     * Constructs a polygon shape. The polygon is defined by the x and y coordinates of it's vertices. For example, the
     * code
     * <pre><code>new Polygon(new double[] {x1, x2, x3}, new double[] {y1, y2, y3});</code></pre> will define a triangle
     * with the vertices (x1, y2), (x2, y2), and (x3, y3).
     *
     * @param vertexX the x coordinates of the polygon's vertices
     * @param vertexY the y coordinates of the polygon's vertices
     * @throws IllegalArgumentException if less than 3 x coordinates are passed
     * @throws IllegalArgumentException if not the same number of x and y coordinates are passed
     *
     * @since 2.0
     */
    public Polygon(final double[] vertexX, final double[] vertexY) {
        if (vertexX.length < 3) {
            throw new IllegalArgumentException("vertexX");
        }

        if (vertexX.length != vertexY.length) {
            throw new IllegalArgumentException("vertexY");
        }

        vertexCount = vertexX.length;
        this.vertexX = Arrays.copyOf(vertexX, vertexCount);
        this.vertexY = Arrays.copyOf(vertexY, vertexCount);
    }

    /**
     * Constructs a polygon shape. The polygon is defined by a sequence of coordinate pairs specifiying the vertices of
     * the polygon. For example, the code
     * <pre><code>new Polygon(x1, y1, x2, y2, x3, y3);</code></pre> will define a triangle with the vertices (x1, y2),
     * (x2, y2), and (x3, y3).
     *
     * @param vertices the vertices of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public Polygon(final double... vertices) {
        if (vertices.length < 6 || vertices.length % 2 == 1) {
            throw new IllegalArgumentException("vertices");
        }

        vertexCount = vertices.length / 2;
        vertexX = new double[vertexCount];
        vertexY = new double[vertexCount];
        for (int i = 0; i < vertexCount; ++i) {
            vertexX[i] = vertices[2 * i];
            vertexY[i] = vertices[2 * i + 1];
        }
    }

    @Override
    public boolean contains(final double x, final double y) {
        // Ray casting algorithm
        // Sources: http://stackoverflow.com/a/218081 (General idea, but intersection test too complicated)
        //          http://geomalgorithms.com/a03-_inclusion.html (C++ code)
        //          http://web.archive.org/web/20120322002749/http://local.wasp.uwa.edu.au/~pbourke/geometry/
        int intersections = 0;
        for (int i = 0; i < vertexCount; ++i) {
            final double v1y = vertexY[i];
            final double v2y = vertexY[(i + 1) % vertexCount];
            // Check if polygon edge crosses the line of ray upwards or downwards
            if ((v1y <= y && v2y > y) || (v1y >= y && v2y < y)) {
                // compute x coordinate of intersection
                final double vt = (y - v1y) / (v2y - v1y);
                final double v1x = vertexX[i];
                final double v2x = vertexX[(i + 1) % vertexCount];
                final double ix = v1x + vt * (v2x - v1x);
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
        canvas.drawPolygon(vertexX, vertexY);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.fillPolygon(vertexX, vertexY);
    }

    /**
     * Returns the number of vertices of this polygon.
     *
     * @return the number of vertices of this polygon
     *
     * @since 2.0
     */
    public int getVertexCount() {
        return vertexX.length;
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
    public double getVertexX(int i) {
        return vertexX[i];
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
    public double getVertexY(int i) {
        return vertexY[i];
    }
}
