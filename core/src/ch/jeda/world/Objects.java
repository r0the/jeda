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
import ch.jeda.ui.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Objects {

    private static final Comparator<WorldObject> BODIES_BY_Z = new BodiesByZ();
    private final List<WorldObject> pendingDeletions;
    private final List<WorldObject> pendingInsertions;
    private WorldObject[] all;
    private boolean dirty;

    Objects() {
        this.pendingDeletions = new ArrayList<WorldObject>();
        this.pendingInsertions = new ArrayList<WorldObject>();
        this.all = new WorldObject[0];
    }

    void add(final WorldObject object) {
        this.pendingDeletions.remove(object);
        this.pendingInsertions.add(object);
        this.dirty = true;
    }

    int count() {
        return this.all.length;
    }

    void draw(final Canvas canvas) {
        for (int i = 0; i < this.all.length; ++i) {
            this.all[i].draw(canvas);
        }
    }

    void drawCollisionShapes(final Canvas canvas) {
//        for (int i = 0; i < this.all.length; ++i) {
//            final Shape shape = this.all[i].getCollisionShape();
//            canvas.setColor(DEBUG_FILL_COLOR);
//            shape.fill(canvas);
//            canvas.setColor(DEBUG_OUTLINE_COLOR);
//            shape.draw(canvas);
//        }
    }

    final WorldObject[] getAll() {
        return Arrays.copyOf(this.all, this.all.length);
    }

    void processPending() {
        if (this.dirty) {
            boolean allChanged = false;
            final Set<WorldObject> objectSet = new HashSet<WorldObject>(Arrays.asList(this.all));
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                final WorldObject object = this.pendingDeletions.get(i);
                if (objectSet.remove(object)) {
                    allChanged = true;
                    object.owner = null;
                }
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                final WorldObject object = this.pendingInsertions.get(i);
                if (!objectSet.contains(object)) {
                    if (objectSet.add(object)) {
                        object.owner = this;
                        allChanged = true;
                    }

                }
            }

            if (allChanged) {
                this.all = objectSet.toArray(new WorldObject[objectSet.size()]);
            }

            Arrays.sort(this.all, BODIES_BY_Z);
            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
            this.dirty = false;
        }
    }

    void remove(final WorldObject object) {
        this.pendingDeletions.add(object);
        this.pendingInsertions.remove(object);
        this.dirty = true;
    }

    void setDirty() {
        this.dirty = true;
    }

    private static class BodiesByZ implements Comparator<WorldObject> {

        @Override
        public int compare(final WorldObject object1, final WorldObject object2) {
            return (int) Math.signum(object1.getZ() - object2.getZ());
        }
    }
}
