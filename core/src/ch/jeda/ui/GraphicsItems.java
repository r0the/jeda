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
package ch.jeda.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GraphicsItems {

    private final List<GraphicsItem> pendingDeletions;
    private final List<GraphicsItem> pendingInsertions;
    private GraphicsItem[] all;
    private boolean dirty;

    GraphicsItems() {
        this.pendingDeletions = new ArrayList<GraphicsItem>();
        this.pendingInsertions = new ArrayList<GraphicsItem>();
        this.all = new GraphicsItem[0];
        this.dirty = false;
    }

    void add(final GraphicsItem object) {
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

    final GraphicsItem[] getAll() {
        return Arrays.copyOf(this.all, this.all.length);
    }

    void processPending() {
        if (this.dirty) {
            boolean allChanged = false;
            final Set<GraphicsItem> itemSet = new HashSet<GraphicsItem>(Arrays.asList(this.all));
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                final GraphicsItem item = this.pendingDeletions.get(i);
                if (itemSet.remove(item)) {
                    allChanged = true;
                    item.owner = null;
                }
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                final GraphicsItem item = this.pendingInsertions.get(i);
                if (!itemSet.contains(item)) {
                    if (itemSet.add(item)) {
                        item.owner = this;
                        allChanged = true;
                    }
                }
            }

            if (allChanged) {
                this.all = itemSet.toArray(new GraphicsItem[itemSet.size()]);
            }

            Arrays.sort(this.all, GraphicsItem.DRAW_ORDER);
            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
            this.dirty = false;
        }
    }

    void remove(final GraphicsItem object) {
        this.pendingDeletions.add(object);
        this.pendingInsertions.remove(object);
        this.dirty = true;
    }

    void setDirty() {
        this.dirty = true;
    }
}
