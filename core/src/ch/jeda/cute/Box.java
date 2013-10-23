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

class Box {

    private final List<CuteObject> objects;
    private Block block;
    private CuteObject[] objectsArray;

    Box() {
        this.block = Block.EMPTY;
        this.objects = new ArrayList();
    }

    void addObject(final CuteObject object) {
        this.objects.add(object);
        this.objectsArray = null;
    }

    Block getBlock() {
        return this.block;
    }

    List<CuteObject> getObjects() {
        return this.objects;
    }

    CuteObject[] getObjectsArray() {
        if (this.objectsArray == null) {
            this.objectsArray = ((CuteObject[]) this.objects.toArray(new CuteObject[this.objects.size()]));
        }
        return this.objectsArray;
    }

    void removeObject(final CuteObject object) {
        this.objects.remove(object);
        this.objectsArray = null;
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
