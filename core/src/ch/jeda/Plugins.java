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
import java.util.ArrayList;
import java.util.List;

class Plugins {

    private final List<PluginWrapper> list;

    Plugins() {
        this.list = new ArrayList<PluginWrapper>();
    }

    void tryAddPlugin(final Class<?> candidate) {
        if (!Helper.hasInterface(candidate, Plugin.class)) {
            return;
        }

        try {
            final Constructor<Plugin> constructor = ((Class<Plugin>) candidate).getDeclaredConstructor();
            constructor.setAccessible(true);
            final Plugin instance = constructor.newInstance();

            this.list.add(new PluginWrapper(instance));
        }
        catch (final InvocationTargetException ex) {
            Log.err(ex.getCause(), "jeda.plugin.error.exception-in-constructor", candidate);
        }
        catch (final Exception ex) {
            Log.err(ex, "jeda.plugin.error.instantiation-exception", candidate);
        }
    }

    void initialize() {
        for (int i = 0; i < this.list.size(); ++i) {
            try {
                this.list.get(i).initialize();
            }
            catch (final Throwable ex) {
                Log.err(ex, "jeda.plugin.error.init", this.list.get(i));
            }
        }
    }
}
