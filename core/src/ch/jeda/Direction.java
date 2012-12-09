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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the eight compass directions. Each direction has a
 * unique <code>int</code> value ranging from 0 for east counterclockwise to
 * 7 for south east.
 */
public final class Direction implements Serializable {

    /**
     * The direction east. This direction has the value 2.
     */
    public static final Direction EAST = new Direction(1, 0, "East", 2);
    /**
     * The direction north. This direction has the value 0.
     */
    public static final Direction NORTH = new Direction(0, -1, "North", 0);
    /**
     * The direction north east. This direction has the value 1.
     */
    public static final Direction NORTH_EAST = new Direction(1, -1, "NorthEast", 1);
    /**
     * The direction north west. This direction has the value 7.
     */
    public static final Direction NORTH_WEST = new Direction(-1, -1, "NorthWest", 7);
    /**
     * The direction south. This direction has the value 4.
     */
    public static final Direction SOUTH = new Direction(0, 1, "South", 4);
    /**
     * The direction south east. This direction has the value 3.
     */
    public static final Direction SOUTH_EAST = new Direction(1, 1, "SouthEast", 3);
    /**
     * The direction south west. This direction has the value 5.
     */
    public static final Direction SOUTH_WEST = new Direction(-1, 1, "SouthWest", 5);
    /**
     * The direction west. This direction has the value 6.
     */
    public static final Direction WEST = new Direction(-1, 0, "West", 6);
    public static final List<Direction> LIST = initAllDirections();
    /**
     * A iterator of all eight directions. it starts with north and iterates
     * clockwise through all directions.
     */
    public static final Iterable<Direction> ALL = LIST;
    private final int dx;
    private final int dy;
    private final int value;
    private final String name;

    private Direction(int dx, int dy, String name, int value) {
        this.dx = dx;
        this.dy = dy;
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the value of this direction.
     * 
     * @return the value of this direction
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns the direction to the left of this direction by 
     * <code>amount</code> steps.
     * For example, <code>NORTH.left(2)</code> will return <code>WEST</code>
     * 
     * @param amount the number of steps to turn
     * @return the new direction
     */
    public Direction left(int amount) {
        if (amount < 0) {
            return this.right(-amount);
        }
        else {
            return LIST.get((value + 8 - (amount % 8)) % 8);
        }
    }

    /**
     * Returns the direction opposite to this direction.
     * 
     * @return the opposite direction
     */
    public Direction opposite() {
        return this.left(4);
    }

    /**
     * Returns the direction to the right of this direction by 
     * <code>amount</code> steps.
     * For example, <code>NORTH.right(2)</code> will return <code>EAST</code>
     * 
     * @param amount the number of steps to turn
     * @return the new direction
     */
    public Direction right(int amount) {
        if (amount < 0) {
            return this.left(-amount);
        }
        else {
            return LIST.get((value + amount) % 8);
        }
    }

    public Location targetLocation(Location origin) {
        return Location.from(origin.x + this.dx, origin.y + this.dy);
    }

    public Location getDelta() {
        return Location.from(dx, dy);
    }

    @Override
    public String toString() {
        return this.name;
    }

    private static List<Direction> initAllDirections() {
        List<Direction> result = new ArrayList();
        result.add(NORTH);
        result.add(NORTH_EAST);
        result.add(EAST);
        result.add(SOUTH_EAST);
        result.add(SOUTH);
        result.add(SOUTH_WEST);
        result.add(WEST);
        result.add(NORTH_WEST);
        return Collections.unmodifiableList(result);
    }
}
