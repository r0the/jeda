/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

import ch.jeda.Size;
import ch.jeda.platform.Event;
import ch.jeda.ui.Key;
import ch.jeda.ui.Window;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasWindow extends BaseWindow implements FocusListener,
                                                        KeyListener,
                                                        MouseListener,
                                                        MouseMotionListener,
                                                        MouseWheelListener {

    private static final int POINTER_ID = 0;
    private static final Map<Integer, Key> BUTTON_MAP = initButtonMap();
    private static final Map<Integer, Map<Integer, Key>> KEY_MAP = initKeyMap();
    private final ImageCanvas canvas;
    private final EnumSet<Window.Feature> features;
    private final Size size;
    private List<Event> eventsIn;
    private List<Event> eventsOut;

    @Override
    public void focusGained(FocusEvent event) {
        this.eventsIn.add(Event.createWindowFocusGained());
    }

    @Override
    public void focusLost(FocusEvent event) {
        this.eventsIn.add(Event.createWindowFocusLost());
    }

    @Override
    public void keyPressed(KeyEvent event) {
        final Key key = mapKey(event);
        if (key != null) {
            this.eventsIn.add(Event.createKeyPressed(key));
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        final Key key = mapKey(event);
        if (key != null) {
            this.eventsIn.add(Event.createKeyReleased(key));
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        final char ch = event.getKeyChar();
        if (ch >= 32 && ch != 127) {
            this.eventsIn.add(Event.createKeyTyped(ch));
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        //this.add(event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.eventsIn.add(Event.createPointerMoved(
                POINTER_ID, event.getX(), event.getY()));
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        if (this.features.contains(Window.Feature.HoveringPointer)) {
            this.eventsIn.add(Event.createPointerAvailable(
                    POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseExited(MouseEvent event) {
        if (this.features.contains(Window.Feature.HoveringPointer)) {
            this.eventsIn.add(Event.createPointerUnavailable(POINTER_ID));
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (this.features.contains(Window.Feature.HoveringPointer)) {
            this.eventsIn.add(Event.createPointerMoved(
                    POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        final Key key = BUTTON_MAP.get(event.getButton());
        if (key != null) {
            this.eventsIn.add(Event.createKeyPressed(key));
        }

        if (!this.features.contains(Window.Feature.HoveringPointer)) {
            this.eventsIn.add(Event.createPointerAvailable(
                    POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        final Key key = BUTTON_MAP.get(event.getButton());
        if (key != null) {
            this.eventsIn.add(Event.createKeyReleased(key));
        }

        if (!this.features.contains(Window.Feature.HoveringPointer)) {
            this.eventsIn.add(Event.createPointerUnavailable(POINTER_ID));
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
//        int amount = event.getWheelRotation();
//        this.add(event);
    }

    CanvasWindow(WindowManager manager, Size size,
                 EnumSet<Window.Feature> features) {
        super(manager);
        this.canvas = new ImageCanvas(size);
        this.features = features;
        this.size = size;

        this.eventsIn = new ArrayList();
        this.eventsOut = new ArrayList();

        this.setResizable(false);
        this.setIgnoreRepaint(true);
        this.getContentPane().add(this.canvas);
        this.setUndecorated(features.contains(Window.Feature.Fullscreen));
        this.pack();
        this.init();
        this.canvas.requestFocus();

        this.canvas.addFocusListener(this);
        this.canvas.addKeyListener(this);
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);
        this.canvas.addMouseWheelListener(this);
    }

    Iterable<Event> fetchEvents() {
        final List<Event> temp = this.eventsIn;
        this.eventsIn = this.eventsOut;
        this.eventsOut = temp;
        this.eventsIn.clear();
        return this.eventsOut;
    }

    EnumSet<Window.Feature> getFeatures() {
        return this.features;
    }

    Size getImageSize() {
        return this.size;
    }

    void setFeature(Window.Feature feature, boolean enabled) {
        if (enabled) {
            this.features.add(feature);
        }
        else {
            this.features.remove(feature);
        }
    }

    void setImage(BufferedImage image) {
        this.canvas.setImage(image);
    }

    void update() {
        this.canvas.repaint();
    }

    private static class ImageCanvas extends java.awt.Canvas {

        private BufferedImage buffer;

        ImageCanvas(Size size) {
            final Dimension d = new Dimension(size.width, size.height);
            this.setPreferredSize(d);
            this.setSize(d);
        }

        void setImage(BufferedImage buffer) {
            this.buffer = buffer;
        }

        @Override
        public void paint(Graphics graphics) {
            if (graphics != null) {
                graphics.drawImage(this.buffer, 0, 0, null);
            }
        }

        @Override
        public void repaint() {
            this.paint(this.getGraphics());
        }
    }

    private static Map<Integer, Key> initButtonMap() {
        final Map<Integer, Key> result = new HashMap();
        result.put(MouseEvent.BUTTON1, Key.MOUSE_PRIMARY);
        result.put(MouseEvent.BUTTON2, Key.MOUSE_MIDDLE);
        result.put(MouseEvent.BUTTON3, Key.MOUSE_SECONDARY);
        return result;
    }

    private static Key mapKey(KeyEvent event) {
        final int keyLocation = event.getKeyLocation();
        if (KEY_MAP.containsKey(keyLocation)) {
            return KEY_MAP.get(keyLocation).get(event.getExtendedKeyCode());
        }
        else {
            return null;
        }
    }

    private static Map<Integer, Map<Integer, Key>> initKeyMap() {
        final Map<Integer, Map<Integer, Key>> result = new HashMap();
        final Map<Integer, Key> standard = new HashMap();
        final Map<Integer, Key> left = new HashMap();
        final Map<Integer, Key> numpad = new HashMap();
        final Map<Integer, Key> right = new HashMap();
        final Map<Integer, Key> unknown = new HashMap();

        standard.put(KeyEvent.VK_A, Key.A);
        left.put(KeyEvent.VK_ALT, Key.ALT_LEFT);
        right.put(KeyEvent.VK_ALT, Key.ALT_RIGHT);
        standard.put(KeyEvent.VK_QUOTE, Key.APOSTROPHE);
        standard.put(KeyEvent.VK_B, Key.B);
        standard.put(KeyEvent.VK_BACK_QUOTE, Key.GRAVE);
        standard.put(KeyEvent.VK_BACK_SPACE, Key.BACKSPACE);
        standard.put(KeyEvent.VK_C, Key.C);
        standard.put(KeyEvent.VK_CAPS_LOCK, Key.CAPS_LOCK);
        left.put(KeyEvent.VK_CONTROL, Key.CTRL_LEFT);
        right.put(KeyEvent.VK_CONTROL, Key.CTRL_RIGHT);
        standard.put(KeyEvent.VK_D, Key.D);
        standard.put(KeyEvent.VK_DELETE, Key.DELETE);
        standard.put(KeyEvent.VK_0, Key.DIGIT_0);
        standard.put(KeyEvent.VK_1, Key.DIGIT_1);
        standard.put(KeyEvent.VK_2, Key.DIGIT_2);
        standard.put(KeyEvent.VK_3, Key.DIGIT_3);
        standard.put(KeyEvent.VK_4, Key.DIGIT_4);
        standard.put(KeyEvent.VK_5, Key.DIGIT_5);
        standard.put(KeyEvent.VK_6, Key.DIGIT_6);
        standard.put(KeyEvent.VK_7, Key.DIGIT_7);
        standard.put(KeyEvent.VK_8, Key.DIGIT_8);
        standard.put(KeyEvent.VK_9, Key.DIGIT_9);
        standard.put(KeyEvent.VK_DOWN, Key.DOWN);
        standard.put(KeyEvent.VK_E, Key.E);
        standard.put(KeyEvent.VK_END, Key.END);
        standard.put(KeyEvent.VK_ENTER, Key.ENTER);
        standard.put(KeyEvent.VK_EQUALS, Key.EQUALS);
        standard.put(KeyEvent.VK_ESCAPE, Key.ESCAPE);
        standard.put(KeyEvent.VK_F, Key.F);
        standard.put(KeyEvent.VK_F1, Key.F1);
        standard.put(KeyEvent.VK_F2, Key.F2);
        standard.put(KeyEvent.VK_F3, Key.F3);
        standard.put(KeyEvent.VK_F4, Key.F4);
        standard.put(KeyEvent.VK_F5, Key.F5);
        standard.put(KeyEvent.VK_F6, Key.F6);
        standard.put(KeyEvent.VK_F7, Key.F7);
        standard.put(KeyEvent.VK_F8, Key.F8);
        standard.put(KeyEvent.VK_F9, Key.F9);
        standard.put(KeyEvent.VK_F10, Key.F10);
        standard.put(KeyEvent.VK_F11, Key.F11);
        standard.put(KeyEvent.VK_F12, Key.F12);
        standard.put(KeyEvent.VK_G, Key.G);
        standard.put(KeyEvent.VK_H, Key.H);
        standard.put(KeyEvent.VK_HOME, Key.HOME);
        standard.put(KeyEvent.VK_I, Key.I);
        standard.put(KeyEvent.VK_INSERT, Key.INSERT);
        standard.put(KeyEvent.VK_J, Key.J);
        standard.put(KeyEvent.VK_K, Key.K);
        standard.put(KeyEvent.VK_L, Key.L);
        standard.put(KeyEvent.VK_LEFT, Key.LEFT);
        standard.put(KeyEvent.VK_M, Key.M);
        standard.put(KeyEvent.VK_CONTEXT_MENU, Key.MENU);
        standard.put(KeyEvent.VK_MINUS, Key.MINUS);
        standard.put(KeyEvent.VK_N, Key.N);
        numpad.put(KeyEvent.VK_NUM_LOCK, Key.NUM_LOCK);
        numpad.put(KeyEvent.VK_NUMPAD0, Key.NUMPAD_0);
        numpad.put(KeyEvent.VK_NUMPAD1, Key.NUMPAD_1);
        numpad.put(KeyEvent.VK_NUMPAD2, Key.NUMPAD_2);
        numpad.put(KeyEvent.VK_NUMPAD3, Key.NUMPAD_3);
        numpad.put(KeyEvent.VK_NUMPAD4, Key.NUMPAD_4);
        numpad.put(KeyEvent.VK_NUMPAD5, Key.NUMPAD_5);
        numpad.put(KeyEvent.VK_NUMPAD6, Key.NUMPAD_6);
        numpad.put(KeyEvent.VK_NUMPAD7, Key.NUMPAD_7);
        numpad.put(KeyEvent.VK_NUMPAD8, Key.NUMPAD_8);
        numpad.put(KeyEvent.VK_NUMPAD9, Key.NUMPAD_9);
        numpad.put(KeyEvent.VK_ADD, Key.NUMPAD_ADD);
        numpad.put(KeyEvent.VK_DIVIDE, Key.NUMPAD_DIVIDE);
        numpad.put(KeyEvent.VK_DECIMAL, Key.NUMPAD_DOT);
        numpad.put(KeyEvent.VK_MULTIPLY, Key.NUMPAD_MULTIPLY);
        numpad.put(KeyEvent.VK_SUBTRACT, Key.NUMPAD_SUBTRACT);
        numpad.put(KeyEvent.VK_ENTER, Key.NUMPAD_ENTER);
        standard.put(KeyEvent.VK_O, Key.O);
        standard.put(KeyEvent.VK_P, Key.P);
        standard.put(KeyEvent.VK_PAGE_DOWN, Key.PAGE_DOWN);
        standard.put(KeyEvent.VK_PAGE_UP, Key.PAGE_UP);
        standard.put(KeyEvent.VK_PAUSE, Key.PAUSE);
        standard.put(KeyEvent.VK_PRINTSCREEN, Key.PRINT_SCREEN);
        standard.put(KeyEvent.VK_Q, Key.Q);
        standard.put(KeyEvent.VK_R, Key.R);
        standard.put(KeyEvent.VK_RIGHT, Key.RIGHT);
        standard.put(KeyEvent.VK_S, Key.S);
        standard.put(KeyEvent.VK_SCROLL_LOCK, Key.SCROLL_LOCK);
        left.put(KeyEvent.VK_SHIFT, Key.SHIFT_LEFT);
        right.put(KeyEvent.VK_SHIFT, Key.SHIFT_RIGHT);
        standard.put(KeyEvent.VK_SPACE, Key.SPACE);
        standard.put(KeyEvent.VK_T, Key.T);
        standard.put(KeyEvent.VK_TAB, Key.TAB);
        standard.put(KeyEvent.VK_U, Key.U);
        standard.put(KeyEvent.VK_UP, Key.UP);
        standard.put(KeyEvent.VK_V, Key.V);
        standard.put(KeyEvent.VK_W, Key.W);
//        standard.put(KeyEvent.VK_WINDOWS, Key.META);

        standard.put(KeyEvent.VK_X, Key.X);
        standard.put(KeyEvent.VK_Y, Key.Y);
        standard.put(KeyEvent.VK_Z, Key.Z);

//        result.put(KeyEvent.VK_ALT_GRAPH, Key.ALT_GRAPH);

        // When caps lock is engaged, the letter keys have location unknown.
        unknown.put(KeyEvent.VK_A, Key.A);
        unknown.put(KeyEvent.VK_B, Key.B);
        unknown.put(KeyEvent.VK_C, Key.C);
        unknown.put(KeyEvent.VK_D, Key.D);
        unknown.put(KeyEvent.VK_E, Key.E);
        unknown.put(KeyEvent.VK_F, Key.F);
        unknown.put(KeyEvent.VK_G, Key.G);
        unknown.put(KeyEvent.VK_H, Key.H);
        unknown.put(KeyEvent.VK_I, Key.I);
        unknown.put(KeyEvent.VK_J, Key.J);
        unknown.put(KeyEvent.VK_K, Key.K);
        unknown.put(KeyEvent.VK_L, Key.L);
        unknown.put(KeyEvent.VK_M, Key.M);
        unknown.put(KeyEvent.VK_N, Key.N);
        unknown.put(KeyEvent.VK_O, Key.O);
        unknown.put(KeyEvent.VK_P, Key.P);
        unknown.put(KeyEvent.VK_Q, Key.Q);
        unknown.put(KeyEvent.VK_R, Key.R);
        unknown.put(KeyEvent.VK_S, Key.S);
        unknown.put(KeyEvent.VK_T, Key.T);
        unknown.put(KeyEvent.VK_U, Key.U);
        unknown.put(KeyEvent.VK_V, Key.V);
        unknown.put(KeyEvent.VK_W, Key.W);
        unknown.put(KeyEvent.VK_X, Key.X);
        unknown.put(KeyEvent.VK_Y, Key.Y);
        unknown.put(KeyEvent.VK_Z, Key.Z);


        result.put(KeyEvent.KEY_LOCATION_LEFT, left);
        result.put(KeyEvent.KEY_LOCATION_NUMPAD, numpad);
        result.put(KeyEvent.KEY_LOCATION_RIGHT, right);
        result.put(KeyEvent.KEY_LOCATION_STANDARD, standard);
        result.put(KeyEvent.KEY_LOCATION_UNKNOWN, unknown);
        return result;
    }
}
