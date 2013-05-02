/*
 * Copyright (C) 2013 by Stefan Rothe
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class PluginWrapper {

    private final Constructor<Plugin> constructor;
    private Plugin instance;

    @Override
    public String toString() {
        return this.constructor.getDeclaringClass().getName();
    }

    static PluginWrapper tryCreate(final Class<?> candidate) {
        final Class[] interfaces = candidate.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            if (interfaces[i].equals(Plugin.class)) {
                try {
                    final Constructor<Plugin> constructor =
                            ((Class<Plugin>) candidate).getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return new PluginWrapper(constructor);
                }
                catch (NoSuchMethodException ex) {
                    return null;
                }
                catch (SecurityException ex) {
                    return null;
                }
            }
        }

        return null;
    }

    void initialize() throws Throwable {
        try {
            this.instance = this.constructor.newInstance();
            this.instance.initialize();
        }
        catch (InvocationTargetException ex) {
            throw (Exception) ex.getCause();
        }
    }

    private PluginWrapper(final Constructor<Plugin> constructor) {
        this.constructor = constructor;
    }
}
