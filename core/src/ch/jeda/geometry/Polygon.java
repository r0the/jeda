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
import ch.jeda.ui.Color;

/**
 * Represents a closed polygon shape.
 *
 * @since 2.0
 */
public final class Polygon extends Shape {

    private final double[] vertices;

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

        this.vertices = vertices;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.setColor(Color.RED);
        canvas.drawPolygon(this.vertices);
    }

    /**
     * Returns the number of vertices of this polygon.
     *
     * @return the number of vertices of this polygon
     *
     * @since 2.0
     */
    public int getVertexCount() {
        return this.vertices.length / 2;
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
        return this.vertices[i / 2];
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
        return this.vertices[i / 2 + 1];
    }
}
