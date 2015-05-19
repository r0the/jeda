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
import static ch.jeda.MathF.*;

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
    private final float centerX;
    private final float centerY;
    private final float radiusX;
    private final float radiusY;

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
        this((float) centerX, (float) centerY, (float) radiusX, (float) radiusY);
    }

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
    public Ellipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
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
        this.centerX = data.readFloat(CENTER_X);
        this.centerY = data.readFloat(CENTER_Y);
        this.radiusX = data.readFloat(RADIUS_X);
        this.radiusY = data.readFloat(RADIUS_Y);
    }

    @Override
    public boolean contains(final float x, final float y) {
        //TODO
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawEllipse(centerX, centerY, radiusX, radiusY);
    }

    @Override
    public void fill(final Canvas canvas) {
        canvas.fillEllipse(centerX, centerY, radiusX, radiusY);
    }

    /**
     * Returns the area of this ellipse.
     *
     * @return the area of this ellipse
     *
     * @since 2.0
     */
    public float getArea() {
        return PI * radiusX * radiusY;
    }

    /**
     * Returns the horizontal coordinate of the ellipse's center.
     *
     * @return the horizontal coordinate of the ellipse's center
     *
     * @since 2.0
     */
    public float getCenterX() {
        return centerX;
    }

    /**
     * Returns the vertical coordinate of the ellipse's center.
     *
     * @return the vertical coordinate of the ellipse's center
     *
     * @since 2.0
     */
    public float getCenterY() {
        return centerY;
    }

    /**
     * Returns the eccentricity of this ellipse.
     *
     * @return the eccentricity of this ellipse
     *
     * @since 2.0
     */
    public float getEccentricity() {
        if (radiusX > radiusY) {
            return sqrt(1 - (radiusY * radiusY / (radiusX * radiusX)));
        }
        else {
            return sqrt(1 - (radiusX * radiusX / (radiusY * radiusY)));
        }
    }

    /**
     * Returns the horizontal radius of this ellipse.
     *
     * @return the horizontal radius of this ellipse
     *
     * @since 2.0
     */
    public float getRadiusX() {
        return radiusX;
    }

    /**
     * Returns the vertical radius of this ellipse.
     *
     * @return the vertical radius of this ellipse
     *
     * @since 2.0
     */
    public float getRadiusY() {
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
        return new Circle(centerX, centerY, sqrt(radiusX * radiusY));
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
        final float[] points = new float[2 * n];
        for (int i = 0; i < n; i = i + 2) {
            float angle = 2f * PI * i / n;
            points[i] = centerX + radiusX * cos(angle);
            points[i + 1] = centerY + radiusY * sin(angle);
        }

        return new Polygon(points);
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
        data.writeFloat(CENTER_X, centerX);
        data.writeFloat(CENTER_Y, centerY);
        data.writeFloat(RADIUS_X, radiusX);
        data.writeFloat(RADIUS_Y, radiusY);
    }
}
