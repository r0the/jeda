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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class ElementsPage {

    final Window window;
    private final Set<Element> elementSet;
    private final String name;
    private boolean active;
    boolean dirty;

    ElementsPage(final Window window, final String name) {
        this.elementSet = new HashSet<Element>();
        this.name = name;
        this.window = window;
        this.dirty = false;
    }

    boolean add(final Element element) {
        if (element == null) {
            return false;
        }
        if (this.elementSet.contains(element)) {
            return false;
        }
        if (element.page != null) {
            this.window.remove(element);
        }
        this.elementSet.add(element);
        this.dirty = true;
        element.page = this;
        return true;
    }

    int count() {
        return this.elementSet.size();
    }

    Collection<Element> getElements() {
        return Collections.unmodifiableCollection(this.elementSet);
    }

    String getName() {
        return this.name;
    }

    boolean isActive() {
        return this.active;
    }

    boolean remove(final Element element) {
        if (element == null) {
            return false;
        }
        if (!this.elementSet.contains(element)) {
            return false;
        }
        this.elementSet.remove(element);
        element.page = null;
        this.dirty = true;
        return true;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
