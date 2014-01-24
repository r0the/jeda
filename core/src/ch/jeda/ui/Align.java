/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

import java.io.Serializable;

abstract class Align implements Serializable {

    static final Align LEFT_TOP = new AlignLeftTop();
    static final Align RIGHT_BOTTOM = new AlignRightBottom();
    static final Align CENTER = new AlignCenter();

    abstract int align(int pos, int size);

    private static class AlignLeftTop extends Align {

        @Override
        public int align(final int pos, final int size) {
            return pos;
        }
    }

    private static class AlignCenter extends Align {

        @Override
        public int align(final int pos, final int size) {
            return pos - size / 2;
        }
    }

    private static class AlignRightBottom extends Align {

        @Override
        public int align(final int pos, final int size) {
            return pos - size;
        }
    }
}
