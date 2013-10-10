/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.geometry;

public final class BoundingBox {

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public BoundingBox() {
        this.reset();
    }

    public BoundingBox(final BoundingBox other) {
        this.minX = other.minX;
        this.minY = other.minY;
        this.maxX = other.maxX;
        this.maxY = other.maxY;
    }

    public boolean contains(final float x, final float y) {
        return this.minX <= x && x <= this.maxX && this.minY <= y && y <= this.maxY;
    }

    public boolean intersects(final BoundingBox other) {
        return other.minX <= this.maxX && this.minX <= other.maxX && other.minY <= this.maxY && this.minY <= other.maxY;
    }

    /**
     * Resets the bounding box.
     */
    final void reset() {
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        this.maxX = -Float.MAX_VALUE;
        this.maxY = -Float.MAX_VALUE;
    }

    void include(final BoundingBox boundingBox) {
        this.minX = Math.min(this.minX, boundingBox.minX);
        this.minY = Math.min(this.minY, boundingBox.minY);
        this.maxX = Math.max(this.maxX, boundingBox.maxX);
        this.maxY = Math.max(this.maxY, boundingBox.maxY);
    }

    /**
     * Adjusts the bounding box to enclose all given points.
     */
    void include(final float[] points) {
        for (int i = 0; i < points.length; i = i + 2) {
            final float x = points[i];
            final float y = points[i + 1];
            this.minX = Math.min(this.minX, x);
            this.minY = Math.min(this.minY, y);
            this.maxX = Math.max(this.maxX, x);
            this.maxY = Math.max(this.maxY, y);
        }
    }
}
