/*
 * Copyright (C) 2014 by Stefan Rothe
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

/**
 * Represents a rotated bitmap image. A bitmap image can be loaded from an image file.
 *
 * @since 1.0
 */
public final class RotatedImage {

    private final Image[] images;

    /**
     * Constructs a rotated image from a file. Loads the contents of the specified image file. Currently, the image file
     * formats JPEG, and PNG are supported.
     * <p>
     * The file can either be located on the local computer, or in the project. To read a resource file, put 'res:' in
     * front of the file path:</p>
     *
     * <tt>RotatedImage sample = new RotatedImage("res:/sample.png");</tt>
     * <p>
     * Rotates the image by the specified number of steps.</p>
     *
     * @param path path to the image file
     * @param steps the number of steps for a full rotation of the image
     * @throws IllegalArgumentException if <tt>steps</tt> is smaller than 1
     *
     * @since 1.0
     */
    public RotatedImage(final String path, final int steps) {
        this(new Image(path), steps);
    }

    /**
     * @since 1.0
     */
    public RotatedImage(final Image image, final int steps) {
        if (steps < 1) {
            throw new IllegalArgumentException("steps");
        }

        this.images = new Image[steps];
        this.images[0] = image;
        for (int i = 1; i < steps; ++i) {
            this.images[i] = image.rotate(i * 2.0 * Math.PI / steps);
        }
    }

    /**
     * Returns the image with the rotation closest matching the specified angle.
     *
     * @param angle
     * @return rotated image
     *
     * @since 1.0
     */
    public Image getImage(final double angle) {
        int index = (int) Math.round(angle / (2.0 * Math.PI) * this.images.length);
        while (index < 0) {
            index = index + this.images.length;
        }

        return this.images[index % this.images.length];
    }
}
