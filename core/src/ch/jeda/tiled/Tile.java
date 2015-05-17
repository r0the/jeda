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

    private final int id;
    private final Image image;
    private final Data properties;
    private final Shape[] shapes;
    private final Terrain[] terrain;
    private final TileSet tileSet;

    Tile(final TiledMap map, final TileSet tileSet, final int id, final Image image, final ElementWrapper element) {
        this.id = id;
        this.image = image;
        this.tileSet = tileSet;
        terrain = Parser.parseTerrain(tileSet, element);
        shapes = Parser.parseShapes(element);
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

//
//    /**
//     * Draws this tile at the specified position.
//     *
//     * @param canvas the canvas to draw this tile on
//     * @param x
//     * @param y
//     * @param alpha
//     *
//     * @since 2.0
//     */
//    public void draw(final Canvas canvas, final int x, final int y, final int alpha) {
//        canvas.drawImage(x, y, getImage(), alpha, Alignment.BOTTOM_LEFT);
//        canvas.setColor(Color.RED);
//        canvas.setLineWidth(3);
//        for (int i = 0; i < shapes.length; ++i) {
//            shapes[i].draw(canvas, x, y - tileSet.getTileHeight());
//        }
//    }
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
     * Returns the horizontal offset for this tile. The tile offset is defined in the tile set this tile belongs to.
     *
     * @return the horizontal offset for this tile
     *
     * @since 2.0
     */
    public int getOffsetX() {
        return this.tileSet.getTileOffsetX();
    }

    /**
     * Returns the vertical offset for this tile. The tile offset is defined in the tile set this tile belongs to.
     *
     * @return the vertical offset for this tile
     *
     * @since 2.0
     */
    public int getOffsetY() {
        return this.tileSet.getTileOffsetY();
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
