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
 * 
 * @since 1
 */
public final class Location implements Serializable {

    /**
     * <code>Location</code> representing the origin of the coordinate system.
     * Both coordinates of this location are zero.
     */
    public static final Location ORIGIN = new Location(0, 0);
    /**
     * The x coordinate of this location.
     * 
     * @since 1
     */
    public final int x;
    /**
     * The y coordinate of this location.
     * 
     * @since 1
     */
    public final int y;

    /**
     * Returns a <code>Location</code> object with the specified
     * <code>x</code> and <code>y</code> coordinates.
     * 
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return location with specified coordinates
     * 
     * @since 1
     */
    public static Location from(int x, int y) {
        if (x == 0 & y == 0) {
            return ORIGIN;
        }
        else {
            return new Location(x, y);
        }
    }

    /**
     * 
     * @param vector
     * @return 
     * 
     * @since 1
     */
    public static Location from(Vector vector) {
        if (vector == null) {
            return null;
        }
        else {
            return from((int) Math.round(vector.x), (int) Math.round(vector.y));
        }
    }

    public double distanceTo(Location location) {
        double dx = this.x - location.x;
        double dy = this.y - location.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Location ensureRange(Location min, Location max) {
        if (min == null) {
            throw new NullPointerException("min");
        }

        if (max == null) {
            throw new NullPointerException("max");
        }

        return from(Math.max(min.x, Math.min(this.x, max.x)),
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
     * Checks if this location is the origin.
     * 
     * @return <code>true</code> if this location is the origin, 
     *         <code>false</code> otherwise.
     */
    public boolean isOrigin() {
        return this.x == 0 && this.y == 0;
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

    /**
     * Returns the neighbor location of this location in the specified
     * direction.
     * 
     * @param direction
     * @return neighbor location
     */
    public Location neighbor(Direction direction) {
        return from(this.x + direction.dx, this.y + direction.dy);
    }

    public Location relativeTo(Location other) {
        return from(this.x - other.x, this.y - other.y);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('(');
        result.append(this.x);
        result.append(", ");
        result.append(this.y);
        result.append(')');
        return result.toString();
    }

    private Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
