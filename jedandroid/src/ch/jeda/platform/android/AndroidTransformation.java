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
package ch.jeda.platform.android;

import android.graphics.Matrix;
import ch.jeda.Transformation;

/**
 *
 * @author stefan
 */
public class AndroidTransformation extends Transformation {

    final Matrix matrix;

    @Override
    public void concatenate(Transformation other) {
        this.matrix.postConcat(((AndroidTransformation) other).matrix);
    }

    @Override
    public boolean invert(Transformation inverse) {
        return this.matrix.invert(((AndroidTransformation) inverse).matrix);
    }

    @Override
    public boolean isIdentity() {
        return this.matrix.isIdentity();
    }

    @Override
    public void rotate(double angle) {
        this.matrix.postRotate((float) Math.toDegrees(angle));
    }

    @Override
    public void scale(double sx, double sy) {
        this.matrix.postScale((float) sx, (float) sy);
    }

    @Override
    public void set(Transformation other) {
        this.matrix.set(((AndroidTransformation) other).matrix);
    }

    @Override
    public void setIdentity() {
        this.matrix.reset();
    }

    @Override
    public void translate(double tx, double ty) {
        this.matrix.postTranslate((float) tx, (float) ty);
    }

    @Override
    protected void transform(float[] points) {
        this.matrix.mapPoints(points);
    }

    @Override
    protected void transformDelta(float[] deltas) {
        this.matrix.mapVectors(deltas);
    }

    AndroidTransformation() {
        this.matrix = new Matrix();
    }
}
