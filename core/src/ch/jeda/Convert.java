/*
 * Copyright (C) 2014 by Stefan Rothe
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
 * @since 1
 */
public final class Convert {

    /**
     * Convert double values to a list.
     *
     * @param values comma-separated double values
     * @return a list of double values
     */
    public static List<Double> toList(final double... values) {
        final ArrayList<Double> result = new ArrayList<Double>();
        for (final double value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of float values.
     *
     * @param values comma-separated float values
     * @return a list of float values
     *
     * @since 1
     */
    public static List<Float> toList(final float... values) {
        final ArrayList<Float> result = new ArrayList<Float>();
        for (final float value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of int values.
     *
     * @param values comma-separated int values
     * @return a list of int values
     *
     * @since 1
     */
    public static List<Integer> toList(final int... values) {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (final int value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Creates and returns a list of String values.
     *
     * @param values comma-separated String values
     * @return a list of String values
     *
     * @since 1
     */
    public static List<String> toList(final String... values) {
        return Arrays.asList(values);
    }

    /**
     * Converts the specified objects to a string.
     *
     * @param objects comma-separated objects
     * @return a string representation of the specified objects
     *
     * @since 1
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
