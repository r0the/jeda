/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
 * This class provides constants to specify alignment when drawing on a {@link Canvas}. The alignment specifies how the
 * element to be drawn is positioned relative to the given coordinates (the alignment point).
 * <p>
 * <strong>Example:</strong>
 * <pre><code> Window window = new Window(200, 200);
 * window.drawString(190, 100, "Alignment Example", Alignment.TOP_RIGHT);
 * window.fillRectangle(190, 100, 50, 50, Alignment.BOTTOM_RIGHT);
 * window.setColor(Color.RED);
 * window.fillCircle(190, 100, 4);</code></pre>
 *
 * @since 1.0
 */
public enum Alignment {

    /**
     * Align at the bottom vertically and center horizontally.
     *
     * @since 1.0
     */
    BOTTOM_CENTER(Align.CENTER, Align.RIGHT_BOTTOM, NewAlign.MIDDLE, NewAlign.MIN),
    /**
     * Align at the bottom vertically and left horizontally.
     *
     * @since 1.0
     */
    BOTTOM_LEFT(Align.LEFT_TOP, Align.RIGHT_BOTTOM, NewAlign.MIN, NewAlign.MIN),
    /**
     * Align at the bottom vertically and right horizontally.
     *
     * @since 1.0
     */
    BOTTOM_RIGHT(Align.RIGHT_BOTTOM, Align.RIGHT_BOTTOM, NewAlign.MAX, NewAlign.MIN),
    /**
     * Center both vertically and horizontally.
     *
     * @since 1.0
     */
    CENTER(Align.CENTER, Align.CENTER, NewAlign.MIDDLE, NewAlign.MIDDLE),
    /**
     * Center vertically and align left horizontally.
     *
     * @since 1.0
     */
    LEFT(Align.LEFT_TOP, Align.CENTER, NewAlign.MIN, NewAlign.MIDDLE),
    /**
     * Center vertically and align right horizontally.
     *
     * @since 1.0
     */
    RIGHT(Align.RIGHT_BOTTOM, Align.CENTER, NewAlign.MAX, NewAlign.MIDDLE),
    /**
     * Align at the top vertically and center horizontally.
     *
     * @since 1.0
     */
    TOP_CENTER(Align.CENTER, Align.LEFT_TOP, NewAlign.MIDDLE, NewAlign.MAX),
    /**
     * Align at the top vertically and left horizontally.
     *
     * @since 1.0
     */
    TOP_LEFT(Align.LEFT_TOP, Align.LEFT_TOP, NewAlign.MIN, NewAlign.MAX),
    /**
     * Align at the top vertically and right horizontally.
     *
     * @since 1.0
     */
    TOP_RIGHT(Align.RIGHT_BOTTOM, Align.LEFT_TOP, NewAlign.MAX, NewAlign.MAX);
    private final Align horizontal;
    private final Align vertical;
    final NewAlign horiz;
    final NewAlign vert;

    private Alignment(final Align horizontal, final Align vertical, final NewAlign horiz, final NewAlign vert) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.horiz = horiz;
        this.vert = vert;
    }

    int oldAlignX(final int x, final int width) {
        return horizontal.align(x, width);
    }

    int oldAlignY(final int y, final int height) {
        return vertical.align(y, height);
    }
}
