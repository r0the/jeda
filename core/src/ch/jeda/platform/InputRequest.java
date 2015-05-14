/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
package ch.jeda.platform;

/**
 * <b>Internal</b>. Do not use this class.
 */
public class InputRequest<T> extends Request {

    private final InputType<T> inputType;
    private final Object lock;
    private boolean cancelled;
    private boolean done;
    private T result;

    public InputRequest(final Class<T> clazz) {
        inputType = InputType.forClass(clazz);
        lock = new Object();
    }

    public void cancelRequest() {
        synchronized (lock) {
            done = true;
            cancelled = true;
            lock.notify();
        }
    }

    public InputType<T> getInputType() {
        return inputType;
    }

    public T getResult() {
        return result;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setResult(final T result) {
        synchronized (lock) {
            done = true;
            this.result = result;
            lock.notify();
        }
    }

    public void waitForResult() {
        synchronized (lock) {
            while (!done) {
                try {
                    lock.wait();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }
}
