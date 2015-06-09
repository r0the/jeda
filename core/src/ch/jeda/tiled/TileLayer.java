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

import ch.jeda.Convert;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Backdrop;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Represents a Tiled tile layer.
 *
 * @since 2.0
 * @version 2
 */
public final class TileLayer extends Layer {

    private static final String CLASS = "class";
    private static final String TYPE = "type";
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
     * @param drawOrder the base draw order for this layer
     *
     * @since 2.1
     */
    @Override
    public void addTo(final PhysicsView view, final int drawOrder) {
        if (!isVisible()) {
            return;
        }

        final BodyType type = BodyType.parse(getProperties().readString(TYPE));
        if (type == null) {
            convertToBackdrop(view, drawOrder);
        }
        else {
            convertToBodies(view, type, drawOrder);
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

    private void convertToBackdrop(final PhysicsView view, final int drawOrder) {
        final int endX = getMap().getWidth();
        final int endY = getMap().getHeight();
        for (int x = 0; x < endX; ++x) {
            for (int y = 0; y < endY; ++y) {
                final Tile tile = getTile(x, y);
                if (tile != null) {
                    final float width = tile.getWidth();
                    final float height = tile.getHeight();
                    final Image image = tile.getImage();
                    final Backdrop backdrop = new Backdrop(x + width / 2f, endY - y - 1f + height / 2f, width, height, image);
                    backdrop.setName(getName());
                    backdrop.setOpacity(getOpacity());
                    backdrop.setDrawOrder(drawOrder);
                    view.add(backdrop);
                }
            }
        }
    }

    private void convertToBodies(final PhysicsView view, final BodyType type, final int drawOrder) {
        final int endX = getMap().getWidth();
        final int endY = getMap().getHeight();
        for (int x = 0; x < endX; ++x) {
            for (int y = 0; y < endY; ++y) {
                final Tile tile = getTile(x, y);
                if (tile != null) {
                    final Body body = Body.create(getProperties().readString(CLASS));
                    final Shape[] shapes = tile.getShapes();
                    final Image image = tile.getImage();
                    final float width = tile.getWidth();
                    final float height = tile.getHeight();
                    if (shapes.length > 0) {
                        for (int i = 0; i < shapes.length; ++i) {
                            body.addShape(shapes[i]);
                        }
                    }
                    else {
                        body.addShape(new Rectangle(-width / 2f, -height / 2f, width, height));
                    }

                    body.setName(getName());
                    body.setDrawOrder(drawOrder);
                    body.setType(type);
                    body.setImage(image, width, height);
                    body.setOpacity(getOpacity());
                    body.setPosition(x + width / 2f, endY - y - 1f + height / 2f);
                    body.setAngularDamping(getProperties().readFloat("angulardamping", 0f));
                    body.setDamping(getProperties().readFloat("damping", 0f));
                    body.setDensity(getProperties().readFloat("density", 1f));
                    body.setFriction(getProperties().readFloat("friction", 0f));
                    view.add(body);
                }
            }
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
            InputStream in = new ByteArrayInputStream(Convert.fromBase64(element.getContent()));
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
