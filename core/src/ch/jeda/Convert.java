/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Provides conversion methods.
 *
 * @version 4
 * @since 1.0
 */
public final class Convert {

    private static final String FALSE_VALUE = "false";
    private static final String TRUE_VALUE = "true";

    /**
     * Decodes Base 64-encoded data.
     *
     * @param base64 the Base 64-encoded data
     * @return the raw data
     *
     * @since 2.1
     */
    public static byte[] fromBase64(final String base64) {
        return Jeda.decodeBase64(base64);
    }

    /**
     * Encodes data with Base 64.
     *
     * @param data the data to encode
     * @return the Base 64-encoded data
     *
     * @since 2.1
     */
    public static String toBase64(final byte[] data) {
        return Jeda.encodeBase64(data);
    }

    /**
     * Converts a @{link java.lang.String} to a <code>boolean</code> value. Returns the <code>boolean</code> value
     * represented by the {@link java.lang.String}. Returns the <code>defaultValue</code>, if <code>value</code> does
     * not represent a valid <code>boolean</code>.
     *
     * @param value the string to be converted to a <code>boolean</code>
     * @param defaultValue the default value
     * @return the <code>boolean</code> value represented by <code>value</code> or the <code>defaultValue</code>
     *
     * @see #toString(boolean)
     * @since 1.2
     */
    public static boolean toBoolean(final String value, final boolean defaultValue) {
        if (FALSE_VALUE.equals(value)) {
            return false;
        }
        else if (TRUE_VALUE.equals(value)) {
            return true;
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Converts a @{link java.lang.String} to a <code>double</code> value. Returns the <code>double</code> value
     * represented by the {@link java.lang.String}. Returns the <code>defaultValue</code>, if <code>value</code> does
     * not represent a valid <code>double</code>.
     *
     * @param value the string to be converted to a <code>double</code>
     * @param defaultValue the default value
     * @return the <code>double</code> value represented by <code>value</code> or the <code>defaultValue</code>
     *
     * @since 1.2
     */
    public static double toDouble(final String value, final double defaultValue) {
        try {
            return Double.parseDouble(value);
        }
        catch (final NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Converts a @{link java.lang.String} to a <code>float</code> value. Returns the <code>float</code> value
     * represented by the {@link java.lang.String}. Returns the <code>defaultValue</code>, if <code>value</code> does
     * not represent a valid <code>float</code>.
     *
     * @param value the string to be converted to a <code>float</code>
     * @param defaultValue the default value
     * @return the <code>float</code> value represented by <code>value</code> or the <code>defaultValue</code>
     *
     * @since 2.0
     */
    public static float toFloat(final String value, final float defaultValue) {
        try {
            return Float.parseFloat(value);
        }
        catch (final NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Converts a @{link java.lang.String} to an <code>int</code> value. Returns the <code>int</code> value represented
     * by the {@link java.lang.String}. Returns the <code>defaultValue</code>, if <code>value</code> does not represent
     * a valid <code>int</code>.
     *
     * @param value the string to be converted to a <code>int</code>
     * @param defaultValue the default value
     * @return the <code>int</code> value represented by <code>value</code> or the <code>defaultValue</code>
     *
     * @since 1.2
     */
    public static int toInt(final String value, final int defaultValue) {
        try {
            return Integer.parseInt(value);
        }
        catch (final NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Creates and returns a list of <code>double</code> values.
     *
     * @param values comma-separated <code>double</code> values
     * @return a list of <code>double</code> values
     *
     * @since 1.0
     */
    public static List<Double> toList(final double... values) {
        final ArrayList<Double> result = new ArrayList<Double>();
        for (final double value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of <code>float</code> values.
     *
     * @param values comma-separated <code>float</code> values
     * @return a list of <code>float</code> values
     *
     * @since 1.0
     */
    public static List<Float> toList(final float... values) {
        final ArrayList<Float> result = new ArrayList<Float>();
        for (final float value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of <code>int</code> values.
     *
     * @param values comma-separated <code>int</code> values
     * @return a list of <code>int</code> values
     *
     * @since 1.0
     */
    public static List<Integer> toList(final int... values) {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (final int value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of {@link java.lang.String} values.
     *
     * @param values comma-separated {@link java.lang.String} values
     * @return a list of {@link java.lang.String} values
     *
     * @since 1.0
     */
    public static List<String> toList(final String... values) {
        return Arrays.asList(values);
    }

    /**
     * Converts a <code>boolean</code> value to a {@link java.lang.String}. Converts <code>true</code> to the string
     * <code>"true"</code> and <code>false</code> to the string <code>"false"</code>.
     *
     * @param value the <code>boolean</code> value to be converted
     * @return the {@link java.lang.String} representing the <code>value</code>
     *
     * @see #toBoolean(java.lang.String, boolean)
     * @since 1.2
     */
    public static String toString(final boolean value) {
        if (value) {
            return TRUE_VALUE;
        }
        else {
            return FALSE_VALUE;
        }
    }

    /**
     * Converts a <code>double</code> value to a {@link java.lang.String}.
     *
     * @param value the <code>double</code> value to be converted
     * @return the {@link java.lang.String} representing the <code>value</code>
     *
     * @see #toDouble(java.lang.String, double)
     * @see #toString(double, int)
     * @since 1.2
     */
    public static String toString(final double value) {
        return String.valueOf(value);
    }

    /**
     * Converts a <code>double</code> value to a {@link java.lang.String}, rounding to the specified number of decimal
     * places.
     *
     * @param value the <code>double</code> value to be converted
     * @param decimalPlaces the number of decimal places to round to
     * @return the {@link java.lang.String} representing the <code>value</code>
     * @throws IllegalArgumentException if <code>decimalPlaces</code> is negative
     *
     * @see #toDouble(java.lang.String, double)
     * @see #toString(double)
     * @since 2.0
     */
    public static String toString(final double value, final int decimalPlaces) {
        if (decimalPlaces <= 0) {
            return String.valueOf((int) value);
        }
        else {
            return String.format("%." + decimalPlaces + "g%n", value);
        }
    }

    /**
     * Converts an <code>int</code> value to a {@link java.lang.String}.
     *
     * @param value the <code>int</code> value to be converted
     * @return the {@link java.lang.String} representing the <code>value</code>
     *
     * @see #toInt(java.lang.String, int)
     * @since 1.2
     */
    public static String toString(final int value) {
        return String.valueOf(value);
    }

    /**
     * Converts the specified objects to a {@link java.lang.String}.
     *
     * @param objects comma-separated objects
     * @return a {@link java.lang.String} representation of the specified objects
     *
     * @since 1.0
     */
    public static String toString(final Object... objects) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < objects.length; ++i) {
            result.append(objectToString(objects[i]));
        }

        return result.toString();
    }

    private static String iterableToString(final Iterable iterable) {
        final StringBuilder result = new StringBuilder();
        result.append('[');
        for (final Object element : iterable) {
            if (result.length() > 1) {
                result.append(", ");
            }

            result.append(toString(element));
        }

        result.append(']');
        return result.toString();
    }

    private static String mapToString(final Map map) {
        final StringBuilder result = new StringBuilder();
        result.append('{');
        for (final Object key : map.keySet()) {
            if (result.length() > 1) {
                result.append(", ");
            }

            result.append(toString(key));
            result.append(": ");
            result.append(toString(map.get(key)));
        }

        result.append('}');
        return result.toString();
    }

    private static String objectToString(final Object object) {
        if (object == null) {
            return "null";
        }
        else if (object.getClass().isArray()) {
            return iterableToString(arrayToList(object));
        }
        else if (object instanceof Iterable) {
            return iterableToString((Iterable) object);
        }
        else if (object instanceof Map) {
            return mapToString((Map) object);
        }
        else {
            return object.toString();
        }
    }

    private static List<Object> arrayToList(final Object array) {
        final List<Object> result = new ArrayList<Object>();
        final Class componentType = array.getClass().getComponentType();
        if (Boolean.TYPE.equals(componentType)) {
            for (boolean element : (boolean[]) array) {
                result.add(element);
            }
        }
        else if (Character.TYPE.equals(componentType)) {
            for (char element : (char[]) array) {
                result.add(element);
            }
        }
        else if (Byte.TYPE.equals(componentType)) {
            for (byte element : (byte[]) array) {
                result.add(element);
            }
        }
        else if (Short.TYPE.equals(componentType)) {
            for (short element : (short[]) array) {
                result.add(element);
            }
        }
        else if (Integer.TYPE.equals(componentType)) {
            for (int element : (int[]) array) {
                result.add(element);
            }
        }
        else if (Long.TYPE.equals(componentType)) {
            for (long element : (long[]) array) {
                result.add(element);
            }
        }
        else if (Float.TYPE.equals(componentType)) {
            for (float element : (float[]) array) {
                result.add(element);
            }
        }
        else if (Double.TYPE.equals(componentType)) {
            for (double element : (double[]) array) {
                result.add(element);
            }
        }
        else {
            result.addAll(Arrays.asList((Object[]) array));
        }

        return result;
    }
}
