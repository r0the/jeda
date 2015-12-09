/*
 * Copyright (C) 2015 by Stefan Rothe
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
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides access to Jeda and system properties.
 *
 * @since 2.6
 */
public class Configuration {

    private static final String JEDA_APPLICATION_PROPERTIES_FILE = "res/jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = "res/jeda/platform.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = "res/jeda/system.properties";
    private static final Set<String> FALSE_STRINGS = initFalseStrings();
    private static final Set<String> TRUE_STRINGS = initTrueStrings();
    private static final java.util.Properties imp = initProperties();
    private static final Properties properties = new Properties(imp);

    /**
     * Returns the value associated with the specified key as <code>boolean</code>. Returns <dd>defaultValue</code> if
     * the specified key is not present or is <code>null</code> or if the value associated with the key cannot be
     * converted to a <code>boolean</code>. Valid <code>boolean</code> values are (case insensitive):
     * <ul>
     * <li>For <code>true</code>: <code>"1"</code>, <code>"on"</code>, <code>"true"</code>, <code>"yes"</code>,
     * <li>For <code>false</code>: <code>"0"</code>, <code>"off"</code>, <code>"false"</code>, <code>"no"</code>,
     * </ul>
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <code>boolean</code>
     *
     * @since 2.6
     */
    public static boolean getBoolean(final String key, final boolean defaultValue) {
        if (!imp.containsKey(key)) {
            return defaultValue;
        }

        String value = imp.getProperty(key);
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
     * Returns the value associated with the specified key as <code>int</code>. Returns <dd>defaultValue</code> if the
     * specified key is not present or is <code>null</code> or if the value associated with the key cannot be converted
     * to an <code>int</code>.
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <code>double</code>
     *
     * @since 2.6
     */
    public static int getInt(final String key, final int defaultValue) {
        if (!imp.containsKey(key)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(imp.getProperty(key));
        }
        catch (final Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with the specified key as <code>String</code>. Returns <code>defaultValue</code> if
     * the specified key is not present or is <code>null</code>.
     *
     * @param key the key of the required value
     * @param defaultValue the value to return when the key is not present
     * @return the value associated with the key as <code>String</code>
     *
     * @since 2.6
     */
    public static String getString(final String key, final String defaultValue) {
        if (imp.containsKey(key)) {
            return imp.getProperty(key);
        }
        else {
            return defaultValue;
        }
    }

    public static Properties getProperties() {
        return properties;
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

    private static java.util.Properties initProperties() {
        final java.util.Properties result = new java.util.Properties();
        loadProperties(result, JEDA_SYSTEM_PROPERTIES_FILE);
        loadProperties(result, JEDA_PLATFORM_PROPERTIES_FILE);
        loadProperties(result, JEDA_APPLICATION_PROPERTIES_FILE);
        result.putAll(System.getProperties());
        return result;
    }

    private static void loadProperties(final java.util.Properties properties, final String path) {
        final URL url = JedaEngine.class.getClassLoader().getResource(path);
        if (url == null) {
            System.err.format(Message.get(Message.ENGINE_ERROR_PROPERTIES_NOT_FOUND), path);
            return;
        }

        InputStream in = null;
        try {
            in = url.openStream();
            properties.load(in);
        }
        catch (final Exception ex) {
            System.err.format(Message.get(Message.ENGINE_ERROR_PROPERTIES_READ), path);
            return;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (final IOException ex) {
                    // ignore
                }
            }
        }
    }
}
