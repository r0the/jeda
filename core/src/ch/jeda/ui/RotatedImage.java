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
 * @deprecated Use {@link ch.jeda.ui.Canvas#setRotation(double)} instead.
 */
public final class RotatedImage {

    private final Image[] images;

    /**
     * @deprecated Use {@link ch.jeda.ui.Canvas#setRotation(double)} instead.
     */
    public RotatedImage(final String path, final int steps) {
        this(new Image(path), steps);
    }

    /**
     * @deprecated Use {@link ch.jeda.ui.Canvas#setRotation(double)} instead.
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
     * @deprecated Use {@link ch.jeda.ui.Canvas#setRotation(double)} instead.
     */
    public Image getImage(final double angle) {
        int index = (int) Math.round(angle / (2.0 * Math.PI) * this.images.length);
        while (index < 0) {
            index = index + this.images.length;
        }

        return this.images[index % this.images.length];
    }
}
