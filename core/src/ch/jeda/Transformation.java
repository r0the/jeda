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
 * @since 1
 */
public final class Transformation implements Serializable {

    private static final float[] IDENTITY = new float[]{1f, 0f, 0f, 0f, 1f, 0f};
    private static final int M00 = 0;
    private static final int M01 = 1;
    private static final int M02 = 2;
    private static final int M10 = 3;
    private static final int M11 = 4;
    private static final int M12 = 5;
    private final float[] m;

    /**
     * Constructs an identity transformation.
     *
     * @since 1
     */
    public Transformation() {
        this.m = new float[6];
        this.m[M00] = 1f;
        this.m[M11] = 1f;
    }

    /**
     * Combines two transformations. Concatenates the transformation with the
     * specified other transformation.
     *
     * @param other the other transformation
     * @return the combined transformation
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void concatenate(final Transformation other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        final float m00 = this.m[M00] * other.m[M00] + this.m[M01] * other.m[M10];
        final float m01 = this.m[M00] * other.m[M01] + this.m[M01] * other.m[M11];
        final float m02 = this.m[M00] * other.m[M02] + this.m[M01] * other.m[M12] + this.m[M02];
        final float m10 = this.m[M10] * other.m[M00] + this.m[M11] * other.m[M10];
        final float m11 = this.m[M10] * other.m[M01] + this.m[M11] * other.m[M00];
        final float m12 = this.m[M10] * other.m[M02] + this.m[M11] * other.m[M12] + this.m[M12];
        this.m[M00] = m00;
        this.m[M01] = m01;
        this.m[M02] = m02;
        this.m[M10] = m10;
        this.m[M11] = m11;
        this.m[M12] = m12;
    }

    public float[] copyToArray(final float[] target) {
        if (target != null && target.length >= 6) {
            System.arraycopy(this.m, 0, target, 0, 6);
            return target;
        }
        else {
            final float[] result = new float[6];
            System.arraycopy(this.m, 0, result, 0, 6);
            return result;
        }
    }

    public boolean invert(final Transformation inverse) {
        final float d = this.m[M00] * this.m[M11] - this.m[M01] * this.m[M10];

        if (Math.abs(d) <= Float.MIN_VALUE) {
            return false;
        }

        if (inverse != null) {
            inverse.m[M00] = this.m[M11] / d;
            inverse.m[M01] = -this.m[M01] / d;
            inverse.m[M02] = (this.m[M01] * this.m[M12] - this.m[M11] * this.m[M02]) / d;
            inverse.m[M10] = -this.m[M10] / d;
            inverse.m[M11] = this.m[M00] / d;
            inverse.m[M12] = (this.m[M10] * this.m[M02] - this.m[M00] * this.m[M12]) / d;
        }

        return true;
    }

    /**
     * Adds a rotation to the transformation. Concatenates the transformation
     * with add a rotation by the specified angle.
     *
     * @param angle the rotation angle
     */
    public void rotate(final double angle) {
        final float sin = (float) Math.sin(angle);
        final float cos = (float) Math.cos(angle);
        final float m00 = this.m[M00];
        final float m01 = this.m[M01];
        final float m10 = this.m[M10];
        final float m11 = this.m[M11];

        this.m[M00] = cos * m00 + sin * m01;
        this.m[M01] = -sin * m00 + cos * m01;
        this.m[M10] = cos * m10 + sin * m11;
        this.m[M11] = -sin * m10 + cos * m11;
    }

    /**
     * Adds a scaling to the transformation. Concatenates the transformation
     * with add a scaling by the specified factors.
     *
     * @param sx the scaling factor along the x-axis
     * @param sy the scaling factor along the y-axis
     */
    public void scale(final float sx, final float sy) {
        this.m[M00] *= sx;
        this.m[M11] *= sy;
    }

    public void set(final Transformation other) {
        System.arraycopy(other.m, 0, this.m, 0, 6);
    }

    /**
     * Sets the transformation to the identity.
     */
    public void setIdentity() {
        System.arraycopy(IDENTITY, 0, this.m, 0, 6);
    }

    public void transformDelta(final Vector v) {
        if (v == null) {
            throw new NullPointerException("v");
        }

        final float vx = v.x;
        v.x = this.m[M00] * vx + this.m[M01] * v.y;
        v.y = this.m[M10] * vx + this.m[M11] * v.y;
    }

    /**
     * Applies the transformation to a vector.
     *
     * @param v the vector to apply the transformation to
     * @throws NullPointerException if <tt>v</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void transformPoint(final Vector v) {
        if (v == null) {
            throw new NullPointerException("v");
        }

        final float vx = v.x;
        v.x = this.m[M00] * vx + this.m[M01] * v.y + this.m[M02];
        v.y = this.m[M10] * vx + this.m[M11] * v.y + this.m[M12];
    }

    /**
     * Adds a translation to the transformation.
     *
     * @param tx the translation along the x-axis
     * @param ty the translation along the y-axis
     *
     * @since 1
     */
    public void translate(final float tx, final float ty) {
        this.m[M02] += tx;
        this.m[M12] += ty;
    }
}
