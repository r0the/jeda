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

import ch.jeda.ui.Color;
import ch.jeda.ui.Image;
import ch.jeda.ui.ImageFilter;

/**
 * An image filter that replaces a color with another.
 *
 * @since 2.0
 */
public class ReplaceColorFilter implements ImageFilter {

    private final Color oldColor;
    private final Color newColor;

    /**
     * Constructs a new color replacement filter.
     *
     * @param oldColor the color to be replaced
     * @param newColor the new color
     *
     * @since 2.0
     */
    public ReplaceColorFilter(final Color oldColor, final Color newColor) {
        if (oldColor == null) {
            throw new NullPointerException("oldColor");
        }

        if (newColor == null) {
            throw new NullPointerException("newColor");
        }

        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    @Override
    public Color apply(final Image image, final int x, final int y) {
        final Color color = image.getPixel(x, y);
        if (color.equals(this.oldColor)) {
            return this.newColor;
        }
        else {
            return color;
        }
    }
}
