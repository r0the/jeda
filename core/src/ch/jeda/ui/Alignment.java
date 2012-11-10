/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

import ch.jeda.Location;
import ch.jeda.Size;
import java.io.Serializable;

/**
 * This class implements alignment as a transformation that is applied when
 * drawing on a {@link Canvas}. The alignment specified how the element to be
 * drawn is positioned relative to the given coordinates (the alignment point).
 *
 * @version 1.0
 */
public final class Alignment implements Serializable {

    private static final Align LEFT_TOP_IMPL = new AlignLeftTop();
    private static final Align RIGHT_BOTTOM_IMPL = new AlignRightBottom();
    private static final Align CENTER_IMPL = new AlignCenter();
    /**
     * Align at the bottom vertically  and center horizontally.
     *
     * @since 1.0
     */
    public static final Alignment BOTTOM_CENTER = new Alignment(CENTER_IMPL, RIGHT_BOTTOM_IMPL);
    /**
     * Align at the bottom vertically  and left horizontally.
     *
     * @since 1.0
     */
    public static final Alignment BOTTOM_LEFT = new Alignment(LEFT_TOP_IMPL, RIGHT_BOTTOM_IMPL);
    /**
     * Align at the bottom vertically  and right horizontally.
     *
     * @since 1.0
     */
    public static final Alignment BOTTOM_RIGHT = new Alignment(RIGHT_BOTTOM_IMPL, RIGHT_BOTTOM_IMPL);
    /**
     * Center both vertically and horizontally.
     *
     * @since 1.0
     */
    public static final Alignment CENTER = new Alignment(CENTER_IMPL, CENTER_IMPL);
    /**
     * Center vertically and align left horizontally.
     *
     * @since 1.0
     */
    public static final Alignment LEFT = new Alignment(LEFT_TOP_IMPL, CENTER_IMPL);
    /**
     * Center vertically and align right horizontally.
     *
     * @since 1.0
     */
    public static final Alignment RIGHT = new Alignment(RIGHT_BOTTOM_IMPL, CENTER_IMPL);
    /**
     * Align at the top vertically and center horizontally.
     *
     * @since 1.0
     */
    public static final Alignment TOP_CENTER = new Alignment(CENTER_IMPL, LEFT_TOP_IMPL);
    /**
     * Align at the top vertically and left horizontally.
     *
     * @since 1.0
     */
    public static final Alignment TOP_LEFT = new Alignment(LEFT_TOP_IMPL, LEFT_TOP_IMPL);
    /**
     * Align at the top vertically and right horizontally.
     *
     * @since 1.0
     */
    public static final Alignment TOP_RIGHT = new Alignment(RIGHT_BOTTOM_IMPL, LEFT_TOP_IMPL);
    private final Align horizontal;
    private final Align vertical;

    private Alignment(Align horizontal, Align vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    Location align(Location anchor, Size size) {
        return new Location(
                this.horizontal.align(anchor.x, size.width),
                this.vertical.align(anchor.y, size.height));
    }

    private static abstract class Align implements Serializable {

        public abstract int align(int pos, int size);
    }

    private static class AlignLeftTop extends Align {

        @Override
        public int align(int pos, int size) {
            return pos;
        }
    }

    private static class AlignCenter extends Align {

        @Override
        public int align(int pos, int size) {
            return pos - size / 2;
        }
    }

    private static class AlignRightBottom extends Align {

        @Override
        public int align(int pos, int size) {
            return pos - size;
        }
    }
}
