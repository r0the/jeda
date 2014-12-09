/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class Elements {

    private final Map<String, ViewPage> pages;
    private final List<Element> pendingDeletions;
    private final List<Element> pendingInsertions;
    private final Window window;
    private Element[] activeElements;
    private ViewPage activePage;
    private ViewPage currentPage;

    Elements(final Window window) {
        this.pages = new HashMap<String, ViewPage>();
        this.pendingDeletions = new ArrayList<Element>();
        this.pendingInsertions = new ArrayList<Element>();
        this.window = window;
        this.activeElements = new Element[0];
        this.activePage = new ViewPage(window, "Main");
        this.currentPage = this.activePage;
    }

    void add(final Element element) {
        if (this.currentPage.add(element) && this.currentPage.isActive()) {
            this.pendingDeletions.remove(element);
            this.pendingInsertions.add(element);
        }
    }

    int count() {
        return this.currentPage.count();
    }

    void draw(final Canvas canvas) {
        for (int i = 0; i < this.activeElements.length; ++i) {
            this.activeElements[i].draw(canvas);
        }
    }

    @SuppressWarnings("unchecked")
    final <T extends Element> T[] get(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        for (int i = 0; i < this.activeElements.length; ++i) {
            if (clazz.isInstance(this.activeElements[i])) {
                // Unchecked cast
                result.add((T) this.activeElements[i]);
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    final Element[] getAll() {
        return Arrays.copyOf(this.activeElements, this.activeElements.length);
    }

    String getCurrentPage() {
        return this.currentPage.getName();
    }

    void processPending() {
        if (!this.currentPage.isActive()) {
            // Page change
            this.removeEventListeners();
            this.activePage.setActive(false);
            this.activePage = this.currentPage;
            this.activePage.setActive(true);
            this.setActiveElements(this.currentPage.getElements());
            this.addEventListeners();
        }
        else if (!this.pendingDeletions.isEmpty() || !this.pendingInsertions.isEmpty()) {
            // Same page, but items changed
            boolean allChanged = false;
            final Set<Element> elementSet = new HashSet<Element>(Arrays.asList(this.activeElements));
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                final Element element = this.pendingDeletions.get(i);
                if (elementSet.remove(element)) {
                    allChanged = true;
                    this.window.removeEventListener(element);
                }
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                final Element element = this.pendingInsertions.get(i);
                if (!elementSet.contains(element)) {
                    if (elementSet.add(element)) {
                        this.window.addEventListener(element);
                        allChanged = true;
                    }
                }
            }

            if (allChanged) {
                this.setActiveElements(elementSet);
            }

            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
        }
    }

    void remove(final Element element) {
        if (this.currentPage.remove(element)) {
            this.pendingDeletions.add(element);
            this.pendingInsertions.remove(element);
        }
    }

    void setPage(final String page) {
        if (!this.currentPage.getName().equals(page)) {
            if (!this.pages.containsKey(page)) {
                this.pages.put(page, new ViewPage(window, page));
            }

            this.currentPage = this.pages.get(page);
        }
    }

    private void addEventListeners() {
        for (int i = 0; i < this.activeElements.length; ++i) {
            this.window.addEventListener(this.activeElements[i]);
        }
    }

    private void removeEventListeners() {
        for (int i = 0; i < this.activeElements.length; ++i) {
            this.window.removeEventListener(this.activeElements[i]);
        }
    }

    private void setActiveElements(final Collection<Element> elements) {
        this.activeElements = elements.toArray(new Element[elements.size()]);
        Arrays.sort(this.activeElements, Element.DRAW_ORDER);
    }
}
