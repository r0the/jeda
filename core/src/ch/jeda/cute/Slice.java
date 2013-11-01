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

import java.util.ArrayList;
import java.util.List;

class Slice {

    private static final List<CuteObject> NO_OBJECTS = new ArrayList<CuteObject>();
    private final Box[] boxes;
    private final int sizeX;
    private final int sizeZ;

    Slice(final int sizeX, final int sizeZ) {
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        int size = sizeX * sizeZ;
        this.boxes = new Box[size];
        for (int i = 0; i < size; i++) {
            this.boxes[i] = new Box();
        }
    }

    void addObject(final int x, final int z, final CuteObject object) {
        if (0 <= x && x < this.sizeX &&
            0 <= z && z < this.sizeZ) {
            this.boxes[(x + z * this.sizeX)].addObject(object);
        }
    }

    Block getBlockAt(final int x, final int z) {
        final Box box = getBoxAt(x, z);
        if (box != null) {
            return box.getBlock();
        }
        else {
            return Block.EMPTY;
        }
    }

    Box getBoxAt(int x, int z) {
        if (0 <= x && x < this.sizeX &&
            0 <= z && z < this.sizeZ) {
            return this.boxes[(x + z * this.sizeX)];
        }
        else {
            return null;
        }
    }

    List<CuteObject> getObjectsAt(int x, int z) {
        final Box box = getBoxAt(x, z);
        if (box != null) {
            return box.getObjects();
        }
        else {
            return NO_OBJECTS;
        }
    }
}
