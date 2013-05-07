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
package ch.jeda.world;

import ch.jeda.ui.Canvas;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class MovableBody extends Body {

    public MovableBody() {
        this.bodyDef.type = BodyType.DYNAMIC;
    }

    public final void applyForceToCenter(final float fx, final float fy) {
        if (this.body != null) {
            this.body.applyForceToCenter(new Vec2(fx, fy));
        }
    }

    public final void applyTorque(float torque) {
        if (this.body != null) {
            this.body.applyTorque(torque);
        }
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    public final double getDirection() {
        if (this.body == null) {
            final Vec2 velocity = this.bodyDef.linearVelocity;
            return Math.atan2(velocity.y, velocity.x);
        }
        else {
            final Vec2 velocity = this.body.getLinearVelocity();
            return Math.atan2(velocity.y, velocity.x);
        }
    }

    public final float getSpeed() {
        if (this.body == null) {
            return this.bodyDef.linearVelocity.length();
        }
        else {
            return this.body.getLinearVelocity().length();
        }
    }

    public final void setAngularDamping(float d) {
        if (this.body == null) {
            this.bodyDef.angularDamping = d;
        }
        else {
            this.body.setAngularDamping(d);
        }
    }

    public final void setAngularVelocity(float w) {
        if (this.body == null) {
            this.bodyDef.angularVelocity = w;
        }
        else {
            this.body.setAngularVelocity(w);
        }
    }

    public final void setDirection(final double direction) {
        final float oldSpeed = this.getSpeed();
        this.doSetLinearVelocity(new Vec2((float) (oldSpeed * Math.cos(direction)),
                                          (float) (oldSpeed * Math.sin(direction))));
    }

    public final void setLinearDamping(final float d) {
        if (this.body == null) {
            this.bodyDef.linearDamping = d;
        }
        else {
            this.body.setLinearDamping(d);
        }
    }

    public final void setLinearVelocity(final float vx, final float vy) {
        if (this.body == null) {
            this.bodyDef.linearVelocity.x = vx;
            this.bodyDef.linearVelocity.y = vy;
        }
        else {
            this.body.setLinearVelocity(new Vec2(vx, vy));
        }
    }

    public final void setSpeed(final float speed) {
        if (speed < 0f) {
            throw new IllegalArgumentException("speed");
        }

        final double oldDirection = this.getDirection();
        this.doSetLinearVelocity(new Vec2((float) (speed * Math.cos(oldDirection)),
                                          (float) (speed * Math.sin(oldDirection))));
    }

    private void doSetLinearVelocity(final Vec2 velocity) {
        if (this.body == null) {
            this.bodyDef.linearVelocity.set(velocity);
        }
        else {
            this.body.setLinearVelocity(velocity);
        }
    }
}
