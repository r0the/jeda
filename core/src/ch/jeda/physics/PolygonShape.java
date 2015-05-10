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
package ch.jeda.physics;

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import org.jbox2d.common.Vec2;

/**
 * Represents a polygon shape.
 *
 * @since 2.0
 */
public class PolygonShape extends Shape {

    private final double[] points;

    /**
     * Constructs a polygon shape. The polygon is defined by a sequence of coordinate pairs specifiying the corners of
     * the polygon. For example, the code
     * <pre><code>new PolygonShape(x1, y1, x2, y2, x3, y3);</code></pre> will define a triangle with the corners (x1,
     * y2), (x2, y2), and (x3, y3).
     *
     * @param points the points of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     *
     * @since 2.0
     */
    public PolygonShape(final double... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = points;
    }

    @Override
    void draw(final Canvas canvas) {
        canvas.setColor(Color.RED);
        canvas.drawPolygon(this.points);
    }

    @Override
    org.jbox2d.collision.shapes.Shape createImp(final double scale) {
        final org.jbox2d.collision.shapes.PolygonShape result = new org.jbox2d.collision.shapes.PolygonShape();
        final Vec2[] vertices = new Vec2[this.points.length / 2];
        for (int i = 0; i < points.length; i = i + 2) {
            vertices[2 * i].set((float) (this.points[i] / scale), (float) (this.points[i + 1] / scale));
        }

        result.set(vertices, vertices.length);
        return result;
    }
}
