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
package ch.jeda.image;

import ch.jeda.ui.ImageFilter;
import ch.jeda.ui.Bitmap;
import ch.jeda.ui.Color;

/**
 * Represents a mask image filter.
 *
 * @since 2.0
 */
public class MaskImageFilter implements ImageFilter {

    /**
     * A blur image filter using a 5x5 matrix.
     *
     * @since 2.0
     */
    public static final ImageFilter BLUR = new MaskImageFilter(new double[][]{
        {0, 0, 1, 0, 0},
        {0, 1, 1, 1, 0},
        {1, 1, 1, 1, 1},
        {0, 1, 1, 1, 0},
        {0, 0, 1, 0, 0}});
    /**
     * An edge detection image filter using a 3x3 matrix.
     *
     * @since 2.0
     */
    public static final ImageFilter EDGE = new MaskImageFilter(new double[][]{
        {-1, -1, -1},
        {-1, 8, -1},
        {-1, -1, -1}}, 1.0);
    /**
     * A diagonal motion blur image filter using a 9x9 matrix.
     *
     * @since 2.0
     */
    public static final ImageFilter MOTION_BLUR = new MaskImageFilter(new double[][]{
        {1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1}});
    /**
     * A sharpen image filter using a 3x3 matrix.
     *
     * @since 2.0
     */
    public static final ImageFilter SHARPEN = new MaskImageFilter(new double[][]{
        {0, -1, 0},
        {-1, 5, -1},
        {0, -1, 0}});
    private final double factor;
    private final int filterSize;
    private final double[][] matrix;

    /**
     * Constructs a new mask image filter.
     *
     * @param matrix the matrix
     * @throws NullPointerException if <tt>matrix</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if the matrix is not a square matrix with an odd number of rows/columns
     */
    public MaskImageFilter(final double[][] matrix) {
        if (matrix == null) {
            throw new NullPointerException("matrix");
        }

        if (matrix.length % 2 == 0) {
            throw new IllegalArgumentException("The matrix must have an odd number of columns.");
        }

        if (matrix[0].length != matrix.length) {
            throw new IllegalArgumentException("The matrix must be square.");
        }

        this.matrix = matrix;
        this.filterSize = matrix.length;
        double f = 0.0;
        for (int x = 0; x < this.filterSize; ++x) {
            for (int y = 0; y < this.filterSize; ++y) {
                f = f + this.matrix[x][y];
            }
        }

        this.factor = 1.0 / f;
    }

    /**
     * Constructs a new mask image filter with a custom factor.
     *
     * @param matrix the matrix
     * @param factor the factor
     * @throws NullPointerException if <tt>matrix</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if the matrix is not a square matrix with an odd number of rows/columns
     */
    public MaskImageFilter(final double[][] matrix, double factor) {
        if (matrix == null) {
            throw new NullPointerException("matrix");
        }

        if (matrix.length % 2 == 0) {
            throw new IllegalArgumentException("The matrix must have an odd number of columns.");
        }

        if (matrix[0].length != matrix.length) {
            throw new IllegalArgumentException("The matrix must be square.");
        }

        this.matrix = matrix;
        this.filterSize = matrix.length;
        this.factor = factor;
    }

    @Override
    public Color apply(final Bitmap source, final int x, final int y) {
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;
        for (int filterX = 0; filterX < this.filterSize; ++filterX) {
            for (int filterY = 0; filterY < this.filterSize; ++filterY) {
                int px = x - filterSize / 2 + filterX;
                int py = y - filterSize / 2 + filterY;
                Color pixel = source.getPixel(px, py);
                red = red + pixel.getRed() * matrix[filterX][filterY];
                green = green + pixel.getGreen() * matrix[filterX][filterY];
                blue = blue + pixel.getBlue() * matrix[filterX][filterY];
            }
        }

        red = red * this.factor;
        green = green * this.factor;
        blue = blue * this.factor;
        return new Color((int) red, (int) green, (int) blue);
    }
}
