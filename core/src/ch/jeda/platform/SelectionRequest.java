/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <b>Internal</b>. Do not use this class.
 */
public final class SelectionRequest<T> extends Request {

    private static final Comparator<ListItem> LIST_ITEM_BY_NAME = new ListItemByName();
    private final List<ListItem<T>> items;
    private final Object lock;
    private boolean cancelled;
    private boolean done;
    private int selectedIndex;

    public SelectionRequest() {
        this.lock = new Object();
        this.items = new ArrayList<ListItem<T>>();
    }

    public void addItem(final String name, final T item) {
        this.items.add(new ListItem<T>(this.items.size(), name, item));
    }

    public void cancelRequest() {
        synchronized (this.lock) {
            this.done = true;
            this.cancelled = true;
            this.lock.notify();
        }
    }

    public T getResult() {
        return this.items.get(this.selectedIndex).item;
    }

    public void setSelectedIndex(final int index) {
        synchronized (this.lock) {
            if (0 <= index && index < items.size()) {
                this.selectedIndex = index;
            }
            else {
                this.cancelled = true;
            }

            this.done = true;
            this.lock.notify();
        }
    }

    public ArrayList<String> getDisplayItems() {
        final ArrayList<String> result = new ArrayList<String>();
        for (ListItem item : this.items) {
            result.add(item.name);
        }

        return result;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void sortItemsByName() {
        Collections.sort(this.items, LIST_ITEM_BY_NAME);
    }

    public void waitForResult() {
        synchronized (this.lock) {
            while (!this.done) {
                try {
                    this.lock.wait();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }

    private static class ListItem<T> {

        private final long id;
        private final String name;
        private final T item;

        public ListItem(final long id, final String name, final T item) {
            this.id = id;
            this.name = initName(name, item);
            this.item = item;
        }

        private static <T> String initName(final String name, final T item) {
            if (name == null) {
                return item.toString();
            }
            else {
                return name;
            }
        }
    }

    private static class ListItemByName implements Comparator<ListItem> {

        @Override
        public int compare(final ListItem o1, final ListItem o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}
