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
import java.util.ArrayList;
import java.util.List;

public class IsoRenderer {

    private final Size mapSize;
    private final Location maxScrollPos;
    private final Location minScrollPos;
    private final List<Image[]> layers;
    private final Size tileSize;
    private final Size viewPort;
    private Location scrollPos;

    public IsoRenderer(Size viewPort, Size mapSize, Size tileSize) {
        if (viewPort == null) {
            throw new NullPointerException("viewPort");
        }

        if (mapSize == null) {
            throw new NullPointerException("mapSize");
        }

        if (tileSize == null) {
            throw new NullPointerException("tileSize");
        }

        this.viewPort = viewPort;
        this.mapSize = mapSize;
        this.tileSize = tileSize;
        this.layers = new ArrayList();

        this.scrollPos = new Location(this.viewPort.width / 2, 0);

        int w = this.tileSize.width * this.mapSize.width / 2;
        int h = this.tileSize.height * this.mapSize.height;
        this.maxScrollPos = new Location(w, 0);
        this.minScrollPos = new Location(this.viewPort.width - w,
                                         this.viewPort.height - h);

    }

    public void drawOn(Canvas canvas) {
        canvas.setColor(Color.BLACK);
        int startX = 0;
        int startY = 0;
        int x = 0;
        int y = 0;

        for (Image[] layer : this.layers) {
            startX = this.scrollPos.x;
            startY = this.scrollPos.y;

            for (int tileY = 0; tileY < this.mapSize.height; ++tileY) {
                x = startX;
                y = startY;

                for (int tileX = 0; tileX < this.mapSize.width; ++tileX) {
                    canvas.drawImage(x, y, layer[tileX + this.mapSize.width * tileY], Alignment.TOP_CENTER);
                    x = x + this.tileSize.width / 2;
                    y = y + this.tileSize.height / 2;
                }

                startX = startX - tileSize.width / 2;
                startY = startY + tileSize.height / 2;
                x = startX;
                y = startY;
            }
        }
    }

    public void fill(int layerIndex, Image image) {
        this.ensureLayer(layerIndex);
        Image[] layer = this.layers.get(layerIndex);
        for (int i = 0; i < layer.length; ++i) {
            layer[i] = image;
        }
    }

    public void scroll(Location delta) {
        if (delta == null) {
            throw new NullPointerException("delta");
        }

        this.scrollPos = new Location(this.scrollPos.x + delta.x, this.scrollPos.y + delta.y);
        this.checkScrollPos();
    }

    public void setTile(int layerIndex, Location location, Image image) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (!this.mapSize.contains(location)) {
            throw new IllegalArgumentException("location");
        }

        this.ensureLayer(layerIndex);
        this.layers.get(layerIndex)[location.x + this.mapSize.width * location.y] = image;
    }

    public Vector toMap(Location canvasLocation) {
        double a = (canvasLocation.x - this.scrollPos.x) / (double) this.tileSize.width;
        double b = (canvasLocation.y - this.scrollPos.y) / (double) this.tileSize.height;
        return new Vector(a + b - 0.5, b - a - 0.5);
    }

    public Location toCanvas(Vector mapLocation) {
        double x = this.scrollPos.x + this.tileSize.width * (mapLocation.x - mapLocation.y) * 0.5;
        double y = this.scrollPos.y + this.tileSize.height * (2.0 + mapLocation.x + mapLocation.y) * 0.5;
        return new Location((int) x, (int) y);
    }

    private void checkScrollPos() {
        this.scrollPos = this.scrollPos.ensureRange(this.minScrollPos, this.maxScrollPos);
    }

    private void ensureLayer(int layerIndex) {
        while (this.layers.size() <= layerIndex) {
            this.layers.add(new Image[this.mapSize.width * this.mapSize.height]);
        }
    }
}
