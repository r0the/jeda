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
package ch.jeda.tmx;

import ch.jeda.Data;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a TMX map.
 *
 * @since 2.0
 */
public final class TmxMap {

    private final Color backgroundColor;
    private final int height;
    private final TmxLayer[] layers;
    private final TmxMapOrientation orientation;
    private final Data properties;
    private final int tileHeight;
    private final TmxTileSets tileSets;
    private final int tileWidth;
    private final int width;

    /**
     * Constructs a new TMX map from the specified file.
     *
     * @param path the path to the TMX map file.
     *
     * @since 2.0
     */
    public TmxMap(String path) {
        if (path == null) {
            throw new NullPointerException("path");
        }

        final StringBuilder prefix = new StringBuilder();
        if (path.startsWith("res:")) {
            prefix.append("res:");
            path = path.substring(4);
        }

        final int index = path.lastIndexOf('/');
        if (index != -1) {
            prefix.append(path.substring(0, index + 1));
            path = path.substring(index + 1);
        }

        final XmlReader reader = new XmlReader(prefix.toString());
        final Element element = reader.read(path);
        this.backgroundColor = element.getColorAttribute(Const.BACKGROUNDCOLOR, Color.WHITE);
        this.height = element.getIntAttribute(Const.HEIGHT);
        this.tileHeight = element.getIntAttribute(Const.TILEHEIGHT);
        this.tileWidth = element.getIntAttribute(Const.TILEWIDTH);
        this.width = element.getIntAttribute(Const.WIDTH);
        this.orientation = parseOrientation(element.getStringAttribute(Const.ORIENTATION));
        this.properties = element.parsePropertiesChild();
        // Read tile sets
        this.tileSets = new TmxTileSets();
        for (final Element tileSetElement : element.getChildren(Const.TILESET)) {
            if (tileSetElement.hasAttribute(Const.SOURCE)) {
                this.tileSets.add(new TmxTileSet(this, reader.read(tileSetElement.getStringAttribute(Const.SOURCE)), reader));
            }
            else {
                this.tileSets.add(new TmxTileSet(this, tileSetElement, reader));
            }
        }

        // Read layers
        final List<TmxLayer> layerList = new ArrayList<TmxLayer>();
        for (final Element layerElement : element.getChildren()) {
            if (layerElement.is("layer")) {
                layerList.add(new TmxTileLayer(this, layerElement));
            }
            else if (layerElement.is("objectgroup")) {
                layerList.add(new TmxObjectGroup(this, layerElement));
            }
            else if (layerElement.is("imagelayer")) {
                layerList.add(new TmxImageLayer(this, layerElement, reader));
            }
        }

        this.layers = layerList.toArray(new TmxLayer[layerList.size()]);
    }

    /**
     * Draws this map. The map is drawn on the specified canvas at the specified offset.
     *
     * @param canvas the canvas to draw the map on
     * @param offsetX the horizontal offset
     * @param offsetY the vertical offset
     *
     * @since 2.0
     */
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        canvas.setColor(this.backgroundColor);
        canvas.fill();
        for (int i = 0; i < this.layers.length; ++i) {
            this.layers[i].draw(canvas, offsetX, offsetY);
        }
    }

    /**
     * Returns the background color for the map. Reading the background color from a TMX file is currently not
     * supported. This method always returns {@link ch.jeda.ui.Color#WHITE}.
     *
     * @return the background color for the map
     *
     * @since 2.0
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Returns the height of the map in tiles.
     *
     * @return the height of the map in tiles
     *
     * @since 2.0
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns all layers of this map.
     *
     * @return all layers of this map
     *
     * @since 2.0
     */
    public TmxLayer[] getLayers() {
        return Arrays.copyOf(this.layers, this.layers.length);
    }

    /**
     * Returns the orientation of this map.
     *
     * @return the orientation of this map
     *
     * @since 2.0
     */
    public TmxMapOrientation getOrientation() {
        return this.orientation;
    }

    /**
     * Returns the properties of this map.
     *
     * @return the properties of this map
     *
     * @since 2.0
     */
    public Data getProperties() {
        return this.properties;
    }

    /**
     * Returns the tile height of this map.
     *
     * @return the tile height of this map
     *
     * @since 2.0
     */
    public int getTileHeight() {
        return this.tileHeight;
    }

    /**
     * Returns the tile width of this map.
     *
     * @return the tile width of this map
     *
     * @since 2.0
     */
    public int getTileWidth() {
        return this.tileWidth;
    }

    /**
     * Returns the width of this map in tiles.
     *
     * @return the width of this map in tiles
     *
     * @since 2.0
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns the tile with the specified global tile id. Returns <tt>null</tt> if no such tile exists in this map.
     *
     * @param globalId the global tile id
     * @return the tile matching the id or <tt>null</tt>
     *
     * @since 2.0
     */
    public TmxTile lookupTile(final int globalId) {
        return this.tileSets.lookupTile(globalId);
    }

    private static TmxMapOrientation parseOrientation(final String value) {
        if (value == null) {
            return TmxMapOrientation.ORTHOGONAL;
        }

        try {
            return TmxMapOrientation.valueOf(value.toUpperCase());
        }
        catch (final IllegalArgumentException ex) {
            return TmxMapOrientation.ORTHOGONAL;
        }
    }
}
