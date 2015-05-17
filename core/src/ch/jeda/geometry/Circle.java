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
 * Represents a circle shape.
 *
 * @since 2.0
 */
public final class Circle extends Shape implements Storable {

    private static final String CENTER_X = "centerx";
    private static final String CENTER_Y = "centery";
    private static final String RADIUS = "radius";
    private final double centerX;
    private final double centerY;
    private final double radius;

    /**
     * Constructs a circle shape. The specified radius must be positive.
     *
     * @param centerX the horizontal coordinate of the circle's center
     * @param centerY the vertical coordinate of the circle's center
     * @param radius the radius of the circle
     * @throws IllegalArgumentException if <code>radius</code> is not positive
     *
     * @since 2.0
     */
    public Circle(final double centerX, final double centerY, final double radius) {
        if (radius <= 0.0) {
            throw new IllegalArgumentException("radius");
        }

        this.centerX = centerX;
        this.centerY = centerY;
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
        this.centerX = data.readDouble(CENTER_X);
        this.centerY = data.readDouble(CENTER_Y);
        this.radius = data.readDouble(RADIUS);
    }

    @Override
    public boolean contains(final double x, final double y) {
        final double dx = centerX - x;
        final double dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.fillCircle(centerX, centerY, radius);
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
     * Returns the horizontal coordinate of the circle's center.
     *
     * @return the horizontal coordinate of the circle's center
     *
     * @since 2.0
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Returns the vertical coordinate of the circle's center.
     *
     * @return the vertical coordinate of the circle's center
     *
     * @since 2.0
     */
    public double getCenterY() {
        return centerY;
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
            x[i] = centerX + radius * Math.cos(angle);
            y[i] = centerY + radius * Math.sin(angle);
        }

        return new Polygon(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Circle(cx=");
        result.append(centerX);
        result.append(", cy=");
        result.append(centerY);
        result.append(", r=");
        result.append(radius);
        result.append(")");
        return result.toString();
    }

    @Override
    public void writeTo(final Data data) {
        data.writeDouble(CENTER_X, centerX);
        data.writeDouble(CENTER_Y, centerY);
        data.writeDouble(RADIUS, radius);
    }
}
