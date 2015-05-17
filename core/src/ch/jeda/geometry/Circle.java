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

import ch.jeda.Data;
import ch.jeda.Storable;
import ch.jeda.ui.Canvas;

/**
 * Represents a filled circle. The center of the circle is at the origin of the coordinate system.
 *
 * @since 2.0
 */
public final class Circle extends Shape implements Storable {

    private static final String RADIUS = "radius";
    private final double radius;

    /**
     * Constructs a circle shape. The specified radius must be positive.
     *
     * @param radius the radius of the circle
     * @throws IllegalArgumentException if <code>radius</code> is not positive
     *
     * @since 2.0
     */
    public Circle(final double radius) {
        if (radius <= 0.0) {
            throw new IllegalArgumentException("radius");
        }

        this.radius = radius;
    }

    /**
     * Constructs a circle from serialized data.
     *
     * @param data the serialized data
     *
     * @since 2.0
     */
    public Circle(final Data data) {
        this.radius = data.readDouble(RADIUS);
    }

    @Override
    public boolean contains(final double x, final double y) {
        return x * x + y * y <= radius * radius;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawCircle(0, 0, radius);
    }

    /**
     * Returns the area of this circle.
     *
     * @return the area of this circle
     *
     * @since 2.0
     */
    public double getArea() {
        return Math.PI * radius * radius;
    }

    /**
     * Returns the circumference of this circle.
     *
     * @return the circumference of this circle
     *
     * @since 2.0
     */
    public double getCircumference() {
        return 2.0 * Math.PI * radius;
    }

    /**
     * Returns the diameter of this circle.
     *
     * @return the diameter of this circle
     *
     * @since 2.0
     */
    public double getDiameter() {
        return 2.0 * radius;
    }

    /**
     * Returns the radius of this circle.
     *
     * @return the radius of this circle
     *
     * @since 2.0
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Creates and returns a polygon that approximates this circle. The number <code>n</code> specifies the number of
     * vertices of the polygon.
     *
     * @param n the number of vertices of the polygon
     * @return a polygon approximating the circle
     *
     * @since 2.0
     */
    public Polygon toPolygon(int n) {
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; ++i) {
            double angle = 2.0 * Math.PI * i / n;
            x[i] = Math.cos(angle);
            y[i] = Math.sin(angle);
        }

        return new Polygon(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Circle(r=");
        result.append(radius);
        result.append(")");
        return result.toString();
    }

    @Override
    public void writeTo(final Data data) {
        data.writeDouble(RADIUS, radius);
    }
}
