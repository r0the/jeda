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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GraphicsItems {

    private final List<GraphicsItem> pendingDeletions;
    private final List<GraphicsItem> pendingInsertions;
    private final Window window;
    private GraphicsItem[] all;
    private boolean dirty;

    GraphicsItems(final Window window) {
        this.pendingDeletions = new ArrayList<GraphicsItem>();
        this.pendingInsertions = new ArrayList<GraphicsItem>();
        this.window = window;
        this.all = new GraphicsItem[0];
        this.dirty = false;
    }

    void add(final GraphicsItem object) {
        if (object != null) {
            this.pendingDeletions.remove(object);
            this.pendingInsertions.add(object);
            this.dirty = true;
        }
    }

    int count() {
        return this.all.length;
    }

    void draw(final Canvas canvas) {
        for (int i = 0; i < this.all.length; ++i) {
            this.all[i].draw(canvas);
        }
    }

    @SuppressWarnings("unchecked")
    final <T extends GraphicsItem> T[] get(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        for (final GraphicsItem item : this.all) {
            if (clazz.isInstance(item)) {
                // Unchecked cast
                result.add((T) item);
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    final GraphicsItem[] getAll() {
        return Arrays.copyOf(this.all, this.all.length);
    }

    Window getWindow() {
        return this.window;
    }

    void processPending() {
        if (this.dirty) {
            boolean allChanged = false;
            final Set<GraphicsItem> itemSet = new HashSet<GraphicsItem>(Arrays.asList(this.all));
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                final GraphicsItem item = this.pendingDeletions.get(i);
                if (itemSet.remove(item)) {
                    allChanged = true;
                    this.window.removeEventListener(item);
                    item.owner = null;
                }
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                final GraphicsItem item = this.pendingInsertions.get(i);
                if (!itemSet.contains(item)) {
                    if (itemSet.add(item)) {
                        item.owner = this;
                        this.window.addEventListener(item);
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
        if (object != null) {
            this.pendingDeletions.add(object);
            this.pendingInsertions.remove(object);
            this.dirty = true;
        }
    }

    void setDirty() {
        this.dirty = true;
    }
}
