/*
 * Copyright (C) 2014 by Stefan Rothe
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
package ch.jeda.event;

import ch.jeda.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the an event queue.
 *
 * @since 1.4
 */
public final class EventQueue {

    private static final String EVENT_ERROR = "jeda.event.error";
    private final List<Event> events;
    private final List<ActionListener> actionListeners;
    private final List<EventQueue> eventQueues;
    private final List<KeyDownListener> keyDownListeners;
    private final List<KeyTypedListener> keyTypedListeners;
    private final List<KeyUpListener> keyUpListeners;
    private final Set<Object> listeners;
    private final List<Object> pendingDeletions;
    private final List<Object> pendingInsertions;
    private final List<PointerDownListener> pointerDownListeners;
    private final List<PointerMovedListener> pointerMovedListeners;
    private final List<PointerUpListener> pointerUpListeners;
    private final List<SensorListener> sensorListeners;
    private final List<TickListener> tickListeners;
    private final List<TurnListener> turnListeners;

    /**
     * Constructs a new event queue.
     *
     * @since 1.4
     */
    public EventQueue() {
        this.events = new ArrayList<Event>();
        this.actionListeners = new ArrayList<ActionListener>();
        this.eventQueues = new ArrayList<EventQueue>();
        this.keyDownListeners = new ArrayList<KeyDownListener>();
        this.keyTypedListeners = new ArrayList<KeyTypedListener>();
        this.keyUpListeners = new ArrayList<KeyUpListener>();
        this.listeners = new HashSet<Object>();
        this.pendingDeletions = new ArrayList<Object>();
        this.pendingInsertions = new ArrayList<Object>();
        this.pointerDownListeners = new ArrayList<PointerDownListener>();
        this.pointerMovedListeners = new ArrayList<PointerMovedListener>();
        this.pointerUpListeners = new ArrayList<PointerUpListener>();
        this.sensorListeners = new ArrayList<SensorListener>();
        this.tickListeners = new ArrayList<TickListener>();
        this.turnListeners = new ArrayList<TurnListener>();
    }

    /**
     * Adds an event to the event queue. Has no effect if <tt>event</tt> is <tt>null</tt>.
     *
     * @param event the event to add
     *
     * @since 1.4
     */
    public void addEvent(final Event event) {
        if (event != null) {
            this.events.add(event);
        }
    }

    /**
     * Adds an event listener to the event queue. Has no effect if <tt>listener</tt> is <tt>null</tt> or the listener
     * has already been added to the event queue.
     *
     * @param listener the listener to add
     *
     * @since 1.4
     */
    public void addListener(final Object listener) {
        if (listener != null) {
            if (this.listeners.contains(listener)) {
                this.pendingDeletions.remove(listener);
            }
            else if (!this.pendingInsertions.contains(listener)) {
                this.pendingInsertions.add(listener);
            }
        }
    }

    /**
     * Sends all events in the queue to the appropriate registered listeners. Remoaves all events from the queue.
     *
     * @since 1.4
     */
    public void processEvents() {
        for (int i = 0; i < this.pendingDeletions.size(); ++i) {
            this.doRemoveListener(this.pendingDeletions.get(i));
        }

        for (int i = 0; i < this.pendingInsertions.size(); ++i) {
            this.doAddListener(this.pendingInsertions.get(i));
        }

        this.pendingDeletions.clear();
        this.pendingInsertions.clear();
        // Distribute events to child event queues.
        for (int i = 0; i < this.eventQueues.size(); ++i) {
            this.eventQueues.get(i).events.addAll(this.events);
        }

        final Event[] eventArray = this.events.toArray(new Event[this.events.size()]);
        this.events.clear();
        for (int i = 0; i < eventArray.length; ++i) {
            this.dispatchEvent(eventArray[i]);
        }
    }

    /**
     * Removes an event listener from the event queue. Has no effect if <tt>listener</tt> is <tt>null</tt> or the
     * listener has not previously been added to the event queue.
     *
     * @param listener the listener to remove
     *
     * @since 1.4
     */
    public void removeListener(final Object listener) {
        if (listener != null) {
            if (!this.listeners.contains(listener)) {
                this.pendingInsertions.remove(listener);
            }
            else if (!this.pendingDeletions.contains(listener)) {
                this.pendingDeletions.add(listener);
            }
        }
    }

    private void dispatchEvent(final Event event) {
        switch (event.getType()) {
            case ACTION:
                for (int j = 0; j < this.actionListeners.size(); ++j) {
                    try {
                        this.actionListeners.get(j).onAction((ActionEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case KEY_DOWN:
                for (int j = 0; j < this.keyDownListeners.size(); ++j) {
                    try {
                        this.keyDownListeners.get(j).onKeyDown((KeyEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case KEY_TYPED:
                for (int j = 0; j < this.keyTypedListeners.size(); ++j) {
                    try {
                        this.keyTypedListeners.get(j).onKeyTyped((KeyEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case KEY_UP:
                for (int j = 0; j < this.keyUpListeners.size(); ++j) {
                    try {
                        this.keyUpListeners.get(j).onKeyUp((KeyEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case POINTER_DOWN:
                for (int j = 0; j < this.pointerDownListeners.size(); ++j) {
                    try {
                        this.pointerDownListeners.get(j).onPointerDown((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case POINTER_MOVED:
                for (int j = 0; j < this.pointerMovedListeners.size(); ++j) {
                    try {
                        this.pointerMovedListeners.get(j).onPointerMoved((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case POINTER_UP:
                for (int j = 0; j < this.pointerUpListeners.size(); ++j) {
                    try {
                        this.pointerUpListeners.get(j).onPointerUp((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
            case SENSOR:
                for (int j = 0; j < this.sensorListeners.size(); ++j) {
                    try {
                        this.sensorListeners.get(j).onSensorChanged((SensorEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case TICK:
                for (int j = 0; j < this.tickListeners.size(); ++j) {
                    try {
                        this.tickListeners.get(j).onTick((TickEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case TURN:
                for (int j = 0; j < this.turnListeners.size(); ++j) {
                    try {
                        this.turnListeners.get(j).onTurn((TurnEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, "jeda.event.error");
                    }
                }

                break;
        }
    }

    private void doAddListener(final Object listener) {
        this.listeners.add(listener);
        if (listener instanceof ActionListener) {
            this.actionListeners.add((ActionListener) listener);
        }

        if (listener instanceof EventQueue) {
            this.eventQueues.add((EventQueue) listener);
        }

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

        if (listener instanceof SensorListener) {
            this.sensorListeners.add((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.add((TickListener) listener);
        }

        if (listener instanceof TurnListener) {
            this.turnListeners.add((TurnListener) listener);
        }
    }

    private void doRemoveListener(final Object listener) {
        this.listeners.remove(listener);
        if (listener instanceof ActionListener) {
            this.actionListeners.remove((ActionListener) listener);
        }

        if (listener instanceof EventQueue) {
            this.eventQueues.remove((EventQueue) listener);
        }

        if (listener instanceof KeyDownListener) {
            this.keyDownListeners.remove((KeyDownListener) listener);
        }

        if (listener instanceof KeyTypedListener) {
            this.keyTypedListeners.remove((KeyTypedListener) listener);
        }

        if (listener instanceof KeyUpListener) {
            this.keyUpListeners.remove((KeyUpListener) listener);
        }

        if (listener instanceof SensorListener) {
            this.sensorListeners.remove((SensorListener) listener);
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
            this.tickListeners.remove((TickListener) listener);
        }

        if (listener instanceof TurnListener) {
            this.turnListeners.remove((TurnListener) listener);
        }
    }
}
