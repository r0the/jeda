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

import ch.jeda.ui.Image;
import java.util.EnumMap;

class Cute {

    private static final EnumMap<Direction, Image> SHADOWS = initShadows();

    static Image loadImage(final String name) {
        return new Image("res:jeda/cute/" + name + ".png");
    }

    static Image getShadow(final Direction direction) {
        return (Image) SHADOWS.get(direction);
    }

    private static EnumMap<Direction, Image> initShadows() {
        EnumMap<Direction, Image> result = new EnumMap<Direction, Image>(Direction.class);
        result.put(Direction.EAST, loadImage("ShadowEast"));
        result.put(Direction.NORTH, loadImage("ShadowNorth"));
        result.put(Direction.NORTH_EAST, loadImage("ShadowNorthEast"));
        result.put(Direction.NORTH_WEST, loadImage("ShadowNorthWest"));
        result.put(Direction.SOUTH, loadImage("ShadowSouth"));
        result.put(Direction.SOUTH_EAST, loadImage("ShadowSouthEast"));
        result.put(Direction.SOUTH_WEST, loadImage("ShadowSouthWest"));
        result.put(Direction.WEST, loadImage("ShadowWest"));
        return result;
    }
}
