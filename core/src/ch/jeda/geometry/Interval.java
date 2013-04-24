/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.geometry;

class Interval {

    final float min;
    final float max;

    Interval(final float min, final float max) {
        this.min = min;
        this.max = max;
    }

    final boolean contains(final Interval other) {
        return (this.min <= other.min && other.max <= this.max);
    }

    final boolean overlapsWith(final Interval other) {
        return !(other.max < this.min || this.max < other.min);
    }

    final float overlap(final Interval other) {
        if (this.overlapsWith(other)) {
            return Math.min(this.max, other.max) - Math.max(this.min, other.min);
        }
        else {
            return 0f;
        }
    }
}
