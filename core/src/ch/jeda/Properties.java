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

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

public class Properties {

    private final java.util.Properties imp;

    Properties() {
        this.imp = new java.util.Properties();
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(this.getString(key));
        }
        catch (Exception ex) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(this.getString(key));
        }
        catch (Exception ex) {
            return defaultValue;
        }
    }

    public String getString(String key) {
        return this.imp.getProperty(key);
    }

    public Set<String> keys() {
        Set<String> result = new TreeSet();
        for (Object key : this.imp.keySet()) {
            result.add(key.toString());
        }

        return result;
    }

    void loadFromResource(String filePath) throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filePath);
        this.imp.load(resource.openStream());
    }

    void loadFromSystem() {
        this.imp.putAll(System.getProperties());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String key : this.keys()) {
            result.append(key);
            result.append('=');
            result.append(this.imp.get(key));
            result.append('\n');
        }

        return result.toString();
    }
}
