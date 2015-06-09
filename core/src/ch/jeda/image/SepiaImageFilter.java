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

import ch.jeda.ui.Image;
import ch.jeda.ui.ImageFilter;
import ch.jeda.ui.Color;

public class SepiaImageFilter implements ImageFilter {

    private static final int SEPIA_DEPTH = 20;
    private final int intensity;

    public SepiaImageFilter(final int intensity) {
        this.intensity = intensity;
    }

    @Override
    public Color apply(final Image source, final int x, final int y) {
        final Color color = source.getPixel(x, y);
        final int gray = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
        return new Color(gray + 2 * SEPIA_DEPTH, gray + SEPIA_DEPTH, gray - this.intensity);
    }
}
