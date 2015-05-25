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
import java.util.Arrays;

/**
 * Represents a Tiled tile.
 *
 * @since 2.0
 */
public final class Tile {

    private final float height;
    private final int id;
    private final Image image;
    private final Data properties;
    private final Shape[] shapes;
    private final Terrain[] terrain;
    private final TileSet tileSet;
    private final float width;

    Tile(final TiledMap map, final TileSet tileSet, final int id, final Image image, final ElementWrapper element) {
        height = image.getHeight() / map.getTileHeight();
        width = image.getWidth() / map.getTileWidth();
        this.id = id;
        this.image = image;
        this.tileSet = tileSet;
        terrain = Parser.parseTerrain(tileSet, element);
        shapes = Parser.parseShapes(element, map, -width / 2f, -height / 2f);
        if (element != null) {
            // Read properties
            properties = element.parsePropertiesChild();
        }
        else {
            properties = new Data();
        }

        // Read animation
        if (element != null) {
            final ElementWrapper animationElement = element.getChild(Const.ANIMATION);
            if (animationElement != null) {
                for (final ElementWrapper frameElement : animationElement.getChildren(Const.FRAME)) {

                }
            }
        }
    }

    /**
     * Returns the height of this tile in meters.
     *
     * @return the height of this tile in meters
     *
     * @since 2.0
     */
    public float getHeight() {
        return height;
    }

    /**
     * Returns the image representing this tile.
     *
     * @return the image representing this tile
     *
     * @since 2.0
     */
    public Image getImage() {
        return image;
    }

    /**
     * Retursn the properties of this tile.
     *
     * @return the properties of this tile
     *
     * @since 2.0
     */
    public Data getProperties() {
        return properties;
    }

    /**
     * Returns the collision shapes of this tile.
     *
     * @return the collision shapes of this tile
     */
    public Shape[] getShapes() {
        return Arrays.copyOf(shapes, shapes.length);
    }

    /**
     * Returns the terrain at the bottom left corner of this tile.
     *
     * @return the terrain at the bottom left corner of this tile
     *
     * @since 2.0
     */
    public Terrain getTerrainBottomLeft() {
        return terrain[2];
    }

    /**
     * Returns the terrain at the bottom right corner of this tile.
     *
     * @return the terrain at the bottom right corner of this tile
     *
     * @since 2.0
     */
    public Terrain getTerrainBottomRight() {
        return terrain[3];
    }

    /**
     * Returns the terrain at the top left corner of this tile.
     *
     * @return the terrain at the top left corner of this tile
     *
     * @since 2.0
     */
    public Terrain getTerrainTopLeft() {
        return terrain[0];
    }

    /**
     * Returns the terrain at the top right corner of this tile.
     *
     * @return the terrain at the top right corner of this tile
     *
     * @since 2.0
     */
    public Terrain getTerrainTopRight() {
        return terrain[1];
    }

    /**
     * Returns the width of this tile in meters.
     *
     * @return the width of this tile in meters
     *
     * @since 2.0
     */
    public float getWidth() {
        return width;
    }

    /**
     * Calculates the horizontal screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileX the horizontal tile coordinate of the tile
     * @return the horizontal screen coordinate of the center of this tile
     */
    int screenX(int tileX) {
        return tileX * tileSet.getMap().getTileWidth() + tileSet.getTileWidth() / 2 +
               tileSet.getTileOffsetX();
    }

    /**
     * Calculates the vertical screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileY the vertical tile coordinate of the tile
     * @return the vertical screen coordinate of the center of this tile
     */
    int screenY(int tileY) {
        return (tileY + 1) * tileSet.getMap().getTileHeight() - tileSet.getTileHeight() / 2 +
               tileSet.getTileOffsetY();
    }
}
