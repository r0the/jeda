/*
 * Copyright (C) 2012 by Stefan Rothe
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

import ch.jeda.Location;
import ch.jeda.platform.EventsImp;
import ch.jeda.ui.Key;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JavaEventsImp implements EventsImp {

    private static final Map<Integer, Key> KEY_MAP = initKeyMap();
    private final FocusEventQueue focusEventQueue;
    private final KeyboardEventQueue keyboardEventQueue;
    private final MouseEventQueue mouseEventQueue;
    private final Set<Key> pressedKeys;
    private final List<Key> typedKeys;
    private boolean clicked;
    private String typedChars;
    private Location pointerLocation;
    private boolean pointerAvailable;

    JavaEventsImp(Component component) {
        this.focusEventQueue = new FocusEventQueue();
        this.keyboardEventQueue = new KeyboardEventQueue();
        this.mouseEventQueue = new MouseEventQueue();
        this.pressedKeys = new HashSet();
        this.typedKeys = new ArrayList();
        this.typedChars = "";
        this.pointerLocation = Location.ORIGIN;

        component.addFocusListener(this.focusEventQueue);
        component.addKeyListener(this.keyboardEventQueue);
        component.addMouseListener(this.mouseEventQueue);
        component.addMouseMotionListener(this.mouseEventQueue);
        component.addMouseWheelListener(this.mouseEventQueue);
    }

    @Override
    public Location getPointerLocation() {
        return this.pointerLocation;
    }

    @Override
    public Set<Key> getPressedKeys() {
        return Collections.unmodifiableSet(this.pressedKeys);
    }

    @Override
    public String getTypedChars() {
        return this.typedChars;
    }

    @Override
    public List<Key> getTypedKeys() {
        return Collections.unmodifiableList(this.typedKeys);
    }

    @Override
    public boolean isClicked() {
        return this.clicked;
    }

    @Override
    public boolean isPointerAvailable() {
        return this.pointerAvailable;
    }

    void update() {
        this.clicked = false;
        this.typedChars = "";
        this.typedKeys.clear();
        this.keyboardEventQueue.swap();
        this.focusEventQueue.swap();
        this.mouseEventQueue.swap();
        for (KeyEvent event : this.keyboardEventQueue) {
            this.handleKeyEvent(event);
        }

        for (FocusEvent event : this.focusEventQueue) {
            this.handleFocusEvent(event);
        }

        for (MouseEvent event : this.mouseEventQueue) {
            this.handleMouseEvent(event);
        }
    }

    private void handleFocusEvent(FocusEvent event) {
        if (event.getID() == FocusEvent.FOCUS_LOST) {
            this.pressedKeys.clear();
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        switch (event.getID()) {
            case KeyEvent.KEY_PRESSED:
                this.keyPressed(event.getKeyCode());
                break;
            case KeyEvent.KEY_RELEASED:
                this.keyReleased(event.getKeyCode());
                break;
            case KeyEvent.KEY_TYPED:
                this.keyTyped(event.getKeyChar());
                break;
        }
    }

    private void handleMouseEvent(MouseEvent event) {
        int button = event.getButton();
        switch (event.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                this.clicked = true;
                break;
            case MouseEvent.MOUSE_DRAGGED:
                break;
            case MouseEvent.MOUSE_ENTERED:
                this.pointerAvailable = true;
                break;
            case MouseEvent.MOUSE_EXITED:
                this.pointerAvailable = false;
                break;
            case MouseEvent.MOUSE_MOVED:
                this.pointerAvailable = true;
                break;
            case MouseEvent.MOUSE_PRESSED:
//                this.recentlyPressedButtons.add(MOUSE_BUTTON_MAP.get(button));
//                this.currentlyPressedButtons.add(MOUSE_BUTTON_MAP.get(button));
                break;
            case MouseEvent.MOUSE_RELEASED:
//                this.recentlyReleasedButtons.add(MOUSE_BUTTON_MAP.get(button));
//                this.currentlyPressedButtons.remove(MOUSE_BUTTON_MAP.get(button));
                break;
            case MouseEvent.MOUSE_WHEEL:
//                this.wheel = this.wheel + ((MouseWheelEvent) event).getWheelRotation();
                break;
        }

        this.pointerLocation = new Location(event.getX(), event.getY());
    }

    private void keyPressed(int keyCode) {
        Key key = KEY_MAP.get(keyCode);
        if (key != null) {
            this.typedKeys.add(key);
            this.pressedKeys.add(key);
        }
    }

    private void keyReleased(int keyCode) {
        Key key = KEY_MAP.get(keyCode);
        if (key != null) {
            this.pressedKeys.remove(key);
        }
    }

    private void keyTyped(char ch) {
        if (ch >= 32 && ch != 127) {
            this.typedChars = this.typedChars + ch;
        }
    }

    private static Map<Integer, Key> initKeyMap() {
        Map<Integer, Key> result = new HashMap();

        result.put(KeyEvent.VK_BACK_SPACE, Key.BACKSPACE);
        result.put(KeyEvent.VK_TAB, Key.TAB);
        result.put(KeyEvent.VK_ENTER, Key.ENTER);
        result.put(KeyEvent.VK_SHIFT, Key.SHIFT);
        result.put(KeyEvent.VK_CONTROL, Key.CONTROL);
        result.put(KeyEvent.VK_ALT, Key.ALT);
        result.put(KeyEvent.VK_PAUSE, Key.PAUSE);
        result.put(KeyEvent.VK_CAPS_LOCK, Key.CAPS_LOCK);
        result.put(KeyEvent.VK_ESCAPE, Key.ESCAPE);
        result.put(KeyEvent.VK_SPACE, Key.SPACE);
        result.put(KeyEvent.VK_PAGE_UP, Key.PAGE_UP);
        result.put(KeyEvent.VK_PAGE_DOWN, Key.PAGE_DOWN);
        result.put(KeyEvent.VK_END, Key.END);
        result.put(KeyEvent.VK_HOME, Key.HOME);
        result.put(KeyEvent.VK_LEFT, Key.LEFT);
        result.put(KeyEvent.VK_UP, Key.UP);
        result.put(KeyEvent.VK_RIGHT, Key.RIGHT);
        result.put(KeyEvent.VK_DOWN, Key.DOWN);
        result.put(KeyEvent.VK_0, Key.D0);
        result.put(KeyEvent.VK_1, Key.D1);
        result.put(KeyEvent.VK_2, Key.D2);
        result.put(KeyEvent.VK_3, Key.D3);
        result.put(KeyEvent.VK_4, Key.D4);
        result.put(KeyEvent.VK_5, Key.D5);
        result.put(KeyEvent.VK_6, Key.D6);
        result.put(KeyEvent.VK_7, Key.D7);
        result.put(KeyEvent.VK_8, Key.D8);
        result.put(KeyEvent.VK_9, Key.D9);
        result.put(KeyEvent.VK_A, Key.A);
        result.put(KeyEvent.VK_B, Key.B);
        result.put(KeyEvent.VK_C, Key.C);
        result.put(KeyEvent.VK_D, Key.D);
        result.put(KeyEvent.VK_E, Key.E);
        result.put(KeyEvent.VK_F, Key.F);
        result.put(KeyEvent.VK_G, Key.G);
        result.put(KeyEvent.VK_H, Key.H);
        result.put(KeyEvent.VK_I, Key.I);
        result.put(KeyEvent.VK_J, Key.J);
        result.put(KeyEvent.VK_K, Key.K);
        result.put(KeyEvent.VK_L, Key.L);
        result.put(KeyEvent.VK_M, Key.M);
        result.put(KeyEvent.VK_N, Key.N);
        result.put(KeyEvent.VK_O, Key.O);
        result.put(KeyEvent.VK_P, Key.P);
        result.put(KeyEvent.VK_Q, Key.Q);
        result.put(KeyEvent.VK_R, Key.R);
        result.put(KeyEvent.VK_S, Key.S);
        result.put(KeyEvent.VK_T, Key.T);
        result.put(KeyEvent.VK_U, Key.U);
        result.put(KeyEvent.VK_V, Key.V);
        result.put(KeyEvent.VK_W, Key.W);
        result.put(KeyEvent.VK_X, Key.X);
        result.put(KeyEvent.VK_Y, Key.Y);
        result.put(KeyEvent.VK_Z, Key.Z);
        result.put(KeyEvent.VK_NUMPAD0, Key.NUMPAD0);
        result.put(KeyEvent.VK_NUMPAD1, Key.NUMPAD1);
        result.put(KeyEvent.VK_NUMPAD2, Key.NUMPAD2);
        result.put(KeyEvent.VK_NUMPAD3, Key.NUMPAD3);
        result.put(KeyEvent.VK_NUMPAD4, Key.NUMPAD4);
        result.put(KeyEvent.VK_NUMPAD5, Key.NUMPAD5);
        result.put(KeyEvent.VK_NUMPAD6, Key.NUMPAD6);
        result.put(KeyEvent.VK_NUMPAD7, Key.NUMPAD7);
        result.put(KeyEvent.VK_NUMPAD8, Key.NUMPAD8);
        result.put(KeyEvent.VK_NUMPAD9, Key.NUMPAD9);
        result.put(KeyEvent.VK_F1, Key.F1);
        result.put(KeyEvent.VK_F2, Key.F2);
        result.put(KeyEvent.VK_F3, Key.F3);
        result.put(KeyEvent.VK_F4, Key.F4);
        result.put(KeyEvent.VK_F5, Key.F5);
        result.put(KeyEvent.VK_F6, Key.F6);
        result.put(KeyEvent.VK_F7, Key.F7);
        result.put(KeyEvent.VK_F8, Key.F8);
        result.put(KeyEvent.VK_F9, Key.F9);
        result.put(KeyEvent.VK_F10, Key.F10);
        result.put(KeyEvent.VK_F11, Key.F11);
        result.put(KeyEvent.VK_F12, Key.F12);
        result.put(KeyEvent.VK_DELETE, Key.DELETE);
        result.put(KeyEvent.VK_NUM_LOCK, Key.NUM_LOCK);
        result.put(KeyEvent.VK_SCROLL_LOCK, Key.SCROLL_LOCK);
        result.put(KeyEvent.VK_PRINTSCREEN, Key.PRINT_SCREEN);
        result.put(KeyEvent.VK_INSERT, Key.INSERT);
        result.put(KeyEvent.VK_QUOTE, Key.QUOTE);
        result.put(KeyEvent.VK_ALT_GRAPH, Key.ALT_GRAPH);
        return result;
    }
}
