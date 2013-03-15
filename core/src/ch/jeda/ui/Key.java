/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
 * Represents a key that can be typed by the user. Key values are used by the 
 * {@link Events} class to represent which keys have been pressed or typed.
 *
 * Key objects cannot be created, they are provided as constants.
 *
 * @see Events
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class Key implements Serializable {

    private static final Map<Integer, String> ID_NAME_MAP = new HashMap();
    private static final Map<String, Integer> NAME_ID_MAP = new HashMap();
    private static int NEXT_ID = 0;
    /**
     * The "A" key.
     */
    public static final Key A = register("A");
    /**
     * The alt graph key. The string representation of this key is "AltGr".
     */
    public static final Key ALT_GRAPH = register("AltGr");
    /**
     * The left alt key. The string representation of this key is "AltLeft".
     */
    public static final Key ALT_LEFT = register("AltLeft");
    /**
     * The left alt key. The string representation of this key is "AltLeft".
     */
    public static final Key ALT_RIGHT = register("AltRight");
    /**
     * The apostrophe key. The string representation of this key is "Apostrophe".
     */
    public static final Key APOSTROPHE = register("Apostrophe");
    /**
     * The "B" key.
     */
    public static final Key B = register("B");
    /**
     * The back key. The string representation of this key is "Back".
     */
    public static final Key BACK = register("Back");
    /**
     * The Backspace key. The string representation of this key is "Backspace".
     */
    public static final Key BACKSPACE = register("Backspace");
    /**
     * The "C" key.
     */
    public static final Key C = register("C");
    /**
     * The caps lock key. The string representation of this key is "CapsLock".
     */
    public static final Key CAPS_LOCK = register("CapsLock");
    /**
     * The left control key. The string representation of this key is "CtrlLeft".
     */
    public static final Key CTRL_LEFT = register("CtrlLeft");
    /**
     * The right control key. The string representation of this key is "CtrlRight".
     */
    public static final Key CTRL_RIGHT = register("CtrlRight");
    /**
     * The "D" key.
     */
    public static final Key D = register("D");
    /**
     * The delete key. The string representation of this key is "Delete".
     */
    public static final Key DELETE = register("Delete");
    /**
     * The "0" digit key. The string representation of this key is "0".
     */
    public static final Key DIGIT_0 = register("0");
    /**
     * The "1" digit key. The string representation of this key is "1".
     */
    public static final Key DIGIT_1 = register("1");
    /**
     * The "2" digit key. The string representation of this key is "2".
     */
    public static final Key DIGIT_2 = register("2");
    /**
     * The "3" digit key. The string representation of this key is "3".
     */
    public static final Key DIGIT_3 = register("3");
    /**
     * The "4" digit key. The string representation of this key is "4".
     */
    public static final Key DIGIT_4 = register("4");
    /**
     * The "5" digit key. The string representation of this key is "5".
     */
    public static final Key DIGIT_5 = register("5");
    /**
     * The "6" digit key. The string representation of this key is "6".
     */
    public static final Key DIGIT_6 = register("6");
    /**
     * The "7" digit key. The string representation of this key is "7".
     */
    public static final Key DIGIT_7 = register("7");
    /**
     * The "8" digit key. The string representation of this key is "8".
     */
    public static final Key DIGIT_8 = register("8");
    /**
     * The "9" digit key. The string representation of this key is "9".
     */
    public static final Key DIGIT_9 = register("9");
    /**
     * The cursor down key. The string representation of this key is "Down".
     */
    public static final Key DOWN = register("Down");
    /**
     * The "E" key.
     */
    public static final Key E = register("E");
    /**
     * The end key. The string representation of this key is "End".
     */
    public static final Key END = register("End");
    /**
     * The enter key. The string representation of this key is "Enter".
     */
    public static final Key ENTER = register("Enter");
    /**
     * The equals key. The string representation of this key is "Equals".
     */
    public static final Key EQUALS = register("Equals");
    /**
     * The escape key. The string representation of this key is "Esc".
     */
    public static final Key ESCAPE = register("Esc");
    /**
     * The "F" key.
     */
    public static final Key F = register("F");
    /**
     * The "F1" function key.
     */
    public static final Key F1 = register("F1");
    /**
     * The "F2" function key.
     */
    public static final Key F2 = register("F2");
    /**
     * The "F3" function key.
     */
    public static final Key F3 = register("F3");
    /**
     * The "F4" function key.
     */
    public static final Key F4 = register("F4");
    /**
     * The "F5" function key.
     */
    public static final Key F5 = register("F5");
    /**
     * The "F6" function key.
     */
    public static final Key F6 = register("F6");
    /**
     * The "F7" function key.
     */
    public static final Key F7 = register("F7");
    /**
     * The "F8" function key.
     */
    public static final Key F8 = register("F8");
    /**
     * The "F9" function key.
     */
    public static final Key F9 = register("F9");
    /**
     * The "F10" function key.
     */
    public static final Key F10 = register("F10");
    /**
     * The "F11" function key.
     */
    public static final Key F11 = register("F11");
    /**
     * The "F12" function key.
     */
    public static final Key F12 = register("F12");
    /**
     * The "G" key.
     */
    public static final Key G = register("G");
    /**
     * The "`" key. The string representation of this key is "Grave".
     */
    public static final Key GRAVE = register("Grave");
    /**
     * The "H" key.
     */
    public static final Key H = register("H");
    /**
     * The home key. The string representation of this key is "Home".
     */
    public static final Key HOME = register("Home");
    /**
     * The "I" key.
     */
    public static final Key I = register("I");
    /**
     * The insert key. The string representation of this key is "Insert".
     */
    public static final Key INSERT = register("Insert");
    /**
     * The "J" key.
     */
    public static final Key J = register("J");
    /**
     * The "K" key.
     */
    public static final Key K = register("K");
    /**
     * The "L" key.
     */
    public static final Key L = register("L");
    /**
     * The cursor left key. The string representation of this key is "Left".
     */
    public static final Key LEFT = register("Left");
    /**
     * The "M" key.
     */
    public static final Key M = register("M");
    /**
     * The menu key. On the Java platform, the context menu key is mapped to
     * this key. On the Android platform, the menu key is mapped to this key.
     * The string representation of this key is "Menu".
     */
    public static final Key MENU = register("Menu");
    /**
     * The "-" key. The string representation of this key is "Minus".
     */
    public static final Key MINUS = register("Minus");
    /**
     * The middle mouse button.
     */
    public static final Key MOUSE_MIDDLE = register("MouseMiddle");
    /**
     * The primary mouse button (usually left).
     */
    public static final Key MOUSE_PRIMARY = register("MousePrimary");
    /**
     * The seconary mouse button (usually right).
     */
    public static final Key MOUSE_SECONDARY = register("MouseSecondary");
    /**
     * The "N" key.
     */
    public static final Key N = register("N");
    /**
     * The num lock key. The string representation of this key is "NumLock".
     */
    public static final Key NUM_LOCK = register("NumLock");
    /**
     * The "0" digit key on the numeric keypad. The string representation of
     * this key is "Numpad0".
     */
    public static final Key NUMPAD_0 = register("Numpad0");
    /**
     * The "1" digit key on the numeric keypad. The string representation of
     * this key is "Numpad1".
     */
    public static final Key NUMPAD_1 = register("Numpad1");
    /**
     * The "2" digit key on the numeric keypad. The string representation of
     * this key is "Numpad2".
     */
    public static final Key NUMPAD_2 = register("Numpad2");
    /**
     * The "3" digit key on the numeric keypad. The string representation of
     * this key is "Numpad3".
     */
    public static final Key NUMPAD_3 = register("Numpad3");
    /**
     * The "4" digit key on the numeric keypad. The string representation of
     * this key is "Numpad4".
     */
    public static final Key NUMPAD_4 = register("Numpad4");
    /**
     * The "5" digit key on the numeric keypad. The string representation of
     * this key is "Numpad5".
     */
    public static final Key NUMPAD_5 = register("Numpad5");
    /**
     * The "6" digit key on the numeric keypad. The string representation of
     * this key is "Numpad6".
     */
    public static final Key NUMPAD_6 = register("Numpad6");
    /**
     * The "7" digit key on the numeric keypad. The string representation of
     * this key is "Numpad7".
     */
    public static final Key NUMPAD_7 = register("Numpad7");
    /**
     * The "8" digit key on the numeric keypad. The string representation of
     * this key is "Numpad8".
     */
    public static final Key NUMPAD_8 = register("Numpad8");
    /**
     * The "9" digit key on the numeric keypad.
     * The string representation of this key is "Numpad9".
     */
    public static final Key NUMPAD_9 = register("Numpad9");
    /**
     * The "+" key on the numeric keypard.
     * The string representation of this key is "NumpadAdd".
     */
    public static final Key NUMPAD_ADD = register("NumpadAdd");
    /**
     * The "/" key on the numeric keypard.
     * The string representation of this key is "NumpadDivide".
     */
    public static final Key NUMPAD_DIVIDE = register("NumpadDivide");
    /**
     * The "." key on the numeric keypard.
     * The string representation of this key is "NumpadDot".
     */
    public static final Key NUMPAD_DOT = register("NumpadDot");
    /**
     * The enter key on the numeric keypard.
     * The string representation of this key is "NumpadEnter".
     */
    public static final Key NUMPAD_ENTER = register("NumpadEnter");
    /**
     * The "*" key on the numeric keypard.
     * The string representation of this key is "NumpadMultiply".
     */
    public static final Key NUMPAD_MULTIPLY = register("NumpadMultiply");
    /**
     * The "-" key on the numeric keypard.
     * The string representation of this key is "NumpadSubtract".
     */
    public static final Key NUMPAD_SUBTRACT = register("NumpadSubtract");
    /**
     * The "O" key.
     */
    public static final Key O = register("O");
    /**
     * The "P" key.
     */
    public static final Key P = register("P");
    /**
     * The page down key. The string representation of this key is "PageDown".
     */
    public static final Key PAGE_DOWN = register("PageDown");
    /**
     * The page up key. The string representation of this key is "PageUp".
     */
    public static final Key PAGE_UP = register("PageUp");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key PAUSE = register("Pause");
    /**
     * The print screen key. The string representation of this key is "PrintScreen".
     */
    public static final Key PRINT_SCREEN = register("PrintScreen");
    /**
     * The "Q" key.
     */
    public static final Key Q = register("Q");
    /**
     * The "R" key.
     */
    public static final Key R = register("R");
    /**
     * The cursor right key. The string representation of this key is "Right".
     */
    public static final Key RIGHT = register("Right");
    /**
     * The "S" key.
     */
    public static final Key S = register("S");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key SCROLL_LOCK = register("ScrollLock");
    /**
     * The left shift key. The string representation of this key is "ShiftLeft".
     */
    public static final Key SHIFT_LEFT = register("ShiftLeft");
    /**
     * The right shift key. The string representation of this key is "ShiftRight".
     */
    public static final Key SHIFT_RIGHT = register("ShiftRight");
    /**
     * The space key. The string representation of this key is "Space".
     */
    public static final Key SPACE = register("Space");
    /**
     * The "T" key.
     */
    public static final Key T = register("T");
    /**
     * The tabulator key. The string representation of this key is "Tab".
     */
    public static final Key TAB = register("Tab");
    /**
     * The "U" key.
     */
    public static final Key U = register("U");
    /**
     * The cursor up key. The string representation of this key is "Up".
     */
    public static final Key UP = register("Up");
    /**
     * The "V" key.
     */
    public static final Key V = register("V");
    /**
     * The Volume Down key.
     */
    public static final Key VOLUME_DOWN = register("VolumeDown");
    /**
     * The Volume Up key.
     */
    public static final Key VOLUME_UP = register("VolumeUp");
    /**
     * The "W" key.
     */
    public static final Key W = register("W");
    /**
     * The "X" key.
     */
    public static final Key X = register("X");
    /**
     * The "Y" key.
     */
    public static final Key Y = register("Y");
    /**
     * The "Z" key.
     */
    public static final Key Z = register("Z");
    /**
     * @since 1
     */
    @XmlElement
    public final int id;

    public Key(String name) {
        this(NAME_ID_MAP.get(name));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Key) {
            return this.id == ((Key) object).id;
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

    private Key() {
        this(0);
    }

    private Key(int id) {
        this.id = id;
    }

    private static Key register(String name) {
        final int id = ++NEXT_ID;
        ID_NAME_MAP.put(id, name);
        NAME_ID_MAP.put(name, id);
        return new Key(id);
    }
}
