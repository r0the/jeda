/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

/**
 * Base class for all Jeda programs.
 * <p>
 * To write a Jeda program, create a class that inherits from ch.jeda.Program and overwrite the {@link #run()} method.
 * <p>
 * <strong>Example:</strong>
 * <pre><code> import ch.jeda.*;
 *
 * public class HelloWorld extends Program {
 *
 * public void run() {
 *    write("Hello World");
 * }</code></pre>
 *
 * @since 1
 */
public abstract class Program {

    private final Object stateLock;
    private ProgramState state;

    /**
     * Constructs a program. The created program will have the state {@link ProgramState#CREATING}.
     *
     * @since 1
     */
    protected Program() {
        this.stateLock = new Object();
        this.state = ProgramState.CREATING;
    }

    /**
     * Returns the current program state.
     *
     * @return current program state.
     *
     * @see ProgramState
     * @since 1
     */
    public final ProgramState getState() {
        synchronized (this.stateLock) {
            return this.state;
        }
    }

    /**
     * Executes the program. Override this method to implement the program. The program should react to changes of the
     * program state. It should not execute program logic while the program state is {@link ProgramState#PAUSED}. The
     * program should return from the {@link #run()} method if the program state is {@link ProgramState#STOPPED}.
     *
     * @since 1
     */
    public abstract void run();

    /**
     * Requests the program to stop. Sets the program state to {@link ProgramState#STOPPED}.
     */
    public final void stop() {
        this.setState(ProgramState.STOPPED);
    }

    /**
     * Returns the Jeda system properties.
     *
     * @return Jeda system properties.
     * @see Properties
     */
    protected final Properties getProperties() {
        return Engine.getContext().getProperties();
    }

    /**
     * Prompts the user to enter a <tt>double</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>double</tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt.
     *
     * @since 1
     */
    protected final double readDouble(final Object... message) {
        return Dialog.readDouble(message);
    }

    /**
     * Prompts the user to enter an <tt>int</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>int<//tt>
     * value. The message may be formatted using simple HTML. Returns <tt>0</tt>
     * if the user cancels the input.
     *
     * @param message the message
     * @return <tt>int</tt> value entered by the user or <tt>0</tt>
     *
     * @since 1
     */
    protected final int readInt(final Object... message) {
        return Dialog.readInt(message);
    }

    /**
     * Prompts the user to enter a <tt>String</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>String<//tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>null</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>String</tt> value entered by the user or <tt>null</tt>
     *
     * @since 1
     */
    protected final String readString(final Object... message) {
        return Dialog.readString(message);
    }

    /**
     * Waits for the specified amount of time.
     *
     * @param milliseconds the amount of milliseconds to wait
     *
     * @since 1
     */
    protected final void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {
        }
    }

    protected final void write(final Object... args) {
        Engine.getContext().write(Util.toString(args));
    }

    final void setState(final ProgramState value) {
        synchronized (this.stateLock) {
            this.state = value;
        }
    }
}
