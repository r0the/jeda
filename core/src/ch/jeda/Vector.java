/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda;

import java.io.Serializable;

/**
 * Represents a two-dimensional vector.
 * 
 * @since 1
 */
public final class Vector implements Serializable {

    /**
     * The x component of this vector.
     */
    public final double x;
    /**
     * The y component of this vector.
     */
    public final double y;
    private double direction;
    /**
     * The null vector. Both components of this vector are 0.
     */
    public static final Vector NULL = new Vector();

    public Vector() {
        this.x = 0.0;
        this.y = 0.0;
        this.direction = 0.0;
    }

    /**
     * Creates a new Vector with the specified x and y coordinates.
     *
     * @param x x coordinate of the new vector
     * @param y y coordinate of the new vector
     *
     * @since 1
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.direction = Double.NaN;
    }

    /**
     * Creates a new Vector from polar coordinates.
     * 
     * @param length the length of the new vector
     * @param direction the direction of the new vector in degrees
     * @return new vector
     * 
     * @since 1
     */
    public static Vector fromPolar(double length, double direction) {
        direction = normalizeDegrees(direction);
        return new Vector(length * cos(direction), -length * sin(direction),
                          direction);
    }

    /**
     * 
     * @param other
     * @return 
     * 
     * @since 1
     */
    public double enclosedAngle(Vector other) {
        return angle(Math.abs(other.x - this.x), Math.abs(other.y - this.y));
    }

    /**
     * Returns the current direction. The direction is value between 0 and 360
     * degrees (including 0, excluding 360). A direction of 0 degrees
     * points to the north, a direction of 90 degrees to the east. 180 degrees
     * point to the south and 270 degrees to the west.
     *
     * @return current direction
     *
     * @since 1
     */
    public double direction() {
        if (Double.isNaN(this.direction)) {
            this.direction = angle(this.x, this.y);
        }
        return this.direction;
    }

    /**
     * Returns the dot product of this vector and <code>other</code>.
     *
     * @param other other vector
     * @return dot product of both vectors
     *
     * @since 1
     */
    public double dot(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns the inverse vector of this vector, which is the vector with the
     * same length as this vector pointing in the opposite direction.
     *
     * @return inverse of this vector
     * 
     * @since 1
     */
    public Vector inverse() {
        return new Vector(-this.x, -this.y);
    }

    /**
     * Returns the current length. The length of a vector is never negative.
     *
     * @return current length
     * 
     * @since 1
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * 
     * @param other
     * @return 
     * 
     * @since 1
     */
    public Vector minus(Vector other) {
        return new Vector(this.x + (-other.x), this.y + (-other.y));
    }

    /**
     * 
     * @return 
     * 
     * @since 1
     */
    public Vector normal() {
        return new Vector(-this.y, this.x);
    }

    /**
     * 
     * @return 
     * 
     * @since 1
     */
    public Vector normalized() {
        return this.times(1.0 / this.length());
    }

    /**
     * 
     * @param other
     * @return 
     * 
     * @since 1
     */
    public Vector plus(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    /**
     * Projects this vector on the other vector. The resulting vector points in
     * the same direction as other. The length of the resulting vector is
     * equal to the dot product of this and other.
     *
     * @param other vector to be projected on
     * @return projection of this on other
     * @since 1
     */
    public Vector projectOn(Vector other) {
        final Vector on = other.normalized();
        return on.times(this.dot(on));
    }

    /**
     * 
     * @param angle
     * @return 
     * 
     * @since 1
     */
    public Vector rotatedBy(double angle) {
        return Vector.fromPolar(this.length(), this.direction() + angle);
    }

    /**
     * 
     * @param v
     * @return 
     * 
     * @since 1
     */
    public Vector times(double v) {
        return new Vector(this.x * v, this.y * v);
    }

    /**
     * 
     * @param length
     * @return 
     * 
     * @since 1
     */
    public Vector withLength(double length) {
        double myLength = this.x * this.x + this.y * this.y;
        if (myLength == 0) {
            return fromPolar(length, this.direction);
        }
        else {
            return this.times(length / Math.sqrt(myLength));
        }
    }

    public Vector withDirection(double direction) {
        return fromPolar(this.length(), direction);
    }

    /**
     * Converts this vector to a location. The locations coordinates
     * correspond to the vector's rounded coordinates.
     * 
     * @return location corresponding to this vector
     * 
     * @since 1
     */
    public Location toLocation() {
        return new Location((int) Math.round(this.x),
                            (int) Math.round(this.y));
    }

    /**
     * Converts this vector to a size. The resulting size's width and height
     * are the rounded absolute values of this vector's coordinates.
     * 
     * @return size corresponding to this vector
     * 
     * @since 1
     */
    public Size toSize() {
        return new Size((int) Math.round(Math.abs(this.x)),
                        (int) Math.round(Math.abs(this.y)));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(x=");
        result.append(this.x);
        result.append(", y=");
        result.append(this.y);
        result.append(", dir=");
        result.append(this.direction());
        result.append(", len=");
        result.append(this.length());
        result.append(")");
        return result.toString();
    }

    private Vector(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    private static double asHeading(double radians) {
        return normalizeDegrees(90 - Math.toDegrees(radians));
    }

    private static double asAngle(double heading) {
        return Math.toRadians(90 - heading);
    }

    private static double normalizeDegrees(double degrees) {
        degrees %= 360;
        if (degrees < 0) {
            degrees += 360;
        }
        if (degrees == 360) {
            return 0;
        }
        return degrees;
    }

    private static double cos(double degrees) {
        return Math.cos(asAngle(degrees));
    }

    private static double sin(double degrees) {
        return Math.sin(asAngle(degrees));
    }

    private static double angle(double x, double y) {
        return asHeading(Math.atan2(-y, x));
    }
}
