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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents events that are taking place on this window. These are typically
 * keys pressed or typed by the user or motion events such as moving or clicking
 * with a mouse, trackball, pen or finger.
 *
 * <p>
 * <strong>Example:</strong>
 * <pre><code>Events events = window.getEvents();
 * if (events.isKeyPressed(Key.A)) {
 *     window.drawText(10, 10, "Taste A ist gedrückt.");
 * }</code></pre>
 *
 * @since 1
 */
public final class Events {

    final EventListenerImp listener;
    private final List<Pointer> newPointers;
    private final Map<Integer, Pointer> pointers;
    private final Set<Key> pressedKeys;
    private final List<Key> typedKeys;
    private Pointer mainPointer;
    private StringBuilder typedChars;

    /**
     * Returns the main pointer. The behaviour of this method depends on the
     * platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Returns
     * the pointer representing the mouse. Returns <tt>null</tt> if the mouse
     * pointer is outside the window.
     * <p>
     * <img src="../../../android.png"> Returns the pointer that first touched
     * the surface. Returns <tt>null</tt> if no pointer touches the surface.
     *
     * @return main pointer
     *
     * @since 1
     */
    public Pointer getMainPointer() {
        return this.mainPointer;
    }

    /**
     * Returns a list of all pointers. The behaviour of this method depends on
     * the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Returns a
     * list containing the pointer representing the mouse. Returns an empty list
     * if the mouse pointer is outside the window.
     * <p>
     * <img src="../../../android.png"> Returns a list of all pointers that are
     * touching the surface. Returns an empty list if no pointer touches the
     * surface.
     *
     * @return list of all pointers
     *
     * @since 1
     */
    public Iterable<Pointer> getPointers() {
        return this.pointers.values();
    }

    /**
     * Returns all pointers that have become available in the window since the
     * last call to {@link Window#update()}.
     *
     * @return all new pointers
     *
     * @since 1
     */
    public Iterable<Pointer> getNewPointers() {
        return this.newPointers;
    }

    /**
     * Returns a set of all pressed keys.
     *
     * @return set of all keys that are pressed.
     *
     * @since 1
     */
    public Set<Key> getPressedKeys() {
        return Collections.unmodifiableSet(this.pressedKeys);
    }

    /**
     * Returns a String representing the recently typed characters. When a
     * character is typed, it is appended to the String. The string is cleared
     * when {@link Window#update()} is called. Returns an empty String ("") when
     * no characters have been typed since the last call to
     * {@link Window#update()}.
     *
     * @return recently typed characters or ""
     *
     * @since 1
     */
    public String getTypedChars() {
        return this.typedChars.toString();
    }

    /**
     * Returns a list of all typed keys.
     *
     * @return recently typed keys
     *
     * @since 1
     */
    public List<Key> getTypedKeys() {
        return Collections.unmodifiableList(this.typedKeys);
    }

    /**
     * Checks if a key is pressed. Returns <tt>true</tt> if the specified key is
     * pressed.
     *
     * @param key the key to check for
     * @return <tt>true</tt> if specified key is currently pressed
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public boolean isKeyPressed(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.pressedKeys.contains(key);
    }

    /**
     * Checks if a key was typed. Returns <tt>true</tt> if the specified key has
     * been typed.
     *
     * @param key the key to check
     * @return <tt>true</tt> if specified key ha been typed
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public boolean isKeyTyped(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.typedKeys.contains(key);
    }

    Events() {
        this.listener = new EventListenerImp();
        this.newPointers = new ArrayList<Pointer>();
        this.pointers = new HashMap<Integer, Pointer>();
        this.pressedKeys = new HashSet<Key>();
        this.typedKeys = new ArrayList<Key>();

        this.typedChars = new StringBuilder();
    }

    void prepare() {
        this.typedChars = new StringBuilder();
        this.typedKeys.clear();
        this.newPointers.clear();
        Pointer pointer;
        for (Pointer p : this.pointers.values()) {
            p.prepare();
        }
    }

    void reset() {
        this.typedChars = new StringBuilder();
        this.typedKeys.clear();
        this.pressedKeys.clear();
        this.pointers.clear();
        this.newPointers.clear();
    }

    private class EventListenerImp implements KeyListener,
                                              KeyTypedListener,
                                              PointerListener,
                                              WindowFocusLostListener {

        @Override
        public void onKeyDown(final KeyEvent event) {
            typedKeys.add(event.getKey());
            pressedKeys.add(event.getKey());
        }

        @Override
        public void onKeyTyped(final KeyEvent event) {
            typedChars.append(event.getKeyChar());
        }

        @Override
        public void onKeyUp(final KeyEvent event) {
            pressedKeys.remove(event.getKey());
        }

        @Override
        public void onPointerDown(final PointerEvent event) {
            if (!pointers.containsKey(event.getPointerId())) {
                pointers.put(event.getPointerId(), new Pointer(event.getPointerId()));
            }

            Pointer pointer = pointers.get(event.getPointerId());
            newPointers.add(pointer);
            pointer.setLocation(event.getX(), event.getY());
            if (mainPointer == null) {
                mainPointer = pointer;
            }
        }

        @Override
        public void onPointerMoved(final PointerEvent event) {
            Pointer pointer = pointers.get(event.getPointerId());
            if (pointer != null) {
                pointer.setLocation(event.getX(), event.getY());
            }
        }

        @Override
        public void onPointerUp(final PointerEvent event) {
            Pointer pointer = pointers.get(event.getPointerId());
            if (pointer != null) {
                pointer.setUnavailable();
                pointers.remove(event.getPointerId());
            }

            newPointers.remove(pointer);
            if (mainPointer == pointer) {
                mainPointer = null;
            }
        }

        @Override
        public void onWindowFocusLost() {
            pressedKeys.clear();
        }
    }
}
