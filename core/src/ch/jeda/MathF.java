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
package ch.jeda;

import org.jbox2d.common.MathUtils;

/**
 * Provides mathematical functions and constants with <code>float</code> precision.
 *
 * @since 2.0
 */
public final class MathF {

    /**
     * The closest appxorimation of pi.
     *
     * @since 2.0
     */
    public static final float PI = MathUtils.PI;
    private static final float TWO_PI = MathUtils.TWOPI;

    private MathF() {
    }

    /**
     * Returns the arcus tangens of <code>y/x</code>.
     *
     * @param y the nomniator
     * @param x the denominator
     * @return the arcus tangens of <code>y/x</code>
     *
     * @since 2.2
     */
    public static float atan2(final float y, final float x) {
        return MathUtils.atan2(y, x);
    }

    /**
     * Returns the cosine of <code>x</code>.
     *
     * @param x the argument
     * @return the cosine of <code>x</code>
     *
     * @since 2.0
     */
    public static float cos(final float x) {
        return MathUtils.cos(x);
    }

    /**
     * Returns the Euclidean distance between the origin and the point (<code>x</code>, <code>y</code>).
     *
     * @param x the horizontal coordinate of the point
     * @param y the vertical coordinate of the point
     * @return the Euclidean distance between the origin and the point
     *
     * @since 2.4
     */
    public static float distance(final float x, final float y) {
        return sqrt(x * x + y * y);
    }

    /**
     * Returns the Euclidean distance between the points (<code>x1</code>, <code>y1</code>) and (<code>x2</code>,
     * <code>y2</code>).
     *
     * @param x1 the horizontal coordinate of the first point
     * @param y1 the vertical coordinate of the first point
     * @param x2 the horizontal coordinate of the second point
     * @param y2 the vertical coordinate of the second point
     * @return the Euclidean distance between the two points
     *
     * @since 2.4
     */
    public static float distance(final float x1, final float y1, final float x2, final float y2) {
        return distance(x1 - x2, y1 - y2);
    }

    /**
     * Returns the normalized value of <code>angle</code>. The normalized angle is the value between <code>0</code> and
     * <code>2 * PI</code>.
     *
     * @param angle the original angle
     * @return the normalized angle
     *
     * @since 2.0
     */
    public static float normalizeAngle(float angle) {
        angle = angle % TWO_PI;
        if (angle < 0) {
            angle = angle + TWO_PI;
        }

        return angle;
    }

    /**
     * Returns the sine of <code>x</code>.
     *
     * @param x the argument
     * @return the sine of <code>x</code>
     *
     * @since 2.0
     */
    public static float sin(final float x) {
        return MathUtils.sin(x);
    }

    /**
     * Returns the square root of <code>x</code>.
     *
     * @param x the argument
     * @return the square root of <code>x</code>
     *
     * @since 2.0
     */
    public static float sqrt(final float x) {
        return MathUtils.sqrt(x);
    }

    /**
     * Converts an angle measured in radians to an angle measured in degrees.
     *
     * @param angleRad the angle measured in radians
     * @return the angle measured in degrees
     *
     * @since 2.0
     */
    public static float toDegrees(final float angleRad) {
        return (float) Math.toDegrees(angleRad);
    }

    /**
     * Converts an angle measured in degrees to an angle measured in radians.
     *
     * @param angleDeg the angle measured in degrees
     * @return the angle measured in radians
     *
     * @since 2.0
     */
    public static float toRadians(final float angleDeg) {
        return (float) Math.toRadians(angleDeg);
    }
}
