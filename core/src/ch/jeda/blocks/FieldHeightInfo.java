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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class FieldHeightInfo {

    private final Field field;
    private final Set<Direction> shadows;
    private final Map<Direction, Integer> slope;

    FieldHeightInfo(Field field) {
        this.field = field;
        this.shadows = new HashSet<Direction>();
        this.slope = new HashMap<Direction, Integer>();
        for (Direction direction : Direction.ALL) {
            this.slope.put(direction, 0);
        }
    }

    Set<Direction> shadows() {
        return this.shadows;
    }

    int slope(Direction direction) {
        return this.slope.get(direction);
    }

    void update() {
        this.calculateSlopes();
        this.calculateShadows();
    }

    private void calculateSlopes() {
        this.slope.clear();
        for (Direction direction : Direction.ALL) {
            this.slope.put(direction, this.calculateSlope(direction));
        }
    }

    private int calculateSlope(Direction direction) {
        int neighbourHeight = 0;
        Field neighbor = this.field.neighbor(direction);
        if (neighbor != null) {
            neighbourHeight = neighbor.height();
        }
        return neighbourHeight - this.field.height();
    }

    private void calculateShadows() {
        int slopeE = this.slope.get(Direction.EAST);
        int slopeN = this.slope.get(Direction.NORTH);
        int slopeNE = this.slope.get(Direction.NORTH_EAST);
        int slopeNW = this.slope.get(Direction.NORTH_WEST);
        int slopeS = this.slope.get(Direction.SOUTH);
        int slopeSE = this.slope.get(Direction.SOUTH_EAST);
        int slopeSW = this.slope.get(Direction.SOUTH_WEST);
        int slopeW = this.slope.get(Direction.WEST);
        this.shadows.clear();
        if (slopeSE > 0 && slopeS <= 0 && slopeE <= 0) {
            this.shadows.add(Direction.SOUTH_EAST);
        }
        if (slopeS > 0) {
            this.shadows.add(Direction.SOUTH);
        }
        if (slopeSW > 0 && slopeS <= 0 && slopeW <= 0) {
            this.shadows.add(Direction.SOUTH_WEST);
        }
        if (slopeE > 0) {
            this.shadows.add(Direction.EAST);
        }
        if (slopeW > 0) {
            this.shadows.add(Direction.WEST);
        }
        if (slopeNE > 0 && slopeN <= 0 && slopeE <= 0) {
            this.shadows.add(Direction.NORTH_EAST);
        }
        if (slopeN > 0) {
            this.shadows.add(Direction.NORTH);
        }
        if (slopeNW > 0 && slopeN <= 0 && slopeW <= 0) {
            this.shadows.add(Direction.NORTH_WEST);
        }
    }
}
