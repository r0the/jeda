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
package ch.jeda;

public class Transformation {

    private final double[] values;

    public Transformation() {
        this.values = new double[6];
        for (int i = 0; i < 6; ++i) {
            this.values[i] = 0f;
        }
    }

    public Transformation(double scaleX, double skewX, double translateX,
                          double scaleY, double skewY, double translateY) {
        this.values = new double[6];
        for (int i = 0; i < 6; ++i) {
            this.values[i] = 0f;
        }
    }

    public Transformation inverse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    public Vector transform(Vector vector) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    public double[] toArray() {
        return this.values;
    }
}
