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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a button of an {@link InputDevice}.
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class Button implements Serializable {

    private static final Map<Integer, String> ID_NAME_MAP = new HashMap();
    private static final Map<String, Integer> NAME_ID_MAP = new HashMap();
    private static int NEXT_ID = 0;
    /**
     * The gamepad "A" button.
     */
    public static final Button A = register("A");
    /**
     * The gamepad "B" button.
     */
    public static final Button B = register("B");
    /**
     * The gamepad left index finger button (top left shoulder button on Xbox
     * compatible controller).
     */
    public static final Button LEFT_INDEX = register("LeftIndex");
    public static final Button LEFT_THUMB = register("LeftThumb");
    public static final Button MODE = register("Mode");
    /**
     * The gamepad right index finger button (top right shoulder button on Xbox
     * compatible controller).
     */
    public static final Button RIGHT_INDEX = register("RightIndex");
    public static final Button RIGHT_THUMB = register("RightThumb");
    /**
     * The gamepad "select" button.
     */
    public static final Button SELECT = register("Select");
    /**
     * The gamepad "start" button.
     */
    public static final Button START = register("Start");
    /**
     * The gamepad "X" button.
     */
    public static final Button X = register("X");
    /**
     * The gamepad "Y" button.
     */
    public static final Button Y = register("Y");
    /**
     * @since 1
     */
    @XmlElement
    public final int id;

    public Button(String name) {
        this(NAME_ID_MAP.get(name));
    }

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
        return 23 * this.id;
    }

    @Override
    public String toString() {
        return ID_NAME_MAP.get(this.id);
    }

    private Button() {
        this(0);
    }

    private Button(int id) {
        this.id = id;
    }

    private static Button register(String name) {
        final int id = ++NEXT_ID;
        ID_NAME_MAP.put(id, name);
        NAME_ID_MAP.put(name, id);
        return new Button(id);
    }
}
