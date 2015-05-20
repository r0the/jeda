/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
 * @since 1.0
 */
public enum Key {

    /**
     * The "A" key.
     *
     * @since 1.0
     */
    A,
    /**
     * The alt graph key. This key is typically located right of the space key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    ALT_GRAPH,
    /**
     * The left alt key.
     *
     * @since 1.0
     */
    ALT_LEFT,
    /**
     * The right alt key.
     *
     * @since 1.0
     */
    ALT_RIGHT,
    /**
     * The apostrophe key.
     *
     * @since 1.0
     */
    APOSTROPHE,
    /**
     * The "B" key.
     *
     * @since 1.0
     */
    B,
    /**
     * The Backslash key.
     *
     * @since 1.0
     */
    BACKSLASH,
    /**
     * The Backspace key. Pressing this key usually deletes the character left of the cursor.
     *
     * @since 1.0
     */
    BACKSPACE,
    /**
     * The "C" key.
     *
     * @since 1.0
     */
    C,
    /**
     * The caps lock key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    CAPS_LOCK,
    /**
     * The comma (",") key.
     *
     * @since 1.0
     */
    COMMA,
    /**
     * The left control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    CTRL_LEFT,
    /**
     * The right control key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    CTRL_RIGHT,
    /**
     * The "D" key.
     *
     * @since 1.0
     */
    D,
    /**
     * The dead diaeresis key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    DEAD_DIAERESIS,
    /**
     * The delete key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    DELETE,
    /**
     * The "0" digit key.
     *
     * @since 1.0
     */
    DIGIT_0,
    /**
     * The "1" digit key.
     *
     * @since 1.0
     */
    DIGIT_1,
    /**
     * The "2" digit key.
     *
     * @since 1.0
     */
    DIGIT_2,
    /**
     * The "3" digit key.
     *
     * @since 1.0
     */
    DIGIT_3,
    /**
     * The "4" digit key.
     *
     * @since 1.0
     */
    DIGIT_4,
    /**
     * The "5" digit key.
     *
     * @since 1.0
     */
    DIGIT_5,
    /**
     * The "6" digit key.
     *
     * @since 1.0
     */
    DIGIT_6,
    /**
     * The "7" digit key.
     *
     * @since 1.0
     */
    DIGIT_7,
    /**
     * The "8" digit key.
     *
     * @since 1.0
     */
    DIGIT_8,
    /**
     * The "9" digit key.
     *
     * @since 1.0
     */
    DIGIT_9,
    /**
     * The dollar ("$") key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    DOLLAR,
    /**
     * The cursor down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Represents the cursor down key.
     * <p>
     * <img src="../../../android.png"> Represents the directional pad down hardware key.
     *
     * @since 1.0
     */
    DOWN,
    /**
     * The "E" key.
     *
     * @since 1.0
     */
    E,
    /**
     * The end key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    END,
    /**
     * The enter key.
     *
     * @since 1.0
     */
    ENTER,
    /**
     * The equals key.
     *
     * @since 1.0
     */
    EQUALS,
    /**
     * The escape key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    ESCAPE,
    /**
     * The "F" key.
     *
     * @since 1.0
     */
    F,
    /**
     * The "F1" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F1,
    /**
     * The "F2" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F2,
    /**
     * The "F3" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F3,
    /**
     * The "F4" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F4,
    /**
     * The "F5" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F5,
    /**
     * The "F6" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F6,
    /**
     * The "F7" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F7,
    /**
     * The "F8" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F8,
    /**
     * The "F9" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F9,
    /**
     * The "F10" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F10,
    /**
     * The "F11" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F11,
    /**
     * The "F12" function key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    F12,
    /**
     * The "G" key.
     *
     * @since 1.0
     */
    G,
    /**
     * The "`" key.
     *
     * @since 1.0
     */
    GRAVE,
    /**
     * The "H" key.
     *
     * @since 1.0
     */
    H,
    /**
     * The home key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    HOME,
    /**
     * The "I" key.
     *
     * @since 1.0
     */
    I,
    /**
     * The insert key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    INSERT,
    /**
     * The "J" key.
     *
     * @since 1.0
     */
    J,
    /**
     * The "K" key.
     *
     * @since 1.0
     */
    K,
    /**
     * The "L" key.
     *
     * @since 1.0
     */
    L,
    /**
     * The cursor left key.
     *
     * @since 1.0
     */
    LEFT,
    /**
     * The "M" key.
     *
     * @since 1.0
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
     * @since 1.0
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
     * @since 1.0
     */
    META,
    /**
     * The minus ("-") key.
     *
     * @since 1.0
     */
    MINUS,
    /**
     * The middle mouse button.
     *
     * @since 1.0
     * @deprecated Use {@link PointerEvent#isPressed(ch.jeda.event.PushButton)} instead.
     */
    MOUSE_MIDDLE,
    /**
     * The primary mouse button (usually left).
     *
     * @since 1.0
     * @deprecated Use {@link PointerEvent#isPressed(ch.jeda.event.PushButton)} instead.
     */
    MOUSE_PRIMARY,
    /**
     * The seconary mouse button (usually right).
     *
     * @since 1.0
     * @deprecated Use {@link PointerEvent#isPressed(ch.jeda.event.PushButton)} instead.
     */
    MOUSE_SECONDARY,
    /**
     * The "N" key.
     *
     * @since 1.0
     */
    N,
    /**
     * The num lock key.
     *
     * @since 1.0
     */
    NUM_LOCK,
    /**
     * The "0" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_0,
    /**
     * The "1" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_1,
    /**
     * The "2" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_2,
    /**
     * The "3" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_3,
    /**
     * The "4" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_4,
    /**
     * The "5" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_5,
    /**
     * The "6" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_6,
    /**
     * The "7" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_7,
    /**
     * The "8" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_8,
    /**
     * The "9" digit key on the numeric keypad.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_9,
    /**
     * The "+" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_ADD,
    /**
     * The "/" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_DIVIDE,
    /**
     * The "." key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_DOT,
    /**
     * The enter key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_ENTER,
    /**
     * The "*" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_MULTIPLY,
    /**
     * The "-" key on the numeric keypard.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    NUMPAD_SUBTRACT,
    /**
     * The "O" key.
     *
     * @since 1.0
     */
    O,
    /**
     * The "P" key.
     *
     * @since 1.0
     */
    P,
    /**
     * The page down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    PAGE_DOWN,
    /**
     * The page up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    PAGE_UP,
    /**
     * The pause key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    PAUSE,
    /**
     * The period (".") key.
     *
     * @since 1.0
     */
    PERIOD,
    /**
     * The print screen key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    PRINT_SCREEN,
    /**
     * The "Q" key.
     *
     * @since 1.0
     */
    Q,
    /**
     * The "R" key.
     *
     * @since 1.0
     */
    R,
    /**
     * The cursor right key.
     *
     * @since 1.0
     */
    RIGHT,
    /**
     * The "S" key.
     *
     * @since 1.0
     */
    S,
    /**
     * The scroll lock key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is available.
     * <p>
     * <img src="../../../android.png"> The key is <b>not</b> available.
     *
     * @since 1.0
     */
    SCROLL_LOCK,
    /**
     * The left shift key.
     *
     * @since 1.0
     */
    SHIFT_LEFT,
    /**
     * The right shift key.
     *
     * @since 1.0
     */
    SHIFT_RIGHT,
    /**
     * The forward slash ("/") key.
     *
     * @since 1.0
     */
    SLASH,
    /**
     * The space key.
     *
     * @since 1.0
     */
    SPACE,
    /**
     * The "T" key.
     *
     * @since 1.0
     */
    T,
    /**
     * The tabulator key.
     *
     * @since 1.0
     */
    TAB,
    /**
     * The "U" key.
     *
     * @since 1.0
     */
    U,
    /**
     * The undefined key.
     *
     * @since 1.0
     */
    UNDEFINED,
    /**
     * The cursor up key.
     *
     * @since 1.0
     */
    UP,
    /**
     * The "V" key.
     *
     * @since 1.0
     */
    V,
    /**
     * The volume down key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     *
     * @since 1.0
     */
    VOLUME_DOWN,
    /**
     * The volume up key.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The key is <b>not</b> available.
     * <p>
     * <img src="../../../android.png"> The key is available.
     *
     * @since 1.0
     */
    VOLUME_UP,
    /**
     * The "W" key.
     *
     * @since 1.0
     */
    W,
    /**
     * The "X" key.
     *
     * @since 1.0
     */
    X,
    /**
     * The "Y" key.
     *
     * @since 1.0
     */
    Y,
    /**
     * The "Z" key.
     *
     * @since 1.0
     */
    Z
}
