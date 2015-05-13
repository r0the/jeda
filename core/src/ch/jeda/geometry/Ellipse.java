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
 * Represents an ellipse shape.
 *
 * @since 2.0
 */
public final class Ellipse extends Shape {

    private final double rx;
    private final double ry;

    /**
     * Constructs a new ellipse shape. With and height must be positive.
     *
     * @param rx the horizontal radius of the ellipse
     * @param ry the vertical radius of the ellipse
     * @throws IllegalArgumentException if <code>rx</code> or <code>ry</code> are not positive
     *
     * @since 2.0
     */
    public Ellipse(final double rx, final double ry) {
        if (rx <= 0.0) {
            throw new IllegalArgumentException("rx");
        }

        if (ry <= 0.0) {
            throw new IllegalArgumentException("ry");
        }

        this.rx = rx;
        this.ry = ry;
    }

    /**
     * Returns the area of this ellipse.
     *
     * @return the area of this ellipse
     *
     * @since 2.0
     */
    public double getArea() {
        return Math.PI * rx * ry;
    }

    /**
     * Returns the eccentricity of this ellipse.
     *
     * @return the eccentricity of this ellipse
     *
     * @since 2.0
     */
    public double getEccentricity() {
        return Math.sqrt(1 - (rx * rx / (ry * ry)));
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawEllipe(0.0, 0.0, rx, ry);
    }

    /**
     * Returns the horizontal radius of this ellipse.
     *
     * @return the horizontal radius of this ellipse
     *
     * @since 2.0
     */
    public double getRx() {
        return this.rx;
    }

    /**
     * Returns the vertical radius of this ellipse.
     *
     * @return the vertical radius of this ellipse
     *
     * @since 2.0
     */
    public double getRy() {
        return this.ry;
    }

}
