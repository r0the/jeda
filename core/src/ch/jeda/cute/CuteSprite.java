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
package ch.jeda.cute;

import ch.jeda.ui.Canvas;

public class CuteSprite extends CuteObject {

    private ControlState state;

    public CuteSprite(final CuteObjectType objectType) {
        this(objectType, 0f, 0f, 0f);
    }

    public CuteSprite(final CuteObjectType image, final float x, final float y, final float z) {
        super(image, x, y, z);
        this.state = CuteSprite.ControlState.IDLE;
    }

    @Override
    public void draw(final Canvas canvas, final float x, final float y) {
        super.draw(canvas, x, y);
    }

    public final boolean isMoving() {
        return this.state != CuteSprite.ControlState.IDLE;
    }

    public final void move(final Direction direction) {
        if (this.state == CuteSprite.ControlState.IDLE) {
            this.state = new CuteSprite.MoveToState(this.getX() + direction.getDx(),
                                                    this.getY() + direction.getDy(), this.getZ(), 5f);
        }
    }

    public final void say(final String message) {
        this.setMessage("says:" + message, 5f);
    }

    @Override
    public void update(final float dt) {
        this.state.update(dt, this);
    }

    private static abstract class ControlState {

        static final ControlState IDLE = new IdleState();

        abstract void update(float paramFloat, CuteSprite paramCuteSprite);
    }

    private static class IdleState extends ControlState {

        @Override
        void update(float dt, CuteSprite object) {
        }
    }

    private static class MoveToState extends ControlState {

        private final float speed;
        private final float targetX;
        private final float targetY;
        private final float targetZ;

        MoveToState(final float targetX, final float targetY, final float targetZ, final float speed) {
            this.speed = speed;
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
        }

        @Override
        void update(final float dt, final CuteSprite object) {
            float dx = this.targetX - object.getX();
            float dy = this.targetY - object.getY();
            float dz = this.targetZ - object.getZ();
            float d = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (Math.abs(d) < this.speed * dt) {
                object.setPosition(this.targetX, this.targetY, this.targetZ);
                object.setVx(0f);
                object.setVy(0f);
                object.setVz(0f);
                object.state = ControlState.IDLE;
            }
            else {
                float f = this.speed / d;
                object.setVx(dx * f);
                object.setVy(dy * f);
                object.setVz(dz * f);
            }
        }
    }
}
