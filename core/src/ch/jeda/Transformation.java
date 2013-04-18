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
package ch.jeda;

import java.io.Serializable;

/**
 * Represents a two-dimensional affine transformation. Transformations can be
 * applied either to {@link Vector} objects or a {@link ch.jeda.ui.Canvas}.
 * <p>
 * A transformation is represented as an 3x3 affine transformation matrix. It
 * has the form
 * <pre>[m00 m01 m02]
 * [m10 m11 m12]
 * [  0   0   1]</pre>
 * <p>
 *
 * @see ch.jeda.ui.Canvas#setTransformation(ch.jeda.Transformation)
 * @see ch.jeda.ui.Canvas#getTransformation()
 * @since 1
 */
public abstract class Transformation implements Serializable {

    public static Transformation createIdentity() {
        return Engine.getContext().createTransformation();
    }

    /**
     * Combines two transformations. Modifies the transformation by adding an
     * other transformation after the current transformation.
     *
     * @param other the other transformation
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public abstract void concatenate(Transformation other);

    /**
     * Inverts the transformation. Calculates and returns the inverse
     * transformation, if such exists.
     *
     * @return the inverted transformation
     * @throws IllegalStateException if transformation is not invertible
     *
     * @since 1
     */
    public abstract boolean invert(Transformation inverse);

    public abstract boolean isIdentity();

    /**
     * Adds a rotation to the transformation. Modifies the transformation by
     * adding a rotation after the transformation.
     *
     * @param angle the rotation angle in radians
     *
     * @since 1
     */
    public abstract void rotate(double angle);

    /**
     * Adds a scaling to the transformation. Modifies the transformation by
     * adding a scaling after the transformation.
     *
     * @param sx the scale factor along x-axis
     * @param sy the scale factor along the y-axis
     *
     * @since 1
     */
    public abstract void scale(double sx, double sy);

    public abstract void set(Transformation other);

    public abstract void setIdentity();

    /**
     * Adds a translation to the transformation. Modifies the transformation by
     * adding a translation after the transformation.
     *
     * @param tx the translation in direction of the x-axis
     * @param ty the translation in direction of the y-axis
     *
     * @since 1
     */
    public abstract void translate(double tx, double ty);

    public final void translate(final Vector t) {
        this.translate(t.x, t.y);
    }

    public final void transformPoint(Vector point) {
        float[] vec = new float[2];
        vec[0] = point.x;
        vec[1] = point.y;
        this.transform(vec);
        point.x = vec[0];
        point.y = vec[1];
    }

    public final void transformDelta(Vector delta) {
        float[] vec = new float[2];
        vec[0] = delta.x;
        vec[1] = delta.y;
        this.transformDelta(vec);
        delta.x = vec[0];
        delta.y = vec[1];
    }

    protected abstract void transform(float[] points);

    protected abstract void transformDelta(float[] deltas);

    protected Transformation() {
    }
}
