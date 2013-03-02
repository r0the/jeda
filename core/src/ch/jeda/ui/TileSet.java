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

import ch.jeda.Location;
import ch.jeda.Size;

public class TileSet {

    private final Image image;
    private final Size size;
    private final Size tileSize;
    private final Image[] tiles;

    public TileSet(String filePath) {
        this(filePath, TileSet.parseSize(filePath));
    }

    public TileSet(String filePath, Size tileSize) {
        this(new Image(filePath), tileSize);
    }

    public TileSet(Image image, Size tileSize) {
        if (image == null) {
            throw new NullPointerException("image");
        }

        if (tileSize == null) {
            throw new NullPointerException("tileSize");
        }

        this.image = image;
        this.tileSize = tileSize;
        this.size = new Size(this.image.getWidth() / this.tileSize.width,
                             this.image.getHeight() / this.tileSize.height);
        this.tiles = new Image[this.size.width * this.size.height];
    }

    public Size getSize() {
        return this.size;
    }

    public Image getTileAt(int x, int y) {
        return this.getTileAt(new Location(x, y));
    }

    public Image getTileAt(Location location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (!this.size.contains(location)) {
            throw new IllegalArgumentException("location");
        }

        int index = location.x + location.y * this.size.width;
        if (this.tiles[index] == null) {
            this.tiles[index] = this.createSubImage(location);
        }

        return this.tiles[index];
    }

    public Size getTileSize() {
        return this.tileSize;
    }

    private Image createSubImage(Location location) {
        location = new Location(location.x * this.tileSize.width,
                                location.y * this.tileSize.height);
        return this.image.createSubImage(location, this.tileSize);
    }

    private static boolean isDigit(char ch) {
        return "0123456789".indexOf(ch) != -1;
    }

    private static Size parseSize(String filePath) {
        int endPos = filePath.lastIndexOf('.');;
        int startPos = endPos;
        if (startPos == -1) {
            return new Size();
        }

        while (startPos > 0 && TileSet.isDigit(filePath.charAt(startPos - 1))) {
            --startPos;
        }

        --startPos;
        while (startPos > 0 && TileSet.isDigit(filePath.charAt(startPos - 1))) {
            --startPos;
        }

        return new Size(filePath.substring(startPos, endPos));
    }
}
