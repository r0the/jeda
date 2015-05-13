/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
import ch.jeda.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the an event queue. This class is thread-safe.
 *
 * @since 1.4
 * @version 2
 */
public final class EventQueue {

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
    private final Set<Object> pendingInsertions;
    private final Set<Object> pendingRemovals;
    private final List<PointerDownListener> pointerDownListeners;
    private final List<PointerMovedListener> pointerMovedListeners;
    private final List<PointerUpListener> pointerUpListeners;
    private final List<ScrollListener> scrollListeners;
    private final List<SensorListener> sensorListeners;
    private final List<TickListener> tickListeners;
    private boolean dragEnabled;
    private List<Event> eventsIn;
    private List<Event> eventsOut;
    private PointerEvent lastDragEvent;

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
        this.pendingInsertions = new HashSet<Object>();
        this.pendingRemovals = new HashSet<Object>();
        this.pointerDownListeners = new ArrayList<PointerDownListener>();
        this.pointerMovedListeners = new ArrayList<PointerMovedListener>();
        this.pointerUpListeners = new ArrayList<PointerUpListener>();
        this.scrollListeners = new ArrayList<ScrollListener>();
        this.sensorListeners = new ArrayList<SensorListener>();
        this.tickListeners = new ArrayList<TickListener>();
        this.dragEnabled = false;
        this.eventsIn = new ArrayList<Event>();
        this.eventsOut = new ArrayList<Event>();
        this.lastDragEvent = null;
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
                    this.pendingRemovals.remove(listener);
                }
                else {
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
                else {
                    this.pendingRemovals.add(listener);
                }
            }
        }
    }

    /**
     * Enables or disables drag mode for this event queue. When the drag mode is enabled, unhandled pointer events are
     * automatically converted to scroll events.
     *
     * @param dragEnabled is the drag mode enabled
     *
     * @since 2.0
     */
    public void setDragEnabled(final boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }

    private void dispatchEvent(final Event event) {
        // Pending listener operations must be processed before every event dispatch. Otherwise, an event might be
        // delivered to a listener that has been removed during the last event dispatch.
        this.processPendingListeners();
        switch (event.getType()) {
            case ACTION:
                this.dispatchActionEvent((ActionEvent) event);
                break;
            case CONNECTION_ACCEPTED:
                this.dispatchConnectionAcceptedEvent((ConnectionEvent) event);
                break;
            case CONNECTION_CLOSED:
                this.dispatchConnectionClosedEvent((ConnectionEvent) event);
                break;
            case KEY_DOWN:
                this.dispatchKeyDownEvent((KeyEvent) event);
                break;
            case KEY_TYPED:
                this.dispatchKeyTypedEvent((KeyEvent) event);
                break;
            case KEY_UP:
                this.dispatchKeyUpEvent((KeyEvent) event);
                break;
            case MESSAGE_RECEIVED:
                this.dispatchMessageReceivedEvent((MessageEvent) event);
                break;
            case POINTER_DOWN:
                this.dispatchPointerDownEvent((PointerEvent) event);
                break;
            case POINTER_MOVED:
                this.dispatchPointerMovedEvent((PointerEvent) event);
                break;
            case POINTER_UP:
                this.dispatchPointerUpEvent((PointerEvent) event);
                break;
            case SCROLL:
                this.dispatchScrollEvent((ScrollEvent) event);
                break;
            case SENSOR:
                this.dispatchSensorEvent((SensorEvent) event);
                break;
            case TICK:
                this.dispatchTickEvent((TickEvent) event);
                break;
        }
    }

    private void dispatchActionEvent(final ActionEvent event) {
        int i = 0;
        while (i < this.actionListeners.size() && !event.isConsumed()) {
            try {
                this.actionListeners.get(i).onAction(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchConnectionAcceptedEvent(final ConnectionEvent event) {
        int i = 0;
        while (i < this.connectionAcceptedListeners.size() && !event.isConsumed()) {
            try {
                this.connectionAcceptedListeners.get(i).onConnectionAccepted(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchConnectionClosedEvent(final ConnectionEvent event) {
        int i = 0;
        while (i < this.connectionClosedListeners.size() && !event.isConsumed()) {
            try {
                this.connectionClosedListeners.get(i).onConnectionClosed(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyDownEvent(final KeyEvent event) {
        int i = 0;
        while (i < this.keyDownListeners.size()) {
            try {
                this.keyDownListeners.get(i).onKeyDown(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyTypedEvent(final KeyEvent event) {
        int i = 0;
        while (i < this.keyTypedListeners.size()) {
            try {
                this.keyTypedListeners.get(i).onKeyTyped(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyUpEvent(final KeyEvent event) {
        int i = 0;
        while (i < this.keyUpListeners.size()) {
            try {
                this.keyUpListeners.get(i).onKeyUp(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchMessageReceivedEvent(final MessageEvent event) {
        int i = 0;
        while (i < this.messageReceivedListeners.size() && !event.isConsumed()) {
            try {
                this.messageReceivedListeners.get(i).onMessageReceived(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchPointerDownEvent(final PointerEvent event) {
        int i = 0;
        while (i < this.pointerDownListeners.size() && !event.isConsumed()) {
            try {
                this.pointerDownListeners.get(i).onPointerDown(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if (this.dragEnabled && !event.isConsumed() && this.lastDragEvent == null) {
            this.lastDragEvent = event;
        }
    }

    private void dispatchPointerMovedEvent(final PointerEvent event) {
        int i = 0;
        while (i < this.pointerMovedListeners.size() && !event.isConsumed()) {
            try {
                this.pointerMovedListeners.get(i).onPointerMoved(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if ((this.lastDragEvent != null) && (event.getPointerId() == this.lastDragEvent.getPointerId()) && !event.isConsumed()) {
            double dx = this.lastDragEvent.getX() - event.getX();
            double dy = this.lastDragEvent.getY() - event.getY();
            this.dispatchScrollEvent(new ScrollEvent(event.getSource(), dx, dy));
            this.lastDragEvent = event;
        }
    }

    private void dispatchPointerUpEvent(final PointerEvent event) {
        int i = 0;
        while (i < this.pointerUpListeners.size() && !event.isConsumed()) {
            try {
                this.pointerUpListeners.get(i).onPointerUp(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if (this.lastDragEvent != null && this.lastDragEvent.getPointerId() == event.getPointerId()) {
            this.lastDragEvent = null;
        }
    }

    private void dispatchScrollEvent(final ScrollEvent event) {
        int i = 0;
        while (i < this.scrollListeners.size() && !event.isConsumed()) {
            try {
                this.scrollListeners.get(i).onScroll(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchSensorEvent(final SensorEvent event) {
        int i = 0;
        while (i < this.sensorListeners.size() && !event.isConsumed()) {
            try {
                this.sensorListeners.get(i).onSensorChanged(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchTickEvent(final TickEvent event) {
        int i = 0;
        while (i < this.tickListeners.size() && !event.isConsumed()) {
            try {
                this.tickListeners.get(i).onTick(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
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

        if (listener instanceof ScrollListener) {
            this.scrollListeners.add((ScrollListener) listener);
        }

        if (listener instanceof SensorListener) {
            this.sensorListeners.add((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.add((TickListener) listener);
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

        if (listener instanceof ScrollListener) {
            this.scrollListeners.remove((ScrollListener) listener);
        }

        if (listener instanceof SensorListener) {
            this.sensorListeners.remove((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.remove((TickListener) listener);
        }
    }

    private void processPendingListeners() {
        synchronized (this.listenerLock) {
            for (final Object listener : this.pendingRemovals) {
                this.doRemoveListener(listener);
            }

            for (final Object listener : this.pendingInsertions) {
                this.doAddListener(listener);
            }

            this.pendingRemovals.clear();
            this.pendingInsertions.clear();
        }
    }
}
