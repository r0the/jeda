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
     *
     * @since 1
     */
    A,
    /**
     * The alt graph key. This key is typically located right of the space key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    ALT_GRAPH,
    /**
     * The left alt key.
     *
     * @since 1
     */
    ALT_LEFT,
    /**
     * The right alt key.
     *
     * @since 1
     */
    ALT_RIGHT,
    /**
     * The apostrophe key.
     *
     * @since 1
     */
    APOSTROPHE,
    /**
     * The "B" key.
     *
     * @since 1
     */
    B,
    /**
     * The Backslash key.
     *
     * @since 1
     */
    BACKSLASH,
    /**
     * The Backspace key. Pressing this key usually deletes the character left of the cursor.
     *
     * @since 1
     */
    BACKSPACE,
    /**
     * The "C" key.
     *
     * @since 1
     */
    C,
    /**
     * The caps lock key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    CAPS_LOCK,
    /**
     * The comma (",") key.
     *
     * @since 1
     */
    COMMA,
    /**
     * The left control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    CTRL_LEFT,
    /**
     * The right control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    CTRL_RIGHT,
    /**
     * The "D" key.
     *
     * @since 1
     */
    D,
    /**
     * The dead diaeresis key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    DEAD_DIAERESIS,
    /**
     * The delete key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    DELETE,
    /**
     * The "0" digit key.
     *
     * @since 1
     */
    DIGIT_0,
    /**
     * The "1" digit key.
     *
     * @since 1
     */
    DIGIT_1,
    /**
     * The "2" digit key.
     *
     * @since 1
     */
    DIGIT_2,
    /**
     * The "3" digit key.
     *
     * @since 1
     */
    DIGIT_3,
    /**
     * The "4" digit key.
     *
     * @since 1
     */
    DIGIT_4,
    /**
     * The "5" digit key.
     *
     * @since 1
     */
    DIGIT_5,
    /**
     * The "6" digit key.
     *
     * @since 1
     */
    DIGIT_6,
    /**
     * The "7" digit key.
     *
     * @since 1
     */
    DIGIT_7,
    /**
     * The "8" digit key.
     *
     * @since 1
     */
    DIGIT_8,
    /**
     * The "9" digit key.
     *
     * @since 1
     */
    DIGIT_9,
    /**
     * The dollar ("$") key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    DOLLAR,
    /**
     * The cursor down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Represents the cursor down key.
     * <p>
     * <img src="../../../android.png"> Represents the directional pad down hardware key.
     *
     * @since 1
     */
    DOWN,
    /**
     * The "E" key.
     *
     * @since 1
     */
    E,
    /**
     * The end key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    END,
    /**
     * The enter key.
     *
     * @since 1
     */
    ENTER,
    /**
     * The equals key.
     *
     * @since 1
     */
    EQUALS,
    /**
     * The escape key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    ESCAPE,
    /**
     * The "F" key.
     *
     * @since 1
     */
    F,
    /**
     * The "F1" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F1,
    /**
     * The "F2" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F2,
    /**
     * The "F3" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F3,
    /**
     * The "F4" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F4,
    /**
     * The "F5" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F5,
    /**
     * The "F6" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F6,
    /**
     * The "F7" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F7,
    /**
     * The "F8" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F8,
    /**
     * The "F9" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F9,
    /**
     * The "F10" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F10,
    /**
     * The "F11" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F11,
    /**
     * The "F12" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    F12,
    /**
     * The "G" key.
     *
     * @since 1
     */
    G,
    /**
     * The "`" key.
     *
     * @since 1
     */
    GRAVE,
    /**
     * The "H" key.
     *
     * @since 1
     */
    H,
    /**
     * The home key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    HOME,
    /**
     * The "I" key.
     *
     * @since 1
     */
    I,
    /**
     * The insert key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    INSERT,
    /**
     * The "J" key.
     *
     * @since 1
     */
    J,
    /**
     * The "K" key.
     *
     * @since 1
     */
    K,
    /**
     * The "L" key.
     *
     * @since 1
     */
    L,
    /**
     * The cursor left key.
     *
     * @since 1
     */
    LEFT,
    /**
     * The "M" key.
     *
     * @since 1
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
     *
     * @since 1
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
     *
     * @since 1
     */
    META,
    /**
     * The minus ("-") key.
     *
     * @since 1
     */
    MINUS,
    /**
     * The middle mouse button.
     *
     * @since 1
     */
    MOUSE_MIDDLE,
    /**
     * The primary mouse button (usually left).
     *
     * @since 1
     */
    MOUSE_PRIMARY,
    /**
     * The seconary mouse button (usually right).
     *
     * @since 1
     */
    MOUSE_SECONDARY,
    /**
     * The "N" key.
     *
     * @since 1
     */
    N,
    /**
     * The num lock key.
     *
     * @since 1
     */
    NUM_LOCK,
    /**
     * The "0" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_0,
    /**
     * The "1" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_1,
    /**
     * The "2" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_2,
    /**
     * The "3" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_3,
    /**
     * The "4" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_4,
    /**
     * The "5" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_5,
    /**
     * The "6" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_6,
    /**
     * The "7" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_7,
    /**
     * The "8" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_8,
    /**
     * The "9" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_9,
    /**
     * The "+" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_ADD,
    /**
     * The "/" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_DIVIDE,
    /**
     * The "." key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_DOT,
    /**
     * The enter key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_ENTER,
    /**
     * The "*" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_MULTIPLY,
    /**
     * The "-" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    NUMPAD_SUBTRACT,
    /**
     * The "O" key.
     *
     * @since 1
     */
    O,
    /**
     * The "P" key.
     *
     * @since 1
     */
    P,
    /**
     * The page down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    PAGE_DOWN,
    /**
     * The page up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    PAGE_UP,
    /**
     * The pause key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    PAUSE,
    /**
     * The period (".") key.
     *
     * @since 1
     */
    PERIOD,
    /**
     * The print screen key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    PRINT_SCREEN,
    /**
     * The "Q" key.
     *
     * @since 1
     */
    Q,
    /**
     * The "R" key.
     *
     * @since 1
     */
    R,
    /**
     * The cursor right key.
     *
     * @since 1
     */
    RIGHT,
    /**
     * The "S" key.
     *
     * @since 1
     */
    S,
    /**
     * The scroll lock key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1
     */
    SCROLL_LOCK,
    /**
     * The left shift key.
     *
     * @since 1
     */
    SHIFT_LEFT,
    /**
     * The right shift key.
     *
     * @since 1
     */
    SHIFT_RIGHT,
    /**
     * The forward slash ("/") key.
     *
     * @since 1
     */
    SLASH,
    /**
     * The space key.
     *
     * @since 1
     */
    SPACE,
    /**
     * The "T" key.
     *
     * @since 1
     */
    T,
    /**
     * The tabulator key.
     *
     * @since 1
     */
    TAB,
    /**
     * The "U" key.
     *
     * @since 1
     */
    U,
    /**
     * The undefined key.
     *
     * @since 1
     */
    UNDEFINED,
    /**
     * The cursor up key.
     *
     * @since 1
     */
    UP,
    /**
     * The "V" key.
     *
     * @since 1
     */
    V,
    /**
     * The volume down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     *
     * @since 1
     */
    VOLUME_DOWN,
    /**
     * The volume up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     *
     * @since 1
     */
    VOLUME_UP,
    /**
     * The "W" key.
     *
     * @since 1
     */
    W,
    /**
     * The "X" key.
     *
     * @since 1
     */
    X,
    /**
     * The "Y" key.
     *
     * @since 1
     */
    Y,
    /**
     * The "Z" key.
     *
     * @since 1
     */
    Z
}
