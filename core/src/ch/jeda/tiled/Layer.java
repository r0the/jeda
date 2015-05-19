/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.tiled;

import ch.jeda.Data;
import ch.jeda.physics.PhysicsView;

/**
 * Base class for Tiled map layers.
 *
 * @since 2.0
 */
public abstract class Layer {

    private final TiledMap map;
    private final String name;
    private final int opacity;
    private final Data properties;
    private final boolean visible;

    Layer(final TiledMap map, final ElementWrapper element) {
        this.map = map;
        name = element.getStringAttribute(Const.NAME);
        opacity = (int) Math.max(0.0, Math.min(element.getFloatAttribute(Const.OPACITY, 1f) * 255, 255.0));
        properties = element.parsePropertiesChild();
        visible = element.getBooleanAttribute(Const.VISIBLE, true);
    }

    /**
     * Adds the contents of this layer to a physics view. The behavior of this method depends on the layer type.
     *
     * @param view the view to add this layer to
     *
     * @since 2.0
     */
    public abstract void addTo(final PhysicsView view);

    /**
     * Returns the map this layer belongs to.
     *
     * @return the map this layer belongs to
     *
     * @since 2.0
     */
    public final TiledMap getMap() {
        return map;
    }

    /**
     * Returns the name of this layer.
     *
     * @return the name of this layer
     *
     * @since 2.0
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the objects contained in this layer.
     *
     * @return the objects contained in this layer
     *
     * @since 2.0
     */
    public TiledObject[] getObjects() {
        return new TiledObject[0];
    }

    /**
     * Returns the opacity of this layer. The opacity is a number between 0 and 255 where 0 means totally transparent
     * and 255 means totally opaque.
     *
     * @return the opacity of this layer
     *
     * @since 2.0
     */
    public final int getOpacity() {
        return opacity;
    }

    /**
     * Returns the properties of this layer.
     *
     * @return the properties of this layer
     *
     * @since 2.0
     */
    public final Data getProperties() {
        return properties;
    }

    /**
     * Returns the tile at the specified tile coordinates. Returns <code>null</code> if there is no tile at the
     * specified coordinates. This implementation always returns <code>null</code>.
     *
     * @param x the horizontal tile coordinate
     * @param y the vertical tile coordinate
     * @return the tile at the specified tile coordinates or <code>null</code>
     *
     * @since 2.0
     */
    public Tile getTile(final int x, final int y) {
        return null;
    }

    /**
     * Checks if this layer is visible.
     *
     * @return <code>true</code> is this layer is visible, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public final boolean isVisible() {
        return visible;
    }
}
