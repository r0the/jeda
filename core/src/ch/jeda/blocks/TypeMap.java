/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TypeMap<T> {

    private final List<T> emptyList;
    private final Map<Class<?>, List<T>> map;
    private final Class<?> topClass;

    public TypeMap(final Class<T> baseClass) {
        this.emptyList = Collections.unmodifiableList(new ArrayList<T>());
        this.map = new HashMap<Class<?>, List<T>>();
        this.topClass = baseClass.getSuperclass();
    }

    public void add(final T item) {
        if (item == null) {
            throw new NullPointerException("item");
        }

        Class<?> key = item.getClass();
        do {
            final List<T> list = this.getList(key, true);
            if (!list.contains(item)) {
                list.add(item);
            }

            key = key.getSuperclass();
        }
        while (!key.equals(this.topClass));
    }

    public void addAll(final Collection<T> collection) {
        if (collection == null) {
            throw new NullPointerException("c");
        }

        for (T item : collection) {
            this.add(item);
        }
    }

    public <S extends T> List<S> get(final Class<S> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        List<S> list = (List<S>) this.getList(key, false);
        if (list != null) {
            return Collections.unmodifiableList(list);
        }
        else {
            return (List<S>) this.emptyList;
        }
    }

    public void remove(final T item) {
        if (item == null) {
            throw new NullPointerException("item");
        }

        Class<?> key = item.getClass();
        do {
            List<T> list = this.getList(key, false);
            if (list != null) {
                list.remove(item);
            }
            key = key.getSuperclass();
        }
        while (!key.equals(this.topClass));
    }

    public void removeAll(final Collection<T> c) {
        if (c == null) {
            throw new NullPointerException("c");
        }

        for (T item : c) {
            this.remove(item);
        }
    }

    private List<T> getList(final Class<?> key, final boolean createList) {
        if (this.map.containsKey(key)) {
            return this.map.get(key);
        }
        else {
            if (createList) {
                final List<T> result = new ArrayList<T>();
                this.map.put(key, result);
                return result;
            }
            else {
                return null;
            }
        }
    }
}
