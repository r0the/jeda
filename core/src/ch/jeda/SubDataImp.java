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

import java.util.ArrayList;
import java.util.Collection;

class SubDataImp implements DataImp {

    private final DataImp baseDataImp;
    private final int cutPos;
    private final String prefix;

    SubDataImp(final DataImp baseDataImp, final String prefix) {
        this.baseDataImp = baseDataImp;
        this.cutPos = prefix.length() + 1;
        this.prefix = prefix;
    }

    @Override
    public void clear() {
        for (final String name : this.baseDataImp.getNames()) {
            if (name.startsWith(this.prefix)) {
                this.baseDataImp.remove(name);
            }
        }
    }

    @Override
    public Collection<String> getNames() {
        final Collection<String> result = new ArrayList<String>();
        for (final String name : this.baseDataImp.getNames()) {
            if (name.startsWith(this.prefix)) {
                result.add(name.substring(this.cutPos));
            }
        }

        return result;
    }

    @Override
    public boolean hasValue(String name) {
        return this.baseDataImp.hasValue(this.baseName(name));
    }

    @Override
    public boolean isEmpty() {
        for (final String name : this.baseDataImp.getNames()) {
            if (name.startsWith(this.prefix)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String read(final String name) {
        return this.baseDataImp.read(this.baseName(name));
    }

    @Override
    public void remove(final String name) {
        this.baseDataImp.remove(this.baseName(name));
    }

    @Override
    public void write(String name, String value) {
        this.baseDataImp.write(this.baseName(name), value);
    }

    private String baseName(final String name) {
        final StringBuilder result = new StringBuilder(this.prefix);
        result.append(name);
        return result.toString();
    }
}
