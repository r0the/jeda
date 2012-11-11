/*
 * Copyright (C) 2012 by Stefan Rothe
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

public abstract class ListInfo<T> extends Request {

    private static final Comparator<ListItem> LIST_ITEM_BY_NAME = new ListItemByName();
    private final List<ListItem<T>> items;

    protected ListInfo() {
        this.items = new ArrayList();
    }

    public final void addItem(String name, T item) {
        this.items.add(new ListItem(this.items.size(), name, item));
    }

    public final void done(int index) {
        if (0 <= index && index < items.size()) {
            this.onSelect(this.items.get(index).item);
        }
        else {
            this.onCancel();
        }
    }

    public final ArrayList<String> getDisplayItems() {
        ArrayList<String> result = new ArrayList();
        for (ListItem item : this.items) {
            result.add(item.name);
        }

        return result;
    }

    public final void sortItemsByName() {
        Collections.sort(this.items, LIST_ITEM_BY_NAME);
    }

    protected abstract void onSelect(T item);

    protected abstract void onCancel();

    private static class ListItem<T> {

        private final long id;
        private final String name;
        private final T item;

        public ListItem(long id, String name, T item) {
            this.id = id;
            this.name = initName(name, item);
            this.item = item;
        }

        private static <T> String initName(String name, T item) {
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
        public int compare(ListItem o1, ListItem o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}
