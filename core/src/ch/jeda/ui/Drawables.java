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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Drawables {

    private static final Comparator<Drawable> SORT_ORDER = new GraphicsItemSortOrder();
    private final List<Drawable> items;
    private final Set<Drawable> pendingDeletions;
    private final Set<Drawable> pendingInsertions;

    Drawables() {
        this.items = new ArrayList<Drawable>();
        this.pendingDeletions = new HashSet<Drawable>();
        this.pendingInsertions = new HashSet<Drawable>();
    }

    final void addDrawable(final Drawable item) {
        if (item != null) {
            if (this.items.contains(item)) {
                this.pendingDeletions.remove(item);
            }
            else if (!this.pendingInsertions.contains(item)) {
                this.pendingInsertions.add(item);
            }
        }
    }

    final void draw(final Canvas canvas) {
        if (!this.pendingDeletions.isEmpty()) {
            this.items.removeAll(this.pendingDeletions);
            this.pendingDeletions.clear();
        }

        if (!this.pendingInsertions.isEmpty()) {
            this.items.addAll(this.pendingInsertions);
            Collections.sort(this.items, SORT_ORDER);
            this.pendingInsertions.clear();
        }

        for (int i = 0; i < this.items.size(); ++i) {
            this.items.get(i).draw(canvas);
        }
    }

    final void removeDrawable(final Drawable item) {
        if (item != null) {
            if (!this.items.contains(item)) {
                this.pendingInsertions.remove(item);
            }
            else if (!this.pendingDeletions.contains(item)) {
                this.pendingDeletions.add(item);
            }
        }
    }

    private static class GraphicsItemSortOrder implements Comparator<Drawable> {

        @Override
        public int compare(final Drawable o1, final Drawable o2) {
            return o1.drawOrder() - o2.drawOrder();
        }
    }
}
