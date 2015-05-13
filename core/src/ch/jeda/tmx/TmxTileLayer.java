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

import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.xml.bind.DatatypeConverter;

/**
 * Represents a TMX tile layer.
 *
 * @since 2.0
 */
public final class TmxTileLayer extends TmxLayer {

    private final TmxTile[] tiles;

    TmxTileLayer(final TmxMap map, final Element element) {
        super(map, element);
        // Read tile ids
        final int width = map.getWidth();
        final int height = map.getHeight();
        final int[] tileIds = parseData(element.getChild(Const.DATA), width, height);
        this.tiles = new TmxTile[width * height];
        for (int i = 0; i < tileIds.length; ++i) {
            this.tiles[i] = map.lookupTile(tileIds[i]);
        }
    }

    @Override
    public void addTo(final PhysicsView view) {
        // TODO
    }

    @Override
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        if (!this.isVisible()) {
            return;
        }

        final int alpha = (int) (this.getOpacity() * 255);
        final int tileHeight = this.getMap().getTileHeight();
        final int tileWidth = this.getMap().getTileWidth();
        int screenX = (int) offsetX;
        int screenY = (int) offsetY + tileHeight;
        int startX = 0;
        int startY = 0;
        int endX = this.getMap().getWidth();
        int endY = this.getMap().getHeight();
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                final TmxTile tile = this.getTile(x, y);
                if (tile != null) {
                    tile.draw(canvas, screenX + tile.getOffsetX(),
                              screenY + tile.getOffsetY(), alpha);
                }
                screenY += tileHeight;
            }

            screenY = (int) offsetY + tileHeight;
            screenX += tileWidth;
        }

        drawDebugOverlay(canvas, (int) offsetX, (int) offsetY);
    }

    @Override
    public final TmxLayerType getType() {
        return TmxLayerType.TILE;
    }

    @Override
    public TmxTile getTile(final int x, final int y) {
        final int index = x + y * this.getMap().getWidth();
        if (0 <= index && index < this.tiles.length && this.tiles[index] != null) {
            return this.tiles[index];
        }

        return null;
    }

    private void drawDebugOverlay(final Canvas canvas, final int offsetX, final int offsetY) {
        canvas.setColor(Color.RED);
        final int tileHeight = this.getMap().getTileHeight();
        final int tileWidth = this.getMap().getTileWidth();
        int screenX = offsetX;
        int screenY = offsetY;
        int startX = 0;
        int startY = 0;
        int endX = this.getMap().getWidth();
        int endY = this.getMap().getHeight();
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                final TmxTile tile = this.getTile(x, y);
                if (tile != null && tile.getTerrainTopLeft() != null) {
                    canvas.fillRectangle(screenX, screenY, 5, 5);
                }

                if (tile != null && tile.getTerrainTopRight() != null) {
                    canvas.fillRectangle(screenX + tileWidth - 5, screenY, 5, 5);
                }

                if (tile != null && tile.getTerrainBottomLeft() != null) {
                    canvas.fillRectangle(screenX, screenY + tileHeight - 5, 5, 5);
                }

                if (tile != null && tile.getTerrainBottomRight() != null) {
                    canvas.fillRectangle(screenX + tileWidth - 5, screenY + tileHeight - 5, 5, 5);
                }

                screenY += tileHeight;
            }

            screenY = offsetY;
            screenX += tileWidth;
        }
    }

    private static int[] parseData(final Element element, final int width, final int height) {
        final String encoding = element.getStringAttribute(Const.ENCODING);
        if (Const.BASE64.equalsIgnoreCase(encoding)) {
            return parseBase64(element, width, height);
        }
        else if (Const.CSV.equalsIgnoreCase(encoding)) {
            return parseCsv(element.getContent(), width, height);
        }
        else {
            final int[] result = new int[width * height];
            int i = 0;
            for (final Element tileElement : element.getChildren(Const.TILE)) {
                result[i] = tileElement.getIntAttribute(Const.GID);
                ++i;
            }

            return result;
        }
    }

    private static int[] parseBase64(final Element element, final int width, final int height) {
        final String compression = element.getStringAttribute(Const.COMPRESSION);
        try {
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(element.getContent()));
            if (Const.GZIP.equalsIgnoreCase(compression)) {
                in = new GZIPInputStream(in);
            }
            else if (Const.ZLIB.equalsIgnoreCase(compression)) {
                in = new InflaterInputStream(in);
            }
            else if (compression != null && !compression.isEmpty()) {
                throw new RuntimeException("Unknown compression method: " + compression);
            }

            final int[] result = new int[width * height];
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    int tileId = 0;
                    tileId |= in.read();
                    tileId |= in.read() << 8;
                    tileId |= in.read() << 16;
                    tileId |= in.read() << 24;
                    result[x + y * width] = tileId;
                }
            }

            return result;
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static int[] parseCsv(final String data, final int width, final int height) {
        final String[] csvTileIds = data.trim().split("[\\s]*,[\\s]*");
        if (csvTileIds.length != width * height) {
            throw new RuntimeException("Number of tiles does not match the layer's width and height");
        }

        final int[] result = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                result[x + y * width] = Integer.parseInt(csvTileIds[x + y * width]);
            }
        }

        return result;
    }
}
