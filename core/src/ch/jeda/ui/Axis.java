/*
 * Copyright (C) 2013 by Stefan Rothe
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an axis of an {@link InputDevice}.
 */
public final class Axis implements Serializable {

    private static final Map<Integer, String> ID_NAME_MAP = new HashMap();
    private static final Map<String, Integer> NAME_ID_MAP = new HashMap();
    private static int NEXT_ID = 0;
    public static final Axis LEFT_X = register("LeftX");
    public static final Axis LEFT_Y = register("LeftY");
    public static final Axis LEFT_Z = register("LeftZ");
    public static final Axis RIGHT_X = register("RightX");
    public static final Axis RIGHT_Y = register("RightY");
    public static final Axis RIGHT_Z = register("RightZ");
    public static final Axis POV = register("POV");
    /**
     * @since 1
     */
    public final int id;

    public Axis(String name) {
        this(NAME_ID_MAP.get(name));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Axis) {
            return this.id == ((Axis) object).id;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 23 * this.id;
    }

    @Override
    public String toString() {
        return ID_NAME_MAP.get(this.id);
    }

    private Axis() {
        this(0);
    }

    private Axis(int id) {
        this.id = id;
    }

    private static Axis register(String name) {
        final int id = ++NEXT_ID;
        ID_NAME_MAP.put(id, name);
        NAME_ID_MAP.put(name, id);
        return new Axis(id);
    }
}
