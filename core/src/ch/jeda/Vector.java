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
     * The x component of the vector.
     */
    public float x;
    /**
     * The y component of the vector.
     */
    public float y;

    /**
     * Constructs a vector. The constructed vector is the null vector (both coordinates are zero).
     */
    public Vector() {
        this.x = 0f;
        this.y = 0f;
    }

    /**
     * Constructs a vector. The constructed vector has the specified x and y coordinates.
     *
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     *
     * @since 1
     */
    public Vector(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a vector. The constructed vector has the specified x and y coordinates.
     *
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     *
     * @since 1
     */
    public Vector(final double x, final double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Constructs a vector. The constructed vector is a copy of the specified vector <tt>v</tt>.
     *
     * @param orig the original vector
     * @throws NullPointerException if <tt>orig</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Vector(final Vector orig) {
        if (orig == null) {
            throw new NullPointerException("orig");
        }

        this.x = orig.x;
        this.y = orig.y;
    }

    /**
     * Adds a vector. The vector <tt>other</tt> is added to the vector.
     *
     * @param other the vector to be added
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void add(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        this.x = this.x + other.x;
        this.y = this.y + other.y;
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param other other vector
     * @return dot product of both vectors
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public float dot(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns the angle enclosed by two vectors.
     *
     * @param other the other vector
     * @return angle enclosed by <tt>this</tt> and <tt>other</tt>
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public double enclosedAngle(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return Math.atan2(Math.abs(other.y - this.y), Math.abs(other.x - this.x));
    }

    /**
     * Inverts the vector. The operation inverts the direction of the vector. The length of the vector remains
     * unchanged.
     */
    public void invert() {
        this.x = -this.x;
        this.y = -this.y;
    }

    /**
     * Returns the length. Calculates and returns the length of the vector.
     *
     * @return the length of the vector
     *
     * @since 1
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Normalizes the vector. The operation sets the length of the vector to 1. The direction of the vector remains
     * unchanged.
     *
     * @since 1
     */
    public void normalize() {
        final float length = (float) this.length();
        this.x = this.x / length;
        this.y = this.y / length;
    }

    public void rotate(final double angle) {
        this.setDirection(this.direction() + angle);
    }

    /**
     * Scales the vector. The operations multiplies the length of the vector with <tt>factor</tt>. The direction of the
     * vector remains unchanged.
     *
     * @param factor the scaling factor
     */
    public void scale(final float factor) {
        this.x = this.x * factor;
        this.y = this.y * factor;
    }

    /**
     * Scales the vector. The operations multiplies the length of the vector with <tt>factor</tt>. The direction of the
     * vector remains unchanged.
     *
     * @param factor the scaling factor
     */
    public void scale(final double factor) {
        final float f = (float) factor;
        this.x = this.x * f;
        this.y = this.y * f;
    }

    /**
     * Sets the coordinates. Sets the coordinates of the vector to the specified values.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void set(final double x, final double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Sets the vector. Sets the coordinates of the vector to the coordinates of the specified vector.
     *
     * @param other the vector
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt.
     */
    public void set(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Sets the direction. Sets the direction of the vector as specified. The direction is measured mathematically. That
     * means it is measured in radians, the x-axis has the direction 0 and the y-axis has the direction of π/2. The
     * length of the vector remains unchanged.
     * <p>
     * Has no effect on the null vector.
     *
     * @param direction the new direction
     */
    public void setDirection(final double direction) {
        final float length = (float) this.length();
        this.x = length * (float) Math.sin(direction);
        this.y = length * (float) Math.cos(direction);
    }

    /**
     * Sets the length. The operations sets the length of the vector to the specified <tt>length</tt>. The direction of
     * the vector remains unchanged.
     *
     * @param length the new length
     */
    public void setLength(final float length) {
        final float factor = length / (float) this.length();
        this.x = this.x * factor;
        this.y = this.y * factor;
    }

    /**
     * Sets the length. The operations sets the length of the vector to the specified <tt>length</tt>. The direction of
     * the vector remains unchanged.
     *
     * @param length the new length
     */
    public void setLength(double length) {
        final float factor = (float) (length / this.length());
        this.x = this.x * factor;
        this.y = this.y * factor;
    }

    /**
     * Subtracts a vector. The vector <tt>other</tt> is subtracted from the vector.
     *
     * @param other the vector to be subtracted
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void subtract(final Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        this.x = this.x - other.x;
        this.y = this.y - other.y;
    }

    /**
     * Returns the direction. Calculates and returns the direction of the vector. The direction is measured
     * mathematically. That means it is measured in radians, the x-axis has the direction 0 and the y-axis has the
     * direction of π/2.
     *
     * @return current direction
     *
     * @since 1
     */
    public double direction() {
        return Math.atan2(this.y, this.x);
    }

    @Deprecated
    public void rotate90() {
        final float temp = this.x;
        this.x = -this.y;
        this.y = temp;
    }

    /**
     * Projects this vector on the other vector. The resulting vector points in the same direction as other. The length
     * of the resulting vector is equal to the dot product of this and other.
     *
     * @param other vector to be projected on
     * @return projection of this on other
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    @Deprecated
    public Vector projectOn(Vector other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        final Vector result = new Vector(other);
        result.normalize();
        result.scale(this.dot(result));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append('(');
        result.append(this.x);
        result.append(", ");
        result.append(this.y);
        result.append(')');
        return result.toString();
    }
}
