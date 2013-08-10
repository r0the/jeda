/*
 * Copyright (C) 2013 by Stefan Rothe
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

public class Dialog {

    /**
     * Prompts the user to enter a <tt>double</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>double</tt>
     * value. The message may be formatted using simple HTML. Returns
     * <tt>0d</tt> if the user cancels the input.
     *
     * @param message the message
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt.
     *
     * @see #readDouble(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static double readDouble(final String message) {
        InputRequest<Double> request = new InputRequest<Double>(InputType.forDouble(), 0d);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter a <tt>double</tt> value. A message is presented to the user along with a field to enter
     * the <tt>double</tt> value. The message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may be formatted using simple HTML. Returns
     * <tt>0.0</tt> if the user cancels the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>double</tt> value entered by the user or <tt>0.0</tt>
     *
     * @see #readDouble(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static double readDouble(final String messageTemplate, final Object... args) {
        return readDouble(Util.args(messageTemplate, args));
    }

//    public static String selectFileName() {
//        InputRequest<String> request = new InputRequest<String>((InputType.forFile(), null);
//        Engine.getContext().showInputRequest(request);
//        request.waitForResult();
//        return request.getResult();
//    }

    /**
     * Prompts the user to enter an <tt>int</tt> value. The specified message is presented to the user along with a
     * field to enter the <tt>int<//tt>
     * value. The message may be formatted using simple HTML. Returns <tt>0</tt>
     * if the user cancels the input.
     *
     * @param message the message
     * @return <tt>int</tt> value entered by the user or <tt>0</tt>
     *
     * @see #readInt(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static int readInt(final String message) {
        InputRequest<Integer> request = new InputRequest<Integer>(InputType.forInt(), 0);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter an <tt>int</tt> value. A message is presented to the user along with a field to enter
     * the <tt>int</tt> value. The message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may be formatted using simple HTML. Returns
     * <tt>0</tt> if the user cancels the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>int</tt> value entered by the user or <tt>0</tt>
     *
     * @see #readInt(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static int readInt(final String messageTemplate, final Object... args) {
        return readInt(Util.args(messageTemplate, args));
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
     * @see #readString(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static String readString(final String message) {
        InputRequest<String> request = new InputRequest<String>(InputType.forString(), "");
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    /**
     * Prompts the user to enter a <tt>String</tt> value. A message is presented to the user along with a field to enter
     * the <tt>String</tt> value. The message to be presented to the user is constructed from the
     * <tt>messageTemplate</tt> and the specified <tt>args</tt> by a call to
     * {@link Util#args(java.lang.String, java.lang.Object[])}. The message may be formatted using simple HTML. Returns
     * <tt>null</tt> if the user cancels the input.
     *
     * @param messageTemplate the message template
     * @param args the arguments to be inserted in the message template
     * @return <tt>String</tt> value entered by the user or <tt>null</tt>
     *
     * @see #readString(java.lang.String)
     * @see Util#args(java.lang.String, java.lang.Object[])
     * @since 1
     */
    public static String readString(final String messageTemplate, final Object... args) {
        return readString(Util.args(messageTemplate, args));
    }
}
