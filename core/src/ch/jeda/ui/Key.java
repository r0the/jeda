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

    private static final Map<Integer, Key> KEY_MAP = new HashMap<Integer, Key>();
    private static final Map<String, Key> NAME_MAP = new HashMap<String, Key>();
    private static final int ANYKEY = -1;
    private static final int NOKEY = -2;
    /**
     * The backspace key. The string representation of this key is "Backspace".
     */
    public static final Key BACKSPACE = new Key(8, "Backspace");
    /**
     * The tabulator key. The string representation of this key is "Tab".
     */
    public static final Key TAB = new Key(9, "Tab");
    /**
     * The enter key. The string representation of this key is "Enter".
     */
    public static final Key ENTER = new Key(10, "Enter");
    /**
     * The shift key. The string representation of this key is "Shift".
     */
    public static final Key SHIFT = new Key(16, "Shift");
    /**
     * The control key. The string representation of this key is "Ctrl".
     */
    public static final Key CONTROL = new Key(17, "Ctrl");
    /**
     * The alt key. The string representation of this key is "Alt".
     */
    public static final Key ALT = new Key(18, "Alt");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key PAUSE = new Key(19, "Pause");
    /**
     * The caps lock key. The string representation of this key is "CapsLock".
     */
    public static final Key CAPS_LOCK = new Key(20, "CapsLock");
    /**
     * The escape key. The string representation of this key is "Esc".
     */
    public static final Key ESCAPE = new Key(27, "Esc");
    /**
     * The space key. The string representation of this key is "Space".
     */
    public static final Key SPACE = new Key(32, "Space");
    /**
     * The page up key. The string representation of this key is "PageUp".
     */
    public static final Key PAGE_UP = new Key(33, "PageUp");
    /**
     * The page down key. The string representation of this key is "PageDown".
     */
    public static final Key PAGE_DOWN = new Key(34, "PageDown");
    /**
     * The end key. The string representation of this key is "End".
     */
    public static final Key END = new Key(35, "End");
    /**
     * The home key. The string representation of this key is "Home".
     */
    public static final Key HOME = new Key(36, "Home");
    /**
     * The cursor left key. The string representation of this key is "Left".
     */
    public static final Key LEFT = new Key(37, "Left");
    /**
     * The cursor up key. The string representation of this key is "Up".
     */
    public static final Key UP = new Key(38, "Up");
    /**
     * The cursor right key. The string representation of this key is "Right".
     */
    public static final Key RIGHT = new Key(39, "Right");
    /**
     * The cursor down key. The string representation of this key is "Down".
     */
    public static final Key DOWN = new Key(40, "Down");
    /**
     * The "0" digit key. The string representation of this key is "0".
     */
    public static final Key D0 = new Key(48, "0");
    /**
     * The "1" digit key. The string representation of this key is "1".
     */
    public static final Key D1 = new Key(49, "1");
    /**
     * The "2" digit key. The string representation of this key is "2".
     */
    public static final Key D2 = new Key(50, "2");
    /**
     * The "3" digit key. The string representation of this key is "3".
     */
    public static final Key D3 = new Key(51, "3");
    /**
     * The "4" digit key. The string representation of this key is "4".
     */
    public static final Key D4 = new Key(52, "4");
    /**
     * The "5" digit key. The string representation of this key is "5".
     */
    public static final Key D5 = new Key(53, "5");
    /**
     * The "6" digit key. The string representation of this key is "6".
     */
    public static final Key D6 = new Key(54, "6");
    /**
     * The "7" digit key. The string representation of this key is "7".
     */
    public static final Key D7 = new Key(55, "7");
    /**
     * The "8" digit key. The string representation of this key is "8".
     */
    public static final Key D8 = new Key(56, "8");
    /**
     * The "9" digit key. The string representation of this key is "9".
     */
    public static final Key D9 = new Key(57, "9");
    /**
     * The "A" key.
     */
    public static final Key A = new Key(65, "A");
    /**
     * The "B" key.
     */
    public static final Key B = new Key(66, "B");
    /**
     * The "C" key.
     */
    public static final Key C = new Key(67, "C");
    /**
     * The "D" key.
     */
    public static final Key D = new Key(68, "D");
    /**
     * The "E" key.
     */
    public static final Key E = new Key(69, "E");
    /**
     * The "F" key.
     */
    public static final Key F = new Key(70, "F");
    /**
     * The "G" key.
     */
    public static final Key G = new Key(71, "G");
    /**
     * The "H" key.
     */
    public static final Key H = new Key(72, "H");
    /**
     * The "I" key.
     */
    public static final Key I = new Key(73, "I");
    /**
     * The "J" key.
     */
    public static final Key J = new Key(74, "J");
    /**
     * The "K" key.
     */
    public static final Key K = new Key(75, "K");
    /**
     * The "L" key.
     */
    public static final Key L = new Key(76, "L");
    /**
     * The "M" key.
     */
    public static final Key M = new Key(77, "M");
    /**
     * The "N" key.
     */
    public static final Key N = new Key(78, "N");
    /**
     * The "O" key.
     */
    public static final Key O = new Key(79, "O");
    /**
     * The "P" key.
     */
    public static final Key P = new Key(80, "P");
    /**
     * The "Q" key.
     */
    public static final Key Q = new Key(81, "Q");
    /**
     * The "R" key.
     */
    public static final Key R = new Key(82, "R");
    /**
     * The "S" key.
     */
    public static final Key S = new Key(83, "S");
    /**
     * The "T" key.
     */
    public static final Key T = new Key(84, "T");
    /**
     * The "U" key.
     */
    public static final Key U = new Key(85, "U");
    /**
     * The "V" key.
     */
    public static final Key V = new Key(86, "V");
    /**
     * The "W" key.
     */
    public static final Key W = new Key(87, "W");
    /**
     * The "X" key.
     */
    public static final Key X = new Key(88, "X");
    /**
     * The "Y" key.
     */
    public static final Key Y = new Key(89, "Y");
    /**
     * The "Z" key.
     */
    public static final Key Z = new Key(90, "Z");
    /**
     * The "0" digit key on the numeric keypad. The string representation of
     * this key is "0".
     */
    public static final Key NUMPAD0 = new Key(96, "0");
    /**
     * The "1" digit key on the numeric keypad. The string representation of
     * this key is "1".
     */
    public static final Key NUMPAD1 = new Key(97, "1");
    /**
     * The "2" digit key on the numeric keypad. The string representation of
     * this key is "2".
     */
    public static final Key NUMPAD2 = new Key(98, "2");
    /**
     * The "3" digit key on the numeric keypad. The string representation of
     * this key is "3".
     */
    public static final Key NUMPAD3 = new Key(99, "3");
    /**
     * The "4" digit key on the numeric keypad. The string representation of
     * this key is "4".
     */
    public static final Key NUMPAD4 = new Key(100, "4");
    /**
     * The "5" digit key on the numeric keypad. The string representation of
     * this key is "5".
     */
    public static final Key NUMPAD5 = new Key(101, "5");
    /**
     * The "6" digit key on the numeric keypad. The string representation of
     * this key is "6".
     */
    public static final Key NUMPAD6 = new Key(102, "6");
    /**
     * The "7" digit key on the numeric keypad. The string representation of
     * this key is "7".
     */
    public static final Key NUMPAD7 = new Key(103, "7");
    /**
     * The "8" digit key on the numeric keypad. The string representation of
     * this key is "8".
     */
    public static final Key NUMPAD8 = new Key(104, "8");
    /**
     * The "9" digit key on the numeric keypad. The string representation of
     * this key is "9".
     */
    public static final Key NUMPAD9 = new Key(105, "9");
    /**
     * The "F1" function key.
     */
    public static final Key F1 = new Key(112, "F1");
    /**
     * The "F2" function key.
     */
    public static final Key F2 = new Key(113, "F2");
    /**
     * The "F3" function key.
     */
    public static final Key F3 = new Key(114, "F3");
    /**
     * The "F4" function key.
     */
    public static final Key F4 = new Key(115, "F4");
    /**
     * The "F5" function key.
     */
    public static final Key F5 = new Key(116, "F5");
    /**
     * The "F6" function key.
     */
    public static final Key F6 = new Key(117, "F6");
    /**
     * The "F7" function key.
     */
    public static final Key F7 = new Key(118, "F7");
    /**
     * The "F8" function key.
     */
    public static final Key F8 = new Key(119, "F8");
    /**
     * The "F9" function key.
     */
    public static final Key F9 = new Key(120, "F9");
    /**
     * The "F10" function key.
     */
    public static final Key F10 = new Key(121, "F10");
    /**
     * The "F11" function key.
     */
    public static final Key F11 = new Key(122, "F11");
    /**
     * The "F12" function key.
     */
    public static final Key F12 = new Key(123, "F12");
    /**
     * The delete key. The string representation of this key is "Delete".
     */
    public static final Key DELETE = new Key(127, "Delete");
    /**
     * The num lock key. The string representation of this key is "NumLock".
     */
    public static final Key NUM_LOCK = new Key(144, "NumLock");
    /**
     * The scroll lock key. The string representation of this key is "ScrollLock".
     */
    public static final Key SCROLL_LOCK = new Key(145, "ScrollLock");
    /**
     * The print screen key. The string representation of this key is "PrintScreen".
     */
    public static final Key PRINT_SCREEN = new Key(154, "PrintScreen");
    /**
     * The insert key. The string representation of this key is "Insert".
     */
    public static final Key INSERT = new Key(155, "Insert");
    /**
     * The quote key. The string representation of this key is "'".
     */
    public static final Key QUOTE = new Key(222, "'");
    /**
     * The alt graph key. The string representation of this key is "AltGr".
     */
    public static final Key ALT_GRAPH = new Key(65406, "AltGr");
    private final int keyCode;
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

    static Key keyFor(int keyCode) {
        return KEY_MAP.get(keyCode);
    }

    private Key(int keyCode, String name) {
        this.keyCode = keyCode;
        this.name = name;
        KEY_MAP.put(keyCode, this);
        NAME_MAP.put(name, this);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Key) {
            return this.keyCode == ((Key) object).keyCode;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.keyCode;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
