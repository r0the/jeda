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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an axis of an {@link InputDevice}.
 */
public final class Axis {

    private static final Map<String, Axis> NAME_AXIS_MAP = new HashMap();
    private static final Map<Axis, String> AXIS_NAME_MAP = new HashMap();
    public static final Axis LEFT_X = new Axis(1, "LeftX");
    public static final Axis LEFT_Y = new Axis(2, "LeftY");
    public static final Axis LEFT_Z = new Axis(3, "LeftZ");
    public static final Axis RIGHT_X = new Axis(4, "RightX");
    public static final Axis RIGHT_Y = new Axis(5, "RightY");
    public static final Axis RIGHT_Z = new Axis(6, "RightZ");
    public static final Axis POV = new Axis(7, "POV");
    private final int id;

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
        return this.id;
    }

    @Override
    public String toString() {
        return AXIS_NAME_MAP.get(this);
    }

    private Axis(int id, String name) {
        this.id = id;
        NAME_AXIS_MAP.put(name, this);
        AXIS_NAME_MAP.put(this, name);
    }
}
