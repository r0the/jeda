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

class EntityStateJumpUp extends EntityState {

    private static final double SPEED = 0.2;
    private final double targetZ;

    public EntityStateJumpUp(double targetZ) {
        this.targetZ = targetZ;
    }

    @Override
    Field nextField(Entity entity) {
        return entity.field();
    }

    @Override
    Vector3D nextRenderPos(Entity entity) {
        Vector3D currentPos = entity.getRenderPos();
        return new Vector3D(currentPos.x, currentPos.y, currentPos.z + SPEED);
    }

    @Override
    EntityState nextState(Entity entity) {
        if (entity.getRenderPos().z <= targetZ) {
            return this;
        }
        else {
            return EntityStateFallDown.INSTANCE;
        }
    }
}