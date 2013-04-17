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

import ch.jeda.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Experimental</b>
 */
public class IsoRenderer {

    private final int mapWidth;
    private final int mapHeight;
    private final int maxScrollX;
    private final int maxScrollY;
    private final int minScrollX;
    private final int minScrollY;
    private final List<Image[]> layers;
    private final int tileWidth;
    private final int tileHeight;
    private int scrollX;
    private int scrollY;

    public IsoRenderer(int viewPortWidth, int viewPortHeight, int mapWidth,
                       int mapHeight, int tileWidth, int tileHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.layers = new ArrayList();

        this.scrollX = viewPortWidth / 2;
        this.scrollY = 0;

        int w = this.tileWidth * this.mapWidth / 2;
        int h = this.tileHeight * this.mapHeight;
        this.maxScrollX = w;
        this.maxScrollY = 0;
        this.minScrollX = viewPortWidth - w;
        this.minScrollY = viewPortHeight - h;
    }

    public void drawOn(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        canvas.setColor(Color.BLACK);
        int startX;
        int startY;
        int x;
        int y;

        for (Image[] layer : this.layers) {
            startX = this.scrollX;
            startY = this.scrollY;

            for (int tileY = 0; tileY < this.mapHeight; ++tileY) {
                x = startX;
                y = startY;

                for (int tileX = 0; tileX < this.mapWidth; ++tileX) {
                    canvas.drawImage(x, y, layer[tileX + this.mapWidth * tileY], Alignment.TOP_CENTER);
                    x = x + this.tileWidth / 2;
                    y = y + this.tileHeight / 2;
                }

                startX = startX - tileWidth / 2;
                startY = startY + tileHeight / 2;
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

    public void scroll(int dx, int dy) {
        this.scrollX = this.scrollX + dx;
        this.scrollY = this.scrollY + dy;
        this.checkScrollPos();
    }

    public void setTile(int layerIndex, int x, int y, Image image) {
        if (!this.contains(x, y)) {
            throw new IllegalArgumentException("x/y");
        }

        this.ensureLayer(layerIndex);
        this.layers.get(layerIndex)[x + this.mapWidth * y] = image;
    }

    public void toMap(Vector pos) {
        final float a = (pos.x - this.scrollX) / (float) this.tileWidth;
        final float b = (pos.y - this.scrollY) / (float) this.tileHeight;
        pos.x = a + b - 0.5f;
        pos.y = b - a - 0.5f;
    }

    public void toCanvas(Vector pos) {
        final float x = this.scrollX + this.tileWidth * (pos.x - pos.y) * 0.5f;
        final float y = this.scrollY + this.tileHeight * (2.0f + pos.x + pos.y) * 0.5f;
        pos.x = x;
        pos.y = y;
    }

    private void checkScrollPos() {
        this.scrollX = Math.max(this.minScrollX, Math.min(this.scrollX, this.maxScrollX));
        this.scrollY = Math.max(this.minScrollY, Math.min(this.scrollY, this.maxScrollY));
    }

    private boolean contains(int x, int y) {
        return 0 <= x && x < this.mapWidth && 0 <= y && y < this.mapHeight;
    }

    private void ensureLayer(int layerIndex) {
        while (this.layers.size() <= layerIndex) {
            this.layers.add(new Image[this.mapWidth * this.mapHeight]);
        }
    }
}
