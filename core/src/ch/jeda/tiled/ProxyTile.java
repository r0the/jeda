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

class ProxyTile extends Tile {

    private final Tile tile;
    private final Image image;
    private final Shape[] shapes;

    ProxyTile(final Tile tile, final boolean flipHorizontally, final boolean flipVertically) {
        this.tile = tile;
        Image img = tile.getImage();
        if (flipHorizontally) {
            img = img.flipHorizontally();
        }

        if (flipVertically) {
            img = img.flipVertically();
        }

        image = img;
        final Shape[] origShapes = tile.getShapes();
        shapes = new Shape[origShapes.length];
        for (int i = 0; i < origShapes.length; ++i) {
            shapes[i] = origShapes[i];
            if (flipHorizontally) {
                shapes[i] = shapes[i].flipHorizontally();
            }

            if (flipVertically) {
                shapes[i] = shapes[i].flipVertically();
            }
        }
    }

    @Override
    public float getHeight() {
        return tile.getHeight();
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public Data getProperties() {
        return tile.getProperties();
    }

    @Override
    public Shape[] getShapes() {
        return shapes;
    }

    @Override
    public Terrain getTerrainBottomLeft() {
        // TODO: Fix
        return tile.getTerrainBottomLeft();
    }

    @Override
    public Terrain getTerrainBottomRight() {
        // TODO: Fix
        return tile.getTerrainBottomRight();
    }

    @Override
    public Terrain getTerrainTopLeft() {
        // TODO: Fix
        return tile.getTerrainTopLeft();
    }

    @Override
    public Terrain getTerrainTopRight() {
        // TODO: Fix
        return tile.getTerrainTopRight();
    }

    @Override
    public float getWidth() {
        return tile.getWidth();
    }

    @Override
    int screenX(int tileX) {
        return tile.screenX(tileX);
    }

    @Override
    int screenY(int tileY) {
        return tile.screenY(tileY);
    }
}
