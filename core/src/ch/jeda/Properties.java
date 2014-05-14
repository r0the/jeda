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
 */
public final class Properties {

    private static final Set<String> FALSE_STRINGS = initFalseStrings();
    private static final Set<String> TRUE_STRINGS = initTrueStrings();
    private final java.util.Properties imp;

    /**
     * @since 1
     */
    public Properties() {
        this.imp = new java.util.Properties();
    }

    /**
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
     * @since 1
     */
    public void addAll(final Properties properties) {
        this.imp.putAll(properties.imp);
    }

    /**
     * @since 1
     */
    public void clear() {
        this.imp.clear();
    }

    /**
     * @since 1
     */
    public boolean containsKey(final String key) {
        return this.imp.containsKey(key);
    }

    /**
     * @since 1
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        String value = this.getString(key);
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
     * @since 1
     */
    public double getDouble(final String key, final double defaultValue) {
        try {
            return Double.parseDouble(this.getString(key));
        }
        catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * @since 1
     */
    public int getInt(final String key, final int defaultValue) {

        try {
            return Integer.parseInt(this.getString(key));
        }
        catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * @since 1
     */
    public String getString(final String key) {
        return this.imp.getProperty(key);
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
