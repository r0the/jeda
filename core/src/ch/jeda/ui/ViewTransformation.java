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
package ch.jeda.ui;

public class ViewTransformation {

    private double maxX;
    private double maxY;
    private double minX;
    private double minY;
    private int viewHeight;
    private int viewWidth;

    public ViewTransformation() {
        minX = -5.0;
        maxX = 5.0;
        minY = -5.0;
        maxY = 5.0;
    }

    public final double getMaxX() {
        return maxX;
    }

    public final double getMinX() {
        return minX;
    }

    public final double getSizeX() {
        return maxX - minX;
    }

    public final void setMaxY(final double maxY) {
        this.maxY = maxY;
    }

    public final void setMinX(final double minX) {
        this.minX = minX;
    }

    public final void setMinY(final double minY) {
        this.minY = minY;
    }

    public final void setRange(final double range) {
        minX = -range / 2.0;
        maxX = range / 2.0;
        minY = -range / 2.0;
        maxY = range / 2.0;
    }

    public final void setRange(final double minX, final double maxX, final double minY, final double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    double screenX(final double x) {
        return ((x - minX) / (maxX - minX)) * viewWidth;
    }

    double screenY(final double y) {
        return ((y - maxY) / (minY - maxY)) * viewHeight;
    }
}
