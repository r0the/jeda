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

import ch.jeda.Direction;

class EntityMoveEvent extends MapEvent {

    private final Direction direction;
    private final Entity entity;

    EntityMoveEvent(Entity entity, Direction direction) {
        this.direction = direction;
        this.entity = entity;
    }

    @Override
    void evaluate() {
        Field fromField = this.entity.field();
        if (fromField == null) {
            // Can't move if entity is not on a field.
            return;
        }
        Field toField = fromField.neighbor(this.direction);
        if (toField == null) {
            // Can't move outside map.
            return;
        }
        this.entity.doStartMove(toField);
    }
}
