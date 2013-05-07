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

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

public abstract class PhysicsObject extends WorldObject {

    final BodyDef bodyDef;
    Body body;

    public final float getX() {
        if (this.body == null) {
            return this.bodyDef.position.x;
        }
        else {
            return this.body.getPosition().x;
        }
    }

    public final float getY() {
        if (this.body == null) {
            return this.bodyDef.position.y;
        }
        else {
            return this.body.getPosition().y;
        }
    }

    public final void setPosition(final float x, final float y) {
        if (this.body == null) {
            this.bodyDef.position.x = x;
            this.bodyDef.position.y = y;
        }
        else {
            this.body.setTransform(new Vec2(x, y), this.body.getAngle());
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.getClass().getName());
        result.append("(x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(")");
        return result.toString();
    }

    protected PhysicsObject() {
        this.bodyDef = new BodyDef();
    }

    void addToWorld(final org.jbox2d.dynamics.World physics) {
        if (this.body == null) {
            this.body = physics.createBody(this.bodyDef);
        }
    }

    void removeFromWorld(final org.jbox2d.dynamics.World physics) {
        if (this.body != null) {
            this.bodyDef.angle = this.body.getAngle();
            this.bodyDef.angularDamping = this.body.getAngularDamping();
            this.bodyDef.angularVelocity = this.body.getAngularVelocity();
            this.bodyDef.linearDamping = this.body.getLinearDamping();
            this.bodyDef.linearVelocity = this.body.getLinearVelocity();
            this.bodyDef.position = this.body.getPosition();
            physics.destroyBody(this.body);
            this.body = null;
        }
    }
}
