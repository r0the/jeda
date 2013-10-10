/*
 * Copyright (C) 2013 by Stefan Rothe
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EventDispatcher {

    private final List<KeyDownListener> keyDownListeners;
    private final List<KeyTypedListener> keyTypedListeners;
    private final List<KeyUpListener> keyUpListeners;
    private final Set<Object> listeners;
    private final List<Object> pendingDeletions;
    private final List<Object> pendingInsertions;
    private final List<PointerDownListener> pointerDownListeners;
    private final List<PointerMovedListener> pointerMovedListeners;
    private final List<PointerUpListener> pointerUpListeners;
    private final List<TickListener> tickListener;
    private final List<WindowFocusLostListener> windowFocusLostListeners;

    EventDispatcher() {
        this.keyDownListeners = new ArrayList<KeyDownListener>();
        this.keyTypedListeners = new ArrayList<KeyTypedListener>();
        this.keyUpListeners = new ArrayList<KeyUpListener>();
        this.listeners = new HashSet<Object>();
        this.pendingDeletions = new ArrayList<Object>();
        this.pendingInsertions = new ArrayList<Object>();
        this.pointerDownListeners = new ArrayList<PointerDownListener>();
        this.pointerMovedListeners = new ArrayList<PointerMovedListener>();
        this.pointerUpListeners = new ArrayList<PointerUpListener>();
        this.tickListener = new ArrayList<TickListener>();
        this.windowFocusLostListeners = new ArrayList<WindowFocusLostListener>();
    }

    final void addListener(final Object listener) {
        if (listener != null) {
            if (this.listeners.contains(listener)) {
                this.pendingDeletions.remove(listener);
            }
            else if (!this.pendingInsertions.contains(listener)) {
                this.pendingInsertions.add(listener);
            }
        }
    }

    final void dispatchEvents(final Event[] events) {
        for (int i = 0; i < this.pendingDeletions.size(); ++i) {
            this.doRemoveListener(this.pendingDeletions.get(i));
        }

        for (int i = 0; i < this.pendingInsertions.size(); ++i) {
            this.doAddListener(this.pendingInsertions.get(i));
        }

        this.pendingDeletions.clear();
        this.pendingInsertions.clear();
        for (int i = 0; i < events.length; ++i) {
            final Event event = events[i];
            switch (event.getType()) {
                case KEY_DOWN:
                    for (int j = 0; j < this.keyDownListeners.size(); ++j) {
                        this.keyDownListeners.get(j).onKeyDown((KeyEvent) event);
                    }

                    break;
                case KEY_TYPED:
                    for (int j = 0; j < this.keyTypedListeners.size(); ++j) {
                        this.keyTypedListeners.get(j).onKeyTyped((KeyEvent) event);
                    }

                    break;
                case KEY_UP:
                    for (int j = 0; j < this.keyUpListeners.size(); ++j) {
                        this.keyUpListeners.get(j).onKeyUp((KeyEvent) event);
                    }

                    break;
                case POINTER_DOWN:
                    for (int j = 0; j < this.pointerDownListeners.size(); ++j) {
                        this.pointerDownListeners.get(j).onPointerDown((PointerEvent) event);
                    }

                    break;
                case POINTER_MOVED:
                    for (int j = 0; j < this.pointerMovedListeners.size(); ++j) {
                        this.pointerMovedListeners.get(j).onPointerMoved((PointerEvent) event);
                    }

                    break;
                case POINTER_UP:
                    for (int j = 0; j < this.pointerUpListeners.size(); ++j) {
                        this.pointerUpListeners.get(j).onPointerUp((PointerEvent) event);
                    }

                    break;
                case WINDOW_FOCUS_LOST:
                    for (int j = 0; j < this.windowFocusLostListeners.size(); ++j) {
                        this.windowFocusLostListeners.get(j).onWindowFocusLost();
                    }

                    break;
            }
        }
    }

    final void removeListener(final Object listener) {
        if (listener != null) {
            if (!this.listeners.contains(listener)) {
                this.pendingInsertions.remove(listener);
            }
            else if (!this.pendingDeletions.contains(listener)) {
                this.pendingDeletions.add(listener);
            }
        }
    }

    void dispatchTick(final TickEvent event) {
        for (int j = 0; j < this.tickListener.size(); ++j) {
            this.tickListener.get(j).onTick((TickEvent) event);
        }
    }

    private void doAddListener(final Object listener) {
        this.listeners.add(listener);
        if (listener instanceof KeyDownListener) {
            this.keyDownListeners.add((KeyDownListener) listener);
        }

        if (listener instanceof KeyTypedListener) {
            this.keyTypedListeners.add((KeyTypedListener) listener);
        }

        if (listener instanceof KeyUpListener) {
            this.keyUpListeners.add((KeyUpListener) listener);
        }

        if (listener instanceof PointerDownListener) {
            this.pointerDownListeners.add((PointerDownListener) listener);
        }

        if (listener instanceof PointerMovedListener) {
            this.pointerMovedListeners.add((PointerMovedListener) listener);
        }

        if (listener instanceof PointerUpListener) {
            this.pointerUpListeners.add((PointerUpListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListener.add((TickListener) listener);
        }

        if (listener instanceof WindowFocusLostListener) {
            this.windowFocusLostListeners.add((WindowFocusLostListener) listener);
        }
    }

    private void doRemoveListener(final Object listener) {
        this.listeners.remove(listener);
        if (listener instanceof KeyDownListener) {
            this.keyDownListeners.remove((KeyDownListener) listener);
        }

        if (listener instanceof KeyTypedListener) {
            this.keyTypedListeners.remove((KeyTypedListener) listener);
        }

        if (listener instanceof KeyUpListener) {
            this.keyUpListeners.remove((KeyUpListener) listener);
        }

        if (listener instanceof PointerDownListener) {
            this.pointerDownListeners.remove((PointerDownListener) listener);
        }

        if (listener instanceof PointerMovedListener) {
            this.pointerMovedListeners.remove((PointerMovedListener) listener);
        }

        if (listener instanceof PointerUpListener) {
            this.pointerUpListeners.remove((PointerUpListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListener.remove((TickListener) listener);
        }

        if (listener instanceof WindowFocusLostListener) {
            this.windowFocusLostListeners.remove((WindowFocusLostListener) listener);
        }
    }
}
