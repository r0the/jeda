/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

/**
 * @deprecated Use a subclass of {@link ch.jeda.ui.InputField} instead.
 * @since 1.0
 */
public class Dialog<T> {

    private final InputRequest<T> inputRequest;

    /**
     * Prompts the user to enter a <tt>double</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>double</tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt.
     *
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public static double readDouble(final Object... message) {
        final Dialog<Double> dialog = new Dialog<Double>(Double.class);
        dialog.setMessage(message);
        dialog.setTitle(Message.get("jeda.gui.input-title"));
        dialog.show();
        if (dialog.getResult() == null) {
            return 0d;
        }
        else {
            return dialog.getResult();
        }
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
     * @deprecated Use {@link ch.jeda.ui.IntInputField} instead.
     * @since 1.0
     */
    public static int readInt(final Object... message) {
        final Dialog<Integer> dialog = new Dialog<Integer>(Integer.class);
        dialog.setMessage(message);
        dialog.setTitle(Message.get("jeda.gui.input-title"));
        dialog.show();
        if (dialog.getResult() == null) {
            return 0;
        }
        else {
            return dialog.getResult();
        }
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
     * @deprecated Use {@link ch.jeda.ui.StringInputField} instead.
     * @since 1.0
     */
    public static String readString(final Object... message) {
        final Dialog<String> dialog = new Dialog<String>(String.class);
        dialog.setMessage(message);
        dialog.setTitle(Message.get("jeda.gui.input-title"));
        dialog.show();
        if (dialog.getResult() == null) {
            return "";
        }
        else {
            return dialog.getResult();
        }
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public Dialog(final Class<T> clazz) {
        this.inputRequest = new InputRequest<T>(clazz);
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public void setMessage(final Object... message) {
        this.inputRequest.setMessage(Convert.toString(message));
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public void setTitle(final Object... title) {
        this.inputRequest.setTitle(Convert.toString(title));
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public void show() {
        Jeda.showInputRequest(this.inputRequest);
        this.inputRequest.waitForResult();
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.InputField} instead.
     * @since 1.0
     */
    public T getResult() {
        return this.inputRequest.getResult();
    }
}
