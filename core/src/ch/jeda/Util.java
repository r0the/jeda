/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
 * Provides utility functions.
 */
public final class Util {

    /**
     * Returns the Euclidean distance between the origin and the point <tt>(x, y)</tt>.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the Euclidean distance between the origin and the point
     *
     * @since 1
     */
    public static float distance(final float x, final float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Creates and returns a list of double values.
     *
     * @param values comma-separated double values
     * @return a list of double values
     */
    public static List<Double> doubleList(final double... values) {
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
     */
    public static List<Float> floatList(final float... values) {
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
     */
    public static List<Integer> intList(final int... values) {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (final int value : values) {
            result.add(value);
        }

        return result;
    }

    /**
     * Checks if a <tt>float</tt> number is almost zero. Return <tt>true</tt> if the value of <tt>f</tt> is closer to
     * zero than five times the smallest representable <tt>float</tt> value.
     *
     * @param f the <tt>float</tt> value
     * @return <tt>true</tt> if <tt>f</tt> is almost zero, <tt>false</tt> otherwise.
     */
    public static boolean isZero(final float f) {
        return Math.abs(f) < 5f * Float.MIN_VALUE;
    }

    /**
     * Loads a text file and returns the content as a list of Strings. Each line of the text file will be represented by
     * a <tt>String</tt> in the returned array. Returns <tt>null</tt> if the file is not present or cannot be read.
     *
     * To read a resource file, put ':' in front of the file path.
     *
     * @param filePath path to the file
     * @return lines of the file as an array of <tt>String</tt>
     */
    public static String[] loadTextFile(final String filePath) {
        return Jeda.loadTextFile(filePath);
    }

    /**
     * Returns a random <tt>int</tt> value between <tt>0</tt> and <tt>(max - 1)</tt>.
     *
     * @param max the upper limit for the random number
     * @return random number
     */
    public static int randomInt(final int max) {
        return (int) (Math.random() * max);
    }

    /**
     * Creates and returns a list of String values.
     *
     * @param values comma-separated String values
     * @return a list of String values
     */
    public static List<String> stringList(final String... values) {
        return Arrays.asList(values);
    }

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
            for (Object element : (Object[]) array) {
                result.add(element);
            }
        }

        return result;
    }
}
