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

import ch.jeda.platform.InputRequest;
import ch.jeda.platform.InputType;

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
     * Prompts the user to input a <tt>double</tt> value. The specified message
     * is displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message
     * @return <tt>double</tt> value entered by the user
     *
     * @see #readDouble(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final double readDouble(String message) {
        InputRequest<Double> request = new InputRequest(InputType.forDouble(), 0d);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to input a <tt>double</tt> value. The specified message
     * is displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>double</tt> value entered by the user
     *
     * @see #readDouble(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final double readDouble(String message, Object... args) {
        return this.readDouble(Util.args(message, args));
    }

    /**
     * Prompts the user to input an <tt>int</tt> value. The specified message is
     * displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message
     * @return <tt>int</tt> value entered by the user
     *
     * @see #readInt(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final int readInt(String message) {
        InputRequest<Integer> request = new InputRequest(InputType.forInt(), 0);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to input an <tt>int</tt> value. The specified message is
     * displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>int</tt> value entered by the user
     *
     * @see #readInt(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final int readInt(String message, Object... args) {
        return this.readInt(Util.args(message, args));
    }

    /**
     * Prompts the user to input a <tt>String</tt> value. The specified message
     * is displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message
     * @return <tt>String</tt> value entered by the user
     *
     * @see #readString(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final String readString(String message) {
        InputRequest<String> request = new InputRequest(InputType.forString(), "");
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to input a <tt>String</tt> value. The specified message
     * is displayed to the user. It may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input prompt.
     *
     * @param message the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>String</tt> value entered by the user
     *
     * @see #readString(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final String readString(String message, Object... args) {
        return this.readString(Util.args(message, args));
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
     * @param message the message
     *
     * @see #write(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final void write(String message) {
        Engine.getContext().write(message);
    }

    /**
     * Writes a message to the Jeda log window.
     *
     * @param message the message template
     * @param args the arguments to be inserted in the message template
     *
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @see #write(java.lang.String)
     * @since 1
     */
    protected final void write(String message, Object... args) {
        this.write(Util.args(message, args));
    }

    final void setState(ProgramState value) {
        synchronized (this.stateLock) {
            this.state = value;
        }
    }
}
