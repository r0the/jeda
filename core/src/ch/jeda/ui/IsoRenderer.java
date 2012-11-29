/*
 * Copyright (C) 2012 by Stefan Rothe
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
import ch.jeda.Vector;

public class IsoRenderer {

    private final Canvas canvas;
    private final Size mapSize;
    private final Location maxScrollPos;
    private final Location minScrollPos;
    private final Image[] tiles;
    private final Size tileSize;
    private Location scrollPos;

    public IsoRenderer(Canvas canvas, Size mapSize, Size tileSize) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        if (mapSize == null) {
            throw new NullPointerException("mapSize");
        }

        if (tileSize == null) {
            throw new NullPointerException("tileSize");
        }

        this.canvas = canvas;
        this.mapSize = mapSize;
        this.tileSize = tileSize;
        this.tiles = new Image[mapSize.width * mapSize.height];
        this.scrollPos = new Location(this.canvas.getWidth() / 2, 0);

        int w = this.tileSize.width * this.mapSize.width / 2;
        int h = this.tileSize.height * this.mapSize.height;
        this.maxScrollPos = new Location(w, 0);
        this.minScrollPos = new Location(this.canvas.getWidth() - w,
                                         this.canvas.getHeight() - h);

    }

    public void draw() {
        this.canvas.setColor(Color.BLACK);
        int startX = this.scrollPos.x;
        int startY = this.scrollPos.y;
        int x = startX;
        int y = startY;
        for (int tileY = 0; tileY < this.mapSize.height; ++tileY) {
            for (int tileX = 0; tileX < this.mapSize.width; ++tileX) {
                this.canvas.drawImage(x, y, this.tiles[tileX + this.mapSize.width * tileY], Alignment.TOP_CENTER);
                x = x + this.tileSize.width / 2;
                y = y + this.tileSize.height / 2;
            }

            startX = startX - tileSize.width / 2;
            startY = startY + tileSize.height / 2;
            x = startX;
            y = startY;
        }
    }

    public void fill(Image image) {
        for (int i = 0; i < this.tiles.length; ++i) {
            this.tiles[i] = image;
        }
    }

    public void scroll(Location delta) {
        if (delta == null) {
            throw new NullPointerException("delta");
        }

        this.scrollPos = new Location(this.scrollPos.x + delta.x, this.scrollPos.y + delta.y);
        this.checkScrollPos();
    }

    public void setTile(int x, int y, Image image) {
        this.setTile(new Location(x, y), image);
    }

    public void setTile(Location location, Image image) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (!this.mapSize.contains(location)) {
            throw new IllegalArgumentException("location");
        }

        this.tiles[location.x + this.mapSize.width * location.y] = image;
    }

    public Location toCanvas(Vector mapLocation) {
        double x = this.scrollPos.x + this.tileSize.width * (mapLocation.x - mapLocation.y) * 0.5;
        double y = this.scrollPos.y + this.tileSize.height * (2.0 + mapLocation.x + mapLocation.y) * 0.5;
        return new Location((int) x, (int) y);
    }

    private void checkScrollPos() {
        this.scrollPos = this.scrollPos.ensureRange(this.minScrollPos, this.maxScrollPos);
    }
}
