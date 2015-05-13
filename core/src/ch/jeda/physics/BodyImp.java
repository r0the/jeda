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

interface BodyImp {

    void addShape(Shape shape);

    void applyForce(double fx, double fy);

    void applyTorque(double torque);

    boolean belongsTo(Physics physics);

    void destroy();

    void drawOverlay(Canvas canvas);

    double getAngleRad();

    double getAngularDamping();

    double getAngularVelocity();

    double getDamping();

    double getDensity();

    double getFriction();

    double getMass();

    Physics getPhysics();

    Shape[] getShapes();

    BodyType getType();

    double getVx();

    double getVy();

    double getX();

    double getY();

    boolean isRotationFixed();

    void setAngleRad(double angle);

    void setAngularDamping(double angularDamping);

    void setAngularVelocity(double angularVelocity);

    void setDamping(double damping);

    void setDensity(double density);

    void setFriction(double friction);

    void setPosition(double x, double y);

    void setRotationFixed(boolean rotationFixed);

    void setType(BodyType type);

    void setVelocity(double vx, double vy);
}
