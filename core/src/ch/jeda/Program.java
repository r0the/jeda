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
 * <strong>Example:</strong>
 * <pre><code> import ch.jeda.*;
 *
 * public class HelloWorld extends Program {
 *
 * @Override
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
     * Constructs a program. The created program will have the state
     * {@link ProgramState#Creating}.
     *
     * @since 1
     */
    protected Program() {
        this.stateLock = new Object();
        this.state = ProgramState.Creating;
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
     * @deprecated Use {@link #stop()} instead.
     */
    @Deprecated
    public final void requestStop() {
        this.setState(ProgramState.Stopped);
    }

    /**
     * Executes the program. Override this method to implement the program. The
     * program should react to changes of the program state. It should not
     * execute program logic while the program state is
     * {@link ProgramState#Paused}. The program should return from the
     * {@link #run()} method if the program state is
     * {@link ProgramState#Stopped}.
     *
     * @since 1
     */
    public abstract void run();

    /**
     * Requests the program to stop. Sets the program state to
     * {@link ProgramState#Stopped}.
     */
    public final void stop() {
        this.setState(ProgramState.Stopped);
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
     * @deprecated Use <tt>this.getState() == ProgramState.Stopped</tt> instead.
     */
    @Deprecated
    protected final boolean stopRequested() {
        return this.getState() == ProgramState.Stopped;
    }

    /**
     * Prompts the user to enter a <tt>double</tt> value. The specified message
     * is presented to the user along with a field to enter the <tt>double</tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt.
     *
     * @see #readDouble(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final double readDouble(final String message) {
        InputRequest<Double> request = new InputRequest<Double>(InputType.forDouble(), 0d);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter a <tt>double</tt> value. A message is presented
     * to the user along with a field to enter the <tt>double</tt> value. The
     * message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may
     * be formatted using simple HTML. Returns <tt>0.0</tt> if the user cancels
     * the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt>
     *
     * @see #readDouble(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final double readDouble(final String messageTemplate, final Object... args) {
        return this.readDouble(Util.args(messageTemplate, args));
    }

    /**
     * Prompts the user to enter an <tt>int</tt> value. The specified message is
     * presented to the user along with a field to enter the <tt>int<//tt>
     * value. The message may be formatted using simple HTML. Returns <tt>0</tt>
     * if the user cancels the input.
     *
     * @param message the message
     * @return <tt>int</tt> value entered by the user or <tt>0</tt>
     *
     * @see #readInt(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final int readInt(final String message) {
        InputRequest<Integer> request = new InputRequest<Integer>(InputType.forInt(), 0);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter an <tt>int</tt> value. A message is presented
     * to the user along with a field to enter the <tt>int</tt> value. The
     * message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may
     * be formatted using simple HTML. Returns <tt>0</tt> if the user cancels
     * the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>int</tt> value entered by the user or <tt>0</tt>
     *
     * @see #readInt(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final int readInt(final String messageTemplate, final Object... args) {
        return this.readInt(Util.args(messageTemplate, args));
    }

    /**
     * Prompts the user to enter a <tt>String</tt> value. The specified message
     * is presented to the user along with a field to enter the <tt>String<//tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>null</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>String</tt> value entered by the user or <tt>null</tt>
     *
     * @see #readString(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final String readString(final String message) {
        InputRequest<String> request = new InputRequest<String>(InputType.forString(), "");
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter a <tt>String</tt> value. A message is presented
     * to the user along with a field to enter the <tt>String</tt> value. The
     * message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may
     * be formatted using simple HTML. Returns <tt>null</tt> if the user cancels
     * the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>String</tt> value entered by the user or <tt>null</tt>
     *
     * @see #readString(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final String readString(final String messageTemplate, final Object... args) {
        return this.readString(Util.args(messageTemplate, args));
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
     * Writes a message. Writes the specified message to both the standard
     * output and to the Jeda log window. Shows the Jeda log window.
     *
     * @param message the message
     *
     * @see #write(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final void write(final String message) {
        Engine.getContext().write(message);
    }

    /**
     * Writes a message. Writes a message to both the standard output and to the
     * Jeda log window. Shows the Jeda log window. The message to be written is
     * constructed from the <tt>messageTemplate</tt> and the
     * specified<tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     *
     * @see #write(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    protected final void write(final String messageTemplate, final Object... args) {
        this.write(Util.args(messageTemplate, args));
    }

    final void setState(final ProgramState value) {
        synchronized (this.stateLock) {
            this.state = value;
        }
    }
}
