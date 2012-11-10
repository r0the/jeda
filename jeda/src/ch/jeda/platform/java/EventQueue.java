/*
 * Copyright (C) 2011 by Stefan Rothe
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

import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class EventQueue<T extends AWTEvent> implements Iterable<T> {

    private List<T> listenQueue;
    private List<T> readQueue;

    EventQueue() {
        this.listenQueue = new ArrayList<T>();
        this.readQueue = new ArrayList<T>();
    }

    synchronized void swap() {
        List<T> temp = this.listenQueue;
        this.listenQueue = this.readQueue;
        this.readQueue = temp;
        this.listenQueue.clear();
    }

    protected synchronized void add(T event) {
        this.listenQueue.add(event);
    }

    @Override
    public Iterator<T> iterator() {
        return this.readQueue.iterator();
    }
}
