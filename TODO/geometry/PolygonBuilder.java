/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

import java.util.Arrays;

final class PolygonBuilder {

    private static final int GROWTH = 20;
    private double[] points;
    private int size;

    PolygonBuilder() {
        this.points = new double[GROWTH];
        this.size = 0;
    }

    void addPoint(final double x, final double y) {
        if (this.points.length < this.size + 2) {
            this.points = Arrays.copyOf(this.points, this.points.length + GROWTH);
        }
        this.size += 2;
        this.points[this.size - 2] = x;
        this.points[this.size - 1] = y;
    }

    Polygon toPolygon() {
        return new Polygon(Arrays.copyOf(this.points, this.size));
    }
}
