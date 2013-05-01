/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

/**
 * Represents a key that can be typed by the user. Key values are used by the
 * {@link Events} class to represent which keys have been pressed or typed. The
 * key value representing a specific keyboard key depends on the
 * <a href="http://en.wikipedia.org/wiki/Keyboard_layout">keyboard layout</a>.
 *
 * @see Events
 */
public enum Key {

    /**
     * The "A" key.
     */
    A,
    /**
     * The alt graph key. The text representation of this key is "AltGr".
     */
    ALT_GRAPH,
    /**
     * The left alt key. The text representation of this key is "AltLeft".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    ALT_LEFT,
    /**
     * The left alt key. The text representation of this key is "AltLeft".
     */
    ALT_RIGHT,
    /**
     * The apostrophe key. The text representation of this key is "Apostrophe".
     */
    APOSTROPHE,
    /**
     * The "B" key.
     */
    B,
    /**
     * The back key. The text representation of this key is "Back".
     */
    BACK,
    /**
     * The Backspace key. The text representation of this key is "Backspace".
     */
    BACKSPACE,
    /**
     * The "C" key.
     */
    C,
    /**
     * The caps lock key. The text representation of this key is "CapsLock".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CAPS_LOCK,
    /**
     * The left control key. The text representation of this key is "CtrlLeft".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CTRL_LEFT,
    /**
     * The right control key. The text representation of this key is
     * "CtrlRight".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CTRL_RIGHT,
    /**
     * The "D" key.
     */
    D,
    /**
     * The delete key. The text representation of this key is "Delete".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    DELETE,
    /**
     * The "0" digit key. The text representation of this key is "0".
     */
    DIGIT_0,
    /**
     * The "1" digit key. The text representation of this key is "1".
     */
    DIGIT_1,
    /**
     * The "2" digit key. The text representation of this key is "2".
     */
    DIGIT_2,
    /**
     * The "3" digit key. The text representation of this key is "3".
     */
    DIGIT_3,
    /**
     * The "4" digit key. The text representation of this key is "4".
     */
    DIGIT_4,
    /**
     * The "5" digit key. The text representation of this key is "5".
     */
    DIGIT_5,
    /**
     * The "6" digit key. The text representation of this key is "6".
     */
    DIGIT_6,
    /**
     * The "7" digit key. The text representation of this key is "7".
     */
    DIGIT_7,
    /**
     * The "8" digit key. The text representation of this key is "8".
     */
    DIGIT_8,
    /**
     * The "9" digit key. The text representation of this key is "9".
     */
    DIGIT_9,
    /**
     * The cursor down key. The text representation of this key is "Down".
     */
    DOWN,
    /**
     * The "E" key.
     */
    E,
    /**
     * The end key. The text representation of this key is "End".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    END,
    /**
     * The enter key. The text representation of this key is "Enter".
     */
    ENTER,
    /**
     * The equals key. The text representation of this key is "Equals".
     */
    EQUALS,
    /**
     * The escape key. The text representation of this key is "Esc".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    ESCAPE,
    /**
     * The "F" key.
     */
    F,
    /**
     * The "F1" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F1,
    /**
     * The "F2" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F2,
    /**
     * The "F3" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F3,
    /**
     * The "F4" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F4,
    /**
     * The "F5" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F5,
    /**
     * The "F6" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F6,
    /**
     * The "F7" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F7,
    /**
     * The "F8" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F8,
    /**
     * The "F9" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F9,
    /**
     * The "F10" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F10,
    /**
     * The "F11" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F11,
    /**
     * The "F12" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F12,
    /**
     * The "G" key.
     */
    G,
    /**
     * The "`" key. The text representation of this key is "Grave".
     */
    GRAVE,
    /**
     * The "H" key.
     */
    H,
    /**
     * The home key. The text representation of this key is "Home".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    HOME,
    /**
     * The "I" key.
     */
    I,
    /**
     * The insert key. The text representation of this key is "Insert".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    INSERT,
    /**
     * The "J" key.
     */
    J,
    /**
     * The "K" key.
     */
    K,
    /**
     * The "L" key.
     */
    L,
    /**
     * The cursor left key. The text representation of this key is "Left".
     */
    LEFT,
    /**
     * The "M" key.
     */
    M,
    /**
     * The menu key. The text representation of this key is "Menu".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png">
     * Represents the context menu key on the keyboard (usually located left of
     * the right control key.
     * <p>
     * <img src="../../../android.png"> Represents the menu key. This is the
     * hardware key that is used to display a menu.
     */
    MENU,
    /**
     * The "-" key. The text representation of this key is "Minus".
     */
    MINUS,
    /**
     * The middle mouse button.
     */
    MOUSE_MIDDLE,
    /**
     * The primary mouse button (usually left).
     */
    MOUSE_PRIMARY,
    /**
     * The seconary mouse button (usually right).
     */
    MOUSE_SECONDARY,
    /**
     * The "N" key.
     */
    N,
    /**
     * The num lock key. The text representation of this key is "NumLock".
     */
    NUM_LOCK,
    /**
     * The "0" digit key on the numeric keypad. The text representation of this
     * key is "Numpad0".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_0,
    /**
     * The "1" digit key on the numeric keypad. The text representation of this
     * key is "Numpad1".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_1,
    /**
     * The "2" digit key on the numeric keypad. The text representation of this
     * key is "Numpad2".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_2,
    /**
     * The "3" digit key on the numeric keypad. The text representation of this
     * key is "Numpad3".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_3,
    /**
     * The "4" digit key on the numeric keypad. The text representation of this
     * key is "Numpad4".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_4,
    /**
     * The "5" digit key on the numeric keypad. The text representation of this
     * key is "Numpad5".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_5,
    /**
     * The "6" digit key on the numeric keypad. The text representation of this
     * key is "Numpad6".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_6,
    /**
     * The "7" digit key on the numeric keypad. The text representation of this
     * key is "Numpad7".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_7,
    /**
     * The "8" digit key on the numeric keypad. The text representation of this
     * key is "Numpad8".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_8,
    /**
     * The "9" digit key on the numeric keypad. The text representation of this
     * key is "Numpad9".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_9,
    /**
     * The "+" key on the numeric keypard. The text representation of this key
     * is "NumpadAdd".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_ADD,
    /**
     * The "/" key on the numeric keypard. The text representation of this key
     * is "NumpadDivide".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_DIVIDE,
    /**
     * The "." key on the numeric keypard. The text representation of this key
     * is "NumpadDot".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_DOT,
    /**
     * The enter key on the numeric keypard. The text representation of this key
     * is "NumpadEnter".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_ENTER,
    /**
     * The "*" key on the numeric keypard. The text representation of this key
     * is "NumpadMultiply".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_MULTIPLY,
    /**
     * The "-" key on the numeric keypard. The text representation of this key
     * is "NumpadSubtract".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_SUBTRACT,
    /**
     * The "O" key.
     */
    O,
    /**
     * The "P" key.
     */
    P,
    /**
     * The page down key. The text representation of this key is "PageDown".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAGE_DOWN,
    /**
     * The page up key. The text representation of this key is "PageUp".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAGE_UP,
    /**
     * The pause key. The text representation of this key is "Pause".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAUSE,
    /**
     * The print screen key. The text representation of this key is
     * "PrintScreen".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PRINT_SCREEN,
    /**
     * The "Q" key.
     */
    Q,
    /**
     * The "R" key.
     */
    R,
    /**
     * The cursor right key. The text representation of this key is "Right".
     */
    RIGHT,
    /**
     * The "S" key.
     */
    S,
    /**
     * The scroll lock key. The text representation of this key is "ScrollLock".
     */
    SCROLL_LOCK,
    /**
     * The left shift key. The text representation of this key is "ShiftLeft".
     */
    SHIFT_LEFT,
    /**
     * The right shift key. The text representation of this key is "ShiftRight".
     */
    SHIFT_RIGHT,
    /**
     * The space key. The text representation of this key is "Space".
     */
    SPACE,
    /**
     * The "T" key.
     */
    T,
    /**
     * The tabulator key. The text representation of this key is "Tab".
     */
    TAB,
    /**
     * The "U" key.
     */
    U,
    UNDEFINED,
    /**
     * The cursor up key. The text representation of this key is "Up".
     */
    UP,
    /**
     * The "V" key.
     */
    V,
    /**
     * The volume down key. The text representation of this key is "VolumeUp".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     */
    VOLUME_DOWN,
    /**
     * The volume up key. The text representation of this key is "VolumeUp".
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key
     * is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     */
    VOLUME_UP,
    /**
     * The "W" key.
     */
    W,
    /**
     * The "X" key.
     */
    X,
    /**
     * The "Y" key.
     */
    Y,
    /**
     * The "Z" key.
     */
    Z
}
