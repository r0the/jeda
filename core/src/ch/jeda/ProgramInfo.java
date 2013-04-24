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
package ch.jeda;

class ProgramInfo {

    private final Class<Program> programClass;
    private final String name;

    ProgramInfo(final Class<Program> programClass, final String name) {
        assert programClass != null;

        this.programClass = programClass;
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    Class<Program> getProgramClass() {
        return this.programClass;
    }

    @Override
    public String toString() {
        return this.programClass.getName();
    }
}
