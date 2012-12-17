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

/**
 * Represents a key that can be typed by the user. Key values are used by the 
 * {@link Events} class to represent which keys have been pressed or typed.
 *
 * Key objects cannot be created, they are provided as constants.
 *
 * @see Events
 */
public final class Key implements Serializable {

    private static final Map<String, Key> NAME_MAP = new HashMap();
    /**
     * The "A" key.
     */
    public static final Key A = new Key("A");
    /**
     * The alt graph key. The string representation of this key is "AltGr".
     */
    public static final Key ALT_GRAPH = new Key("AltGr");
    /**
     * The left alt key. The string representation of this key is "AltLeft".
     */
    public static final Key ALT_LEFT = new Key("AltLeft");
    /**
     * The left alt key. The string representation of this key is "AltLeft".
     */
    public static final Key ALT_RIGHT = new Key("AltRight");
    /**
     * The apostrophe key. The string representation of this key is "Apostrophe".
     */
    public static final Key APOSTROPHE = new Key("Apostrophe");
    /**
     * The "B" key.
     */
    public static final Key B = new Key("B");
    /**
     * The back key. The string representation of this key is "Back".
     */
    public static final Key BACK = new Key("Back");
    /**
     * The Backspace key. The string representation of this key is "Backspace".
     */
    public static final Key BACKSPACE = new Key("Backspace");
    /**
     * The "C" key.
     */
    public static final Key C = new Key("C");
    /**
     * The caps lock key. The string representation of this key is "CapsLock".
     */
    public static final Key CAPS_LOCK = new Key("CapsLock");
    /**
     * The left control key. The string representation of this key is "CtrlLeft".
     */
    public static final Key CTRL_LEFT = new Key("CtrlLeft");
    /**
     * The right control key. The string representation of this key is "CtrlRight".
     */
    public static final Key CTRL_RIGHT = new Key("CtrlRight");
    /**
     * The "D" key.
     */
    public static final Key D = new Key("D");
    /**
     * The delete key. The string representation of this key is "Delete".
     */
    public static final Key DELETE = new Key("Delete");
    /**
     * The "0" digit key. The string representation of this key is "0".
     */
    public static final Key DIGIT_0 = new Key("0");
    /**
     * The "1" digit key. The string representation of this key is "1".
     */
    public static final Key DIGIT_1 = new Key("1");
    /**
     * The "2" digit key. The string representation of this key is "2".
     */
    public static final Key DIGIT_2 = new Key("2");
    /**
     * The "3" digit key. The string representation of this key is "3".
     */
    public static final Key DIGIT_3 = new Key("3");
    /**
     * The "4" digit key. The string representation of this key is "4".
     */
    public static final Key DIGIT_4 = new Key("4");
    /**
     * The "5" digit key. The string representation of this key is "5".
     */
    public static final Key DIGIT_5 = new Key("5");
    /**
     * The "6" digit key. The string representation of this key is "6".
     */
    public static final Key DIGIT_6 = new Key("6");
    /**
     * The "7" digit key. The string representation of this key is "7".
     */
    public static final Key DIGIT_7 = new Key("7");
    /**
     * The "8" digit key. The string representation of this key is "8".
     */
    public static final Key DIGIT_8 = new Key("8");
    /**
     * The "9" digit key. The string representation of this key is "9".
     */
    public static final Key DIGIT_9 = new Key("9");
    /**
     * The cursor down key. The string representation of this key is "Down".
     */
    public static final Key DOWN = new Key("Down");
    /**
     * The "E" key.
     */
    public static final Key E = new Key("E");
    /**
     * The end key. The string representation of this key is "End".
     */
    public static final Key END = new Key("End");
    /**
     * The enter key. The string representation of this key is "Enter".
     */
    public static final Key ENTER = new Key("Enter");
    /**
     * The equals key. The string representation of this key is "Equals".
     */
    public static final Key EQUALS = new Key("Equals");
    /**
     * The escape key. The string representation of this key is "Esc".
     */
    public static final Key ESCAPE = new Key("Esc");
    /**
     * The "F" key.
     */
    public static final Key F = new Key("F");
    /**
     * The "F1" function key.
     */
    public static final Key F1 = new Key("F1");
    /**
     * The "F2" function key.
     */
    public static final Key F2 = new Key("F2");
    /**
     * The "F3" function key.
     */
    public static final Key F3 = new Key("F3");
    /**
     * The "F4" function key.
     */
    public static final Key F4 = new Key("F4");
    /**
     * The "F5" function key.
     */
    public static final Key F5 = new Key("F5");
    /**
     * The "F6" function key.
     */
    public static final Key F6 = new Key("F6");
    /**
     * The "F7" function key.
     */
    public static final Key F7 = new Key("F7");
    /**
     * The "F8" function key.
     */
    public static final Key F8 = new Key("F8");
    /**
     * The "F9" function key.
     */
    public static final Key F9 = new Key("F9");
    /**
     * The "F10" function key.
     */
    public static final Key F10 = new Key("F10");
    /**
     * The "F11" function key.
     */
    public static final Key F11 = new Key("F11");
    /**
     * The "F12" function key.
     */
    public static final Key F12 = new Key("F12");
    /**
     * The "G" key.
     */
    public static final Key G = new Key("G");
    /**
     * The "`" key. The string representation of this key is "Grave".
     */
    public static final Key GRAVE = new Key("Grave");
    /**
     * The "H" key.
     */
    public static final Key H = new Key("H");
    /**
     * The home key. The string representation of this key is "Home".
     */
    public static final Key HOME = new Key("Home");
    /**
     * The "I" key.
     */
    public static final Key I = new Key("I");
    /**
     * The insert key. The string representation of this key is "Insert".
     */
    public static final Key INSERT = new Key("Insert");
    /**
     * The "J" key.
     */
    public static final Key J = new Key("J");
    /**
     * The "K" key.
     */
    public static final Key K = new Key("K");
    /**
     * The "L" key.
     */
    public static final Key L = new Key("L");
    /**
     * The cursor left key. The string representation of this key is "Left".
     */
    public static final Key LEFT = new Key("Left");
    /**
     * The "M" key.
     */
    public static final Key M = new Key("M");
    /**
     * The menu key. On the Java platform, the context menu key is mapped to
     * this key. On the Android platform, the menu key is mapped to this key.
     * The string representation of this key is "Menu".
     */
    public static final Key MENU = new Key("Menu");
    /**
     * The "-" key. The string representation of this key is "Minus".
     */
    public static final Key MINUS = new Key("Minus");
    /**
     * The middle mouse button.
     */
    public static final Key MOUSE_MIDDLE = new Key("MouseMiddle");
    /**
     * The primary mouse button (usually left).
     */
    public static final Key MOUSE_PRIMARY = new Key("MousePrimary");
    /**
     * The seconary mouse button (usually right).
     */
    public static final Key MOUSE_SECONDARY = new Key("MouseSecondary");
    /**
     * The "N" key.
     */
    public static final Key N = new Key("N");
    /**
     * The num lock key. The string representation of this key is "NumLock".
     */
    public static final Key NUM_LOCK = new Key("NumLock");
    /**
     * The "0" digit key on the numeric keypad. The string representation of
     * this key is "Numpad0".
     */
    public static final Key NUMPAD_0 = new Key("Numpad0");
    /**
     * The "1" digit key on the numeric keypad. The string representation of
     * this key is "Numpad1".
     */
    public static final Key NUMPAD_1 = new Key("Numpad1");
    /**
     * The "2" digit key on the numeric keypad. The string representation of
     * this key is "Numpad2".
     */
    public static final Key NUMPAD_2 = new Key("Numpad2");
    /**
     * The "3" digit key on the numeric keypad. The string representation of
     * this key is "Numpad3".
     */
    public static final Key NUMPAD_3 = new Key("Numpad3");
    /**
     * The "4" digit key on the numeric keypad. The string representation of
     * this key is "Numpad4".
     */
    public static final Key NUMPAD_4 = new Key("Numpad4");
    /**
     * The "5" digit key on the numeric keypad. The string representation of
     * this key is "Numpad5".
     */
    public static final Key NUMPAD_5 = new Key("Numpad5");
    /**
     * The "6" digit key on the numeric keypad. The string representation of
     * this key is "Numpad6".
     */
    public static final Key NUMPAD_6 = new Key("Numpad6");
    /**
     * The "7" digit key on the numeric keypad. The string representation of
     * this key is "Numpad7".
     */
    public static final Key NUMPAD_7 = new Key("Numpad7");
    /**
     * The "8" digit key on the numeric keypad. The string representation of
     * this key is "Numpad8".
     */
    public static final Key NUMPAD_8 = new Key("Numpad8");
    /**
     * The "9" digit key on the numeric keypad.
     * The string representation of this key is "Numpad9".
     */
    public static final Key NUMPAD_9 = new Key("Numpad9");
    /**
     * The "+" key on the numeric keypard.
     * The string representation of this key is "NumpadAdd".
     */
    public static final Key NUMPAD_ADD = new Key("NumpadAdd");
    /**
     * The "/" key on the numeric keypard.
     * The string representation of this key is "NumpadDivide".
     */
    public static final Key NUMPAD_DIVIDE = new Key("NumpadDivide");
    /**
     * The "." key on the numeric keypard.
     * The string representation of this key is "NumpadDot".
     */
    public static final Key NUMPAD_DOT = new Key("NumpadDot");
    /**
     * The enter key on the numeric keypard.
     * The string representation of this key is "NumpadEnter".
     */
    public static final Key NUMPAD_ENTER = new Key("NumpadEnter");
    /**
     * The "*" key on the numeric keypard.
     * The string representation of this key is "NumpadMultiply".
     */
    public static final Key NUMPAD_MULTIPLY = new Key("NumpadMultiply");
    /**
     * The "-" key on the numeric keypard.
     * The string representation of this key is "NumpadSubtract".
     */
    public static final Key NUMPAD_SUBTRACT = new Key("NumpadSubtract");
    /**
     * The "O" key.
     */
    public static final Key O = new Key("O");
    /**
     * The "P" key.
     */
    public static final Key P = new Key("P");
    /**
     * The page down key. The string representation of this key is "PageDown".
     */
    public static final Key PAGE_DOWN = new Key("PageDown");
    /**
     * The page up key. The string representation of this key is "PageUp".
     */
    public static final Key PAGE_UP = new Key("PageUp");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key PAUSE = new Key("Pause");
    /**
     * The print screen key. The string representation of this key is "PrintScreen".
     */
    public static final Key PRINT_SCREEN = new Key("PrintScreen");
    /**
     * The "Q" key.
     */
    public static final Key Q = new Key("Q");
    /**
     * The "R" key.
     */
    public static final Key R = new Key("R");
    /**
     * The cursor right key. The string representation of this key is "Right".
     */
    public static final Key RIGHT = new Key("Right");
    /**
     * The "S" key.
     */
    public static final Key S = new Key("S");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key SCROLL_LOCK = new Key("ScrollLock");
    /**
     * The left shift key. The string representation of this key is "ShiftLeft".
     */
    public static final Key SHIFT_LEFT = new Key("ShiftLeft");
    /**
     * The right shift key. The string representation of this key is "ShiftRight".
     */
    public static final Key SHIFT_RIGHT = new Key("ShiftRight");
    /**
     * The space key. The string representation of this key is "Space".
     */
    public static final Key SPACE = new Key("Space");
    /**
     * The "T" key.
     */
    public static final Key T = new Key("T");
    /**
     * The tabulator key. The string representation of this key is "Tab".
     */
    public static final Key TAB = new Key("Tab");
    /**
     * The "U" key.
     */
    public static final Key U = new Key("U");
    /**
     * The cursor up key. The string representation of this key is "Up".
     */
    public static final Key UP = new Key("Up");
    /**
     * The "V" key.
     */
    public static final Key V = new Key("V");
    /**
     * The Volume Down key.
     */
    public static final Key VOLUME_DOWN = new Key("VolumeDown");
    /**
     * The Volume Up key.
     */
    public static final Key VOLUME_UP = new Key("VolumeUp");
    /**
     * The "W" key.
     */
    public static final Key W = new Key("W");
    /**
     * The "X" key.
     */
    public static final Key X = new Key("X");
    /**
     * The "Y" key.
     */
    public static final Key Y = new Key("Y");
    /**
     * The "Z" key.
     */
    public static final Key Z = new Key("Z");
    private static int NEXT_ID = 0;
    private final int id;
    private final String name;

    /**
     * Returns the with the specified string representation. Returns null if 
     * the name does not correspond to any key.
     *
     * @param name name of the key
     * @return Key the key with the specified name
     */
    public static Key fromString(String name) {
        if (NAME_MAP.containsKey(name)) {
            return NAME_MAP.get(name);
        }
        else {
            return null;
        }
    }

    private Key(String name) {
        this.id = NEXT_ID;
        ++NEXT_ID;
        this.name = name;
        NAME_MAP.put(name, this);
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
        return this.id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
