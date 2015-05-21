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
public class PolygonalChain extends Shape {

    // TODO: Implementation with one array
    private final int vertexCount;
    private final float[] vertexX;
    private final float[] vertexY;

    /**
     * Constructs a polygonal chain shape. The polygonal chain is defined by a sequence of coordinate pairs specifiying
     * it's vertices. For example, the code
     * <pre><code>new PolygonalChain(x1, y1, x2, y2, x3, y3);</code></pre> will define a chain consisting of the two
     * line segments (x1, y2) to (x2, y2) and (x2, y2) to (x3, y3).
     *
     * @param vertices the vertices of the polyline as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 4 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public PolygonalChain(final float... vertices) {
        if (vertices.length < 4 || vertices.length % 2 == 1) {
            throw new IllegalArgumentException("vertices");
        }

        vertexCount = vertices.length / 2;
        vertexX = new float[vertexCount];
        vertexY = new float[vertexCount];
        for (int i = 0; i < vertexCount; ++i) {
            vertexX[i] = vertices[2 * i];
            vertexY[i] = vertices[2 * i + 1];
        }
    }

    @Override
    public boolean contains(final float x, final float y) {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        for (int i = 0; i < vertexCount - 1; ++i) {
            canvas.drawLine(vertexX[i], vertexY[i], vertexX[i + 1], vertexY[i + 1]);
        }
    }

    @Override
    public void fill(final Canvas canvas) {
        this.draw(canvas);
    }

    /**
     * Returns the number of vertices of this polygonal chain.
     *
     * @return the number of vertices of this polygonal chain
     *
     * @since 2.0
     */
    public int getVertexCount() {
        return vertexCount;
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
    public float getVertexX(int i) {
        return vertexX[i];
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
    public float getVertexY(int i) {
        return vertexY[i];
    }
}