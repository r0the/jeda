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
package ch.jeda.ui;

/**
 * Defines the interface for an image filter.
 *
 * @since 2.1
 */
public interface ImageFilter {

    /**
     * Determines the new color of a pixel. This method is called by the
     * {@link ch.jeda.ui.Image#filter(ch.jeda.ui.ImageFilter)} method to determine the color of the pixel at the
     * coordinates (<tt>x</tt>, <tt>y</tt>) of the new bitmap.
     *
     * @param source the source image
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the color of the pixel
     */
    Color apply(Image source, int x, int y);
}
