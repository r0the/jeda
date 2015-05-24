/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda.cute;

import ch.jeda.ui.Window;

public class CuteSprite extends CuteObject {

    private ControlState state;

    public CuteSprite(final CuteObjectType objectType) {
        this(objectType, 0f, 0f, 0f);
    }

    public CuteSprite(final CuteObjectType image, final double x, final double y, final double z) {
        super(image, x, y, z);
        state = CuteSprite.ControlState.IDLE;
    }

    public final boolean isMoving() {
        return state != CuteSprite.ControlState.IDLE;
    }

    public final void move(final Direction direction) {
        move(direction, getIntZ());
    }

    public final void move(final Direction direction, int targetZ) {
        if (state == CuteSprite.ControlState.IDLE) {
            state = new CuteSprite.MoveToState(getX() + direction.getDx(),
                                               getY() + direction.getDy(), targetZ, 5.0);
        }
    }

    public final void say(final String message) {
        setMessage("says:" + message, 5f);
    }

    @Override
    protected void draw(final Window canvas, final double x, final double y) {
        super.draw(canvas, x, y);
    }

    @Override
    protected void update(final double dt) {
        state.update(dt, this);
    }

    private static abstract class ControlState {

        static final ControlState IDLE = new IdleState();

        abstract void update(double dt, CuteSprite object);
    }

    private static class IdleState extends ControlState {

        @Override
        void update(double dt, CuteSprite object) {
        }
    }

    private static class MoveToState extends ControlState {

        private final double speed;
        private final double targetX;
        private final double targetY;
        private final double targetZ;

        MoveToState(final double targetX, final double targetY, final double targetZ, final double speed) {
            this.speed = speed;
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
        }

        @Override
        void update(final double dt, final CuteSprite object) {
            double dx = targetX - object.getX();
            double dy = targetY - object.getY();
            double dz = targetZ - object.getZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (Math.abs(d) < speed * dt) {
                object.setPosition(targetX, targetY, targetZ);
                object.setVx(0.0);
                object.setVy(0.0);
                object.setVz(0.0);
                object.state = ControlState.IDLE;
            }
            else {
                double f = speed / d;
                object.setVx(dx * f);
                object.setVy(dy * f);
                object.setVz(dz * f);
            }
        }
    }
}
