/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
 * This class represents a location on a two-dimensional integral grid with two
 * integer coordinates. It is used to refer to the location of a pixel on a
 * drawing surface or a field on a board game.
 */
public final class Location implements Serializable {

    /**
     * <code>Location</code> representing the origin. Both coordinates of this
     * location are zero.
     */
    public static final Location ORIGIN = new Location(0, 0);
    /**
     * The x coordinate of this location.
     */
    public final int x;
    /**
     * The y coordinate of this location.
     */
    public final int y;

    /**
     * Creates a new <code>Location</code> object with the specified
     * <code>x</code> and <code>y</code>
     * coordinates.
     * 
     * @param x the x coordinate of this location
     * @param y the y coordinate of this location
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
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
            Location other = (Location) object;
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

    public Location inverse() {
        return new Location(-this.x, -this.y);
    }

    public boolean isInside(Location min, Size size) {
        if (min == null) {
            throw new NullPointerException("min");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        return min.x <= this.x && this.x < min.x + size.width
               && min.y <= this.y && this.y < min.y + size.height;
    }

    public boolean isInside(Location min, Location max) {
        if (min == null) {
            throw new NullPointerException("min");
        }

        if (max == null) {
            throw new NullPointerException("max");
        }

        return min.x <= this.x && this.x < max.x && min.y <= this.y && this.y < max.y;
    }

    public boolean isInside(int minX, int minY, int maxX, int maxY) {
        return minX <= this.x && this.x < maxX && minY <= this.y && this.y < maxY;
    }

    /**
     * Returns the Manhattan distance from this location to the
     * <code>other</code> location.
     * 
     * @param other the other location
     * @return Manhattan distance from this to <code>other</code> location.
     * @throws NullPointerException if the value of <code>other</code> is <code>null</code>
     */
    public int manhattanDistanceTo(Location other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public Location plus(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return new Location(this.x + size.width, this.y + size.width);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Location(");
        result.append(this.x);
        result.append(", ");
        result.append(this.y);
        result.append(')');
        return result.toString();
    }

    /**
     * Converts this location to a {@link ch.jeda.Vector}.
     * 
     * @return <code>Vector</code> representing this location
     */
    public Vector toVector() {
        return new Vector(this.x, this.y);
    }
}
