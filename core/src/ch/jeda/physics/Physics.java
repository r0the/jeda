/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.physics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

final class Physics {

    private final Set<Body> bodySet;
    private Body[] bodies;
    private boolean debugging;
    private final org.jbox2d.dynamics.World imp;
    private double scale;
    private boolean paused;

    public Physics() {
        this.bodySet = new HashSet<Body>();
        this.bodies = null;
        this.debugging = false;
        this.imp = new World(new Vec2(0f, 0f));
        this.imp.setContactListener(new PhysicsContactListener());
        // Set default gravity. If default gravity is zero, it cannot be changed later on.
        this.imp.setGravity(new Vec2(0f, 9.81f));
        this.paused = false;
        this.scale = 10.0;
    }

    public void add(final Body body) {
        if (body == null || this.bodySet.contains(body)) {
            return;
        }

        final Physics oldPhysics = body.getPhysics();
        if (oldPhysics != null) {
            oldPhysics.remove(body);
        }

        this.bodySet.add(body);
        this.bodies = null;
        body.setPhysics(this);
    }

    public Body[] getBodies() {
        this.checkBodies();
        return Arrays.copyOf(this.bodies, this.bodies.length);
    }

    public double getScale() {
        return this.scale;
    }

    public boolean isDebugging() {
        return this.debugging;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void remove(final Body body) {
        if (body == null || !this.bodySet.contains(body)) {
            return;
        }

        this.bodySet.remove(body);
        this.bodies = null;
        body.setPhysics(null);
    }

    public void setDebugging(final boolean debugging) {
        this.debugging = debugging;
    }

    public void setGravity(final double ax, final double ay) {
        this.imp.setGravity(new Vec2((float) ax, (float) ay));
    }

    public void setScale(final double scale) {
        this.scale = scale;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    public void step(final double seconds) {
        if (!this.paused) {
            this.imp.step((float) seconds, 6, 2);
            this.checkBodies();
            for (final Body body : this.bodies) {
                body.step(seconds);
            }
        }
    }

    org.jbox2d.dynamics.Body createBodyImp(final BodyDef bodyDef) {
        return this.imp.createBody(bodyDef);
    }

    void destroyBodyImp(final org.jbox2d.dynamics.Body imp) {
        this.imp.destroyBody(imp);
    }

    float scaleLength(final double length) {
        return (float) (length / this.scale);
    }

    private void checkBodies() {
        if (this.bodies == null) {
            this.bodies = this.bodySet.toArray(new Body[this.bodySet.size()]);
        }
    }
}
