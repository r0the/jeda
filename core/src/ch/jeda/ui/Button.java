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
 * Represents a button of an {@link InputDevice}.
 */
public final class Button {

    private static final Map<String, Button> NAME_BUTTON_MAP = new HashMap();
    private static final Map<Button, String> BUTTON_NAME_MAP = new HashMap();
    /**
     * The gamepad "A" button.
     */
    public static final Button A = new Button(1, "A");
    /**
     * The gamepad "B" button.
     */
    public static final Button B = new Button(2, "B");
    /**
     * The gamepad left index finger button (top left shoulder button on Xbox
     * compatible controller).
     */
    public static final Button LEFT_INDEX = new Button(3, "LeftIndex");
    public static final Button LEFT_THUMB = new Button(4, "LeftThumb");
    public static final Button MODE = new Button(5, "Mode");
    /**
     * The gamepad right index finger button (top right shoulder button on Xbox
     * compatible controller).
     */
    public static final Button RIGHT_INDEX = new Button(6, "RightIndex");
    public static final Button RIGHT_THUMB = new Button(7, "RightThumb");
    /**
     * The gamepad "select" button.
     */
    public static final Button SELECT = new Button(8, "Select");
    /**
     * The gamepad "start" button.
     */
    public static final Button START = new Button(9, "Start");
    /**
     * The gamepad "X" button.
     */
    public static final Button X = new Button(10, "X");
    /**
     * The gamepad "Y" button.
     */
    public static final Button Y = new Button(11, "Y");
    private final int id;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Button) {
            return this.id == ((Button) object).id;
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
        return BUTTON_NAME_MAP.get(this);
    }

    private Button(int id, String name) {
        this.id = id;
        NAME_BUTTON_MAP.put(name, this);
        BUTTON_NAME_MAP.put(this, name);
    }
}
