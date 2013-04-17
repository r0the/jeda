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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the eight compass directions.
 *
 * @since 1
 */
public final class Direction implements Serializable {

    /**
     * The direction east. The text representation of this direction is
     * <tt>"East"</tt>. Its number is <tt>0</tt>.
     *
     * @since 1
     */
    public static final Direction EAST = new Direction(1, 0, "East", 0);
    /**
     * The direction north. The text representation of this direction is
     * <tt>"North"</tt>. Its number is <tt>2</tt>.
     *
     * @since 1
     */
    public static final Direction NORTH = new Direction(0, -1, "North", 2);
    /**
     * The direction north east. The text representation of this direction is
     * <tt>"NorthEast"</tt>. Its number is <tt>1</tt>.
     *
     * @since 1
     */
    public static final Direction NORTH_EAST = new Direction(1, -1, "NorthEast", 1);
    /**
     * The direction north west. The text representation of this direction is
     * <tt>"NorthWest"</tt>. Its number is <tt>3</tt>.
     *
     * @since 1
     */
    public static final Direction NORTH_WEST = new Direction(-1, -1, "NorthWest", 3);
    /**
     * The direction south. The text representation of this direction is
     * <tt>"South"</tt>. Its number is <tt>6</tt>.
     *
     * @since 1
     */
    public static final Direction SOUTH = new Direction(0, 1, "South", 6);
    /**
     * The direction south east. The text representation of this direction is
     * <tt>"SouthEast"</tt>. Its number is <tt>7</tt>.
     *
     * @since 1
     */
    public static final Direction SOUTH_EAST = new Direction(1, 1, "SouthEast", 7);
    /**
     * The direction south west. The text representation of this direction is
     * <tt>"SouthWest"</tt>. Its number is <tt>5</tt>.
     *
     * @since 1
     */
    public static final Direction SOUTH_WEST = new Direction(-1, 1, "SouthWest", 5);
    /**
     * The direction west. The text representation of this direction is
     * <tt>"West"</tt>. Its number is <tt>4</tt>.
     *
     * @since 1
     */
    public static final Direction WEST = new Direction(-1, 0, "West", 4);
    /**
     * A list of all eight directions. The list is ordered counterclickwise and
     * starts with the direction {@link #EAST}.
     *
     * @since 1
     */
    public static final List<Direction> ALL = initAllDirections();
    /**
     * The number of the direction.
     *
     * @since 1
     */
    public final int number;
    public final int dx;
    public final int dy;
    private static final Map<String, Direction> NAME_MAP = initNameMap();
    private final String name;

    /**
     * Returns the direction for the specified name. Returns <tt>null</tt> if
     * the specified name is not valid.
     *
     * @param name the name of a direction
     * @return direction for the specified number or <tt>null</tt>
     *
     * @since 1
     */
    public static Direction parse(final String name) {
        if (NAME_MAP.containsKey(name)) {
            return NAME_MAP.get(name);
        }
        else {
            return null;
        }
    }

    /**
     * Returns a direction clockwise of the direction. Returns the direction
     * that lies <tt>amount</tt> steps clockwise of this direction.
     * <p>
     * For example, <tt>Direction.NORTH.clockwise(2)</tt> will return
     * <tt>Direction.EAST</tt>.
     *
     * @param amount the number of steps to turn
     * @return the new direction
     *
     * @since 1
     */
    public Direction clockwise(final int amount) {
        if (amount < 0) {
            return this.counterclockwise(-amount);
        }
        else {
            return ALL.get((this.number + 8 - (amount % 8)) % 8);
        }
    }

    /**
     * Returns a direction counterclockwise of this direction. Returns the
     * direction that lies <tt>amount</tt> steps counterclockwise of this
     * direction.
     * <p>
     * For example, <tt>Direction.NORTH.counterclockwise(2)</tt> will return
     * <tt>Direction.WEST</tt>.
     *
     * @param amount the number of steps to turn
     * @return the new direction
     *
     * @since 1
     */
    public Direction counterclockwise(final int amount) {
        if (amount < 0) {
            return this.clockwise(-amount);
        }
        else {
            return ALL.get((this.number + amount) % 8);
        }
    }

    @Override
    public boolean equals(final Object object) {
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
        return 23 + 17 * this.number;
    }

    /**
     * Returns the opposite direction. Returns the direction that points
     * opposite to the direction.
     * <p>
     * For example, <tt>Direction.NORTH.opposite()</tt> will return
     * <tt>Direction.SOUTH</tt>.
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

    private Direction(final int dx, final int dy, final String name,
                      final int value) {
        this.dx = dx;
        this.dy = dy;
        this.name = name;
        this.number = value;
    }

    private static List<Direction> initAllDirections() {
        final List<Direction> result = new ArrayList<Direction>();
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

    private static Map<String, Direction> initNameMap() {
        final Map<String, Direction> result = new HashMap<String, Direction>();
        for (Direction direction : ALL) {
            result.put(direction.toString(), direction);
        }

        return result;
    }
}
