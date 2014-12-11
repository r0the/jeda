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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the an event queue. This class is thread-safe.
 *
 * @since 1.4
 */
public final class EventQueue {

    private static final String EVENT_ERROR = "jeda.event.error";
    private final List<ActionListener> actionListeners;
    private final List<ConnectionAcceptedListener> connectionAcceptedListeners;
    private final List<ConnectionClosedListener> connectionClosedListeners;
    private final Object eventLock;
    private final List<EventQueue> eventQueues;
    private final List<KeyDownListener> keyDownListeners;
    private final List<KeyTypedListener> keyTypedListeners;
    private final List<KeyUpListener> keyUpListeners;
    private final Object listenerLock;
    private final Set<Object> listeners;
    private final List<MessageReceivedListener> messageReceivedListeners;
    private final List<Object> pendingDeletions;
    private final List<Object> pendingInsertions;
    private final List<PointerDownListener> pointerDownListeners;
    private final List<PointerMovedListener> pointerMovedListeners;
    private final List<PointerUpListener> pointerUpListeners;
    private final List<SensorListener> sensorListeners;
    private final List<TickListener> tickListeners;
    private final List<TurnListener> turnListeners;
    private List<Event> eventsIn;
    private List<Event> eventsOut;

    /**
     * Constructs a new event queue.
     *
     * @since 1.4
     */
    public EventQueue() {
        this.actionListeners = new ArrayList<ActionListener>();
        this.connectionAcceptedListeners = new ArrayList<ConnectionAcceptedListener>();
        this.connectionClosedListeners = new ArrayList<ConnectionClosedListener>();
        this.eventLock = new Object();
        this.eventQueues = new ArrayList<EventQueue>();
        this.keyDownListeners = new ArrayList<KeyDownListener>();
        this.keyTypedListeners = new ArrayList<KeyTypedListener>();
        this.keyUpListeners = new ArrayList<KeyUpListener>();
        this.listenerLock = new Object();
        this.listeners = new HashSet<Object>();
        this.messageReceivedListeners = new ArrayList<MessageReceivedListener>();
        this.pendingDeletions = new ArrayList<Object>();
        this.pendingInsertions = new ArrayList<Object>();
        this.pointerDownListeners = new ArrayList<PointerDownListener>();
        this.pointerMovedListeners = new ArrayList<PointerMovedListener>();
        this.pointerUpListeners = new ArrayList<PointerUpListener>();
        this.sensorListeners = new ArrayList<SensorListener>();
        this.tickListeners = new ArrayList<TickListener>();
        this.turnListeners = new ArrayList<TurnListener>();
        this.eventsIn = new ArrayList<Event>();
        this.eventsOut = new ArrayList<Event>();
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
            synchronized (this.eventLock) {
                this.eventsIn.add(event);
            }
        }
    }

    /**
     * Adds a collection of events to the event queue. Has no effect if <tt>events</tt> is <tt>null</tt>.
     *
     * @param events the collection of events to add
     *
     * @since 1.4
     */
    public void addEvents(final Collection<Event> events) {
        if (events != null) {
            synchronized (this.eventLock) {
                this.eventsIn.addAll(events);
            }
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
            synchronized (this.listenerLock) {
                if (this.listeners.contains(listener)) {
                    this.pendingDeletions.remove(listener);
                }
                else if (!this.pendingInsertions.contains(listener)) {
                    this.pendingInsertions.add(listener);
                }
            }
        }
    }

    /**
     * Sends all events in the queue to the appropriate registered listeners. Remoaves all events from the queue.
     *
     * @since 1.4
     */
    public void processEvents() {
        // Switch input and output list
        synchronized (this.eventLock) {
            final List<Event> temp = this.eventsOut;
            this.eventsOut = this.eventsIn;
            this.eventsIn = temp;
            this.eventsIn.clear();
        }

        // Distribute events to child event queues.
        for (int i = 0; i < this.eventQueues.size(); ++i) {
            this.eventQueues.get(i).addEvents(this.eventsOut);
        }

        // Dispatch events
        for (int i = 0; i < this.eventsOut.size(); ++i) {
            this.dispatchEvent(this.eventsOut.get(i));
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
            synchronized (this.listenerLock) {
                if (!this.listeners.contains(listener)) {
                    this.pendingInsertions.remove(listener);
                }
                else if (!this.pendingDeletions.contains(listener)) {
                    this.pendingDeletions.add(listener);
                }
            }
        }
    }

    private void dispatchEvent(final Event event) {
        // Pending listener operations must be processed before every event dispatch. Otherwise, an event might be
        // delivered to a listener that has been removed during the last event dispatch.
        this.processPendingListeners();
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
            case CONNECTION_ACCEPTED:
                for (int j = 0; j < this.connectionAcceptedListeners.size(); ++j) {
                    try {
                        this.connectionAcceptedListeners.get(j).onConnectionAccepted((ConnectionEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case CONNECTION_CLOSED:
                for (int j = 0; j < this.connectionClosedListeners.size(); ++j) {
                    try {
                        this.connectionClosedListeners.get(j).onConnectionClosed((ConnectionEvent) event);
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
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case KEY_TYPED:
                for (int j = 0; j < this.keyTypedListeners.size(); ++j) {
                    try {
                        this.keyTypedListeners.get(j).onKeyTyped((KeyEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case KEY_UP:
                for (int j = 0; j < this.keyUpListeners.size(); ++j) {
                    try {
                        this.keyUpListeners.get(j).onKeyUp((KeyEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case MESSAGE_RECEIVED:
                for (int j = 0; j < this.messageReceivedListeners.size(); ++j) {
                    try {
                        this.messageReceivedListeners.get(j).onMessageReceived((MessageEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case POINTER_DOWN:
                for (int j = 0; j < this.pointerDownListeners.size(); ++j) {
                    try {
                        this.pointerDownListeners.get(j).onPointerDown((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case POINTER_MOVED:
                for (int j = 0; j < this.pointerMovedListeners.size(); ++j) {
                    try {
                        this.pointerMovedListeners.get(j).onPointerMoved((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
                    }
                }

                break;
            case POINTER_UP:
                for (int j = 0; j < this.pointerUpListeners.size(); ++j) {
                    try {
                        this.pointerUpListeners.get(j).onPointerUp((PointerEvent) event);
                    }
                    catch (final Throwable ex) {
                        Log.err(ex, EVENT_ERROR);
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
                        Log.err(ex, EVENT_ERROR);
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

        if (listener instanceof ConnectionAcceptedListener) {
            this.connectionAcceptedListeners.add((ConnectionAcceptedListener) listener);
        }

        if (listener instanceof ConnectionClosedListener) {
            this.connectionClosedListeners.add((ConnectionClosedListener) listener);
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

        if (listener instanceof MessageReceivedListener) {
            this.messageReceivedListeners.add((MessageReceivedListener) listener);
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

        if (listener instanceof ConnectionAcceptedListener) {
            this.connectionAcceptedListeners.remove((ConnectionAcceptedListener) listener);
        }

        if (listener instanceof ConnectionClosedListener) {
            this.connectionClosedListeners.remove((ConnectionClosedListener) listener);
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

        if (listener instanceof MessageReceivedListener) {
            this.messageReceivedListeners.remove((MessageReceivedListener) listener);
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

        if (listener instanceof SensorListener) {
            this.sensorListeners.remove((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.remove((TickListener) listener);
        }

        if (listener instanceof TurnListener) {
            this.turnListeners.remove((TurnListener) listener);
        }
    }

    private void processPendingListeners() {
        synchronized (this.listenerLock) {
            for (int i = 0; i < this.pendingDeletions.size(); ++i) {
                this.doRemoveListener(this.pendingDeletions.get(i));
            }

            for (int i = 0; i < this.pendingInsertions.size(); ++i) {
                this.doAddListener(this.pendingInsertions.get(i));
            }

            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
        }
    }
}
