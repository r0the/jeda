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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the an event queue. This class is thread-safe.
 *
 * @since 1.4
 */
public final class EventQueue {

    private static final String EVENT_ERROR = "jeda.event.error";
    private final Object eventLock;
    private final List<EventQueue> eventQueues;
    private final Object listenerLock;
    private final Set<Object> listeners;
    private final Map<Object, ListenerWrapper> listenerMap;
    private final List<Object> pendingDeletions;
    private final List<Object> pendingInsertions;
    private final List<TickListener> tickListeners;
    private List<Event> eventsIn;
    private List<Event> eventsOut;

    /**
     * Constructs a new event queue.
     *
     * @since 1.4
     */
    public EventQueue() {
        this.eventLock = new Object();
        this.eventQueues = new ArrayList<EventQueue>();
        this.listenerLock = new Object();
        this.listeners = new HashSet<Object>();
        this.listenerMap = new HashMap<Object, ListenerWrapper>();
        this.pendingDeletions = new ArrayList<Object>();
        this.pendingInsertions = new ArrayList<Object>();
        this.tickListeners = new ArrayList<TickListener>();
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
            default:
                for (final ListenerWrapper wrapper : this.listenerMap.values()) {
                    wrapper.handleEvent(event);
                }

                break;
        }
    }

    private void doAddListener(final Object listener) {
        this.listeners.add(listener);
        this.listenerMap.put(listener, new ListenerWrapper(listener));
        if (listener instanceof EventQueue) {
            this.eventQueues.add((EventQueue) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.add((TickListener) listener);
        }
    }

    private void doRemoveListener(final Object listener) {
        this.listeners.remove(listener);
        this.listenerMap.remove(listener);
        if (listener instanceof EventQueue) {
            this.eventQueues.remove((EventQueue) listener);
        }

        if (listener instanceof TickListener) {
            this.tickListeners.remove((TickListener) listener);
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
