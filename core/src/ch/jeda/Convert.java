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
 * @since 1.0
 */
public final class Convert {

    private static final String FALSE_VALUE = "false";
    private static final String TRUE_VALUE = "true";

    /**
     * Converts a @{link java.lang.String} to a <tt>boolean</tt> value. Returns the <tt>boolean</tt> value represented
     * by the {@link java.lang.String}. Returns the <tt>defaultValue</tt>, if <tt>value</tt> does not represent a valid
     * <tt>boolean</tt>.
     *
     * @param value the string to be converted to a <tt>boolean</tt>
     * @param defaultValue the default value
     * @return the <tt>boolean</tt> value represented by <tt>value</tt> or the <tt>defaultValue</tt>
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
     * Converts a @{link java.lang.String} to a <tt>double</tt> value. Returns the <tt>double</tt> value represented by
     * the {@link java.lang.String}. Returns the <tt>defaultValue</tt>, if <tt>value</tt> does not represent a valid
     * <tt>double</tt>.
     *
     * @param value the string to be converted to a <tt>double</tt>
     * @param defaultValue the default value
     * @return the <tt>double</tt> value represented by <tt>value</tt> or the <tt>defaultValue</tt>
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
     * Converts a @{link java.lang.String} to a <tt>float</tt> value. Returns the <tt>float</tt> value represented by
     * the {@link java.lang.String}. Returns the <tt>defaultValue</tt>, if <tt>value</tt> does not represent a valid
     * <tt>float</tt>.
     *
     * @param value the string to be converted to a <tt>float</tt>
     * @param defaultValue the default value
     * @return the <tt>float</tt> value represented by <tt>value</tt> or the <tt>defaultValue</tt>
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
     * Converts a @{link java.lang.String} to an <tt>int</tt> value. Returns the <tt>int</tt> value represented by the
     * {@link java.lang.String}. Returns the <tt>defaultValue</tt>, if <tt>value</tt> does not represent a valid
     * <tt>int</tt>.
     *
     * @param value the string to be converted to a <tt>int</tt>
     * @param defaultValue the default value
     * @return the <tt>int</tt> value represented by <tt>value</tt> or the <tt>defaultValue</tt>
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
     * Creates and returns a list of <tt>double</tt> values.
     *
     * @param values comma-separated <tt>double</tt> values
     * @return a list of <tt>double</tt> values
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
     * Creates and returns a list of <tt>float</tt> values.
     *
     * @param values comma-separated <tt>float</tt> values
     * @return a list of <tt>float</tt> values
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
     * Creates and returns a list of <tt>int</tt> values.
     *
     * @param values comma-separated <tt>int</tt> values
     * @return a list of <tt>int</tt> values
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
     * Converts a <tt>boolean</tt> value to a {@link java.lang.String}. Converts <tt>true</tt> to the string
     * <tt>"true"</tt> and <tt>false</tt> to the string <tt>"false"</tt>.
     *
     * @param value the <tt>boolean</tt> value to be converted
     * @return the {@link java.lang.String} representing the <tt>value</tt>
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
     * Converts a <tt>double</tt> value to a {@link java.lang.String}.
     *
     * @param value the <tt>double</tt> value to be converted
     * @return the {@link java.lang.String} representing the <tt>value</tt>
     *
     * @see #toDouble(java.lang.String, double)
     * @see #toString(double, int)
     * @since 1.2
     */
    public static String toString(final double value) {
        return String.valueOf(value);
    }

    /**
     * Converts a <tt>double</tt> value to a {@link java.lang.String}, rounding to the specified number of decimal
     * places.
     *
     * @param value the <tt>double</tt> value to be converted
     * @param decimalPlaces the number of decimal places to round to
     * @return the {@link java.lang.String} representing the <tt>value</tt>
     * @throws IllegalArgumentException if <tt>decimalPlaces</tt> is negative
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
     * Converts an <tt>int</tt> value to a {@link java.lang.String}.
     *
     * @param value the <tt>int</tt> value to be converted
     * @return the {@link java.lang.String} representing the <tt>value</tt>
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
