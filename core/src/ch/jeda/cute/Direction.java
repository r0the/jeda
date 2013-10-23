/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.cute;

public enum Direction {

    EAST(1, 0),
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    NORTH_WEST(-1, -1),
    SOUTH(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0);
    private final int dx;
    private final int dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return this.dx;
    }

    public int getDy() {
        return this.dy;
    }

    public Direction getOpposite() {
        switch (this) {
            case EAST:
                return WEST;
            case NORTH:
                return SOUTH;
            case NORTH_EAST:
                return SOUTH_WEST;
            case NORTH_WEST:
                return SOUTH_EAST;
            case SOUTH:
                return NORTH;
            case SOUTH_EAST:
                return NORTH_WEST;
            case SOUTH_WEST:
                return NORTH_EAST;
            case WEST:
                return EAST;
        }
        return null;
    }
}
