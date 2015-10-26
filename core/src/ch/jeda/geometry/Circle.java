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
import ch.jeda.ui.Canvas;
import ch.jeda.Storable;
import static ch.jeda.MathF.*;

/**
 * Represents a circle shape.
 *
 * @since 2.0
 */
public final class Circle extends Shape implements Storable {

    private static final String CENTER_X = "centerx";
    private static final String CENTER_Y = "centery";
    private static final String RADIUS = "radius";
    private final float centerX;
    private final float centerY;
    private final float radius;

    /**
     * Constructs a circle shape. A negative radius is changed to its absolute value.
     *
     * @param centerX the horizontal coordinate of the circle's center
     * @param centerY the vertical coordinate of the circle's center
     * @param radius the radius of the circle
     *
     * @since 2.0
     */
    public Circle(final double centerX, final double centerY, final double radius) {
        this((float) centerX, (float) centerY, (float) radius);
    }

    /**
     * Constructs a circle shape. A negative radius is changed to it's absolute value.
     *
     * @param centerX the horizontal coordinate of the circle's center
     * @param centerY the vertical coordinate of the circle's center
     * @param radius the radius of the circle
     *
     * @since 2.0
     */
    public Circle(final float centerX, final float centerY, final float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = Math.abs(radius);
    }

    /**
     * Constructs a circle from serialized data.
     *
     * @param data the serialized data
     *
     * @since 2.0
     */
    public Circle(final Data data) {
        this.centerX = data.readFloat(CENTER_X);
        this.centerY = data.readFloat(CENTER_Y);
        this.radius = Math.abs(data.readFloat(RADIUS));
    }

    @Override
    public boolean contains(final float x, final float y) {
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

    @Override
    public Shape flipHorizontally() {
        return new Circle(-centerX, centerY, radius);
    }

    @Override
    public Shape flipVertically() {
        return new Circle(centerX, -centerY, radius);
    }

    /**
     * Returns the area of this circle.
     *
     * @return the area of this circle
     *
     * @since 2.0
     */
    public float getArea() {
        return PI * radius * radius;
    }

    /**
     * Returns the horizontal coordinate of the circle's center.
     *
     * @return the horizontal coordinate of the circle's center
     *
     * @since 2.0
     */
    public float getCenterX() {
        return centerX;
    }

    /**
     * Returns the vertical coordinate of the circle's center.
     *
     * @return the vertical coordinate of the circle's center
     *
     * @since 2.0
     */
    public float getCenterY() {
        return centerY;
    }

    /**
     * Returns the circumference of this circle.
     *
     * @return the circumference of this circle
     *
     * @since 2.0
     */
    public float getCircumference() {
        return 2f * PI * radius;
    }

    /**
     * Returns the diameter of this circle.
     *
     * @return the diameter of this circle
     *
     * @since 2.0
     */
    public float getDiameter() {
        return 2f * radius;
    }

    /**
     * Returns the radius of this circle.
     *
     * @return the radius of this circle
     *
     * @since 2.0
     */
    public float getRadius() {
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
        final float[] points = new float[2 * n];
        for (int i = 0; i < n; ++i) {
            float angle = 2f * PI * i / n;
            points[2 * i] = centerX - radius * cos(angle);
            points[2 * i + 1] = centerY - radius * sin(angle);
        }

        return new Polygon(points);
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
        data.writeFloat(CENTER_X, centerX);
        data.writeFloat(CENTER_Y, centerY);
        data.writeFloat(RADIUS, radius);
    }

}
