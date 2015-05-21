/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.platform.java;

import ch.jeda.event.Button;
import ch.jeda.event.Key;
import ch.jeda.ui.MouseCursor;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

class Mapper {

    private static final Map<Integer, Key> BUTTON_MAP = initButtonMap();
    private static final Map<Integer, Map<Integer, Key>> KEY_MAP = initKeyMap();
    private static final EnumMap<MouseCursor, Cursor> MOUSE_CURSOR_MAP = initCursorMap();

    static EnumSet<Button> mapButtons(int modifiers) {
        final EnumSet<Button> result = EnumSet.noneOf(Button.class);
        if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            result.add(Button.PRIMARY);
        }

        if ((modifiers & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
            result.add(Button.TERTIARY);
        }

        if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            result.add(Button.SECONDARY);
        }

        return result;
    }

    static Key mapButton(final int button) {
        return BUTTON_MAP.get(button);
    }

    static Cursor mapCursor(final MouseCursor mouseCursor) {
        return MOUSE_CURSOR_MAP.get(mouseCursor);
    }

    static Key mapKey(final java.awt.event.KeyEvent event) {
        final int keyLocation = event.getKeyLocation();
        if (KEY_MAP.containsKey(keyLocation)) {
            int keyCode = event.getExtendedKeyCode();
            // Java on MacOS doesn't return extended key codes
            if (keyCode == 0) {
                keyCode = event.getKeyCode();
            }

            return KEY_MAP.get(keyLocation).get(keyCode);
        }
        else {
            return null;
        }
    }

    private static Map<Integer, Key> initButtonMap() {
        final Map<Integer, Key> result = new HashMap();
        result.put(MouseEvent.BUTTON1, Key.MOUSE_PRIMARY);
        result.put(MouseEvent.BUTTON2, Key.MOUSE_MIDDLE);
        result.put(MouseEvent.BUTTON3, Key.MOUSE_SECONDARY);
        return result;
    }

    private static Map<Integer, Map<Integer, Key>> initKeyMap() {
        final Map<Integer, Map<Integer, Key>> result = new HashMap();
        final Map<Integer, Key> standard = new HashMap();
        final Map<Integer, Key> left = new HashMap();
        final Map<Integer, Key> numpad = new HashMap();
        final Map<Integer, Key> right = new HashMap();
        final Map<Integer, Key> unknown = new HashMap();

        standard.put(java.awt.event.KeyEvent.VK_0, Key.DIGIT_0);
        standard.put(java.awt.event.KeyEvent.VK_1, Key.DIGIT_1);
        standard.put(java.awt.event.KeyEvent.VK_2, Key.DIGIT_2);
        standard.put(java.awt.event.KeyEvent.VK_3, Key.DIGIT_3);
        standard.put(java.awt.event.KeyEvent.VK_4, Key.DIGIT_4);
        standard.put(java.awt.event.KeyEvent.VK_5, Key.DIGIT_5);
        standard.put(java.awt.event.KeyEvent.VK_6, Key.DIGIT_6);
        standard.put(java.awt.event.KeyEvent.VK_7, Key.DIGIT_7);
        standard.put(java.awt.event.KeyEvent.VK_8, Key.DIGIT_8);
        standard.put(java.awt.event.KeyEvent.VK_9, Key.DIGIT_9);
        standard.put(java.awt.event.KeyEvent.VK_A, Key.A);
        standard.put(java.awt.event.KeyEvent.VK_ALT_GRAPH, Key.ALT_GRAPH);
        left.put(java.awt.event.KeyEvent.VK_ALT, Key.ALT_LEFT);
        right.put(java.awt.event.KeyEvent.VK_ALT, Key.ALT_RIGHT);
        standard.put(java.awt.event.KeyEvent.VK_QUOTE, Key.APOSTROPHE);
        standard.put(java.awt.event.KeyEvent.VK_B, Key.B);
        standard.put(java.awt.event.KeyEvent.VK_BACK_QUOTE, Key.GRAVE);
        standard.put(java.awt.event.KeyEvent.VK_BACK_SLASH, Key.BACKSLASH);
        standard.put(java.awt.event.KeyEvent.VK_BACK_SPACE, Key.BACKSPACE);
        standard.put(java.awt.event.KeyEvent.VK_C, Key.C);
        standard.put(java.awt.event.KeyEvent.VK_CAPS_LOCK, Key.CAPS_LOCK);
        standard.put(java.awt.event.KeyEvent.VK_COMMA, Key.COMMA);
        left.put(java.awt.event.KeyEvent.VK_CONTROL, Key.CTRL_LEFT);
        right.put(java.awt.event.KeyEvent.VK_CONTROL, Key.CTRL_RIGHT);
        standard.put(java.awt.event.KeyEvent.VK_D, Key.D);
        standard.put(java.awt.event.KeyEvent.VK_DEAD_DIAERESIS, Key.DEAD_DIAERESIS);
        standard.put(java.awt.event.KeyEvent.VK_DELETE, Key.DELETE);
        standard.put(java.awt.event.KeyEvent.VK_DOLLAR, Key.DOLLAR);
        standard.put(java.awt.event.KeyEvent.VK_DOWN, Key.DOWN);
        standard.put(java.awt.event.KeyEvent.VK_E, Key.E);
        standard.put(java.awt.event.KeyEvent.VK_END, Key.END);
        standard.put(java.awt.event.KeyEvent.VK_ENTER, Key.ENTER);
        standard.put(java.awt.event.KeyEvent.VK_EQUALS, Key.EQUALS);
        standard.put(java.awt.event.KeyEvent.VK_ESCAPE, Key.ESCAPE);
        standard.put(java.awt.event.KeyEvent.VK_F, Key.F);
        standard.put(java.awt.event.KeyEvent.VK_F1, Key.F1);
        standard.put(java.awt.event.KeyEvent.VK_F2, Key.F2);
        standard.put(java.awt.event.KeyEvent.VK_F3, Key.F3);
        standard.put(java.awt.event.KeyEvent.VK_F4, Key.F4);
        standard.put(java.awt.event.KeyEvent.VK_F5, Key.F5);
        standard.put(java.awt.event.KeyEvent.VK_F6, Key.F6);
        standard.put(java.awt.event.KeyEvent.VK_F7, Key.F7);
        standard.put(java.awt.event.KeyEvent.VK_F8, Key.F8);
        standard.put(java.awt.event.KeyEvent.VK_F9, Key.F9);
        standard.put(java.awt.event.KeyEvent.VK_F10, Key.F10);
        standard.put(java.awt.event.KeyEvent.VK_F11, Key.F11);
        standard.put(java.awt.event.KeyEvent.VK_F12, Key.F12);
        standard.put(java.awt.event.KeyEvent.VK_G, Key.G);
        standard.put(java.awt.event.KeyEvent.VK_H, Key.H);
        standard.put(java.awt.event.KeyEvent.VK_HOME, Key.HOME);
        standard.put(java.awt.event.KeyEvent.VK_I, Key.I);
        standard.put(java.awt.event.KeyEvent.VK_INSERT, Key.INSERT);
        standard.put(java.awt.event.KeyEvent.VK_J, Key.J);
        standard.put(java.awt.event.KeyEvent.VK_K, Key.K);
        standard.put(java.awt.event.KeyEvent.VK_L, Key.L);
        standard.put(java.awt.event.KeyEvent.VK_LEFT, Key.LEFT);
        standard.put(java.awt.event.KeyEvent.VK_M, Key.M);
        standard.put(java.awt.event.KeyEvent.VK_CONTEXT_MENU, Key.MENU);
        standard.put(java.awt.event.KeyEvent.VK_MINUS, Key.MINUS);
        standard.put(java.awt.event.KeyEvent.VK_N, Key.N);
        numpad.put(java.awt.event.KeyEvent.VK_NUM_LOCK, Key.NUM_LOCK);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD0, Key.NUMPAD_0);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD1, Key.NUMPAD_1);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD2, Key.NUMPAD_2);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD3, Key.NUMPAD_3);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD4, Key.NUMPAD_4);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD5, Key.NUMPAD_5);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD6, Key.NUMPAD_6);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD7, Key.NUMPAD_7);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD8, Key.NUMPAD_8);
        numpad.put(java.awt.event.KeyEvent.VK_NUMPAD9, Key.NUMPAD_9);
        numpad.put(java.awt.event.KeyEvent.VK_ADD, Key.NUMPAD_ADD);
        numpad.put(java.awt.event.KeyEvent.VK_DIVIDE, Key.NUMPAD_DIVIDE);
        numpad.put(java.awt.event.KeyEvent.VK_DECIMAL, Key.NUMPAD_DOT);
        numpad.put(java.awt.event.KeyEvent.VK_MULTIPLY, Key.NUMPAD_MULTIPLY);
        numpad.put(java.awt.event.KeyEvent.VK_SUBTRACT, Key.NUMPAD_SUBTRACT);
        numpad.put(java.awt.event.KeyEvent.VK_ENTER, Key.NUMPAD_ENTER);
        standard.put(java.awt.event.KeyEvent.VK_O, Key.O);
        standard.put(java.awt.event.KeyEvent.VK_P, Key.P);
        standard.put(java.awt.event.KeyEvent.VK_PAGE_DOWN, Key.PAGE_DOWN);
        standard.put(java.awt.event.KeyEvent.VK_PAGE_UP, Key.PAGE_UP);
        standard.put(java.awt.event.KeyEvent.VK_PAUSE, Key.PAUSE);
        standard.put(java.awt.event.KeyEvent.VK_PERIOD, Key.PERIOD);
        standard.put(java.awt.event.KeyEvent.VK_PRINTSCREEN, Key.PRINT_SCREEN);
        standard.put(java.awt.event.KeyEvent.VK_Q, Key.Q);
        standard.put(java.awt.event.KeyEvent.VK_R, Key.R);
        standard.put(java.awt.event.KeyEvent.VK_RIGHT, Key.RIGHT);
        standard.put(java.awt.event.KeyEvent.VK_S, Key.S);
        standard.put(java.awt.event.KeyEvent.VK_SCROLL_LOCK, Key.SCROLL_LOCK);
        left.put(java.awt.event.KeyEvent.VK_SHIFT, Key.SHIFT_LEFT);
        right.put(java.awt.event.KeyEvent.VK_SHIFT, Key.SHIFT_RIGHT);
        standard.put(java.awt.event.KeyEvent.VK_SLASH, Key.SLASH);
        standard.put(java.awt.event.KeyEvent.VK_SPACE, Key.SPACE);
        standard.put(java.awt.event.KeyEvent.VK_T, Key.T);
        standard.put(java.awt.event.KeyEvent.VK_TAB, Key.TAB);
        standard.put(java.awt.event.KeyEvent.VK_U, Key.U);
        standard.put(java.awt.event.KeyEvent.VK_UP, Key.UP);
        standard.put(java.awt.event.KeyEvent.VK_V, Key.V);
        standard.put(java.awt.event.KeyEvent.VK_W, Key.W);
        standard.put(java.awt.event.KeyEvent.VK_WINDOWS, Key.META);
        standard.put(java.awt.event.KeyEvent.VK_X, Key.X);
        standard.put(java.awt.event.KeyEvent.VK_Y, Key.Y);
        standard.put(java.awt.event.KeyEvent.VK_Z, Key.Z);

//        result.put(java.awt.event.KeyEvent.VK_ALT_GRAPH, Key.ALT_GRAPH);
        // When caps lock is engaged, the letter keys have location unknown.
        unknown.put(java.awt.event.KeyEvent.VK_A, Key.A);
        unknown.put(java.awt.event.KeyEvent.VK_B, Key.B);
        unknown.put(java.awt.event.KeyEvent.VK_C, Key.C);
        unknown.put(java.awt.event.KeyEvent.VK_D, Key.D);
        unknown.put(java.awt.event.KeyEvent.VK_E, Key.E);
        unknown.put(java.awt.event.KeyEvent.VK_F, Key.F);
        unknown.put(java.awt.event.KeyEvent.VK_G, Key.G);
        unknown.put(java.awt.event.KeyEvent.VK_H, Key.H);
        unknown.put(java.awt.event.KeyEvent.VK_I, Key.I);
        unknown.put(java.awt.event.KeyEvent.VK_J, Key.J);
        unknown.put(java.awt.event.KeyEvent.VK_K, Key.K);
        unknown.put(java.awt.event.KeyEvent.VK_L, Key.L);
        unknown.put(java.awt.event.KeyEvent.VK_M, Key.M);
        unknown.put(java.awt.event.KeyEvent.VK_N, Key.N);
        unknown.put(java.awt.event.KeyEvent.VK_O, Key.O);
        unknown.put(java.awt.event.KeyEvent.VK_P, Key.P);
        unknown.put(java.awt.event.KeyEvent.VK_Q, Key.Q);
        unknown.put(java.awt.event.KeyEvent.VK_R, Key.R);
        unknown.put(java.awt.event.KeyEvent.VK_S, Key.S);
        unknown.put(java.awt.event.KeyEvent.VK_T, Key.T);
        unknown.put(java.awt.event.KeyEvent.VK_U, Key.U);
        unknown.put(java.awt.event.KeyEvent.VK_V, Key.V);
        unknown.put(java.awt.event.KeyEvent.VK_W, Key.W);
        unknown.put(java.awt.event.KeyEvent.VK_X, Key.X);
        unknown.put(java.awt.event.KeyEvent.VK_Y, Key.Y);
        unknown.put(java.awt.event.KeyEvent.VK_Z, Key.Z);

        result.put(java.awt.event.KeyEvent.KEY_LOCATION_LEFT, left);
        result.put(java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD, numpad);
        result.put(java.awt.event.KeyEvent.KEY_LOCATION_RIGHT, right);
        result.put(java.awt.event.KeyEvent.KEY_LOCATION_STANDARD, standard);
        result.put(java.awt.event.KeyEvent.KEY_LOCATION_UNKNOWN, unknown);
        return result;
    }

    private static Cursor createInvisibleCursor() {
        final java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        final int[] pixels = new int[16 * 16];
        final java.awt.Image image = toolkit.createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
        return toolkit.createCustomCursor(image, new java.awt.Point(0, 0), "invisibleCursor");
    }

    private static EnumMap<MouseCursor, Cursor> initCursorMap() {
        final EnumMap<MouseCursor, Cursor> result = new EnumMap(MouseCursor.class
        );
        result.put(MouseCursor.CROSSHAIR,
                   new Cursor(Cursor.CROSSHAIR_CURSOR));
        result.put(MouseCursor.DEFAULT,
                   new Cursor(Cursor.DEFAULT_CURSOR));
        result.put(MouseCursor.HAND,
                   new Cursor(Cursor.HAND_CURSOR));
        result.put(MouseCursor.INVISIBLE, createInvisibleCursor());
        result.put(MouseCursor.TEXT,
                   new Cursor(Cursor.TEXT_CURSOR));
        return result;
    }
}
