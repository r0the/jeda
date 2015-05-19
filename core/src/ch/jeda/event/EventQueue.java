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
        actionListeners = new ArrayList<ActionListener>();
        connectionAcceptedListeners = new ArrayList<ConnectionAcceptedListener>();
        connectionClosedListeners = new ArrayList<ConnectionClosedListener>();
        eventLock = new Object();
        eventQueues = new ArrayList<EventQueue>();
        keyDownListeners = new ArrayList<KeyDownListener>();
        keyTypedListeners = new ArrayList<KeyTypedListener>();
        keyUpListeners = new ArrayList<KeyUpListener>();
        listenerLock = new Object();
        listeners = new HashSet<Object>();
        messageReceivedListeners = new ArrayList<MessageReceivedListener>();
        pendingInsertions = new HashSet<Object>();
        pendingRemovals = new HashSet<Object>();
        pointerDownListeners = new ArrayList<PointerDownListener>();
        pointerMovedListeners = new ArrayList<PointerMovedListener>();
        pointerUpListeners = new ArrayList<PointerUpListener>();
        scrollListeners = new ArrayList<ScrollListener>();
        sensorListeners = new ArrayList<SensorListener>();
        tickListeners = new ArrayList<TickListener>();
        dragEnabled = false;
        eventsIn = new ArrayList<Event>();
        eventsOut = new ArrayList<Event>();
        lastDragEvent = null;
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
            synchronized (eventLock) {
                eventsIn.add(event);
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
            synchronized (eventLock) {
                eventsIn.addAll(events);
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
            synchronized (listenerLock) {
                if (listeners.contains(listener)) {
                    pendingRemovals.remove(listener);
                }
                else {
                    pendingInsertions.add(listener);
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
        synchronized (eventLock) {
            final List<Event> temp = eventsOut;
            eventsOut = eventsIn;
            eventsIn = temp;
            eventsIn.clear();
        }

        // Distribute events to child event queues.
        for (int i = 0; i < eventQueues.size(); ++i) {
            eventQueues.get(i).addEvents(eventsOut);
        }

        // Dispatch events
        for (int i = 0; i < eventsOut.size(); ++i) {
            dispatchEvent(eventsOut.get(i));
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
            synchronized (listenerLock) {
                if (!listeners.contains(listener)) {
                    pendingInsertions.remove(listener);
                }
                else {
                    pendingRemovals.add(listener);
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
        processPendingListeners();
        switch (event.getType()) {
            case ACTION:
                dispatchActionEvent((ActionEvent) event);
                break;
            case CONNECTION_ACCEPTED:
                dispatchConnectionAcceptedEvent((ConnectionEvent) event);
                break;
            case CONNECTION_CLOSED:
                dispatchConnectionClosedEvent((ConnectionEvent) event);
                break;
            case KEY_DOWN:
                dispatchKeyDownEvent((KeyEvent) event);
                break;
            case KEY_TYPED:
                dispatchKeyTypedEvent((KeyEvent) event);
                break;
            case KEY_UP:
                dispatchKeyUpEvent((KeyEvent) event);
                break;
            case MESSAGE_RECEIVED:
                dispatchMessageReceivedEvent((MessageEvent) event);
                break;
            case POINTER_DOWN:
                dispatchPointerDownEvent((PointerEvent) event);
                break;
            case POINTER_MOVED:
                dispatchPointerMovedEvent((PointerEvent) event);
                break;
            case POINTER_UP:
                dispatchPointerUpEvent((PointerEvent) event);
                break;
            case SCROLL:
                dispatchScrollEvent((ScrollEvent) event);
                break;
            case SENSOR:
                dispatchSensorEvent((SensorEvent) event);
                break;
            case TICK:
                dispatchTickEvent((TickEvent) event);
                break;
        }
    }

    private void dispatchActionEvent(final ActionEvent event) {
        int i = 0;
        while (i < actionListeners.size() && !event.isConsumed()) {
            try {
                actionListeners.get(i).onAction(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchConnectionAcceptedEvent(final ConnectionEvent event) {
        int i = 0;
        while (i < connectionAcceptedListeners.size() && !event.isConsumed()) {
            try {
                connectionAcceptedListeners.get(i).onConnectionAccepted(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchConnectionClosedEvent(final ConnectionEvent event) {
        int i = 0;
        while (i < connectionClosedListeners.size() && !event.isConsumed()) {
            try {
                connectionClosedListeners.get(i).onConnectionClosed(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyDownEvent(final KeyEvent event) {
        int i = 0;
        while (i < keyDownListeners.size()) {
            try {
                keyDownListeners.get(i).onKeyDown(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyTypedEvent(final KeyEvent event) {
        int i = 0;
        while (i < keyTypedListeners.size()) {
            try {
                keyTypedListeners.get(i).onKeyTyped(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchKeyUpEvent(final KeyEvent event) {
        int i = 0;
        while (i < keyUpListeners.size()) {
            try {
                keyUpListeners.get(i).onKeyUp(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchMessageReceivedEvent(final MessageEvent event) {
        int i = 0;
        while (i < messageReceivedListeners.size() && !event.isConsumed()) {
            try {
                messageReceivedListeners.get(i).onMessageReceived(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchPointerDownEvent(final PointerEvent event) {
        int i = 0;
        while (i < pointerDownListeners.size() && !event.isConsumed()) {
            try {
                pointerDownListeners.get(i).onPointerDown(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if (dragEnabled && !event.isConsumed() && lastDragEvent == null) {
            lastDragEvent = event;
        }
    }

    private void dispatchPointerMovedEvent(final PointerEvent event) {
        int i = 0;
        while (i < pointerMovedListeners.size() && !event.isConsumed()) {
            try {
                pointerMovedListeners.get(i).onPointerMoved(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if ((lastDragEvent != null) && (event.getPointerId() == lastDragEvent.getPointerId()) && !event.isConsumed()) {
            float dx = lastDragEvent.getX() - event.getX();
            float dy = lastDragEvent.getY() - event.getY();
            dispatchScrollEvent(new ScrollEvent(event.getSource(), dx, dy));
            lastDragEvent = event;
        }
    }

    private void dispatchPointerUpEvent(final PointerEvent event) {
        int i = 0;
        while (i < pointerUpListeners.size() && !event.isConsumed()) {
            try {
                pointerUpListeners.get(i).onPointerUp(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }

        if (lastDragEvent != null && lastDragEvent.getPointerId() == event.getPointerId()) {
            lastDragEvent = null;
        }
    }

    private void dispatchScrollEvent(final ScrollEvent event) {
        int i = 0;
        while (i < scrollListeners.size() && !event.isConsumed()) {
            try {
                scrollListeners.get(i).onScroll(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchSensorEvent(final SensorEvent event) {
        int i = 0;
        while (i < sensorListeners.size() && !event.isConsumed()) {
            try {
                sensorListeners.get(i).onSensorChanged(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void dispatchTickEvent(final TickEvent event) {
        int i = 0;
        while (i < tickListeners.size() && !event.isConsumed()) {
            try {
                tickListeners.get(i).onTick(event);
            }
            catch (final Throwable ex) {
                Log.err(ex, Message.EVENT_ERROR);
            }

            ++i;
        }
    }

    private void doAddListener(final Object listener) {
        listeners.add(listener);
        if (listener instanceof ActionListener) {
            actionListeners.add((ActionListener) listener);
        }

        if (listener instanceof ConnectionAcceptedListener) {
            connectionAcceptedListeners.add((ConnectionAcceptedListener) listener);
        }

        if (listener instanceof ConnectionClosedListener) {
            connectionClosedListeners.add((ConnectionClosedListener) listener);
        }

        if (listener instanceof EventQueue) {
            eventQueues.add((EventQueue) listener);
        }

        if (listener instanceof KeyDownListener) {
            keyDownListeners.add((KeyDownListener) listener);
        }

        if (listener instanceof KeyTypedListener) {
            keyTypedListeners.add((KeyTypedListener) listener);
        }

        if (listener instanceof KeyUpListener) {
            keyUpListeners.add((KeyUpListener) listener);
        }

        if (listener instanceof MessageReceivedListener) {
            messageReceivedListeners.add((MessageReceivedListener) listener);
        }

        if (listener instanceof PointerDownListener) {
            pointerDownListeners.add((PointerDownListener) listener);
        }

        if (listener instanceof PointerMovedListener) {
            pointerMovedListeners.add((PointerMovedListener) listener);
        }

        if (listener instanceof PointerUpListener) {
            pointerUpListeners.add((PointerUpListener) listener);
        }

        if (listener instanceof ScrollListener) {
            scrollListeners.add((ScrollListener) listener);
        }

        if (listener instanceof SensorListener) {
            sensorListeners.add((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            tickListeners.add((TickListener) listener);
        }
    }

    private void doRemoveListener(final Object listener) {
        listeners.remove(listener);
        if (listener instanceof ActionListener) {
            actionListeners.remove((ActionListener) listener);
        }

        if (listener instanceof ConnectionAcceptedListener) {
            connectionAcceptedListeners.remove((ConnectionAcceptedListener) listener);
        }

        if (listener instanceof ConnectionClosedListener) {
            connectionClosedListeners.remove((ConnectionClosedListener) listener);
        }

        if (listener instanceof EventQueue) {
            eventQueues.remove((EventQueue) listener);
        }

        if (listener instanceof KeyDownListener) {
            keyDownListeners.remove((KeyDownListener) listener);
        }

        if (listener instanceof KeyTypedListener) {
            keyTypedListeners.remove((KeyTypedListener) listener);
        }

        if (listener instanceof KeyUpListener) {
            keyUpListeners.remove((KeyUpListener) listener);
        }

        if (listener instanceof MessageReceivedListener) {
            messageReceivedListeners.remove((MessageReceivedListener) listener);
        }

        if (listener instanceof PointerDownListener) {
            pointerDownListeners.remove((PointerDownListener) listener);
        }

        if (listener instanceof PointerMovedListener) {
            pointerMovedListeners.remove((PointerMovedListener) listener);
        }

        if (listener instanceof PointerUpListener) {
            pointerUpListeners.remove((PointerUpListener) listener);
        }

        if (listener instanceof ScrollListener) {
            scrollListeners.remove((ScrollListener) listener);
        }

        if (listener instanceof SensorListener) {
            sensorListeners.remove((SensorListener) listener);
        }

        if (listener instanceof TickListener) {
            tickListeners.remove((TickListener) listener);
        }
    }

    private void processPendingListeners() {
        synchronized (listenerLock) {
            for (final Object listener : pendingRemovals) {
                doRemoveListener(listener);
            }

            for (final Object listener : pendingInsertions) {
                doAddListener(listener);
            }

            pendingRemovals.clear();
            pendingInsertions.clear();
        }
    }
}
