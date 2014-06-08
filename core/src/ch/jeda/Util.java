/*
 * Copyright (C) 2012 - 2014 by Stefan Rothe
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

/**
 * Provides utility functions.
 *
 * @since 1.0
 */
public final class Util {

    /**
     * Returns the Euclidean distance between the origin and the point <tt>(x, y)</tt>.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the Euclidean distance between the origin and the point
     *
     * @since 1.0
     */
    public static double distance(final double x, final double y) {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Checks if a <tt>float</tt> number is almost zero. Return <tt>true</tt> if the value of <tt>f</tt> is closer to
     * zero than five times the smallest representable <tt>float</tt> value.
     *
     * @param f the <tt>float</tt> value
     * @return <tt>true</tt> if <tt>f</tt> is almost zero, <tt>false</tt> otherwise.
     *
     * @since 1.0
     */
    public static boolean isZero(final float f) {
        return Math.abs(f) < 5f * Float.MIN_VALUE;
    }

    /**
     * @deprecated Use {@link Jeda#loadTextFile(java.lang.String)} instead.
     * @since 1.0
     */
    public static String[] loadTextFile(final String filePath) {
        return Jeda.loadTextFile(filePath);
    }

    /**
     * Returns a random <tt>int</tt> value between <tt>0</tt> and <tt>(max - 1)</tt>.
     *
     * @param max the upper limit for the random number
     * @return random number
     *
     * @since 1.0
     */
    public static int randomInt(final int max) {
        return (int) (Math.random() * max);
    }
}
