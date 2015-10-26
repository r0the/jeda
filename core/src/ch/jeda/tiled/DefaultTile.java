/*
 * Copyright (C) 2015 by Stefan Rothe
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

class DefaultTile extends Tile {

    private final float height;
    private final int id;
    private final Image image;
    private final Data properties;
    private final Shape[] shapes;
    private final Terrain[] terrain;
    private final TileSet tileSet;
    private final float width;

    DefaultTile(final TiledMap map, final TileSet tileSet, final int id, final Image image,
                final ElementWrapper element) {
        height = (float) image.getHeight() / map.getTileHeight();
        width = (float) image.getWidth() / map.getTileWidth();
        this.id = id;
        this.image = image;
        this.tileSet = tileSet;
        terrain = Parser.parseTerrain(tileSet, element);
        shapes = Parser.parseShapes(element, map, -width / 2f, height / 2f);
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

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public Data getProperties() {
        return properties;
    }

    @Override
    public Shape[] getShapes() {
        return Arrays.copyOf(shapes, shapes.length);
    }

    @Override
    public Terrain getTerrainBottomLeft() {
        return terrain[2];
    }

    @Override
    public Terrain getTerrainBottomRight() {
        return terrain[3];
    }

    @Override
    public Terrain getTerrainTopLeft() {
        return terrain[0];
    }

    @Override
    public Terrain getTerrainTopRight() {
        return terrain[1];
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    final int screenX(int tileX) {
        return tileX * tileSet.getMap().getTileWidth() + tileSet.getTileWidth() / 2 +
               tileSet.getTileOffsetX();
    }

    @Override
    final int screenY(int tileY) {
        return (tileY + 1) * tileSet.getMap().getTileHeight() - tileSet.getTileHeight() / 2 +
               tileSet.getTileOffsetY();
    }
}
