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
package ch.jeda.tmx;

import ch.jeda.Data;

/**
 * Represents a TMX terrain.
 *
 * @since 1.6
 */
public final class TmxTerrain {

    private final String name;
    private final Data properties;

    TmxTerrain(final Element element) {
        this.name = element.getStringAttribute(Const.NAME);
        this.properties = element.parsePropertiesChild();
    }

    /**
     * Returns the name of the terrain.
     *
     * @return the name of the terrain
     *
     * @since 1.6
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the properties of the terrain.
     *
     * @return the properties of the terrain
     *
     * @since 1.6
     */
    public Data getProperties() {
        return this.properties;
    }
}
