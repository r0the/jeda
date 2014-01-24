/*
 * Copyright (C) 2011 - 2014 by Stefan Rothe
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
 * @since 1
 */
public enum Alignment {

    /**
     * Align at the bottom vertically and center horizontally.
     *
     * @since 1
     */
    BOTTOM_CENTER(Align.CENTER, Align.RIGHT_BOTTOM),
    /**
     * Align at the bottom vertically and left horizontally.
     *
     * @since 1
     */
    BOTTOM_LEFT(Align.LEFT_TOP, Align.RIGHT_BOTTOM),
    /**
     * Align at the bottom vertically and right horizontally.
     *
     * @since 1
     */
    BOTTOM_RIGHT(Align.RIGHT_BOTTOM, Align.RIGHT_BOTTOM),
    /**
     * Center both vertically and horizontally.
     *
     * @since 1
     */
    CENTER(Align.CENTER, Align.CENTER),
    /**
     * Center vertically and align left horizontally.
     *
     * @since 1
     */
    LEFT(Align.LEFT_TOP, Align.CENTER),
    /**
     * Center vertically and align right horizontally.
     *
     * @since 1
     */
    RIGHT(Align.RIGHT_BOTTOM, Align.CENTER),
    /**
     * Align at the top vertically and center horizontally.
     *
     * @since 1
     */
    TOP_CENTER(Align.CENTER, Align.LEFT_TOP),
    /**
     * Align at the top vertically and left horizontally.
     *
     * @since 1
     */
    TOP_LEFT(Align.LEFT_TOP, Align.LEFT_TOP),
    /**
     * Align at the top vertically and right horizontally.
     *
     * @since 1
     */
    TOP_RIGHT(Align.RIGHT_BOTTOM, Align.LEFT_TOP);
    private final Align horizontal;
    private final Align vertical;

    int alignX(final int x, final int width) {
        return this.horizontal.align(x, width);
    }

    int alignY(final int y, final int height) {
        return this.vertical.align(y, height);
    }

    private Alignment(final Align horizontal, final Align vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
