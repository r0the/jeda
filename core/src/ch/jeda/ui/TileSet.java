/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.ui;

public class TileSet {

    public final int height;
    public final int tileHeight;
    public final int tileWidth;
    public final int width;
    private final Image image;
    private final Image[] tiles;

    public TileSet(final String filePath,
                   final int tileWidth, final int tileHeight) {
        this(new Image(filePath), tileWidth, tileHeight);
    }

    public TileSet(final Image image,
                   final int tileWidth, final int tileHeight) {
        if (image == null) {
            throw new NullPointerException("image");
        }

        if (tileWidth < 1) {
            throw new IllegalArgumentException("tileWidth");
        }
        if (tileHeight < 1) {
            throw new IllegalArgumentException("tileHeight");
        }

        this.height = image.getWidth() / tileWidth;
        this.width = image.getHeight() / tileHeight;
        this.image = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tiles = new Image[this.width * this.height];
    }

    public int getHeight() {
        return this.height;
    }

    public Image getTileAt(final int x, final int y) {
        if (!this.contains(x, y)) {
            throw new IllegalArgumentException("x, y");
        }

        int index = x + y * this.width;
        if (this.tiles[index] == null) {
            this.tiles[index] = this.image.createSubImage(
                    x * this.tileWidth, y * this.tileWidth,
                    this.tileWidth, this.tileHeight);
        }

        return this.tiles[index];
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getWidth() {
        return this.width;
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }
}
