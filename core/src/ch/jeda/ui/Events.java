/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

import ch.jeda.platform.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents events that are taking place on this window. These
 * are typically keys pressed or typed by the user or motion events such as
 * moving or clicking with a mouse, trackball, pen or finger.
 * 
 * @since 1
 */
public final class Events {

    private final List<Pointer> newPointers;
    private final Map<Integer, Pointer> pointers;
    private final Set<Key> pressedKeys;
    private final List<Key> typedKeys;
    private StringBuilder typedChars;

    /**
     * Returns all pointers that are currently available in the window.
     * 
     * @return all pointers
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
     * Returns a set of all keys that are currently pressed.
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
     * no characters have been typed since the last call to {@link Window#update()}.
     *
     * @return recently typed characters or ""
     * 
     * @since 1
     */
    public String getTypedChars() {
        return this.typedChars.toString();
    }

    /**
     * Returns a set of all keys that have been typed recently.
     * 
     * @return recently typed keys
     * 
     * @since 1
     */
    public List<Key> getTypedKeys() {
        return Collections.unmodifiableList(this.typedKeys);
    }

    /**
     * Checks whether the specified key is currently pressed.
     *
     * @param key the key to check for
     * @return <code>true</code> if specified key is currently pressed
     * @throws NullPointerException when key is null
     * 
     * @since 1
     */
    public boolean isKeyPressed(Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.pressedKeys.contains(key);
    }

    /**
     * Checks whether a key was typed recently.
     *
     * @param key key to check
     * @return <code>true</code> if specified key was typed recently
     * @throws NullPointerException when key is null
     * 
     * @since 1
     */
    public boolean isKeyTyped(Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.typedKeys.contains(key);
    }

    Events() {
        this.newPointers = new ArrayList();
        this.pointers = new HashMap();
        this.pressedKeys = new HashSet();
        this.typedKeys = new ArrayList();

        this.typedChars = new StringBuilder();
    }

    void digestEvents(Iterable<Event> events) {
        this.typedChars = new StringBuilder();
        this.typedKeys.clear();
        this.newPointers.clear();
        Pointer pointer = null;
        for (Pointer p : this.pointers.values()) {
            p.prepare();
        }

        for (Event event : events) {
            switch (event.type) {
                case KeyPressed:
                    this.typedKeys.add(event.key);
                    this.pressedKeys.add(event.key);
                    break;
                case KeyReleased:
                    this.pressedKeys.remove(event.key);
                    break;
                case KeyTyped:
                    this.typedChars.append(event.keyChar);
                    break;
                case PointerAvailable:
                    if (!this.pointers.containsKey(event.pointerId)) {
                        this.pointers.put(event.pointerId, new Pointer(event.pointerId));
                    }

                    pointer = this.pointers.get(event.pointerId);
                    this.newPointers.add(pointer);
                    pointer.setLocation(event.location);
                    break;
                case PointerUnavailable:
                    pointer = this.pointers.get(event.pointerId);
                    if (pointer != null) {
                        pointer.setLocation(null);
                        this.pointers.remove(event.pointerId);
                    }

                    this.newPointers.remove(pointer);
                    break;
                case PointerMoved:
                    pointer = this.pointers.get(event.pointerId);
                    if (pointer != null) {
                        pointer.setLocation(event.location);
                    }

                    break;
                case WindowFocusLost:
                    this.pressedKeys.clear();
                    break;
            }
        }
    }

    void reset() {
        this.typedChars = new StringBuilder();
        this.typedKeys.clear();
        this.pressedKeys.clear();
        this.pointers.clear();
        this.newPointers.clear();
    }
}
