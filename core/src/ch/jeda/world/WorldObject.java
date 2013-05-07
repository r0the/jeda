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
package ch.jeda.world;

import ch.jeda.ui.Canvas;

public abstract class WorldObject {

    Objects owner;
    private float z;

    public abstract boolean contains(float x, float y);

    public abstract void draw(Canvas canvas);

    public final float getZ() {
        return this.z;
    }

    public final void setZ(final float z) {
        this.z = z;
        if (this.owner != null) {
            this.owner.setDirty();
        }
    }
}
