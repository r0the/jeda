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
import ch.jeda.MathF;
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DetachedBodyImp implements BodyImp {

    private final List<Shape> shapes;
    private float angle;
    private float angularDamping;
    private float angularVelocity;
    private float damping;
    private float density;
    private float friction;
    private boolean rotationFixed;
    private BodyType type;
    private float vx;
    private float vy;
    private float x;
    private float y;

    DetachedBodyImp(final BodyImp oldImp) {
        shapes = new ArrayList<Shape>(Arrays.asList(oldImp.getShapes()));
        angle = oldImp.getAngleRad();
        angularDamping = oldImp.getAngularDamping();
        angularVelocity = oldImp.getAngularVelocity();
        damping = oldImp.getDamping();
        density = oldImp.getDensity();
        friction = oldImp.getFriction();
        rotationFixed = oldImp.isRotationFixed();
        type = oldImp.getType();
        vx = oldImp.getVx();
        vy = oldImp.getVy();
        x = oldImp.getX();
        y = oldImp.getY();
    }

    DetachedBodyImp() {
        shapes = new ArrayList<Shape>();
        angle = 0f;
        angularDamping = 0f;
        angularVelocity = 0f;
        damping = 0f;
        density = 1f;
        friction = 0f;
        rotationFixed = false;
        type = BodyType.DYNAMIC;
        vx = 0f;
        vy = 0f;
        x = 0f;
        y = 0f;
    }

    @Override
    public void addShape(final Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void applyForce(final float fx, final float fy) {
        Log.w("Cannot apply a force to a body that isn't part of a physics simulation.");
    }

    @Override
    public void applyForce(final float fx, final float fy, final float x, final float y) {
        Log.w("Cannot apply a force to a body that isn't part of a physics simulation.");
    }

    @Override
    public void applyLocalForceRad(final float force, final float angle) {
        Log.w("Cannot apply a force to a body that isn't part of a physics simulation.");
    }

    @Override
    public void applyLocalForceRad(final float force, final float angle, final float x, final float y) {
        Log.w("Cannot apply a force to a body that isn't part of a physics simulation.");
    }

    @Override
    public void applyTorque(final float torque) {
        Log.w("Cannot apply a torque to a body that isn't part of a physics simulation.");
    }

    @Override
    public boolean belongsTo(final Physics physics) {
        return false;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void drawDebugOverlay(final Canvas canvas) {
    }

    @Override
    public float getAngleRad() {
        return angle;
    }

    @Override
    public float getAngularDamping() {
        return angularDamping;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getDamping() {
        return damping;
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public float getMass() {
        return 0f;
    }

    @Override
    public Physics getPhysics() {
        return null;
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[shapes.size()]);
    }

    @Override
    public BodyType getType() {
        return type;
    }

    @Override
    public float getVelocity() {
        return MathF.sqrt(vx * vx + vy * vy);
    }

    @Override
    public float getVx() {
        return vx;
    }

    @Override
    public float getVy() {
        return vy;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public boolean isRotationFixed() {
        return rotationFixed;
    }

    @Override
    public void setAngleRad(final float angle) {
        this.angle = angle;
    }

    @Override
    public void setAngularDamping(final float angularDamping) {
        this.angularDamping = angularDamping;
    }

    @Override
    public void setAngularVelocity(final float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public void setDamping(final float damping) {
        this.damping = damping;
    }

    @Override
    public void setDensity(final float density) {
        this.density = density;
    }

    @Override
    public void setFriction(final float friction) {
        this.friction = friction;
    }

    @Override
    public void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setRotationFixed(final boolean rotationFixed) {
        this.rotationFixed = rotationFixed;
    }

    @Override
    public void setType(final BodyType type) {
        this.type = type;
    }

    @Override
    public void setVelocity(final float vx, final float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public boolean shouldDrawOverlay() {
        return false;
    }
}
