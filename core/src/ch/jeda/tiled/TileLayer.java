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

import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.xml.bind.DatatypeConverter;

/**
 * Represents a Tiled tile layer.
 *
 * @since 2.0
 */
public final class TileLayer extends Layer {

    private final Tile[] tiles;

    TileLayer(final TiledMap map, final ElementWrapper element) {
        super(map, element);
        // Read tile ids
        final int width = map.getWidth();
        final int height = map.getHeight();
        final int[] tileIds = parseData(element.getChild(Const.DATA), width, height);
        tiles = new Tile[width * height];
        for (int i = 0; i < tileIds.length; ++i) {
            tiles[i] = map.lookupTile(tileIds[i]);
        }
    }

    /**
     * Adds the contents of this layer to a physics view. The behavior of this method depends on the following custom
     * properties of the layer:
     * <ul>
     * <li><b>class</b>: The name of a Java class that will represent the tiles of this layer. The default is
     * <code>ch.jeda.physics.Body</code>.
     * <li><b>type</b>: One of the following: <code>background</code>, <code>dynamic</code>, <code>static</code>, or
     * <code>kinematic</code>. The default is <code>background</code>.
     * </ul>
     *
     * @param view the physics view
     *
     * @since 2.0
     */
    @Override
    public void addTo(final PhysicsView view) {
        if (!isVisible()) {
            return;
        }

        final BodyType type = BodyType.parse(getProperties().readString("type"));

        final int tileHeight = getMap().getTileHeight();
        final int tileWidth = getMap().getTileWidth();
        int viewX = tileWidth / 2;
        int viewY = tileHeight / 2;
        int endX = getMap().getWidth();
        int endY = getMap().getHeight();
        for (int x = 0; x < endX; ++x) {
            for (int y = 0; y < endY; ++y) {
                final Tile tile = getTile(x, y);
                if (tile != null) {
                    final Body body = Body.create(getProperties().readString("class"));
                    final Shape[] shapes = tile.getShapes();
                    final Image image = tile.getImage();
                    if (shapes.length > 0) {
                        for (int i = 0; i < shapes.length; ++i) {
                            body.addShape(shapes[i]);
                        }
                    }
                    else {
                        body.addShape(new Rectangle(0.0, 0.0, image.getWidth(), image.getHeight()));
                    }

                    body.setType(type);
                    body.setImage(tile.getImage());
                    body.setOpacity(getOpacity());
                    body.setPosition(viewX + tile.getOffsetX() + (image.getWidth() - tileWidth) / 2.0,
                                     viewY + tile.getOffsetY() - (image.getHeight() - tileHeight) / 2.0);
                    view.add(body);
                }

                viewY += tileHeight;
            }

            viewY = tileHeight / 2;
            viewX += tileWidth;
        }
    }

    @Override
    public Tile getTile(final int x, final int y) {
        final int index = x + y * getMap().getWidth();
        if (0 <= index && index < tiles.length && tiles[index] != null) {
            return tiles[index];
        }

        return null;
    }

    private void drawDebugOverlay(final Canvas canvas, final int offsetX, final int offsetY) {
        canvas.setColor(Color.RED);
        final int tileHeight = getMap().getTileHeight();
        final int tileWidth = getMap().getTileWidth();
        int screenX = offsetX;
        int screenY = offsetY;
        int startX = 0;
        int startY = 0;
        int endX = getMap().getWidth();
        int endY = getMap().getHeight();
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                final Tile tile = getTile(x, y);
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

    private static int[] parseData(final ElementWrapper element, final int width, final int height) {
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
            for (final ElementWrapper tileElement : element.getChildren(Const.TILE)) {
                result[i] = tileElement.getIntAttribute(Const.GID);
                ++i;
            }

            return result;
        }
    }

    private static int[] parseBase64(final ElementWrapper element, final int width, final int height) {
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
