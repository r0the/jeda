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
import ch.jeda.ui.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TmxTileSet {

    private final int firstGlobalId;
    private final Image image;
    private final TmxMap map;
    private final String name;
    private final Data properties;
    private final TmxTerrain[] terrainTypes;
    private final int tileHeight;
    private final int tileOffsetX;
    private final int tileOffsetY;
    private final List<TmxTile> tiles;
    private final int tileWidth;

    TmxTileSet(final TmxMap map, final Element element, final XmlReader reader) {
        this.firstGlobalId = element.getIntAttribute(Const.FIRSTGID);
        this.map = map;
        this.name = element.getStringAttribute(Const.NAME);
        this.tileHeight = element.getIntAttribute(Const.TILEHEIGHT);
        this.tileWidth = element.getIntAttribute(Const.TILEWIDTH);

        // Read tile offset
        final Element tileOffsetElement = element.getChild("tileoffset");
        if (tileOffsetElement == null) {
            this.tileOffsetX = 0;
            this.tileOffsetY = 0;
        }
        else {
            this.tileOffsetX = tileOffsetElement.getIntAttribute(Const.X);
            this.tileOffsetY = tileOffsetElement.getIntAttribute(Const.Y);
        }

        // Read properties
        this.properties = element.parsePropertiesChild();
        // Read image
        this.image = reader.loadImageChild(element);

        // Read terrain types
        List<TmxTerrain> terrainTypesList = new ArrayList<TmxTerrain>();
        final Element terrainTypesElement = element.getChild(Const.TERRAINTYPES);
        if (terrainTypesElement != null) {
            for (final Element terrainElement : terrainTypesElement.getChildren(Const.TERRAIN)) {
                terrainTypesList.add(new TmxTerrain(terrainElement));
            }
        }

        this.terrainTypes = terrainTypesList.toArray(new TmxTerrain[terrainTypesList.size()]);
        // Read additional tile information
        final Map<Integer, Element> tileElements = new HashMap<Integer, Element>();
        for (final Element tileElement : element.getChildren(Const.TILE)) {
            final int id = tileElement.getIntAttribute(Const.ID);
            tileElements.put(id, tileElement);
        }

        // Create all tiles
        this.tiles = new ArrayList<TmxTile>();

        final int spacing = element.getIntAttribute(Const.SPACING);
        final int margin = element.getIntAttribute(Const.MARGIN);

        int nextX = margin;
        int nextY = margin;

        while (nextY + this.tileHeight + margin <= this.image.getHeight()) {
            final Image tileImage = this.image.subImage(nextX, nextY, this.tileWidth, this.tileHeight);
            final int tileId = tiles.size();
            tiles.add(new TmxTile(map, this, tileId, tileImage, tileElements.get(tileId)));
            nextX += this.tileWidth + spacing;
            if (nextX + this.tileWidth + margin > this.image.getWidth()) {
                nextX = margin;
                nextY += this.tileHeight + spacing;
            }
        }
    }

    public int getFirstGlobalId() {
        return this.firstGlobalId;
    }

    public TmxMap getMap() {
        return this.map;
    }

    public String getName() {
        return this.name;
    }

    public final Data getProperties() {
        return this.properties;
    }

    public TmxTerrain[] getTerrains() {
        return Arrays.copyOf(this.terrainTypes, this.terrainTypes.length);
    }

    public TmxTile getTile(final int index) {
        if (0 <= index && index < this.tiles.size()) {
            return this.tiles.get(index);
        }
        else {
            return null;
        }
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public int getTileOffsetX() {
        return this.tileOffsetX;
    }

    public int getTileOffsetY() {
        return this.tileOffsetY;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public TmxTerrain lookupTerrain(final int index) {
        if (0 <= index && index < this.terrainTypes.length) {
            return this.terrainTypes[index];
        }
        else {
            return null;
        }
    }
}
