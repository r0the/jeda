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
package ch.jeda.platform.android;

import ch.jeda.event.EventType;
import ch.jeda.event.Key;
import ch.jeda.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

class EventMapper {

    private static final Map<Integer, EventSource> INPUT_DEVICE_MAP = new HashMap<Integer, EventSource>();
    private static final Map<Integer, Key> KEY_MAP = initKeyMap();

    static KeyEvent mapEvent(final android.view.KeyEvent event) {
        final Key key = KEY_MAP.get(event.getKeyCode());
        if (key == null) {
            return null;
        }

        switch (event.getAction()) {
            case android.view.KeyEvent.ACTION_DOWN:
                return new KeyEvent(mapDevice(event), EventType.KEY_DOWN, key, event.getRepeatCount());
            case android.view.KeyEvent.ACTION_UP:
                return new KeyEvent(mapDevice(event), EventType.KEY_UP, key);
            default:
                return null;
        }
    }

    static EventSource mapDevice(final android.view.InputEvent event) {
        final android.view.InputDevice device = event.getDevice();
        final int id = device.getId();
        if (!INPUT_DEVICE_MAP.containsKey(id)) {
            INPUT_DEVICE_MAP.put(id, new EventSource(device.getName()));
        }

        return INPUT_DEVICE_MAP.get(id);
    }

    private static Map<Integer, Key> initKeyMap() {
        final Map<Integer, Key> result = new HashMap();
        result.put(android.view.KeyEvent.KEYCODE_A, Key.A);
        result.put(android.view.KeyEvent.KEYCODE_ALT_LEFT, Key.ALT_LEFT);
        result.put(android.view.KeyEvent.KEYCODE_ALT_RIGHT, Key.ALT_RIGHT);
        result.put(android.view.KeyEvent.KEYCODE_APOSTROPHE, Key.APOSTROPHE);
        result.put(android.view.KeyEvent.KEYCODE_B, Key.B);
        result.put(android.view.KeyEvent.KEYCODE_BACKSLASH, Key.BACKSLASH);
        result.put(android.view.KeyEvent.KEYCODE_COMMA, Key.COMMA);
        // Android DEL key has the back space functionality
        result.put(android.view.KeyEvent.KEYCODE_DEL, Key.BACKSPACE);
        result.put(android.view.KeyEvent.KEYCODE_C, Key.C);
        result.put(android.view.KeyEvent.KEYCODE_D, Key.D);
        result.put(android.view.KeyEvent.KEYCODE_0, Key.DIGIT_0);
        result.put(android.view.KeyEvent.KEYCODE_1, Key.DIGIT_1);
        result.put(android.view.KeyEvent.KEYCODE_2, Key.DIGIT_2);
        result.put(android.view.KeyEvent.KEYCODE_3, Key.DIGIT_3);
        result.put(android.view.KeyEvent.KEYCODE_4, Key.DIGIT_4);
        result.put(android.view.KeyEvent.KEYCODE_5, Key.DIGIT_5);
        result.put(android.view.KeyEvent.KEYCODE_6, Key.DIGIT_6);
        result.put(android.view.KeyEvent.KEYCODE_7, Key.DIGIT_7);
        result.put(android.view.KeyEvent.KEYCODE_8, Key.DIGIT_8);
        result.put(android.view.KeyEvent.KEYCODE_9, Key.DIGIT_9);
        result.put(android.view.KeyEvent.KEYCODE_DPAD_DOWN, Key.DOWN);
        result.put(android.view.KeyEvent.KEYCODE_E, Key.E);
        result.put(android.view.KeyEvent.KEYCODE_ENTER, Key.ENTER);
        result.put(android.view.KeyEvent.KEYCODE_EQUALS, Key.EQUALS);
        result.put(android.view.KeyEvent.KEYCODE_F, Key.F);
        result.put(android.view.KeyEvent.KEYCODE_G, Key.G);
        result.put(android.view.KeyEvent.KEYCODE_GRAVE, Key.GRAVE);
        result.put(android.view.KeyEvent.KEYCODE_H, Key.H);
        result.put(android.view.KeyEvent.KEYCODE_I, Key.I);
        result.put(android.view.KeyEvent.KEYCODE_J, Key.J);
        result.put(android.view.KeyEvent.KEYCODE_K, Key.K);
        result.put(android.view.KeyEvent.KEYCODE_L, Key.L);
        result.put(android.view.KeyEvent.KEYCODE_DPAD_LEFT, Key.LEFT);
        result.put(android.view.KeyEvent.KEYCODE_M, Key.M);
        result.put(android.view.KeyEvent.KEYCODE_MENU, Key.MENU);
        result.put(android.view.KeyEvent.KEYCODE_MINUS, Key.MINUS);
        result.put(android.view.KeyEvent.KEYCODE_N, Key.N);
        result.put(android.view.KeyEvent.KEYCODE_O, Key.O);
        result.put(android.view.KeyEvent.KEYCODE_P, Key.P);
        result.put(android.view.KeyEvent.KEYCODE_PAGE_DOWN, Key.PAGE_DOWN);
        result.put(android.view.KeyEvent.KEYCODE_PAGE_UP, Key.PAGE_UP);
        result.put(android.view.KeyEvent.KEYCODE_PERIOD, Key.PERIOD);
        result.put(android.view.KeyEvent.KEYCODE_Q, Key.Q);
        result.put(android.view.KeyEvent.KEYCODE_R, Key.R);
        result.put(android.view.KeyEvent.KEYCODE_DPAD_RIGHT, Key.RIGHT);
        result.put(android.view.KeyEvent.KEYCODE_S, Key.S);
        result.put(android.view.KeyEvent.KEYCODE_SHIFT_LEFT, Key.SHIFT_LEFT);
        result.put(android.view.KeyEvent.KEYCODE_SHIFT_RIGHT, Key.SHIFT_RIGHT);
        result.put(android.view.KeyEvent.KEYCODE_SLASH, Key.SLASH);
        result.put(android.view.KeyEvent.KEYCODE_SPACE, Key.SPACE);
        result.put(android.view.KeyEvent.KEYCODE_T, Key.T);
        result.put(android.view.KeyEvent.KEYCODE_TAB, Key.TAB);
        result.put(android.view.KeyEvent.KEYCODE_U, Key.U);
        result.put(android.view.KeyEvent.KEYCODE_DPAD_UP, Key.UP);
        result.put(android.view.KeyEvent.KEYCODE_V, Key.V);
        result.put(android.view.KeyEvent.KEYCODE_VOLUME_DOWN, Key.VOLUME_DOWN);
        result.put(android.view.KeyEvent.KEYCODE_VOLUME_UP, Key.VOLUME_UP);
        result.put(android.view.KeyEvent.KEYCODE_W, Key.W);
        result.put(android.view.KeyEvent.KEYCODE_X, Key.X);
        result.put(android.view.KeyEvent.KEYCODE_Y, Key.Y);
        result.put(android.view.KeyEvent.KEYCODE_Z, Key.Z);

// Not supported
//        result.put(android.view.KeyEvent.KEYCODE_, Key.END);
//        result.put(android.view.KeyEvent.KEYCODE_, Key.HOME);
//        result.put(android.view.KeyEvent.KEYCODE_, Key.PAUSE);
//        result.put(android.view.KeyEvent.KEYCODE_, Key.PRINT_SCREEN);
//        result.put(android.view.KeyEvent.KEYCODE_, Key.INSERT);
//        result.put(android.view.KeyEvent.KEYCODE_, Key.DELETE);
// Only since API level 11
//        result.put(android.view.KeyEvent.KEYCODE_ALT_GRAPH, Key.ALT_GRAPH);
        result.put(android.view.KeyEvent.KEYCODE_CAPS_LOCK, Key.CAPS_LOCK);
        result.put(android.view.KeyEvent.KEYCODE_CTRL_LEFT, Key.CTRL_LEFT);
        result.put(android.view.KeyEvent.KEYCODE_CTRL_RIGHT, Key.CTRL_RIGHT);
        result.put(android.view.KeyEvent.KEYCODE_ESCAPE, Key.ESCAPE);
        result.put(android.view.KeyEvent.KEYCODE_F1, Key.F1);
        result.put(android.view.KeyEvent.KEYCODE_F2, Key.F2);
        result.put(android.view.KeyEvent.KEYCODE_F3, Key.F3);
        result.put(android.view.KeyEvent.KEYCODE_F4, Key.F4);
        result.put(android.view.KeyEvent.KEYCODE_F5, Key.F5);
        result.put(android.view.KeyEvent.KEYCODE_F6, Key.F6);
        result.put(android.view.KeyEvent.KEYCODE_F7, Key.F7);
        result.put(android.view.KeyEvent.KEYCODE_F8, Key.F8);
        result.put(android.view.KeyEvent.KEYCODE_F9, Key.F9);
        result.put(android.view.KeyEvent.KEYCODE_F10, Key.F10);
        result.put(android.view.KeyEvent.KEYCODE_F11, Key.F11);
        result.put(android.view.KeyEvent.KEYCODE_F12, Key.F12);
        result.put(android.view.KeyEvent.KEYCODE_NUM_LOCK, Key.NUM_LOCK);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_0, Key.NUMPAD_0);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_1, Key.NUMPAD_1);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_2, Key.NUMPAD_2);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_3, Key.NUMPAD_3);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_4, Key.NUMPAD_4);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_5, Key.NUMPAD_5);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_6, Key.NUMPAD_6);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_7, Key.NUMPAD_7);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_8, Key.NUMPAD_8);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_9, Key.NUMPAD_9);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_ADD, Key.NUMPAD_ADD);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_DIVIDE, Key.NUMPAD_DIVIDE);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_DOT, Key.NUMPAD_DOT);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_ENTER, Key.NUMPAD_ENTER);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_MULTIPLY, Key.NUMPAD_MULTIPLY);
        result.put(android.view.KeyEvent.KEYCODE_NUMPAD_SUBTRACT, Key.NUMPAD_SUBTRACT);
        result.put(android.view.KeyEvent.KEYCODE_SCROLL_LOCK, Key.SCROLL_LOCK);
        return result;
    }

    private static class EventSource {

        private final String name;

        public EventSource(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
