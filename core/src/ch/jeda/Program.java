/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
package ch.jeda;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the base class for all programs written using the Jeda framework.
 * <p>
 * To write a Jeda program, create a class that inherits from ch.jeda.Program
 * and overwrite the {@link #run()} method.
 * <p>
 * @version 1.0
 */
public abstract class Program {

    private final AtomicBoolean stopRequested;

    /**
     * Initializes this Program object.
     *
     * @since 1.0
     */
    protected Program() {
        this.stopRequested = new AtomicBoolean(false);
    }

    /**
     * Request this program to stop. After a call to this method, the method
     * {@link #stopRequested()} will return <code>true</code>.
     *
     * @see #stopRequested()
     * @since 1.0
     */
    public final void requestStop() {
        this.stopRequested.set(true);
    }

    /**
     * Override this method to implement the program. If the program contains
     * a loop, the result of {@link #stopRequested()} should be used as a
     * breaking condition.
     *
     * @since 1.0
     */
    public abstract void run();

    /**
     * Checks whether stop has been requested. This method returns <code>
     * true</code> if {@link #requestStop()} has been called at least once,
     * otherwise it returns <code>false</code>.
     * 
     * @return <code>true</code> if <code>requestStop()</code> has been called.
     * @see #requestStop()
     * @since 1.0
     */
    protected final boolean stopRequested() {
        return this.stopRequested.get();
    }

    /**
     * Prompts the user to input a double value. The specified
     * <code>message</code> is displayed to the user. It may be formatted using
     * simple HTML. Returns <code>0d</code> if the user cancels the input
     * prompt.
     * 
     * @param message the message displayed to the user
     * @return double value entered by the user
     * 
     * @since 1.0
     */
    protected final double readDouble(String message) {
        return Engine.getCurrentEngine().readDouble(message);
    }

    /**
     * Prompts the user to input an int value. The specified
     * <code>message</code> is displayed to the user. It may be formatted using
     * simple HTML. Returns <code>0d</code> if the user cancels the input
     * prompt.
     * 
     * @param message the message displayed to the user
     * @return iny value entered by the user
     * 
     * @since 1.0
     */
    protected final int readInt(String message) {
        return Engine.getCurrentEngine().readInt(message);
    }

    /**
     * Waits for the specified amount of time.
     * 
     * @param milliseconds the amount of milliseconds to wait
     * 
     * @since 1.0
     */
    protected final void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {
        }
    }

    /**
     * Writes a message to the Jeda log window.
     * 
     * @param message the message to write
     */
    protected final void write(String message) {
        Engine.getCurrentEngine().write(message);
    }

    protected final void write(String message, Object... args) {
        Engine.getCurrentEngine().write(message, args);
    }
}
