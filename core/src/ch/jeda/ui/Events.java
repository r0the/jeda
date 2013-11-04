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

import ch.jeda.event.KeyTypedListener;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.Key;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.KeyListener;
import ch.jeda.event.PointerListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated Use event system instead.
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
     * @deprecated Use event system instead.
     */
    public Pointer getMainPointer() {
        return this.mainPointer;
    }

    /**
     * @deprecated Use event system instead.
     */
    public Iterable<Pointer> getPointers() {
        return this.pointers.values();
    }

    /**
     * @deprecated Use event system instead.
     */
    public Iterable<Pointer> getNewPointers() {
        return this.newPointers;
    }

    /**
     * @deprecated Use event system instead.
     */
    public Set<Key> getPressedKeys() {
        return Collections.unmodifiableSet(this.pressedKeys);
    }

    /**
     * @deprecated Use event system instead.
     */
    public String getTypedChars() {
        return this.typedChars.toString();
    }

    /**
     * @deprecated Use event system instead.
     */
    public List<Key> getTypedKeys() {
        return Collections.unmodifiableList(this.typedKeys);
    }

    /**
     * @deprecated Use event system instead.
     */
    public boolean isKeyPressed(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.pressedKeys.contains(key);
    }

    /**
     * @deprecated Use event system instead.
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
            pointer.setLocation((int) event.getX(), (int) event.getY());
            if (mainPointer == null) {
                mainPointer = pointer;
            }
        }

        @Override
        public void onPointerMoved(final PointerEvent event) {
            Pointer pointer = pointers.get(event.getPointerId());
            if (pointer != null) {
                pointer.setLocation((int) event.getX(), (int) event.getY());
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
