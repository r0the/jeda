/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
import ch.jeda.event.EventQueue;
import ch.jeda.event.EventType;
import ch.jeda.event.Key;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.ScrollEvent;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.ViewFeature;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import javax.swing.SwingUtilities;

class JavaViewImp implements ViewImp, FocusListener, KeyListener,
                             MouseListener, MouseMotionListener, MouseWheelListener {

    private static final EventSource KEYBOARD = new EventSource("Keyboard");
    private static final boolean LINUX = System.getProperty("os.name").endsWith("Linux");
    private static final EventSource MOUSE = new EventSource("Mouse");
    private static final int POINTER_ID = 0;
    private final BitmapCanvas bitmapCanvas;
    private final EnumSet<ViewFeature> features;
    protected final BaseWindow window;
    // BEGIN workaround to Java bug on Linux platform
    private final Map<Key, KeyReleaseTimer> keyReleaseTimer;
    // END workaround to Java bug on Linux platform
    private final Map<Key, Integer> keyRepeatCount;
    private EventQueue eventQueue;

    JavaViewImp(final BaseWindow window, final int width, final int height,
                final EnumSet<ViewFeature> features) {
        this.features = features;
        keyRepeatCount = new EnumMap<Key, Integer>(Key.class);
        keyReleaseTimer = new EnumMap<Key, KeyReleaseTimer>(Key.class);

        bitmapCanvas = new BitmapCanvas(width, height);
        this.window = window;
        window.getContentPane().add(bitmapCanvas);
        window.setResizable(false);
        window.setIgnoreRepaint(true);
        window.setUndecorated(features.contains(ViewFeature.FULLSCREEN));

        window.getContentPane().addKeyListener(this);
        window.addKeyListener(this);

        bitmapCanvas.setFocusable(true);
        bitmapCanvas.requestFocus();
        bitmapCanvas.addFocusListener(this);
        bitmapCanvas.addKeyListener(this);
        bitmapCanvas.addMouseListener(this);
        bitmapCanvas.addMouseMotionListener(this);
        bitmapCanvas.addMouseWheelListener(this);

        window.pack();
        window.init();
        window.setVisible(true);
    }

    @Override
    public void close() {
        window.dispose();
    }

    @Override
    public CanvasImp getCanvas() {
        return bitmapCanvas.getCanvasImp();
    }

    @Override
    public EnumSet<ViewFeature> getFeatures() {
        return features;
    }

    @Override
    public boolean isVisible() {
        return window.isVisible();
    }

    @Override
    public void setEventQueue(final EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public void setFeature(final ViewFeature feature, final boolean enabled) {
        if (enabled) {
            features.add(feature);
        }
        else {
            features.remove(feature);
        }
    }

    @Override
    public void setMouseCursor(final MouseCursor mouseCursor) {
        assert mouseCursor != null;

        window.setCursor(Mapper.mapCursor(mouseCursor));
    }

    @Override
    public void setTitle(final String title) {
        window.setTitle(title);
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bitmapCanvas.repaint();
            }
        });
    }

    @Override
    public void focusGained(final FocusEvent event) {
    }

    @Override
    public void focusLost(final FocusEvent event) {
    }

    @Override
    public void keyPressed(final java.awt.event.KeyEvent event) {
        final Key key = Mapper.mapKey(event);
        if (key != null) {
            int count = 0;
            if (keyRepeatCount.containsKey(key)) {
                count = keyRepeatCount.get(key);
            }

            // BEGIN workaround to Java bug on Linux platform
            if (LINUX) {
                if (keyReleaseTimer.containsKey(key)) {
                    keyReleaseTimer.get(key).cancel();
                }
            }
            // END workaround to Java bug on Linux platform

            postEvent(new KeyEvent(KEYBOARD, EventType.KEY_DOWN, key, count));
            keyRepeatCount.put(key, count + 1);
        }
    }

    @Override
    public void keyReleased(final java.awt.event.KeyEvent event) {
        final Key key = Mapper.mapKey(event);
        if (key != null) {

            // BEGIN workaround to Java bug on Linux platform
            if (LINUX) {
                if (!keyReleaseTimer.containsKey(key)) {
                    keyReleaseTimer.put(key, new KeyReleaseTimer(key, this));
                }

                keyReleaseTimer.get(key).start();
            }
            // END workaround to Java bug on Linux platform
            else {
                keyReleased(key);
            }
        }
    }

    @Override
    public void keyTyped(final java.awt.event.KeyEvent event) {
        final char ch = event.getKeyChar();
        switch (ch) {
            case '\b':
                postEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, Key.BACKSPACE));
                break;
            case '\t':
                postEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, Key.TAB));
                break;
            case '\n':
                postEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, Key.ENTER));
                break;
            case '\r':
                postEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, Key.ENTER));
                break;
            default:
                if (!Character.isISOControl(ch)) {
                    postEvent(new KeyEvent(KEYBOARD, EventType.KEY_TYPED, ch));
                }

                break;
        }
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        postEvent(new PointerEvent(MOUSE, EventType.POINTER_MOVED, POINTER_ID, event.getX(), event.getY()));
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        if (features.contains(ViewFeature.HOVERING_POINTER)) {
            postEvent(new PointerEvent(MOUSE, EventType.POINTER_DOWN, POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        if (features.contains(ViewFeature.HOVERING_POINTER)) {
            postEvent(new PointerEvent(MOUSE, EventType.POINTER_UP, POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        if (features.contains(ViewFeature.HOVERING_POINTER)) {
            postEvent(new PointerEvent(MOUSE, EventType.POINTER_MOVED, POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        final Key key = Mapper.mapButton(event.getButton());
        if (key != null) {
            postEvent(new KeyEvent(MOUSE, EventType.KEY_DOWN, key));
        }

        if (!features.contains(ViewFeature.HOVERING_POINTER)) {
            postEvent(new PointerEvent(MOUSE, EventType.POINTER_DOWN, POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        final Key key = Mapper.mapButton(event.getButton());
        if (key != null) {
            postEvent(new KeyEvent(MOUSE, EventType.KEY_UP, key));
        }

        if (!features.contains(ViewFeature.HOVERING_POINTER)) {
            postEvent(new PointerEvent(MOUSE, EventType.POINTER_UP, POINTER_ID, event.getX(), event.getY()));
        }
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent event) {
        postEvent(new ScrollEvent(MOUSE, 0.0, event.getWheelRotation()));
    }

    void keyReleased(final Key key) {
        postEvent(new KeyEvent(KEYBOARD, EventType.KEY_UP, key));
        keyRepeatCount.remove(key);
    }

    private void postEvent(final Event event) {
        if (eventQueue != null) {
            eventQueue.addEvent(event);
        }
    }

    private static class EventSource {

        private final String name;

        public EventSource(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
