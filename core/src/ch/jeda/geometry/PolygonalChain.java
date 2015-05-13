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
 * Represents an open polygonal chain shape.
 *
 * @since 2.0
 */
public class PolygonalChain extends Shape {

    private final double[] x;
    private final double[] y;

    /**
     * Constructs a polygonal chain shape. The polygonal chain is defined by a sequence of coordinate pairs specifiying
     * it's vertices. For example, the code
     * <pre><code>new Polyline(x1, y1, x2, y2, x3, y3);</code></pre> will define a chain consisting of the two line
     * segments (x1, y2) to (x2, y2) and (x2, y2) to (x3, y3).
     *
     * @param vertices the vertices of the polyline as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 4 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public PolygonalChain(final double... vertices) {
        if (vertices.length < 4 || vertices.length % 2 == 1) {
            throw new IllegalArgumentException("vertices");
        }

        final int count = vertices.length / 2;
        this.x = new double[count];
        this.y = new double[count];
        for (int i = 0; i < count; ++i) {
            this.x[i] = vertices[2 * i];
            this.y[i] = vertices[2 * i + 1];
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        for (int i = 0; i < this.x.length - 1; ++i) {
            canvas.drawLine(this.x[i], this.y[i], this.x[i + 1], this.y[i + 1]);
        }
    }

    /**
     * Returns the number of vertices of this polygonal chain.
     *
     * @return the number of vertices of this polygonal chain
     *
     * @since 2.0
     */
    public int getVertexCount() {
        return this.x.length;
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
    public double getVertexX(int i) {
        return this.x[i];
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
    public double getVertexY(int i) {
        return this.y[i];
    }
}
