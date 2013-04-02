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
 * Transformations are immutable objects. That means that they cannot be
 * changed, an object always represents the same transformation. New
 * transformation objects are created by applying a transformation to an
 * existing one, for example by calling {@link #rotatedBy(double)}.
 *
 * @see {@link ch.jeda.ui.Canvas#pushTransformation(ch.jeda.Transformation)}
 * @see {@link ch.jeda.ui.Canvas#popTransformation()}
 * @since 1
 */
public final class Transformation implements Serializable {

    /**
     * The identity transformation. This transformation maps every point of the
     * plane to itself.
     *
     * @since 1
     */
    public static final Transformation IDENTITY = new Transformation();
    /**
     * The scale factor along the x-axis. This value corresponds to the element
     * m00 of the transformation matrix.
     *
     * @since 1
     */
    public final float scaleX;
    /**
     * The skew factor along the x-axis. This value corresponds to the element
     * m01 of the transformation matrix.
     *
     * @since 1
     */
    public final float skewX;
    /**
     * The translation along the x-axis. This value corresponds to the element
     * m02 of the transformation matrix.
     *
     * @since 1
     */
    public final float translateX;
    /**
     * The skew factor along the y-axis. This value corresponds to the element
     * m10 of the transformation matrix.
     *
     * @since 1
     */
    public final float skewY;
    /**
     * The scale factor along the y-axis. This value corresponds to the element
     * m11 of the transformation matrix.
     *
     * @since 1
     */
    public final float scaleY;
    /**
     * The translation along the y-axis. This value corresponds to the element
     * m12 of the transformation matrix.
     *
     * @since 1
     */
    public final float translateY;

    /**
     * Creates a new rotation transformation. The returned transformation
     * represents a clockwise rotation of the specified angle with the origin as
     * fixed point.
     * <p>
     * The angle has to be specified in radians.
     * <p>
     * Note that this method uses the usual mathematical interpretation of a
     * positive angle. The inverse rotation direction results from the y-axis
     * pointing downwards.
     *
     * @param angle the rotation angle
     * @return rotation transformation
     *
     * @since 1
     */
    public static Transformation rotate(double angle) {
        final float sin = (float) Math.sin(angle);
        final float cos = (float) Math.cos(angle);

        return new Transformation(cos, -sin, 0f, sin, cos, 0f);
    }

    /**
     * Creates a new scaling transformation. The returned transformation
     * represents a scaling with the origin as fixed point.
     *
     * @param sx the scaling factor for the x-axis
     * @param sy the scaling factor for the y-axis
     * @return scaling transformation
     *
     * @since 1
     */
    public static Transformation scale(double sx, double sy) {
        return new Transformation((float) sx, 0f, 0f, 0f, (float) sy, 0f);
    }

    /**
     * Creates a new translation transformation. The returned transformation
     * represents a translation by the specified vector.
     *
     * @param t the translation vector
     * @return translation transformation
     *
     * @since 1
     */
    public static Transformation translate(Vector t) {
        return new Transformation(1f, 0f, (float) t.x, 0f, 1f, (float) t.y);
    }

    /**
     * Creates a new translation transformation. The returned transformation
     * represents a translation by the specified values in direction of both
     * axes.
     *
     * @param tx the translation in direction of the x-axis
     * @param ty the translation in direction of the y-axis
     * @return translation transformation
     *
     * @since 1
     */
    public static Transformation translate(double tx, double ty) {
        return new Transformation(1f, 0f, (float) tx, 0f, 1f, (float) ty);
    }

    /**
     * Combines two transformations. Calculates and returns a transformation
     * that results from applying the other transformation after this
     * transformation.
     *
     * @param other the other transformation
     * @return the combined transformation
     * @throws NullPointerException if <tt>other</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Transformation combinedWith(Transformation other) {
        if (other == null) {
            throw new NullPointerException("other");
        }
        else if (this == IDENTITY) {
            return other;
        }
        else if (other == IDENTITY) {
            return this;
        }
        else {
            return new Transformation(
                    this.scaleX * other.scaleX + this.skewX * other.skewY,
                    this.scaleX * other.skewX + this.skewX * other.scaleY,
                    this.scaleX * other.translateX + this.skewX * other.translateY + this.translateX,
                    this.skewY * other.scaleX + this.scaleY * other.skewY,
                    this.skewY * other.skewX + this.scaleY * other.scaleX,
                    this.skewY * other.translateX + this.scaleY * other.translateY + this.translateY);
        }
    }

    /**
     * Inverts the transformation. Calculates and returns the inverse
     * transformation, if such exists.
     *
     * @return the inverted transformation
     * @throws IllegalStateException if transformation is not invertible
     *
     * @since 1
     */
    public Transformation inverted() {
        final float d = this.scaleX * this.scaleY - this.skewX * this.skewY;

        if (Math.abs(d) <= Double.MIN_VALUE) {
            throw new IllegalStateException("Transformation is not invertible.");
        }

        return new Transformation(
                this.scaleY / d, -this.skewX / d,
                (this.skewX * this.translateY - this.scaleY * this.translateX) / d,
                -this.skewY / d, this.scaleX / d,
                (this.skewY * this.translateX - this.scaleX * this.translateY) / d);
    }

    /**
     * Adds a rotation to the transformation. Calculates and returns a new
     * transformation that results from adding a rotation after this
     * transformation.
     * <p>
     * Same as <tt>combinedWidth(Translation.rotate(angle)</tt>
     *
     * @param angle the rotation angle in radians
     * @return the combined new transformation
     *
     * @since 1
     */
    public Transformation rotatedBy(double angle) {
        final float sin = (float) Math.sin(angle);
        final float cos = (float) Math.cos(angle);

        return new Transformation(
                cos * this.scaleX + sin * this.skewX,
                -sin * this.scaleX + cos * this.skewX,
                this.translateX,
                cos * this.skewY + sin * this.scaleY,
                -sin * this.skewY + cos * this.scaleY,
                this.translateY);
    }

    /**
     * Adds a scaling to the transformation. Calculates and returns a new
     * transformation that results from adding a scaling after this
     * transformation.
     * <p>
     * Same as <tt>combinedWidth(Translation.scale(sx, sy)</tt>
     *
     * @param sx the scale factor along x-axis
     * @param sy the scale factor along the y-axis
     * @return the combined new transformation
     *
     * @since 1
     */
    public Transformation scaledBy(double sx, double sy) {
        return new Transformation(
                this.scaleX * (float) sx, this.skewX, this.translateX,
                this.skewY, this.scaleY * (float) sy, this.translateY);
    }

    /**
     * Applies the transformation to a vector. Calculates and returns the
     * resulting vector.
     *
     * @param v the vector to apply the transformation to
     * @return the transformed vector
     * @throws NullPointerException if <tt>v</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Vector transform(Vector v) {
        if (v == null) {
            throw new NullPointerException("v");
        }

        return new Vector(
                this.scaleX * v.x + this.skewX * v.y + this.translateX,
                this.skewY * v.x + this.scaleY * v.y + this.translateY);
    }

    public Vector transformSize(Vector v) {
        return new Vector(
                this.scaleX * v.x + this.skewX * v.y,
                this.skewY * v.x + this.scaleY * v.y);
    }

    /**
     * Adds a translation to the transformation. Calculates and returns a new
     * transformation that results from adding a translation after this
     * transformation.
     * <p>
     * Same as <tt>combinedWidth(Translation.translate(t)</tt>
     *
     * @param t the translation vector
     * @return the combined new transformation
     * @throws NullPointerException if <tt>t</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Transformation translatedBy(Vector t) {
        if (t == null) {
            throw new NullPointerException("t");
        }

        return new Transformation(
                this.scaleX, this.skewX, this.translateX + (float) t.x,
                this.skewY, this.scaleY, this.translateY + (float) t.y);
    }

    /**
     * Adds a translation to the transformation. Calculates and returns a new
     * transformation that results from adding a translation after this
     * transformation.
     * <p>
     * Same as <tt>combinedWidth(Translation.translate(tx, ty)</tt>
     *
     * @param tx the translation in direction of the x-axis
     * @param ty the translation in direction of the y-axis
     * @return the combined new transformation
     *
     * @since 1
     */
    public Transformation translatedBy(double tx, double ty) {
        return new Transformation(
                this.scaleX, this.skewX, this.translateX + (float) tx,
                this.skewY, this.scaleY, this.translateY + (float) ty);
    }

    private Transformation() {
        this(1f, 0f, 0f, 0f, 1f, 0f);
    }

    private Transformation(float scaleX, float skewX, float translateX,
                           float skewY, float scaleY, float translateY) {
        this.scaleX = scaleX;
        this.skewX = skewX;
        this.translateX = translateX;
        this.skewY = skewY;
        this.scaleY = scaleY;
        this.translateY = translateY;
    }
}
