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

    void addSensor(Sensor sensor);

    void addShape(Shape shape);

    void applyForce(float fx, float fy);

    void applyForce(float fx, float fy, float x, float y);

    void applyLocalForceRad(float force, float angle);

    void applyLocalForceRad(float force, float angle, float x, float y);

    void applyTorque(float torque);

    boolean belongsTo(Physics physics);

    void destroy();

    void drawOverlay(Canvas canvas);

    float getAngleRad();

    float getAngularDamping();

    float getAngularVelocity();

    float getDamping();

    float getDensity();

    float getDirectionRad();

    float getFriction();

    org.jbox2d.dynamics.Body getJBoxBody();

    float getMass();

    Physics getPhysics();

    Shape[] getShapes();

    BodyType getType();

    float getVelocity();

    float getVx();

    float getVy();

    float getX();

    float getY();

    boolean isRotationFixed();

    void setAngleRad(float angle);

    void setAngularDamping(float angularDamping);

    void setAngularVelocity(float angularVelocity);

    void setDamping(float damping);

    void setDensity(float density);

    void setFriction(float friction);

    void setGravityIgnored(boolean gravityIgnored);

    void setPosition(float x, float y);

    void setRotationFixed(boolean rotationFixed);

    void setType(BodyType type);

    void setVelocity(float vx, float vy);
}
