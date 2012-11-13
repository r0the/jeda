/*
 * Copyright (C) 2011 by Stefan Rothe
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
package ch.jeda.blocks;

class Movement {

    enum State {

        Upwards, AcrossStart, Across, AcrossEnd, Downwards, Finished
    };
    final Direction direction;
    final Field sourceField;
    final Field targetField;
    State state;
    int step;

    Movement(Field sourceField, Field targetField, Direction direction) {
        this.direction = direction;
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.state = State.Upwards;
        this.step = 5;
    }

    int slope() {
        return this.sourceField.slope(this.direction);
    }

    int targetHeight() {
        return this.targetField.height();
    }

    int sourceHeight() {
        return this.sourceField.height();
    }
}
