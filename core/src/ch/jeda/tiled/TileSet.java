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
import ch.jeda.ui.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TileSet {

    private final int firstGlobalId;
    private final Image image;
    private final TiledMap map;
    private final String name;
    private final Data properties;
    private final Terrain[] terrainTypes;
    private final int tileHeight;
    private final int tileOffsetX;
    private final int tileOffsetY;
    private final List<Tile> tiles;
    private final int tileWidth;

    TileSet(final TiledMap map, final ElementWrapper element, final XmlReader reader) {
        firstGlobalId = element.getIntAttribute(Const.FIRSTGID);
        this.map = map;
        name = element.getStringAttribute(Const.NAME);
        tileHeight = element.getIntAttribute(Const.TILEHEIGHT);
        tileWidth = element.getIntAttribute(Const.TILEWIDTH);

        // Read tile offset
        final ElementWrapper tileOffsetElement = element.getChild("tileoffset");
        if (tileOffsetElement == null) {
            tileOffsetX = 0;
            tileOffsetY = 0;
        }
        else {
            tileOffsetX = tileOffsetElement.getIntAttribute(Const.X);
            tileOffsetY = tileOffsetElement.getIntAttribute(Const.Y);
        }

        // Read properties
        properties = element.parsePropertiesChild();
        // Read image
        image = reader.loadImageChild(element);

        // Read terrain types
        List<Terrain> terrainTypesList = new ArrayList<Terrain>();
        final ElementWrapper terrainTypesElement = element.getChild(Const.TERRAINTYPES);
        if (terrainTypesElement != null) {
            for (final ElementWrapper terrainElement : terrainTypesElement.getChildren(Const.TERRAIN)) {
                terrainTypesList.add(new Terrain(terrainElement));
            }
        }

        terrainTypes = terrainTypesList.toArray(new Terrain[terrainTypesList.size()]);
        // Read additional tile information
        final Map<Integer, ElementWrapper> tileElements = new HashMap<Integer, ElementWrapper>();
        for (final ElementWrapper tileElement : element.getChildren(Const.TILE)) {
            final int id = tileElement.getIntAttribute(Const.ID);
            tileElements.put(id, tileElement);
        }

        // Create all tiles
        tiles = new ArrayList<Tile>();
        if (image != null) {
            // We are reading a real tile set
            final int spacing = element.getIntAttribute(Const.SPACING);
            final int margin = element.getIntAttribute(Const.MARGIN);

            int nextX = margin;
            int nextY = margin;

            while (nextY + tileHeight + margin <= image.getHeight()) {
                final Image tileImage = image.subImage(nextX, nextY, tileWidth, tileHeight);
                final int tileId = tiles.size();
                tiles.add(new Tile(map, this, tileId, tileImage, tileElements.get(tileId)));
                nextX += tileWidth + spacing;
                if (nextX + tileWidth + margin > image.getWidth()) {
                    nextX = margin;
                    nextY += tileHeight + spacing;
                }
            }
        }
        else {
            // We are reading an image collection
            for (int tileId = 0; tileId < tileElements.size(); ++tileId) {
                if (tileElements.containsKey(tileId)) {
                    final ElementWrapper tileElement = tileElements.get(tileId);
                    final Image tileImage = reader.loadImageChild(tileElement);
                    tiles.add(new Tile(map, this, tileId, tileImage, tileElement));
                }
                else {
                    tiles.add(null);
                }
            }
        }
    }

    public int getFirstGlobalId() {
        return firstGlobalId;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    public final Data getProperties() {
        return properties;
    }

    public Terrain[] getTerrains() {
        return Arrays.copyOf(terrainTypes, terrainTypes.length);
    }

    public Tile getTile(final int index) {
        if (0 <= index && index < tiles.size()) {
            return tiles.get(index);
        }
        else {
            return null;
        }
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileOffsetX() {
        return tileOffsetX;
    }

    public int getTileOffsetY() {
        return tileOffsetY;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public Terrain lookupTerrain(final int index) {
        if (0 <= index && index < terrainTypes.length) {
            return terrainTypes[index];
        }
        else {
            return null;
        }
    }
}
