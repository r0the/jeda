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

import ch.jeda.event.Event;
import ch.jeda.event.EventType;
import ch.jeda.event.TurnAxis;
import ch.jeda.event.TurnEvent;
import ch.jeda.event.Key;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.PointerEvent;
import ch.jeda.ui.WindowFeature;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// On Linux, Java generates repeating keyReleased. This is a known bug, see
// http://bugs.sun.com/view_bug.do?bug_id=4153069
// This class implements a workaround.
class CanvasWindow extends BaseWindow implements FocusListener,
                                                 KeyListener,
                                                 MouseListener,
                                                 MouseMotionListener,
                                                 MouseWheelListener {

    private static final int KEY_RELEASE_TIMEOUT = 2;
    private static final EventSource KEYBOARD = new EventSource("Keyboard");
    private static final boolean LINUX = System.getProperty("os.name").endsWith("Linux");
    private static final EventSource MOUSE = new EventSource("Mouse");
    private static final EventSource WINDOW = new EventSource("Window");
    private static final int POINTER_ID = 0;
    private static final Map<Integer, Key> BUTTON_MAP = initButtonMap();
    private static final Map<Integer, Map<Integer, Key>> KEY_MAP = initKeyMap();
    private final ImageCanvas canvas;
    private final Collection<Event> events;
    private final EnumSet<WindowFeature> features;
    private final int height;
    // BEGIN workaround to Java bug on Linux platform
    private final Map<Key, KeyReleaseTimer> keyReleaseTimer;
    // END workaround to Java bug on Linux platform
    private final Map<Key, Integer> keyRepeatCount;
    private final int width;

    @Override
    public void focusGained(final FocusEvent event) {
    }

    @Override
    public void focusLost(final FocusEvent event) {
        this.addEvent(new Event(WINDOW, EventType.WINDOW_FOCUS_LOST));
    }

    @Override
    public void keyPressed(final java.awt.event.KeyEvent event) {
        final Key key = mapKey(event);
        if (key != null) {
            int count = 0;
            if (this.keyRepeatCount.containsKey(key)) {
                count = this.keyRepeatCount.get(key);
            }

            // BEGIN workaround to Java bug on Linux platform
            if (LINUX) {
                if (this.keyReleaseTimer.containsKey(key)) {
                    this.keyReleaseTimer.get(key).cancel();
                }
            }
            // END workaround to Java bug on Linux platform

            this.addEvent(new KeyEvent(KEYBOARD, EventType.KEY_DOWN, key, count));
            this.keyRepeatCount.put(key, count + 1);
        }
    }

    @Override
    public void keyReleased(final java.awt.event.KeyEvent event) {
        final Key key = mapKey(event);
        if (key != null) {

            // BEGIN workaround to Java bug on Linux platform
            if (LINUX) {
                if (!this.keyReleaseTimer.containsKey(key)) {
                    this.keyReleaseTimer.put(key, new KeyReleaseTimer(key, this));
                }

                this.keyReleaseTimer.get(key).start();
            }
            // END workaround to Java bug on Linux platform
            else {
                this.keyReleased(key);
            }
        }
    }

    @Override
    public void keyTyped(final java.awt.event.KeyEvent event) {
        final char ch = event.getKeyChar();
        if (ch >= 32 && ch != 127) {
            this.addEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, ch));
        }
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_MOVED,
                                       POINTER_ID, event.getX(), event.getY()));
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        if (this.features.contains(WindowFeature.HOVERING_POINTER)) {
            this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_DOWN,
                                           POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        if (this.features.contains(WindowFeature.HOVERING_POINTER)) {
            this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_UP, POINTER_ID,
                                           event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        if (this.features.contains(WindowFeature.HOVERING_POINTER)) {
            this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_MOVED, POINTER_ID,
                                           event.getX(), event.getY()));
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        final Key key = BUTTON_MAP.get(event.getButton());
        if (key != null) {
            this.addEvent(new KeyEvent(MOUSE, EventType.KEY_DOWN, key));
        }

        if (!this.features.contains(WindowFeature.HOVERING_POINTER)) {
            this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_DOWN, POINTER_ID,
                                           event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        final Key key = BUTTON_MAP.get(event.getButton());
        if (key != null) {
            this.addEvent(new KeyEvent(MOUSE, EventType.KEY_UP, key));
        }

        if (!this.features.contains(WindowFeature.HOVERING_POINTER)) {
            this.addEvent(new PointerEvent(MOUSE, EventType.POINTER_UP, POINTER_ID,
                                           event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent event) {
        this.addEvent(new TurnEvent(MOUSE, event.getWheelRotation(), TurnAxis.MOUSE_WHEEL));
    }

    CanvasWindow(final WindowManager manager, final int width, final int height,
                 final EnumSet<WindowFeature> features) {
        super(manager);
        this.canvas = new ImageCanvas(width, height);
        this.events = new ConcurrentLinkedQueue<Event>();
        this.features = features;
        this.height = height;
        this.keyRepeatCount = new EnumMap<Key, Integer>(Key.class);
        this.keyReleaseTimer = new EnumMap<Key, KeyReleaseTimer>(Key.class);
        this.width = width;
        this.setResizable(false);
        this.setIgnoreRepaint(true);
        this.getContentPane().add(this.canvas);
        this.setUndecorated(features.contains(WindowFeature.FULLSCREEN));
        this.pack();
        this.init();

        this.canvas.setFocusable(true);
        this.canvas.requestFocus();
        this.canvas.addFocusListener(this);
        this.canvas.addKeyListener(this);
        this.getContentPane().addKeyListener(this);
        this.addKeyListener(this);
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);
        this.canvas.addMouseWheelListener(this);
    }

    Event[] fetchEvents() {
        final Event[] result = this.events.toArray(new Event[0]);
        this.events.clear();
        return result;
    }

    EnumSet<WindowFeature> getFeatures() {
        return this.features;
    }

    int getImageHeight() {
        return this.height;
    }

    int getImageWidth() {
        return this.width;
    }

    void setFeature(final WindowFeature feature, final boolean enabled) {
        if (enabled) {
            this.features.add(feature);
        }
        else {
            this.features.remove(feature);
        }
    }

    void setImage(final BufferedImage image) {
        this.canvas.setImage(image);
    }

    void update() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                canvas.repaint();
            }
        });
    }

    private void addEvent(final Event event) {
        this.events.add(event);
    }

    private void keyReleased(final Key key) {
        this.addEvent(new KeyEvent(KEYBOARD, EventType.KEY_UP, key));
        this.keyRepeatCount.remove(key);
    }

    private static Map<Integer, Key> initButtonMap() {
        final Map<Integer, Key> result = new HashMap();
        result.put(MouseEvent.BUTTON1, Key.MOUSE_PRIMARY);
        result.put(MouseEvent.BUTTON2, Key.MOUSE_MIDDLE);
        result.put(MouseEvent.BUTTON3, Key.MOUSE_SECONDARY);
        return result;
    }

    private static Key mapKey(final java.awt.event.KeyEvent event) {
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

    private static class ImageCanvas extends java.awt.Canvas {

        private BufferedImage buffer;

        ImageCanvas(final int width, final int height) {
            final Dimension d = new Dimension(width, height);
            this.setPreferredSize(d);
            this.setSize(d);
        }

        void setImage(final BufferedImage buffer) {
            this.buffer = buffer;
        }

        @Override
        public void paint(final Graphics graphics) {
            if (graphics != null) {
                graphics.drawImage(this.buffer, 0, 0, null);
            }
        }

        @Override
        public void repaint() {
            this.paint(this.getGraphics());
        }
    }

    private static class KeyReleaseTimer implements ActionListener {

        private final Key key;
        private final Timer timer;
        private final CanvasWindow window;
        private boolean ok;

        public KeyReleaseTimer(final Key key, final CanvasWindow window) {
            this.key = key;
            this.timer = new Timer(KEY_RELEASE_TIMEOUT, this);
            this.window = window;
            this.ok = true;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            assert EventQueue.isDispatchThread();
            if (this.ok) {
                this.cancel();
                this.window.keyReleased(key);
            }
        }

        void start() {
            this.ok = true;
            this.timer.start();
        }

        void cancel() {
            this.ok = false;
            this.timer.stop();
        }
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
