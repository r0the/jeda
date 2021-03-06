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

import ch.jeda.Log;
import ch.jeda.ui.Canvas;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.JointDef;

final class Physics {

    private final Set<Body> bodySet;
    private final PhysicsDebugDraw debugDraw;
    private final org.jbox2d.dynamics.World imp;
    private Body[] bodies;
    private boolean debugging;
    private float scale;

    public Physics() {
        bodySet = new HashSet<Body>();
        debugDraw = new PhysicsDebugDraw();
        imp = new World(new Vec2(0f, 0f));
        imp.setDebugDraw(debugDraw);
        imp.setContactListener(new PhysicsContactListener());
        imp.setContactFilter(new PhysicsContactFilter());
        // Set default gravity. If default gravity is zero, it cannot be changed later on.
        imp.setGravity(new Vec2(0f, -9.81f));
        bodies = null;
        debugging = false;
        scale = 100f;
    }

    public void add(final Body body) {
        if (body == null || bodySet.contains(body)) {
            return;
        }

        final Physics oldPhysics = body.getPhysics();
        if (oldPhysics != null) {
            oldPhysics.remove(body);
        }

        bodySet.add(body);
        bodies = null;
        body.setPhysics(this);
    }

    public Body[] getBodies() {
        checkBodies();
        return Arrays.copyOf(bodies, bodies.length);
    }

    public float getScale() {
        return scale;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public void remove(final Body body) {
        if (body == null || !bodySet.contains(body)) {
            return;
        }

        bodySet.remove(body);
        bodies = null;
        body.setPhysics(null);
    }

    public void setDebugging(final boolean debugging) {
        this.debugging = debugging;
    }

    public void setGravity(final double ax, final double ay) {
        imp.setGravity(new Vec2((float) ax, (float) ay));
        org.jbox2d.dynamics.Body body = imp.getBodyList();
        while (body != null) {
            body.setAwake(true);
            body = body.m_next;
        }
    }

    public void setScale(final float scale) {
        this.scale = scale;
    }

    public void step(final double seconds) {
        imp.step((float) seconds, 6, 2);
        checkBodies();
        for (final Body body : bodies) {
            body.checkJoints();
        }
    }

    org.jbox2d.dynamics.Body createJBoxBody(final BodyDef bodyDef) {
        if (imp.isLocked()) {
            Log.e("Cannot create body, physics Engine is locked!");
        }

        return imp.createBody(bodyDef);
    }

    org.jbox2d.dynamics.joints.Joint createJBoxJoint(final JointDef jointDef) {
        if (imp.isLocked()) {
            Log.e("Cannot create joint, physics Engine is locked!");
        }

        return imp.createJoint(jointDef);
    }

    void destroyJBoxBody(final org.jbox2d.dynamics.Body jboxBody) {
        if (imp.isLocked()) {
            Log.e("Cannot destroy body, physics Engine is locked!");
        }

        imp.destroyBody(jboxBody);
    }

    void destroyJBoxJoint(final org.jbox2d.dynamics.joints.Joint jboxJoint) {
        if (imp.isLocked()) {
            Log.e("Cannot destroy joint, physics Engine is locked!");
        }

        imp.destroyJoint(jboxJoint);
    }

    void drawDebugOverlay(final Canvas canvas) {
        if (debugging) {
            debugDraw.setCanvas(canvas);
            imp.drawDebugData();
        }
    }

    float scaleLength(final float length) {
        return (float) (length / scale);
    }

    private void checkBodies() {
        if (bodies == null) {
            bodies = bodySet.toArray(new Body[bodySet.size()]);
        }
    }
}
