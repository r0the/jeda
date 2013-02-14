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
 * unique <code>int</code> number ranging from 0 for east counterclockwise to 7
 * for south east.
 * 
 * @since 1
 */
public final class Direction implements Serializable {

    /**
     * The direction east. This direction has the value 0.
     * 
     * @since 1
     */
    public static final Direction EAST = new Direction(1, 0, "East", 0);
    /**
     * The direction north. This direction has the value 2.
     * 
     * @since 1
     */
    public static final Direction NORTH = new Direction(0, -1, "North", 2);
    /**
     * The direction north east. This direction has the value 1.
     * 
     * @since 1
     */
    public static final Direction NORTH_EAST = new Direction(1, -1, "NorthEast", 1);
    /**
     * The direction north west. This direction has the value 3.
     * 
     * @since 1
     */
    public static final Direction NORTH_WEST = new Direction(-1, -1, "NorthWest", 3);
    /**
     * The direction south. This direction has the value 6.
     * 
     * @since 1
     */
    public static final Direction SOUTH = new Direction(0, 1, "South", 6);
    /**
     * The direction south east. This direction has the value 7.
     * 
     * @since 1
     */
    public static final Direction SOUTH_EAST = new Direction(1, 1, "SouthEast", 7);
    /**
     * The direction south west. This direction has the value 5.
     * 
     * @since 1
     */
    public static final Direction SOUTH_WEST = new Direction(-1, 1, "SouthWest", 5);
    /**
     * The direction west. This direction has the value 4.
     * 
     * @since 1
     */
    public static final Direction WEST = new Direction(-1, 0, "West", 4);
    private static final List<Direction> LIST = initAllDirections();
    /**
     * A iterator of all eight directions. it starts with north and iterates
     * clockwise through all directions.
     * 
     * @since 1
     */
    public static final Iterable<Direction> ALL = LIST;
    /**
     * The number of this direction.
     * 
     * @since 1
     */
    public final int number;
    final int dx;
    final int dy;
    private final String name;

    /**
     * Returns the direction for the specified number.
     * Returns <code>null</code> if the specified number is not between 0 and 7.
     * 
     * @param number the number of a direction
     * @return direction for the specified number or <code>null</code>
     * 
     * @since 1
     */
    public static Direction from(int number) {
        if (number < 0 || number > 7) {
            return null;
        }
        else {
            return LIST.get(number);
        }
    }

    /**
     * Returns the direction for the specified name.
     * Returns <code>null</code> if the specified name is not valid.
     * 
     * @param name the name of a direction
     * @return direction for the specified number or <code>null</code>
     * 
     * @since 1
     */
    public static Direction parse(String name) {
        for (Direction direction : LIST) {
            if (direction.name.equals(name)) {
                return direction;
            }
        }

        return null;
    }

    /**
     * Returns the direction that lies <code>amount</code> steps clockwise
     * of this direction.
     * For example, <code>NORTH.clockwise(2)</code> will return
     * <code>EAST</code>
     * 
     * @param amount the number of steps to turn
     * @return the new direction
     * 
     * @since 1
     */
    public Direction clockwise(int amount) {
        if (amount < 0) {
            return this.counterclockwise(-amount);
        }
        else {
            return LIST.get((number + 8 - (amount % 8)) % 8);
        }
    }

    /**
     * Returns the direction that lies <code>amount</code> steps
     * counterclockwise of this direction.
     * For example, <code>NORTH.counterclockwise(2)</code> will return
     * <code>WEST</code>
     * 
     * @param amount the number of steps to turn
     * @return the new direction
     * 
     * @since 1
     */
    public Direction counterclockwise(int amount) {
        if (amount < 0) {
            return this.clockwise(-amount);
        }
        else {
            return LIST.get((number + amount) % 8);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Direction) {
            final Direction other = (Direction) object;
            return this.number == other.number;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 17 * this.number;
    }

    /**
     * Returns the direction opposite to this direction.
     * 
     * @return the opposite direction
     * 
     * @since 1
     */
    public Direction opposite() {
        return this.clockwise(4);
    }

    /**
     * Returns the name of this direction.
     * 
     * @return the name of this direction
     * 
     * @since 1
     */
    @Override
    public String toString() {
        return this.name;
    }

    private Direction(int dx, int dy, String name, int value) {
        this.dx = dx;
        this.dy = dy;
        this.name = name;
        this.number = value;
    }

    private static List<Direction> initAllDirections() {
        List<Direction> result = new ArrayList();
        result.add(EAST);
        result.add(NORTH_EAST);
        result.add(NORTH);
        result.add(NORTH_WEST);
        result.add(WEST);
        result.add(SOUTH_WEST);
        result.add(SOUTH);
        result.add(SOUTH_EAST);
        return Collections.unmodifiableList(result);
    }
}
