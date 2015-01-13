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
package ch.jeda;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

class DefaultDataImp implements DataImp {

    private final Map<String, String> map;

    DefaultDataImp() {
        this.map = new TreeMap<String, String>();
    }

    DefaultDataImp(final Map<String, String> map) {
        if (map == null) {
            this.map = new TreeMap<String, String>();
        }
        else {
            this.map = new TreeMap<String, String>(map);
        }
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Collection<String> getNames() {
        return this.map.keySet();
    }

    @Override
    public boolean hasValue(final String name) {
        return this.map.containsKey(name);
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public String read(final String name) {
        return this.map.get(name);
    }

    @Override
    public void remove(final String name) {
        this.map.remove(name);
    }

    @Override
    public void write(final String name, final String value) {
        this.map.put(name, value);
    }
}
