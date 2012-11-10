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

    public static final Location ORIGIN = new Location(0, 0);
    /**
     * The x coordinate of this location.
     */
    public final int x;
    /**
     * The y coordinate of this location.
     */
    public final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location ensureRange(Location min, Location max) {
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
        return min.x <= this.x && this.x < min.x + size.width
               && min.y <= this.y && this.y < min.y + size.height;
    }

    public boolean isInside(Location min, Location max) {
        return min.x <= this.x && this.x < max.x && min.y <= this.y && this.y < max.y;
    }

    public boolean isInside(int minX, int minY, int maxX, int maxY) {
        return minX <= this.x && this.x < maxX && minY <= this.y && this.y < maxY;
    }

    public Location plus(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return new Location(this.x + size.width, this.y + size.width);
    }

//    public Location times(double factor) {
//        return new Location((int) (this.x * factor), (int) (this.y * factor));
//    }
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
//    public Vector toVector() {
//        return new Vector(this.x, this.y);
//    }
}
