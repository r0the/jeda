/*
 * Copyright (C) 2012 - 2014 by Stefan Rothe
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a map of keys to values.
 *
 * @since 1
 * @version 2
 */
public final class Properties {

    private static final Set<String> FALSE_STRINGS = initFalseStrings();
    private static final Set<String> TRUE_STRINGS = initTrueStrings();
    private final java.util.Properties imp;

    /**
     * Constructs empty properties.
     *
     * @since 1
     */
    public Properties() {
        this.imp = new java.util.Properties();
    }

    /**
     * Loads properties from the specified file. The file must be a Java properties file.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param filePath the path of the file to read
     *
     * @since 1
     */
    public Properties(final String filePath) {
        this();
        final InputStream in = Jeda.openResource(filePath);
        if (in == null) {
            return;
        }

        try {
            this.imp.load(in);
        }
        catch (final Exception ex) {
            Log.err("jeda.file.error.read", filePath);
        }
        finally {
            try {
                in.close();
            }
            catch (final IOException ex) {
                // ignore
            }
        }
    }

    Properties(final java.util.Properties imp) {
        this.imp = imp;
    }

    /**
     * Adds the specified properties to these properties. Values of existing keys are overwritten by the values of
     * corresponding keys in <tt>properties</tt>. Has no effect if <tt>properties</tt> is <tt>null</tt>.
     *
     * @param properties the properties to be added to these properties
     * @deprecated Use {@link #putAll(ch.jeda.Properties)} instead
     *
     * @since 1
     */
    public void addAll(final Properties properties) {
        if (properties != null) {
            this.imp.putAll(properties.imp);
        }
    }

    /**
     * Removes all keys and values from the properties.
     *
     * @since 1
     */
    public void clear() {
        this.imp.clear();
    }

    /**
     * Checks if the properties contains the specified key. Returns <tt>false</tt> if <tt>key</tt> is <tt>null</tt>.
     *
     * @param key the key to be checked for
     * @return <tt>true</tt> if the specified is present, <tt>false</tt> otherwise
     *
     * @since 1
     */
    public boolean containsKey(final String key) {
        if (key == null) {
            return false;
        }
        else {
            return this.imp.containsKey(key);
        }
    }

    /**
     * Returns the value associated with the specified key as <tt>boolean</tt>. Returns <dd>defaultValue</tt> if the
     * specified key is not present or is <tt>null</tt> or if the value associated with the key cannot be converted to a
     * <tt>boolean</tt>. Valid <tt>boolean</tt> values are (case insensitive):
     * <ul>
     * <li>For <tt>true</tt>: <tt>"1"</tt>, <tt>"on"</tt>, <tt>"true"</tt>, <tt>"yes"</tt>,
     * <li>For <tt>false</tt>: <tt>"0"</tt>, <tt>"off"</tt>, <tt>"false"</tt>, <tt>"no"</tt>,
     * </ul>
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <tt>boolean</tt>
     *
     * @since 1
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        if (!this.imp.containsKey(key)) {
            return defaultValue;
        }

        String value = this.imp.getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        value = value.toLowerCase();
        if (TRUE_STRINGS.contains(value)) {
            return true;
        }

        if (FALSE_STRINGS.contains(value)) {
            return false;
        }

        return defaultValue;
    }

    /**
     * Returns the value associated with the specified key as <tt>double</tt>. Returns <dd>defaultValue</tt> if the
     * specified key is not present or is <tt>null</tt> or if the value associated with the key cannot be converted to a
     * <tt>double</tt>.
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <tt>double</tt>
     *
     * @since 1
     */
    public double getDouble(final String key, final double defaultValue) {
        if (!this.imp.containsKey(key)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(this.imp.getProperty(key));
        }
        catch (final Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the specified key as <tt>int</tt>. Returns <dd>defaultValue</tt> if the
     * specified key is not present or is <tt>null</tt> or if the value associated with the key cannot be converted to
     * an <tt>int</tt>.
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <tt>double</tt>
     *
     * @since 1
     */
    public int getInt(final String key, final int defaultValue) {
        if (!this.imp.containsKey(key)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(this.imp.getProperty(key));
        }
        catch (final Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the specified key as <tt>String</tt>. Returns <dd>null</tt> if the specified
     * key is not present or is <tt>null</tt>.
     *
     * @param key the key of the required value
     * @return the value associated with the key as <tt>String</tt>
     *
     * @since 1
     */
    public String getString(final String key) {
        if (this.imp.containsKey(key)) {
            return this.imp.getProperty(key);
        }
        else {
            return null;
        }
    }

    /**
     * Returns the value associated with the specified key as <tt>String</tt>. Returns <dd>defaultValue</tt> if the
     * specified key is not present or is <tt>null</tt>.
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <tt>String</tt>
     *
     * @since 2
     */
    public String getString(final String key, final String defaultValue) {
        if (this.imp.containsKey(key)) {
            return this.imp.getProperty(key);
        }
        else {
            return defaultValue;
        }
    }

    /**
     * @since 1
     */
    public Set<String> keys() {
        final Set<String> result = new TreeSet<String>();
        for (Object key : this.imp.keySet()) {
            result.add(key.toString());
        }

        return result;
    }

    /**
     * Associate the specified key with the specified value.
     *
     * @since 2
     */
    public void put(final String key, final boolean value) {
        this.imp.put(key, String.valueOf(value));
    }

    /**
     * Associate the specified key with the specified value.
     *
     * @since 2
     */
    public void put(final String key, final double value) {
        this.imp.put(key, String.valueOf(value));
    }

    /**
     * Associate the specified key with the specified value.
     *
     * @since 2
     */
    public void put(final String key, final int value) {
        this.imp.put(key, String.valueOf(value));
    }

    /**
     * Associate the specified key with the specified value.
     *
     * @since 2
     */
    public void put(final String key, final String value) {
        this.imp.put(key, value);
    }

    /**
     * Adds the specified properties to these properties. Values of existing keys are overwritten by the values of
     * corresponding keys in <tt>properties</tt>. Has no effect if <tt>properties</tt> is <tt>null</tt>.
     *
     * @param properties the properties to be added to these properties
     *
     * @since 2
     */
    public void putAll(final Properties properties) {
        if (properties != null) {
            this.imp.putAll(properties.imp);
        }
    }

    /**
     * @since 1
     */
    public Set<String> sections() {
        final Set<String> result = new TreeSet<String>();
        for (String key : this.keys()) {
            final int pos = key.indexOf('.');
            if (pos != -1) {
                result.add(key.substring(0, pos));
            }
        }

        return result;
    }

    /**
     * @since 1
     */
    public Properties section(final String prefix) {
        final int len = prefix.length() + 1;
        final Properties result = new Properties();
        for (String key : this.keys()) {
            if (key.startsWith(prefix)) {
                result.imp.put(key.substring(len), this.imp.get(key));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (String key : this.keys()) {
            result.append(key);
            result.append('=');
            result.append(this.imp.get(key));
            result.append('\n');
        }

        return result.toString();
    }

    private static Set<String> initFalseStrings() {
        final Set<String> result = new HashSet<String>();
        result.add("0");
        result.add("off");
        result.add("no");
        result.add("false");
        return result;
    }

    private static Set<String> initTrueStrings() {
        final Set<String> result = new HashSet<String>();
        result.add("1");
        result.add("on");
        result.add("yes");
        result.add("true");
        return result;
    }
}
