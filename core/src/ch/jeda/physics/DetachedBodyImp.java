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

import ch.jeda.ui.Canvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DetachedBodyImp implements BodyImp {

    private final List<Shape> shapes;
    private double angle;
    private double angularDaming;
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
        this.shapes = new ArrayList<Shape>(Arrays.asList(oldImp.getShapes()));
        this.angle = oldImp.getAngleRad();
        this.angularDaming = oldImp.getAngularDamping();
        this.angularVelocity = oldImp.getAngularVelocity();
        this.damping = oldImp.getDamping();
        this.density = oldImp.getDensity();
        this.friction = oldImp.getFriction();
        this.rotationFixed = oldImp.isRotationFixed();
        this.type = oldImp.getType();
        this.vx = oldImp.getVx();
        this.vy = oldImp.getVy();
        this.x = oldImp.getX();
        this.y = oldImp.getY();
    }

    DetachedBodyImp() {
        this.shapes = new ArrayList<Shape>();
        this.angle = 0.0;
        this.angularDaming = 0.0;
        this.angularVelocity = 0.0;
        this.damping = 0.0;
        this.density = 1.0;
        this.friction = 0.0;
        this.rotationFixed = false;
        this.type = BodyType.DYNAMIC;
        this.vx = 0.0;
        this.vy = 0.0;
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public void addShape(final Shape shape) {
        this.shapes.add(shape);
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
        return this.angle;
    }

    @Override
    public double getAngularDamping() {
        return this.angularDaming;
    }

    @Override
    public double getAngularVelocity() {
        return this.angularVelocity;
    }

    @Override
    public double getDamping() {
        return this.damping;
    }

    @Override
    public double getDensity() {
        return this.density;
    }

    @Override
    public double getFriction() {
        return this.friction;
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
        return this.shapes.toArray(new Shape[this.shapes.size()]);
    }

    @Override
    public BodyType getType() {
        return this.type;
    }

    @Override
    public double getVx() {
        return this.vx;
    }

    @Override
    public double getVy() {
        return this.vy;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public boolean isRotationFixed() {
        return this.rotationFixed;
    }

    @Override
    public void setAngleRad(final double angle) {
        this.angle = angle;
    }

    @Override
    public void setAngularDamping(final double angularDamping) {
        this.angularDaming = angularDamping;
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
