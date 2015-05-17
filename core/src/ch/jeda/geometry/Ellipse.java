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

    private static final String CENTER_X = "centerx";
    private static final String CENTER_Y = "centery";
    private static final String RADIUS_X = "radiusx";
    private static final String RADIUS_Y = "radiusy";
    private final double centerX;
    private final double centerY;
    private final double radiusX;
    private final double radiusY;

    /**
     * Constructs a new ellipse shape. With and height must be positive.
     *
     * @param centerX the horizontal coordinate of the ellipse's center
     * @param centerY the vertical coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     * @throws IllegalArgumentException if <code>rx</code> or <code>ry</code> are not positive
     *
     * @since 2.0
     */
    public Ellipse(final double centerX, final double centerY, final double radiusX, final double radiusY) {
        if (radiusX <= 0.0) {
            throw new IllegalArgumentException("radiusX");
        }

        if (radiusY <= 0.0) {
            throw new IllegalArgumentException("radiusY");
        }

        this.centerX = centerX;
        this.centerY = centerY;
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
        this.centerX = data.readDouble(CENTER_X);
        this.centerY = data.readDouble(CENTER_Y);
        this.radiusX = data.readDouble(RADIUS_X);
        this.radiusY = data.readDouble(RADIUS_Y);
    }

    @Override
    public boolean contains(final double x, final double y) {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawEllipse(0.0, 0.0, radiusX, radiusY);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.fillEllipse(0.0, 0.0, radiusX, radiusY);
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
     * Returns the horizontal coordinate of the ellipse's center.
     *
     * @return the horizontal coordinate of the ellipse's center
     *
     * @since 2.0
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Returns the vertical coordinate of the ellipse's center.
     *
     * @return the vertical coordinate of the ellipse's center
     *
     * @since 2.0
     */
    public double getCenterY() {
        return centerY;
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

    /**
     * Creates and returns a circle that approximates this ellipse. The circle has the same center as the ellipse. The
     * circle's radius is the geometric mean of the ellipse's radii.
     *
     * @return a circle approximating the ellipse
     *
     * @since 2.0
     */
    public Circle toCircle() {
        return new Circle(centerX, centerY, Math.sqrt(radiusX * radiusY));
    }

    /**
     * Creates and returns a polygon that approximates this ellipse. The number <code>n</code> specifies the number of
     * vertices of the polygon.
     *
     * @param n the number of vertices of the polygon
     * @return a polygon approximating the ellipse
     *
     * @since 2.0
     */
    public Polygon toPolygon(int n) {
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; ++i) {
            double angle = 2.0 * Math.PI * i / n;
            x[i] = centerX + radiusX * Math.cos(angle);
            y[i] = centerY + radiusY * Math.sin(angle);
        }

        return new Polygon(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Ellipse(cx=");
        result.append(centerX);
        result.append(", cy=");
        result.append(centerY);
        result.append(", rx=");
        result.append(radiusX);
        result.append(", ry=");
        result.append(radiusY);
        result.append(")");
        return result.toString();
    }

    @Override
    public void writeTo(final Data data) {
        data.writeDouble(CENTER_X, centerX);
        data.writeDouble(CENTER_Y, centerY);
        data.writeDouble(RADIUS_X, radiusX);
        data.writeDouble(RADIUS_Y, radiusY);
    }
}
