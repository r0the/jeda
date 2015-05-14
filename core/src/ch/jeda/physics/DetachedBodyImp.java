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

import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DetachedBodyImp implements BodyImp {

    private final List<Shape> shapes;
    private double angle;
    private double angularDamping;
    private double angularVelocity;
    private double damping;
    private double density;
    private double friction;
    private boolean rotationFixed;
    private BodyType type;
    private double vx;
    private double vy;
    private double x;
    private double y;

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
        angle = 0.0;
        angularDamping = 0.0;
        angularVelocity = 0.0;
        damping = 0.0;
        density = 1.0;
        friction = 0.0;
        rotationFixed = false;
        type = BodyType.DYNAMIC;
        vx = 0.0;
        vy = 0.0;
        x = 0.0;
        y = 0.0;
    }

    @Override
    public void addShape(final Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void applyForce(final double fx, final double fy) {
        throw new IllegalStateException("Cannot apply force to a body that has not been added to a physics.");
    }

    @Override
    public void applyTorque(final double torque) {
        throw new IllegalStateException("Cannot apply torque to a body that has not been added to a physics.");
    }

    @Override
    public boolean belongsTo(final Physics physics) {
        return false;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void drawOverlay(final Canvas canvas) {
    }

    @Override
    public double getAngleRad() {
        return angle;
    }

    @Override
    public double getAngularDamping() {
        return angularDamping;
    }

    @Override
    public double getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public double getDamping() {
        return damping;
    }

    @Override
    public double getDensity() {
        return density;
    }

    @Override
    public double getFriction() {
        return friction;
    }

    @Override
    public double getMass() {
        return 0.0;
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
    public double getVx() {
        return vx;
    }

    @Override
    public double getVy() {
        return vy;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean isRotationFixed() {
        return rotationFixed;
    }

    @Override
    public void setAngleRad(final double angle) {
        this.angle = angle;
    }

    @Override
    public void setAngularDamping(final double angularDamping) {
        this.angularDamping = angularDamping;
    }

    @Override
    public void setAngularVelocity(final double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public void setDamping(final double damping) {
        this.damping = damping;
    }

    @Override
    public void setDensity(final double density) {
        this.density = density;
    }

    @Override
    public void setFriction(final double friction) {
        this.friction = friction;
    }

    @Override
    public void setPosition(final double x, final double y) {
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
    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }
}
