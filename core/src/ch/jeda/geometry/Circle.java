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
 * Represents a circle shape.
 *
 * @since 2.0
 */
public final class Circle extends Shape {

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

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawCircle(0, 0, this.radius);
    }

    /**
     * Returns the area of this circle.
     *
     * @return the area of this circle
     *
     * @since 2.0
     */
    public double getArea() {
        return Math.PI * this.radius * this.radius;
    }

    /**
     * Returns the circumference of this circle.
     *
     * @return the circumference of this circle
     *
     * @since 2.0
     */
    public double getCircumference() {
        return 2.0 * Math.PI * this.radius;
    }

    /**
     * Returns the diameter of this circle.
     *
     * @return the diameter of this circle
     *
     * @since 2.0
     */
    public double getDiameter() {
        return 2.0 * this.radius;
    }

    /**
     * Returns the radius of this circle.
     *
     * @return the radius of this circle
     *
     * @since 2.0
     */
    public double getRadius() {
        return this.radius;
    }
}
