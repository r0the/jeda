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
 * To write a Jeda program, create a class that inherits from ch.jeda.Program
 * and overwrite the {@link #run()} method.
 * <p>
 *
 * @since 1
 */
public abstract class Program {

    private final Object stateLock;
    private ProgramState state;

    /**
     * Constructs a program.
     *
     * @since 1
     */
    protected Program() {
        this.stateLock = new Object();
        this.state = ProgramState.Created;
    }

    public final ProgramState getState() {
        synchronized (this.stateLock) {
            return this.state;
        }
    }

    /**
     * @deprecated This method should not be called. Return from the
     * <tt>run()</tt> method to stop a Jeda program.
     */
    @Deprecated
    public final void requestStop() {
        this.setState(ProgramState.Stopped);
    }

    /**
     * Executes the program. Override this method to implement the program. If
     * the program contains a loop, the result of {@link #stopRequested()}
     * should be used as a breaking condition.
     *
     * @since 1
     */
    public abstract void run();

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
     * @deprecated Use <tt>this.getState() == ProgramState.Stopped</tt> instead.
     */
    @Deprecated
    protected final boolean stopRequested() {
        return this.getState() == ProgramState.Stopped;
    }

    /**
     * Prompts the user to input a
     * <code>double</code> value. The specified
     * <code>message</code> is displayed to the user. It may be formatted using
     * simple HTML. Returns
     * <code>0d</code> if the user cancels the input prompt.
     *
     * @param message the message displayed to the user
     * @return <code>double</code> value entered by the user
     *
     * @since 1.0
     */
    protected final double readDouble(String message) {
        return Engine.getContext().readDouble(message);
    }

    /**
     * Prompts the user to input an
     * <code>int</code> value. The specified
     * <code>message</code> is displayed to the user. It may be formatted using
     * simple HTML. Returns
     * <code>0d</code> if the user cancels the input prompt.
     *
     * @param message the message displayed to the user
     * @return <code>int</code> value entered by the user
     *
     * @since 1.0
     */
    protected final int readInt(String message) {
        return Engine.getContext().readInt(message);
    }

    /**
     * Prompts the user to input a
     * <code>String</code> value. The specified
     * <code>message</code> is displayed to the user. It may be formatted using
     * simple HTML. Returns
     * <code>0d</code> if the user cancels the input prompt.
     *
     * @param message the message displayed to the user
     * @return <code>String</code> value entered by the user
     *
     * @since 1.0
     */
    protected final String readString(String message) {
        return Engine.getContext().readString(message);
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

    /**
     * Writes a message to the Jeda log window.
     *
     * @param message the message to write
     */
    protected final void write(String message) {
        Engine.getContext().write(message);
    }

    /**
     * Writes a message to the Jeda log window.
     *
     * @param message the message to write
     * @param args
     */
    protected final void write(String message, Object... args) {
        Engine.getContext().write(message, args);
    }

    final void setState(ProgramState value) {
        synchronized (this.stateLock) {
            this.state = value;
        }
    }
}
