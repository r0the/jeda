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

    private Dialog() {
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
    public static double readDouble(final Object... message) {
        final InputRequest<Double> request = new InputRequest<Double>(InputType.forDouble(), 0d);
        request.setMessage(Util.toString(message));
        request.setTitle(Helper.getMessage("jeda.gui.input-title"));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
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
     * @since 1
     */
    public static int readInt(final Object... message) {
        final InputRequest<Integer> request = new InputRequest<Integer>(InputType.forInt(), 0);
        request.setMessage(Util.toString(message));
        request.setTitle(Helper.getMessage("jeda.gui.input-title"));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
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
    public static String readString(final Object... message) {
        final InputRequest<String> request = new InputRequest<String>(InputType.forString(), "");
        request.setMessage(Util.toString(message));
        request.setTitle(Helper.getMessage("jeda.gui.input-title"));
        Engine.getContext().showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }
}
