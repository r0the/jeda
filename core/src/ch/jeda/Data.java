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

import ch.jeda.event.Key;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 * Stores named values of different types. Each value is identified by a unique {@link java.lang.String} called
 * <i>name</i>. Data objects can be used to convert data of different types to a String to be stored or transmitted over
 * a network.
 *
 * @since 1.2
 * @version 2
 */
public final class Data {

    private static final boolean DEFAULT_BOOLEAN = false;
    private static final double DEFAULT_DOUBLE = 0.0;
    private static final int DEFAULT_INT = 0;
    private static final Key DEFAULT_KEY = Key.UNDEFINED;
    private static final String DEFAULT_STRING = null;
    private static final char NEW_LINE = 0x0085;
    private static final char LINE_SEPARATOR = 0x2028;
    private static final char PARAGRAPH_SEPARATOR = 0x2029;
    private final DataImp imp;

    /**
     * Constructs an empty data object.
     *
     * @since 1.2
     */
    public Data() {
        this(new DefaultDataImp());
    }

    /**
     * Construct a data object from a string.
     *
     * @param string the string
     *
     * @since 1.3
     */
    public Data(final String string) {
        this();
        if (string != null) {
            String[] entries = string.split(",");
            for (final String entry : entries) {
                String[] parts = entry.split("=");
                if (parts.length == 2) {
                    this.imp.write(unescape(parts[0]), unescape(parts[1]));
                }
            }
        }
    }

    /**
     * Constructs a data object from a map of strings.
     *
     * @param map the map of strings
     *
     * @since 1.6
     */
    public Data(final Map<String, String> map) {
        this(new DefaultDataImp(map));
    }

    Data(final DataImp imp) {
        this.imp = imp;
    }

    /**
     * Clears all values in the data object. After a call of this method, the data object is empty.
     *
     * @since 1.2
     */
    public void clear() {
        this.imp.clear();
    }

    /**
     * Returns a set of all names of values stores in this data object.
     *
     * @return set of all names
     *
     * @since 1.2
     */
    public String[] getNames() {
        final Collection<String> names = this.imp.getNames();
        return names.toArray(new String[names.size()]);
    }

    /**
     * Checks if there is a value stored for the specified name.
     *
     * @param name the name to check
     * @return <tt>true</tt> if there is a value stored for the specified name, otherwise <tt>false</tt>
     *
     * @since 1.2
     */
    public boolean hasValue(final String name) {
        return this.imp.hasValue(name);
    }

    /**
     * Checks if the data object is empty.
     *
     * @return <tt>true</tt> if the data object is empty, otherwise <tt>false</tt>
     *
     * @since 1.2
     */
    public boolean isEmpty() {
        return this.imp.isEmpty();
    }

    /**
     * Returns the value associated with the specified name as a <tt>boolean</tt>. Returns the <tt>false</tt>, if there
     * is no valid <tt>boolean</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <tt>boolean</tt> value associated with the name or <tt>false</tt>
     *
     * @since 1.2
     */
    public boolean readBoolean(final String name) {
        return this.readBoolean(name, DEFAULT_BOOLEAN);
    }

    /**
     * Returns the value associated with the specified name as a <tt>boolean</tt>. Returns <tt>defaultValue</tt>, if
     * there is no valid <tt>boolean</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <tt>boolean</tt> value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public boolean readBoolean(final String name, final boolean defaultValue) {
        if (!this.imp.hasValue(name)) {
            return defaultValue;
        }
        else {
            return Convert.toBoolean(this.imp.read(name), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public boolean[] readBooleans(final String name) {
        return this.readBooleans(name, DEFAULT_BOOLEAN);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public boolean[] readBooleans(final String name, final boolean defaultValue) {
        final boolean[] result = new boolean[this.readInt(arrayLengthName(name), 0)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.readBoolean(arrayItemName(name, i), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as a <tt>double</tt>. Returns <tt>0.0</tt>, if there is no
     * valid <tt>double</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <tt>double</tt> value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public double readDouble(final String name) {
        return this.readDouble(name, DEFAULT_DOUBLE);
    }

    /**
     * Returns the value associated with the specified name as a <tt>double</tt>. Returns <tt>defaultValue</tt>, if
     * there is no valid <tt>double</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <tt>double</tt> value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public double readDouble(final String name, final double defaultValue) {
        if (!this.imp.hasValue(name)) {
            return defaultValue;
        }
        else {
            return Convert.toDouble(this.imp.read(name), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public double[] readDoubles(final String name) {
        return this.readDoubles(name, DEFAULT_DOUBLE);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public double[] readDoubles(final String name, final double defaultValue) {
        final double[] result = new double[this.readInt(arrayLengthName(name), 0)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.readDouble(arrayItemName(name, i), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as an <tt>int</tt>. Returns <tt>0</tt>, if there is no valid
     * <tt>int</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <tt>int</tt> value associated with the name or <tt>0</tt>
     *
     * @since 1.2
     */
    public int readInt(final String name) {
        return this.readInt(name, DEFAULT_INT);
    }

    /**
     * Returns the value associated with the specified name as an <tt>int</tt>. Returns <tt>defaultValue</tt>, if there
     * is no valid <tt>int</tt> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <tt>int</tt> value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public int readInt(final String name, final int defaultValue) {
        if (!this.imp.hasValue(name)) {
            return defaultValue;
        }
        else {
            return Convert.toInt(this.imp.read(name), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public int[] readInts(final String name) {
        return this.readInts(name, DEFAULT_INT);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public int[] readInts(final String name, final int defaultValue) {
        final int[] result = new int[this.readInt(arrayLengthName(name), 0)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.readInt(arrayItemName(name, i), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as a {@link ch.jeda.event.Key}. Returns
     * {@link ch.jeda.event.Key#UNDEFINED}, if there is no valid {@link ch.jeda.event.Key} value associated with the
     * name.
     *
     * @param name the name of the value to retrieve
     * @return the {@link ch.jeda.event.Key} value associated with the name or {@link ch.jeda.event.Key#UNDEFINED}
     *
     * @since 1.2
     */
    public Key readKey(final String name) {
        return this.readKey(name, DEFAULT_KEY);
    }

    /**
     * Returns the value associated with the specified name as a {@link ch.jeda.event.Key}. Returns
     * <tt>defaultValue</tt>, if there is no valid {@link ch.jeda.event.Key} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the {@link ch.jeda.event.Key} value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public Key readKey(final String name, final Key defaultValue) {
        if (!this.imp.hasValue(name)) {
            return defaultValue;
        }

        try {
            return Key.valueOf(this.imp.read(name));
        }
        catch (final IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public Key[] readKeys(final String name) {
        return this.readKeys(name, DEFAULT_KEY);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public Key[] readKeys(final String name, final Key defaultValue) {
        final Key[] result = new Key[this.readInt(arrayLengthName(name), 0)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.readKey(arrayItemName(name, i), defaultValue);
        }

        return result;
    }

    /**
     * Reads an {@link ch.jeda.Storable} object with the specified name. Creates a new object and calls the object's
     * {@link ch.jeda.Storable#readFrom(ch.jeda.Data)} method. Returns <tt>null</tt>, there is no valid object
     * associated with the name.
     *
     * @param <T> the class of the object to return
     * @param name the name of the object to retrieve
     * @return the {@link ch.jeda.Storable} object associated with the name or <tt>null</tt>
     *
     * @since 1.2
     */
    public <T extends Storable> T readObject(final String name) {
        return this.readObject(name, null);
    }

    /**
     * Reads an {@link ch.jeda.Storable} object with the specified name. Creates a new object and calls the object's
     * {@link ch.jeda.Storable#readFrom(ch.jeda.Data)} method. Returns <tt>defaultValue</tt>, there is no valid object
     * associated with the name.
     *
     * @param <T> the class of the object to return
     * @param name the name of the object to retrieve
     * @param defaultValue the default value
     * @return the {@link ch.jeda.Storable} object associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    public <T extends Storable> T readObject(final String name, final T defaultValue) {
        final String className = this.readString(name);
        try {
            final Class clazz = Class.forName(className);
            final Constructor ctor = clazz.getConstructor(Data.class);
            ctor.setAccessible(true);
            return (T) ctor.newInstance(this.subData(prefix(name)));
        }
        catch (final NoSuchMethodException ex) {
            Log.err(Message.DATA_ERROR_CONSTRUCTOR_NOT_FOUND, className);
        }
        catch (final ClassNotFoundException ex) {
            Log.err(Message.DATA_ERROR_CLASS_NOT_FOUND, className);
        }
        catch (final InstantiationException ex) {
            Log.err(Message.DATA_ERROR_INSTANTIATION, className);
        }
        catch (final IllegalAccessException ex) {
            Log.err(Message.DATA_ERROR_ACCESS, className);
        }
        catch (final ExceptionInInitializerError ex) {
            Log.err(ex.getCause(), Message.DATA_ERROR_CLASS_INITIALIZER, className);
        }
        catch (final InvocationTargetException ex) {
            Log.err(ex.getCause(), Message.DATA_ERROR_CONSTRUCTOR, className);
        }

        return defaultValue;
    }

    /**
     * Returns the value associated with the specified name as a {@link java.lang.String}. Returns <tt>null</tt>, if
     * there is no valid {@link java.lang.String} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the {@link java.lang.String} value associated with the name or <tt>null</tt>
     *
     * @since 1.2
     */
    public String readString(final String name) {
        return this.readString(name, DEFAULT_STRING);
    }

    /**
     * Returns the value associated with the specified name as a {@link java.lang.String}. Returns
     * <tt>defaultValue</tt>, if there is no valid {@link java.lang.String} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the {@link java.lang.String} value associated with the name or <tt>defaultValue</tt>
     *
     * @since 1.2
     */
    public String readString(final String name, final String defaultValue) {
        if (!this.imp.hasValue(name)) {
            return defaultValue;
        }

        return this.imp.read(name);
    }

    /**
     * Returns the array associated with the specified name . Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public String[] readStrings(final String name) {
        return this.readStrings(name, DEFAULT_STRING);
    }

    /**
     * Returns the array associated with the specified name . Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public String[] readStrings(final String name, final String defaultValue) {
        final String[] result = new String[this.readInt(arrayLengthName(name), 0)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.readString(arrayItemName(name, i), defaultValue);
        }

        return result;
    }

    /**
     * Removes a value associated with the specified name from the data object. Also removes all values stored under a
     * name starting with <tt>name + '.'</tt>.
     *
     * @param name the name of the value to remove
     *
     * @since 1.2
     */
    public void remove(final String name) {
        this.imp.remove(name);
        final String prefix = prefix(name);
        for (final String candidate : this.imp.getNames()) {
            if (candidate.startsWith(prefix)) {
                this.imp.remove(candidate);
            }
        }
    }

    /**
     * Serializes the data to a line of text.
     *
     * @return the serialized data as a line of text
     *
     * @since 1.3
     */
    public String toLine() {
        final StringBuilder result = new StringBuilder();
        for (final String name : this.imp.getNames()) {
            if (result.length() > 0) {
                result.append(',');
            }

            result.append(escape(name));
            result.append('=');
            result.append(escape(this.imp.read(name)));
        }

        return result.toString();
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeBoolean(final String name, final boolean value) {
        this.writeString(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeBooleans(final String name, final boolean[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeBoolean(arrayItemName(name, i), values[i]);
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeDouble(final String name, final double value) {
        this.writeString(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeDoubles(final String name, final double[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeDouble(arrayItemName(name, i), values[i]);
        }

    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeInt(final String name, final int value) {
        this.writeString(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeInts(final String name, final int[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeInt(arrayItemName(name, i), values[i]);
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name. A <tt>null</tt> value is not added to the data. Instead, a previously stored value with the
     * same name is removed.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeKey(final String name, final Key value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        this.remove(name);
        if (value != null) {
            this.writeString(name, value.toString());
        }
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeKeys(final String name, final Key[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeKey(arrayItemName(name, i), values[i]);
        }
    }

    /**
     * Stores a {@link ch.jeda.Storable} object in the data object.
     *
     * @param name the name of the object
     * @param value the {@link ch.jeda.Storable} object to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeObject(final String name, final Storable value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        this.remove(name);
        if (value != null) {
            this.writeString(name, value.getClass().getName());
            value.writeTo(this.subData(prefix(name)));
        }
    }

    /**
     * Stores an array of {@link ch.jeda.Storable} objects in the data object.
     *
     * @param name the name of the array
     * @param values the array of {@link ch.jeda.Storable} objects to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeObjects(final String name, final Storable[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeObject(arrayItemName(name, i), values[i]);
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <tt>name</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeString(final String name, final String value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        this.remove(name);
        if (value != null) {
            this.imp.write(name, value);
        }
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <tt>name</tt> or <tt>values</tt> is <tt>null</tt>
     *
     * @since 1.2
     */
    public void writeStrings(final String name, final String[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        this.remove(name);
        this.writeInt(arrayLengthName(name), values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeString(arrayItemName(name, i), values[i]);
        }
    }

    @Override
    public String toString() {
        return this.toLine();
    }

    private Data subData(final String namePrefix) {
        return new Data(new SubDataImp(this.imp, namePrefix));
    }

    private static String arrayLengthName(final String name) {
        final StringBuilder result = new StringBuilder(name);
        result.append(".length");
        return result.toString();
    }

    private static String arrayItemName(final String name, int index) {
        final StringBuilder result = new StringBuilder(name);
        result.append('.');
        result.append(index);
        return result.toString();
    }

    private static String prefix(final String name) {
        final StringBuilder result = new StringBuilder(name);
        result.append(".");
        return result.toString();
    }

    private static String escape(final String text) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            switch (ch) {
                case '\n':
                    result.append("\\n");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case ',':
                    result.append("\\c");
                    break;
                case '=':
                    result.append("\\e");
                    break;
                case '\\':
                    result.append("\\b");
                    break;
                case NEW_LINE:
                    result.append("\\l");
                    break;
                case LINE_SEPARATOR:
                    result.append("\\s");
                    break;
                case PARAGRAPH_SEPARATOR:
                    result.append("\\p");
                    break;
                default:
                    result.append(ch);
                    break;
            }
        }

        return result.toString();
    }

    private static String unescape(final String text) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            if (ch == '\\' && i < text.length() - 1) {
                ++i;
                final char escapeChar = text.charAt(i);
                switch (escapeChar) {
                    case 'n':
                        result.append("\n");
                        break;
                    case 'f':
                        result.append("\f");
                        break;
                    case 'r':
                        result.append("\r");
                        break;
                    case 'c':
                        result.append(",");
                        break;
                    case 'e':
                        result.append("=");
                        break;
                    case 'b':
                        result.append("\\");
                        break;
                    case 'l':
                        result.append(NEW_LINE);
                        break;
                    case 's':
                        result.append(LINE_SEPARATOR);
                        break;
                    case 'p':
                        result.append(PARAGRAPH_SEPARATOR);
                        break;
                    default:
                        // ignore invalid escape
                        result.append(ch);
                        result.append(escapeChar);
                        break;
                }
            }
            else {
                result.append(ch);
            }
        }

        return result.toString();
    }
}
