/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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

class Box {

    private final List<CuteObject> objects;
    private Block block;
    private CuteObject[] objectsArray;

    Box() {
        block = Block.EMPTY;
        objects = new ArrayList<CuteObject>();
    }

    void addObject(final CuteObject object) {
        objects.add(object);
        objectsArray = null;
    }

    Block getBlock() {
        return block;
    }

    List<CuteObject> getObjects() {
        return objects;
    }

    CuteObject[] getObjectsArray() {
        if (objectsArray == null) {
            objectsArray = ((CuteObject[]) objects.toArray(new CuteObject[objects.size()]));
        }
        return objectsArray;
    }

    void removeObject(final CuteObject object) {
        objects.remove(object);
        objectsArray = null;
    }

    void setBlock(final Block block) {
        if (block == null) {
            this.block = Block.EMPTY;
        }
        else {
            this.block = block;
        }
    }
}
