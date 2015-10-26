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
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Image;

/**
 * Represents a Tiled tile.
 *
 * @since 2.0
 */
public abstract class Tile {

    protected Tile() {
    }

    /**
     * Returns the height of this tile in meters.
     *
     * @return the height of this tile in meters
     *
     * @since 2.0
     */
    public abstract float getHeight();

    /**
     * Returns the image representing this tile.
     *
     * @return the image representing this tile
     *
     * @since 2.0
     */
    public abstract Image getImage();

    /**
     * Retursn the properties of this tile.
     *
     * @return the properties of this tile
     *
     * @since 2.0
     */
    public abstract Data getProperties();

    /**
     * Returns the collision shapes of this tile.
     *
     * @return the collision shapes of this tile
     */
    public abstract Shape[] getShapes();

    /**
     * Returns the terrain at the bottom left corner of this tile.
     *
     * @return the terrain at the bottom left corner of this tile
     *
     * @since 2.0
     */
    public abstract Terrain getTerrainBottomLeft();

    /**
     * Returns the terrain at the bottom right corner of this tile.
     *
     * @return the terrain at the bottom right corner of this tile
     *
     * @since 2.0
     */
    public abstract Terrain getTerrainBottomRight();

    /**
     * Returns the terrain at the top left corner of this tile.
     *
     * @return the terrain at the top left corner of this tile
     *
     * @since 2.0
     */
    public abstract Terrain getTerrainTopLeft();

    /**
     * Returns the terrain at the top right corner of this tile.
     *
     * @return the terrain at the top right corner of this tile
     *
     * @since 2.0
     */
    public abstract Terrain getTerrainTopRight();

    /**
     * Returns the width of this tile in meters.
     *
     * @return the width of this tile in meters
     *
     * @since 2.0
     */
    public abstract float getWidth();

    /**
     * Calculates the horizontal screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileX the horizontal tile coordinate of the tile
     * @return the horizontal screen coordinate of the center of this tile
     */
    abstract int screenX(int tileX);

    /**
     * Calculates the vertical screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileY the vertical tile coordinate of the tile
     * @return the vertical screen coordinate of the center of this tile
     */
    abstract int screenY(int tileY);
}
