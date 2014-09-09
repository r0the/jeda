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

final class GraphicsItems {

    private final Map<String, GraphicsItemsPage> pages;
    private final List<GraphicsItem> pendingDeletions;
    private final List<GraphicsItem> pendingInsertions;
    private final Window window;
    private GraphicsItem[] activeItems;
    private GraphicsItemsPage activePage;
    private GraphicsItemsPage currentPage;

    GraphicsItems(final Window window) {
        this.pages = new HashMap<String, GraphicsItemsPage>();
        this.pendingDeletions = new ArrayList<GraphicsItem>();
        this.pendingInsertions = new ArrayList<GraphicsItem>();
        this.window = window;
        this.activeItems = new GraphicsItem[0];
        this.activePage = new GraphicsItemsPage(window, "Main");
        this.currentPage = this.activePage;
    }

    void add(final GraphicsItem object) {
        if (this.currentPage.add(object) && this.currentPage.isActive()) {
            this.pendingDeletions.remove(object);
            this.pendingInsertions.add(object);
        }
    }

    int count() {
        return this.currentPage.count();
    }

    void draw(final Canvas canvas) {
        for (int i = 0; i < this.activeItems.length; ++i) {
            this.activeItems[i].draw(canvas);
        }
    }

    @SuppressWarnings("unchecked")
    final <T extends GraphicsItem> T[] get(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        for (int i = 0; i < this.activeItems.length; ++i) {
            if (clazz.isInstance(this.activeItems[i])) {
                // Unchecked cast
                result.add((T) this.activeItems[i]);
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    final GraphicsItem[] getAll() {
        return Arrays.copyOf(this.activeItems, this.activeItems.length);
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
            this.setActiveItems(this.currentPage.getItems());
            this.addEventListeners();
        }
        else if (!this.pendingDeletions.isEmpty() || !this.pendingInsertions.isEmpty()) {
            // Same page, but items changed
            boolean allChanged = false;
            final Set<GraphicsItem> itemSet = new HashSet<GraphicsItem>(Arrays.asList(this.activeItems));
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                final GraphicsItem item = this.pendingDeletions.get(i);
                if (itemSet.remove(item)) {
                    allChanged = true;
                    this.window.removeEventListener(item);
                }
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                final GraphicsItem item = this.pendingInsertions.get(i);
                if (!itemSet.contains(item)) {
                    if (itemSet.add(item)) {
                        this.window.addEventListener(item);
                        allChanged = true;
                    }
                }
            }

            if (allChanged) {
                this.setActiveItems(itemSet);
            }

            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
        }
    }

    void remove(final GraphicsItem object) {
        if (this.currentPage.remove(object)) {
            this.pendingDeletions.add(object);
            this.pendingInsertions.remove(object);
        }
    }

    void setPage(final String page) {
        if (!this.currentPage.getName().equals(page)) {
            if (!this.pages.containsKey(page)) {
                this.pages.put(page, new GraphicsItemsPage(window, page));
            }

            this.currentPage = this.pages.get(page);
        }
    }

    private void addEventListeners() {
        for (int i = 0; i < this.activeItems.length; ++i) {
            this.window.addEventListener(this.activeItems[i]);
        }
    }

    private void removeEventListeners() {
        for (int i = 0; i < this.activeItems.length; ++i) {
            this.window.removeEventListener(this.activeItems[i]);
        }
    }

    private void setActiveItems(final Collection<GraphicsItem> items) {
        this.activeItems = items.toArray(new GraphicsItem[items.size()]);
        Arrays.sort(this.activeItems, GraphicsItem.DRAW_ORDER);
    }
}
