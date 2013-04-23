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

import ch.jeda.Location;

class EntityStateMoveAcross extends EntityState {

    private static final double SPEED = 0.1;
    private Field toField;

    public EntityStateMoveAcross(Field toField) {
        this.toField = toField;
    }

    @Override
    Field nextField(Entity entity) {
        double dx = entity.getRenderPos().x - this.toField.getPosition().x;
        double dy = entity.getRenderPos().y - this.toField.getPosition().y;
        if (dx * dx + dy * dy < 0.5 * 0.5) {
            return this.toField;
        }
        else {
            return entity.field();
        }
    }

    @Override
    Vector3D nextRenderPos(Entity entity) {
        Vector3D currentPos = entity.getRenderPos();
        double x = currentPos.x;
        double y = currentPos.y;
        double z = currentPos.z;
        Location fieldPos = this.toField.getPosition();
        if (z < this.toField.height()) {
            z = Math.min(z + SPEED, this.toField.height());
        }
        else {
            if (x < fieldPos.x) {
                x = Math.min(x + SPEED, fieldPos.x);
            }
            else if (x > fieldPos.x) {
                x = Math.max(x - SPEED, fieldPos.x);
            }
            if (y < fieldPos.y) {
                y = Math.min(y + SPEED, fieldPos.y);
            }
            else if (y > fieldPos.y) {
                y = Math.max(y - SPEED, fieldPos.y);
            }
        }
        return new Vector3D(x, y, z);
    }

    @Override
    EntityState nextState(Entity entity) {
        double dx = entity.getRenderPos().x - this.toField.getPosition().x;
        double dy = entity.getRenderPos().y - this.toField.getPosition().y;
        if (dx * dx + dy * dy > 0.001) {
            return this;
        }
        else {
            return EntityStateIdle.INSTANCE;
        }
    }
}
