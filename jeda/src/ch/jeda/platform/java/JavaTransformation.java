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
package ch.jeda.platform.java;

import ch.jeda.Transformation;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

class JavaTransformation extends Transformation {

    final AffineTransform matrix;

    JavaTransformation() {
        this.matrix = new AffineTransform();
    }

    @Override
    public void concatenate(Transformation other) {
        this.matrix.concatenate(((JavaTransformation) other).matrix);
    }

    @Override
    public boolean invert(Transformation inverse) {
        try {
            final AffineTransform other = ((JavaTransformation) inverse).matrix;
            other.setTransform(this.matrix);
            other.invert();
            return true;
        }
        catch (NoninvertibleTransformException ex) {
            return false;
        }
    }

    @Override
    public boolean isIdentity() {
        return this.matrix.isIdentity();
    }

    @Override
    public void rotate(double angle) {
        this.matrix.rotate(angle);
    }

    @Override
    public void scale(double sx, double sy) {
        this.matrix.scale(sx, sy);
    }

    @Override
    public void set(Transformation other) {
        this.matrix.setTransform(((JavaTransformation) other).matrix);
    }

    @Override
    public void setIdentity() {
        this.matrix.setToIdentity();
    }

    @Override
    protected void transform(float[] points) {
        this.matrix.transform(points, 0, points, 0, points.length >> 1);
    }

    @Override
    protected void transformDelta(float[] points) {
        this.matrix.transform(points, 0, points, 0, points.length >> 1);
        // Reverse the translation
        final float dx = (float) -this.matrix.getTranslateX();
        final float dy = (float) -this.matrix.getTranslateY();
        for (int i = 0; i < points.length; i = i + 2) {
            points[i] += dx;
            points[i + 1] += dy;
        }
    }

    @Override
    public void translate(double tx, double ty) {
        this.matrix.translate(tx, ty);
    }
}
