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
 * @deprecated Don't use this class.
 */
@Deprecated
public final class Location implements Serializable {

    /**
     * The origin of the coordinate system. Both coordinates of this location are zero.
     */
    public static final Location ORIGIN = new Location();
    /**
     * The x coordinate of the location.
     */
    public final int x;
    /**
     * The y coordinate of the location.
     */
    public final int y;

    /**
     * Constructs a location. The location represents the origin. Both coordinates of this location are zero.
     */
    @Deprecated
    public Location() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructs a location. The location will have the specified <tt>x</tt>
     * and <tt>y</tt> coordinates.
     *
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance. Calculates and returns the Euclidean distance from the location to the
     * specified other location.
     *
     * @param other the other location
     * @return Euclidean distance between this and other location
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     */
    public double distanceTo(Location other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        final double dx = this.x - other.x;
        final double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Location ensureRange(Location min, Location max) {
        if (min == null) {
            throw new NullPointerException("min");
        }

        if (max == null) {
            throw new NullPointerException("max");
        }

        return new Location(Math.max(min.x, Math.min(this.x, max.x)),
                            Math.max(min.y, Math.min(this.y, max.y)));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Location) {
            final Location other = (Location) object;
            return this.x == other.x && this.y == other.y;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.y;
        return hash;
    }

    /**
     * Calculates the Manhattan distance. Calculates and returns the Manhattan distance from the location to the
     * specified other location.
     *
     * @param other the other location
     * @return Manhattan distance from this to <tt>other</tt> location.
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     */
    public int manhattanDistanceTo(Location other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public Location minus(Location other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return new Location(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns a neighbour of this location. Returns the neighbour location of the location in the specified direction.
     *
     * @param direction
     * @return neighbor location
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     */
    public Location neighbor(Direction direction) {
        if (direction == null) {
            throw new NullPointerException("direction");
        }

        return new Location(this.x + direction.dx, this.y + direction.dy);
    }

    public Location plus(Location other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return new Location(this.x + other.x, this.y + other.y);
    }

    /**
     * Converts the location to a vector. Returns a vector with the same coordinates as the location.
     *
     * @return vector representation of the location
     */
    public Vector toVector() {
        return new Vector(this.x, this.y);
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
