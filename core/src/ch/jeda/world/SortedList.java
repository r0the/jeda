/*
 * Copyright (C) 2011 by Stefan Rothe
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This class provides a sorted list.
 *
 * @param <T>
 */
public class SortedList<T> implements Collection<T> {

    private final List<T> list = new ArrayList<T>();
    private final Comparator<T> comparator;

    /**
     * Initializes a SortedList with default comparison.
     */
    public SortedList() {
        this(null);
    }

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public boolean add(T item) {
        int index = Collections.binarySearch(this.list, item, this.comparator);
        if (index < 0) {
            index = -index - 1;
        }
        this.list.add(index, item);
        return true;
    }

    public boolean addAll(Collection<? extends T> collection) {
        this.list.addAll(collection);
        Collections.sort(this.list, this.comparator);
        return true;
    }

    public void clear() {
        this.list.clear();
    }

    public boolean contains(Object object) {
        return Collections.binarySearch(this.list, object, null) >= 0;
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object object : collection) {
            if (!this.contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof SortedList<?>)
               && this.list.equals(((SortedList<?>) object).list);
    }

    public int findIndex(Object object) {
        return Collections.binarySearch(this.list, object, null);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    public boolean remove(Object object) {
        return this.list.remove(object);
    }

    public boolean removeAll(Collection<?> collection) {
        return this.list.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.list.retainAll(collection);
    }

    public int size() {
        return this.list.size();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T> T[] toArray(T[] array) {
        return this.list.toArray(array);
    }
}
