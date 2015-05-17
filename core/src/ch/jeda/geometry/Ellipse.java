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
 * Represents an ellipse shape.
 *
 * @since 2.0
 */
public final class Ellipse extends Shape implements Storable {

    private static final String RADIUS_X = "radiusx";
    private static final String RADIUS_Y = "radiusy";
    private final double radiusX;
    private final double radiusY;

    /**
     * Constructs a new ellipse shape. With and height must be positive.
     *
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     * @throws IllegalArgumentException if <code>rx</code> or <code>ry</code> are not positive
     *
     * @since 2.0
     */
    public Ellipse(final double radiusX, final double radiusY) {
        if (radiusX <= 0.0) {
            throw new IllegalArgumentException("radiusX");
        }

        if (radiusY <= 0.0) {
            throw new IllegalArgumentException("radiusY");
        }

        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    /**
     * Constructs an ellipse from serialized data.
     *
     * @param data the serialized data
     *
     * @since 2.0
     */
    public Ellipse(final Data data) {
        this.radiusX = data.readDouble(RADIUS_X);
        this.radiusY = data.readDouble(RADIUS_Y);
    }

    @Override
    public boolean contains(final double x, final double y) {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawEllipe(0.0, 0.0, radiusX, radiusY);
    }

    /**
     * Returns the area of this ellipse.
     *
     * @return the area of this ellipse
     *
     * @since 2.0
     */
    public double getArea() {
        return Math.PI * radiusX * radiusY;
    }

    /**
     * Returns the eccentricity of this ellipse.
     *
     * @return the eccentricity of this ellipse
     *
     * @since 2.0
     */
    public double getEccentricity() {
        if (radiusX > radiusY) {
            return Math.sqrt(1 - (radiusY * radiusY / (radiusX * radiusX)));
        }
        else {
            return Math.sqrt(1 - (radiusX * radiusX / (radiusY * radiusY)));
        }
    }

    /**
     * Returns the horizontal radius of this ellipse.
     *
     * @return the horizontal radius of this ellipse
     *
     * @since 2.0
     */
    public double getRadiusX() {
        return radiusX;
    }

    /**
     * Returns the vertical radius of this ellipse.
     *
     * @return the vertical radius of this ellipse
     *
     * @since 2.0
     */
    public double getRadiusY() {
        return radiusY;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Ellipse(rx=");
        result.append(radiusX);
        result.append(", ry=");
        result.append(radiusY);
        result.append(")");
        return result.toString();
    }

    @Override
    public void writeTo(final Data data) {
        data.writeDouble(RADIUS_X, radiusX);
        data.writeDouble(RADIUS_Y, radiusY);
    }
}
