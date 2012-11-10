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
package ch.jeda;

import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * Replaces placeholders in a string with the corresponding arguments. The
     * message template may contain placeholders having the form <tt>{N}</tt>
     * where <tt>N</tt> is a number. This method will replace the placeholder
     * <tt>{0}</tt> with the first argument after <tt>message</tt>, <tt>{1}</tt>
     * with the second argument and so on.
     *
     * @param message message template
     * @param args arguments to be inserted in the message template
     * @return resulting message
     */
    public static String args(String message, Object... args) {
        if (args == null) {
            return message;
        }
        String result = message;
        int i = 0;
        for (Object arg : args) {
            String key = "{" + i + "}";
            String val = "null";
            if (arg != null) {
                val = arg.toString();
            }
            result = result.replace(key, val);
            i = i + 1;
        }
        return result;
    }

    /**
     * Creates and returns a list of double values.
     *
     * @param values comma-separated double values
     * @return a list of double values
     */
    public static List<Double> doubleList(double... values) {
        ArrayList<Double> result = new ArrayList<Double>();
        for (double value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of int values.
     *
     * @param values comma-separated int values
     * @return a list of int values
     */
    public static List<Integer> intList(int... values) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Returns a random int number between 0 and (max - 1).
     *
     * @param max upper limit for the random number
     * @return random number
     */
    public static int randomInt(int max) {
        return (int) (Math.random() * max);
    }

    /**
     * Creates and returns a list of String values.
     *
     * @param values comma-separated String values
     * @return a list of String values
     */
    public static List<String> stringList(String... values) {
        ArrayList<String> result = new ArrayList<String>();
        for (String value : values) {
            result.add(value);
        }
        return result;
    }
}
