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
package ch.jeda.event;

/**
 * Represents a key that can be typed by the user. Key values are used by the {@link ch.jeda.event.KeyEvent} class to
 * represent which keys have been pressed or typed. The key value representing a specific keyboard key depends on the
 * <a href="http://en.wikipedia.org/wiki/Keyboard_layout">keyboard layout</a>.
 *
 * @since 1
 */
public enum Key {

    /**
     * The "A" key.
     */
    A,
    /**
     * The alt graph key. This key is typically located right of the space key.
     */
    ALT_GRAPH,
    /**
     * The left alt key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    ALT_LEFT,
    /**
     * The right alt key.
     */
    ALT_RIGHT,
    /**
     * The apostrophe key.
     */
    APOSTROPHE,
    /**
     * The "B" key.
     */
    B,
    /**
     * The back key.
     */
    BACK,
    /**
     * The Backslash key.
     */
    BACKSLASH,
    /**
     * The Backspace key.
     */
    BACKSPACE,
    /**
     * The "C" key.
     */
    C,
    /**
     * The caps lock key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CAPS_LOCK,
    /**
     * The comma (",") key.
     */
    COMMA,
    /**
     * The left control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CTRL_LEFT,
    /**
     * The right control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    CTRL_RIGHT,
    /**
     * The "D" key.
     */
    D,
    /**
     * The dead diaeresis key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    DEAD_DIAERESIS,
    /**
     * The delete key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    DELETE,
    /**
     * The "0" digit key.
     */
    DIGIT_0,
    /**
     * The "1" digit key.
     */
    DIGIT_1,
    /**
     * The "2" digit key.
     */
    DIGIT_2,
    /**
     * The "3" digit key.
     */
    DIGIT_3,
    /**
     * The "4" digit key.
     */
    DIGIT_4,
    /**
     * The "5" digit key.
     */
    DIGIT_5,
    /**
     * The "6" digit key.
     */
    DIGIT_6,
    /**
     * The "7" digit key.
     */
    DIGIT_7,
    /**
     * The "8" digit key.
     */
    DIGIT_8,
    /**
     * The "9" digit key.
     */
    DIGIT_9,
    /**
     * The dollar ("$") key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    DOLLAR,
    /**
     * The cursor down key.
     */
    DOWN,
    /**
     * The "E" key.
     */
    E,
    /**
     * The end key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    END,
    /**
     * The enter key.
     */
    ENTER,
    /**
     * The equals key.
     */
    EQUALS,
    /**
     * The escape key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
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
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F1,
    /**
     * The "F2" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F2,
    /**
     * The "F3" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F3,
    /**
     * The "F4" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F4,
    /**
     * The "F5" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F5,
    /**
     * The "F6" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F6,
    /**
     * The "F7" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F7,
    /**
     * The "F8" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F8,
    /**
     * The "F9" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F9,
    /**
     * The "F10" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F10,
    /**
     * The "F11" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F11,
    /**
     * The "F12" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    F12,
    /**
     * The "G" key.
     */
    G,
    /**
     * The "`" key.
     */
    GRAVE,
    /**
     * The "H" key.
     */
    H,
    /**
     * The home key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    HOME,
    /**
     * The "I" key.
     */
    I,
    /**
     * The insert key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
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
     * The cursor left key.
     */
    LEFT,
    /**
     * The "M" key.
     */
    M,
    /**
     * The menu key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Represents the context menu key on the keyboard
     * (usually located left of the right control key.
     * <p>
     * <img src="../../../android.png"> Represents the menu key. This is the hardware key that is used to display a
     * menu.
     */
    MENU,
    /**
     * The meta key. The meaning of this key constant is OS-dependent.
     * <p>
     * <img src="../../../windows.png"> Represents the Windows key.
     * <p>
     * <img src="../../../linux.png"> Represents the Windows key.
     * </p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    META,
    /**
     * The minus ("-") key.
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
     * The num lock key.
     */
    NUM_LOCK,
    /**
     * The "0" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_0,
    /**
     * The "1" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_1,
    /**
     * The "2" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_2,
    /**
     * The "3" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_3,
    /**
     * The "4" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_4,
    /**
     * The "5" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_5,
    /**
     * The "6" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_6,
    /**
     * The "7" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_7,
    /**
     * The "8" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_8,
    /**
     * The "9" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_9,
    /**
     * The "+" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_ADD,
    /**
     * The "/" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_DIVIDE,
    /**
     * The "." key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_DOT,
    /**
     * The enter key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_ENTER,
    /**
     * The "*" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    NUMPAD_MULTIPLY,
    /**
     * The "-" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
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
     * The page down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAGE_DOWN,
    /**
     * The page up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAGE_UP,
    /**
     * The pause key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     */
    PAUSE,
    /**
     * The period (".") key.
     */
    PERIOD,
    /**
     * The print screen key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
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
     * The cursor right key.
     */
    RIGHT,
    /**
     * The "S" key.
     */
    S,
    /**
     * The scroll lock key.
     */
    SCROLL_LOCK,
    /**
     * The left shift key.
     */
    SHIFT_LEFT,
    /**
     * The right shift key.
     */
    SHIFT_RIGHT,
    /**
     * The forward slash ("/") key.
     */
    SLASH,
    /**
     * The space key.
     */
    SPACE,
    /**
     * The "T" key.
     */
    T,
    /**
     * The tabulator key.
     */
    TAB,
    /**
     * The "U" key.
     */
    U,
    UNDEFINED,
    /**
     * The cursor up key.
     */
    UP,
    /**
     * The "V" key.
     */
    V,
    /**
     * The volume down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     */
    VOLUME_DOWN,
    /**
     * The volume up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
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
