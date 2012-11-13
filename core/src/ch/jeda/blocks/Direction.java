/*
 * Copyright (C) 2011 by Stefan Rothe
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
package ch.jeda.blocks;

import ch.jeda.Location;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a directions.
 */
public final class Direction {

    /**
     * The direction east.
     */
    public static final Direction EAST = new Direction(1, 0, "East");
    /**
     * The direction north.
     */
    public static final Direction NORTH = new Direction(0, -1, "North");
    /**
     * The direction north east.
     */
    public static final Direction NORTH_EAST = new Direction(1, -1, "NorthEast");
    /**
     * The direction north west.
     */
    public static final Direction NORTH_WEST = new Direction(-1, -1, "NorthWest");
    /**
     * The direction south.
     */
    public static final Direction SOUTH = new Direction(0, 1, "South");
    /**
     * The direction south east.
     */
    public static final Direction SOUTH_EAST = new Direction(1, 1, "SouthEast");
    /**
     * The direction south west.
     */
    public static final Direction SOUTH_WEST = new Direction(-1, 1, "SouthWest");
    /**
     * The direction west.
     */
    public static final Direction WEST = new Direction(-1, 0, "West");
    /**
     * A list of all eight directions. The list starts with NORTH and iterates
     * through the directions clockwise.
     */
    public static final Iterable<Direction> ALL = allDirections();
    private static final Map<Direction, Direction> INVERSE_MAP = inverseMap();
    private final int dx;
    private final int dy;
    private final String name;

    private static Iterable<Direction> allDirections() {
        List<Direction> result = new ArrayList<Direction>();
        result.add(NORTH);
        result.add(NORTH_EAST);
        result.add(EAST);
        result.add(SOUTH_EAST);
        result.add(SOUTH);
        result.add(SOUTH_WEST);
        result.add(WEST);
        result.add(NORTH_WEST);
        return result;
    }

    private static Map<Direction, Direction> inverseMap() {
        Map<Direction, Direction> result = new HashMap<Direction, Direction>();
        result.put(NORTH, SOUTH);
        result.put(NORTH_EAST, SOUTH_WEST);
        result.put(EAST, WEST);
        result.put(SOUTH_EAST, NORTH_WEST);
        result.put(SOUTH, NORTH);
        result.put(SOUTH_WEST, NORTH_EAST);
        result.put(WEST, EAST);
        result.put(NORTH_WEST, SOUTH_EAST);
        return result;
    }

    private Direction(int dx, int dy, String name) {
        this.dx = dx;
        this.dy = dy;
        this.name = name;
    }

    public Direction inverse() {
        return INVERSE_MAP.get(this);
    }

    public Location targetLocation(Location origin) {
        return new Location(origin.x + this.dx, origin.y + this.dy);
    }

    public Location getDelta() {
        return new Location(dx, dy);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
