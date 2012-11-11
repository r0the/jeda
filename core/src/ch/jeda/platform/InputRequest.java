/*
 * Copyright (C) 2012 by Stefan Rothe
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

public class InputRequest<T> extends Request {

    private final InputType<T> inputType;
    private final Object lock;
    private boolean cancelled;
    private boolean done;
    private T result;

    public InputRequest(InputType<T> inputType, T defaultValue) {
        assert inputType != null;

        this.inputType = inputType;
        this.lock = new Object();
        this.result = defaultValue;
    }

    public void cancelRequest() {
        synchronized (this.lock) {
            this.done = true;
            this.cancelled = true;
            this.lock.notify();
        }
    }

    public InputType<T> getInputType() {
        return this.inputType;
    }

    public T getResult() {
        return this.result;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setResult(T result) {
        synchronized (this.lock) {
            this.done = true;
            this.result = result;
            this.lock.notify();
        }
    }

    public void waitForResult() {
        synchronized (this.lock) {
            while (!this.done) {
                try {
                    this.lock.wait();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }
}
