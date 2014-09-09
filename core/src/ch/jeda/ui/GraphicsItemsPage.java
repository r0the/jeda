/*
 * Copyright (C) 2014 by Stefan Rothe
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class GraphicsItemsPage {

    final Window window;
    private final Set<GraphicsItem> itemSet;
    private final String name;
    private boolean active;
    boolean dirty;

    GraphicsItemsPage(final Window window, final String name) {
        this.itemSet = new HashSet<GraphicsItem>();
        this.name = name;
        this.window = window;
        this.dirty = false;
    }

    boolean add(final GraphicsItem object) {
        if (object == null) {
            return false;
        }

        if (this.itemSet.contains(object)) {
            return false;
        }

        if (object.page != null) {
            this.window.remove(object);
        }

        this.itemSet.add(object);
        this.dirty = true;
        object.page = this;
        return true;
    }

    int count() {
        return this.itemSet.size();
    }

    Collection<GraphicsItem> getItems() {
        return Collections.unmodifiableCollection(this.itemSet);
    }

    String getName() {
        return this.name;
    }

    boolean isActive() {
        return this.active;
    }

    boolean remove(final GraphicsItem object) {
        if (object == null) {
            return false;
        }

        if (!this.itemSet.contains(object)) {
            return false;
        }

        this.itemSet.remove(object);
        object.page = null;
        this.dirty = true;
        return true;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
