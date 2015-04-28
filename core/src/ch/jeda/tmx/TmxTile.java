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
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a TMX tile.
 *
 * @since 2.0
 */
public class TmxTile {

    private final int id;
    private final Image image;
    private final TmxObject[] objects;
    private final Data properties;
    private final TmxTerrain[] terrain;
    private final TmxTileSet tileSet;

    TmxTile(final TmxMap map, final TmxTileSet tileSet, final int id, final Image image, final Element element) {
        this.id = id;
        this.image = image;
        this.terrain = new TmxTerrain[4];
        this.tileSet = tileSet;
        if (element != null) {
            // Read properties
            this.properties = element.parsePropertiesChild();
            // Read terrain info
            final String terrainInfo = element.getStringAttribute(Const.TERRAIN);
            if (terrainInfo != null) {
                final String[] parts = terrainInfo.split(",");
                int i = 0;
                while (i < 4 && i < parts.length) {
                    try {
                        this.terrain[i] = tileSet.lookupTerrain(Integer.parseInt(parts[i]));
                    }
                    catch (final NumberFormatException ex) {
                        // ignore
                    }

                    ++i;
                }
            }
        }
        else {
            this.properties = new Data();
        }

        // Read object group
        final List<TmxObject> objectList = new ArrayList<TmxObject>();
        if (element != null) {
            final Element objectGroupElement = element.getChild(Const.OBJECTGROUP);
            if (objectGroupElement != null) {
                for (final Element objectElement : objectGroupElement.getChildren(Const.OBJECT)) {
                    objectList.add(new TmxObject(map, objectElement));
                }
            }
        }

        this.objects = objectList.toArray(new TmxObject[objectList.size()]);

        // Read animation
        if (element != null) {
            final Element animationElement = element.getChild(Const.ANIMATION);
            if (animationElement != null) {
                for (final Element frameElement : animationElement.getChildren(Const.FRAME)) {

                }
            }
        }
    }

    /**
     * Draws this tile at the specified position.
     *
     * @param canvas
     * @param x
     * @param y
     * @param alpha
     *
     * @since 2.0
     */
    public final void draw(final Canvas canvas, final int x, final int y, final int alpha) {
        canvas.drawImage(x, y, this.getImage(), alpha, Alignment.BOTTOM_LEFT);
        canvas.setColor(Color.RED);
        canvas.setLineWidth(3);
        for (int i = 0; i < this.objects.length; ++i) {
            this.objects[i].draw(canvas, x, y - this.tileSet.getTileHeight());
        }
    }

    /**
     * Returns the height of this tile in pixels.
     *
     * @return the height of this tile in pixels
     *
     * @since 2.0
     */
    public final int getHeight() {
        return this.tileSet.getTileHeight();
    }

    public final int getId() {
        return this.id;
    }

    /**
     * Returns the image representing this tile.
     *
     * @return the image representing this tile
     *
     * @since 2.0
     */
    public final Image getImage() {
        return this.image;
    }

    public TmxObject[] getObjects() {
        return Arrays.copyOf(this.objects, this.objects.length);
    }

    public final int getOffsetX() {
        return this.tileSet.getTileOffsetX();
    }

    public final int getOffsetY() {
        return this.tileSet.getTileOffsetX();
    }

    public final Data getProperties() {
        return this.properties;
    }

    public final TmxTerrain getTerrainBottomLeft() {
        return this.terrain[2];
    }

    public final TmxTerrain getTerrainBottomRight() {
        return this.terrain[3];
    }

    public final TmxTerrain getTerrainTopLeft() {
        return this.terrain[0];
    }

    public final TmxTerrain getTerrainTopRight() {
        return this.terrain[1];
    }

    public final int getWidth() {
        return this.tileSet.getTileWidth();
    }

    /**
     * Calculates the horizontal screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileX the horizontal tile coordinate of the tile
     * @return the horizontal screen coordinate of the center of this tile
     */
    public final int screenX(int tileX) {
        return tileX * this.tileSet.getMap().getTileWidth() + this.tileSet.getTileWidth() / 2 +
               this.tileSet.getTileOffsetX();
    }

    /**
     * Calculates the vertical screen coordinate of the center of this tile from a given tile coordinate.
     *
     * @param tileY the vertical tile coordinate of the tile
     * @return the vertical screen coordinate of the center of this tile
     */
    public final int screenY(int tileY) {
        return (tileY + 1) * this.tileSet.getMap().getTileHeight() - this.tileSet.getTileHeight() / 2 +
               this.tileSet.getTileOffsetY();
    }
}
