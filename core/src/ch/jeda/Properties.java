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

public class Properties {

    private final java.util.Properties imp;

    Properties() {
        this.imp = new java.util.Properties();
    }

    public String getString(String key) {
        return this.imp.getProperty(key);
    }

    void loadFromResource(String filePath) throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filePath);
        this.imp.load(resource.openStream());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String key : this.imp.stringPropertyNames()) {
            result.append(key);
            result.append('=');
            result.append(this.imp.get(key));
            result.append('\n');
        }

        return result.toString();
    }
}
