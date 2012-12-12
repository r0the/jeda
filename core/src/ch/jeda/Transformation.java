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

import java.io.Serializable;

public final class Transformation implements Serializable {

    public static final Transformation IDENTITY = new Transformation();
    public final double scaleX;
    public final double skewX;
    public final double translateX;
    public final double skewY;
    public final double scaleY;
    public final double translateY;

    private Transformation() {
        this(1d, 0d, 0d, 0d, 1d, 0d);
    }

    private Transformation(double scaleX, double skewX, double translateX,
                           double skewY, double scaleY, double translateY) {
        this.scaleX = scaleX;
        this.skewX = skewX;
        this.translateX = translateX;
        this.skewY = skewY;
        this.scaleY = scaleY;
        this.translateY = translateY;
    }

    public Transformation inverted() {
        double d = this.scaleX * this.scaleY - this.skewX * this.skewY;

        if (Math.abs(d) <= Double.MIN_VALUE) {
            throw new IllegalStateException("Transformation is not invertible.");
        }

        return new Transformation(
                this.scaleY / d, -this.skewX / d,
                (this.skewX * this.translateY - this.scaleY * this.translateX) / d,
                -this.skewY / d, this.scaleX / d,
                (this.skewY * this.translateX - this.scaleX * this.translateY) / d);
    }

    public Transformation rotatedBy(double angle) {
        double a = Math.toRadians(angle);
        double sin = Math.sin(a);
        double cos = Math.cos(a);

        return new Transformation(
                cos * this.scaleX + sin * this.skewX, -sin * this.scaleX + cos * this.skewX, this.translateX,
                cos * this.skewY + sin * this.scaleY, -sin * this.skewY + cos * this.scaleY, this.translateY);
    }

//    public Transformation rotatedBy90() {
//        return new Transformation(
//                this.skewX, -this.scaleX, this.translateX,
//                this.scaleY, -this.skewY, this.translateY);
//    }
    public Vector transform(Vector v) {
        return Vector.from(
                this.scaleX * v.x + this.skewX * v.y + this.translateX,
                this.skewY * v.x + this.scaleY * v.y + this.translateY);
    }

    public Transformation translatedBy(Vector v) {
        return new Transformation(
                this.scaleX, this.skewX, this.translateX + v.x,
                this.skewY, this.scaleY, this.translateY + v.y);
    }
}
