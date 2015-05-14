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
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Element;
import ch.jeda.ui.Image;

/**
 * Represents a body.
 *
 * @since 2.0
 */
public class Body extends Element {

    private Image image;
    private BodyImp imp;

    /**
     * Constructs a body.
     *
     * @since 2.0
     */
    public Body() {
        image = null;
        imp = new DetachedBodyImp();
    }

    /**
     * Adds a shape to this body.
     *
     * @param shape the shape to add
     *
     * @since 2.0
     */
    public final void addShape(final Shape shape) {
        imp.addShape(shape);
    }

    /**
     * Applies a force to this body. The force is applied to the body's center of mass in the direction of the body's
     * current angle. The unit of the force is Newton.
     *
     * @param f the force in Newton
     *
     * @since 2.0
     */
    public void applyForce(final double f) {
        final double angle = imp.getAngleRad();
        applyForce(f * Math.cos(angle), f * Math.sin(angle));
    }

    /**
     * Applies a force to this body. The unit of the force is Newton.
     *
     * @param fx the horizontal component of the force in Newton
     * @param fy the vertical component of the force in Newton
     *
     * @since 2.0
     */
    public void applyForce(final double fx, final double fy) {
        imp.applyForce(fx, fy);
    }

    /**
     * Applies a torque to the body. The unit of the torque is Newton meter.
     *
     * @param torque the torque in Newton meter
     *
     * @since 2.0
     */
    public void applyTorque(final double torque) {
        imp.applyTorque(torque);
    }

    /**
     * Returns the current angle of the body in degrees.
     *
     * @return the current angle of the body in degrees
     *
     * @see #getAngleRad()
     * @see #setAngleDeg(double)
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public final double getAngleDeg() {
        return Math.toDegrees(imp.getAngleRad());
    }

    /**
     * Returns the current angle of the body in radians.
     *
     * @return the current angle of the body in radians
     *
     * @see #getAngleDeg()
     * @see #setAngleDeg(double)
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public final double getAngleRad() {
        return imp.getAngleRad();
    }

    /**
     * Returns the angular damping of the body.
     *
     * @return the angular damping of the body
     *
     * @see #setAngularDamping(double)
     * @since 2.0
     */
    public final double getAngularDamping() {
        return imp.getAngularDamping();
    }

    /**
     * Returns the current angular velocity of the body in radians per second.
     *
     * @return the current angular velocity of the body in radians per second
     *
     * @see #setAngularVelocity(double)
     * @since 2.0
     */
    public final double getAngularVelocity() {
        return imp.getAngularVelocity();
    }

    /**
     * Returns the linear damping of the body.
     *
     * @return the linear damping of the body
     *
     * @see #setDamping(double)
     * @since 2.0
     */
    public final double getDamping() {
        return imp.getDamping();
    }

    /**
     * Returns the density of the body. The unit of the density is kilograms per square meter
     *
     * @return the density of the body in kilograms per square meter
     *
     * @see #setDensity(double)
     * @since 2.0
     */
    public final double getDensity() {
        return imp.getDensity();
    }

    /**
     * Returns the friction of the body.
     *
     * @return the friction of the body
     *
     * @see #setFriction(double)
     * @since 2.0
     */
    public final double getFriction() {
        return imp.getFriction();
    }

    /**
     * Returns the image of the body.
     *
     * @return the image of the body
     *
     * @see #setImage(ch.jeda.ui.Image)
     * @since 2.0
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the mass of the body in kilograms. The body's mass is determined by it's density and the areas of it's
     * shapes.
     *
     * @return the mass of the body in kilograms
     *
     * @see #addShape(ch.jeda.physics.Shape)
     * @see #getDensity()
     * @see #getShapes()
     * @see #setDensity(double)
     * @since 2.0
     */
    public double getMass() {
        return imp.getMass();
    }

    /**
     * Returns the shapes of this body.
     *
     * @return the shapes of this body
     *
     * @since 2.0
     */
    public final Shape[] getShapes() {
        return imp.getShapes();
    }

    /**
     * Returns the horizontal component of the body's current linear velocity in meters per second.
     *
     * @return the horizontal component of the body's current linear velocity in meters per second
     *
     * @see #setVelocity(double, double)
     * @see #getVy()
     * @since 2.0
     */
    public final double getVx() {
        return imp.getVx();
    }

    /**
     * Returns the vertical component of the body's current linear velocity in meters per second.
     *
     * @return the vertical component of the body's current linear velocity in meters per second
     *
     * @see #setVelocity(double, double)
     * @see #getVx()
     * @since 2.0
     */
    public final double getVy() {
        return imp.getVy();
    }

    /**
     * Returns the x coordinate of the shape in pixels.
     *
     * @return the x coordinate of the shape in pixels
     *
     * @see #setPosition(double, double)
     * @see #getY()
     * @since 2.0
     */
    public final double getX() {
        return imp.getX();
    }

    /**
     * Returns the y coordinate of the shape in pixels.
     *
     * @return the y coordinate of the shape in pixels
     *
     * @see #setPosition(double, double)
     * @see #getX()
     * @since 2.0
     */
    public final double getY() {
        return imp.getY();
    }

    /**
     * Sets the rotation angle of the body in degrees. Has no effect if the body currenly is in a physics simulation.
     *
     * @param angle the angle of the body in degrees
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public void setAngleDeg(final double angle) {
        imp.setAngleRad(Math.toRadians(angle));
    }

    /**
     * Sets the rotation angle of the body in radians. Has no effect if the body currenly is in a physics simulation.
     *
     * @param angle the angle of the body in radians
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleDeg(double)
     * @since 2.0
     */
    public void setAngleRad(final double angle) {
        imp.setAngleRad(angle);
    }

    /**
     * Sets the angular damping of the body.
     *
     * @param angularDamping the angular damping
     *
     * @see #getAngularDamping()
     * @since 2.0
     */
    public void setAngularDamping(final double angularDamping) {
        imp.setAngularDamping(angularDamping);
    }

    /**
     * Sets the angular velocity of the body in radian per second.
     *
     * @param angularVelocity the angular velocity in radian per second
     *
     * @see #getAngularVelocity()
     * @since 2.0
     */
    public void setAngularVelocity(final double angularVelocity) {
        imp.setAngularVelocity(angularVelocity);
    }

    /**
     * Sets the linear damping of the body.
     *
     * @param damping the linear damping
     *
     * @see #getDamping()
     * @since 2.0
     */
    public void setDamping(final double damping) {
        imp.setDamping(damping);
    }

    /**
     * Sets the density of the body in kilogram per square meter.
     *
     * @param density the density of the body in kilogram per square meter
     *
     * @see #getDensity()
     * @since 2.0
     */
    public void setDensity(final double density) {
        imp.setDensity(density);
    }

    /**
     * Sets the friction of the body.
     *
     * @param friction the friction of the body
     *
     * @see #getFriction()
     * @since 2.0
     */
    public void setFriction(final double friction) {
        imp.setFriction(friction);
    }

    /**
     * Sets the image for this body.
     *
     * @param image the image
     *
     * @see #getImage()
     * @since 2.0
     */
    public void setImage(final Image image) {
        this.image = image;
    }

    /**
     * Sets the position of the body. If the body is in a physics simulation, the body will be removed from the
     * simulation, then added to the simulation at the specified coordinates.
     *
     * @param x the x coordinate of the body in pixels
     * @param y the y coordinate of the body in pixels
     *
     * @see #getX()
     * @see #getY()
     * @since 2.0
     */
    public final void setPosition(final double x, final double y) {
        final Physics physics = imp.getPhysics();
        if (physics == null) {
            imp.setPosition(x, y);
        }
        else {
            BodyImp detachedImp = new DetachedBodyImp(imp);
            detachedImp.setPosition(x, y);
            imp.destroy();
            imp = new PhysicsBodyImp(physics, this, detachedImp);
        }
    }

    /**
     * Disallows or allows the body to rotate.
     *
     * @param fixed
     *
     * @since 2.0
     */
    public final void setRotationFixed(final boolean fixed) {
        imp.setRotationFixed(fixed);
    }

    /**
     * Sets the type of the body.
     *
     * @param type the type of the body
     *
     * @since 2.0
     */
    public final void setType(final BodyType type) {
        imp.setType(type);
    }

    /**
     * Sets the velocity of the body.
     *
     * @param vx the horizontal component of the velocity
     * @param vy the vertical component of the velocity
     *
     * @since 2.0
     */
    public final void setVelocity(final double vx, final double vy) {
        imp.setVelocity(vx, vy);
    }

    /**
     * Invoked when the body just has made contact with another body. Override this method to add new behavior.
     *
     * @param other the other body
     *
     * @since 2.0
     */
    protected void beginContact(final Body other) {
    }

    /**
     * Invoked when the contact with another body end. Override this method to add new behavior.
     *
     * @param other the other body
     *
     * @since 2.0
     */
    protected void endContact(final Body other) {
    }

    @Override
    protected final void draw(final Canvas canvas) {
        canvas.setRotationRad(getAngleRad());
        canvas.setTranslation(getX(), getY());
        if (image != null) {
            canvas.drawImage(0, 0, image, Alignment.CENTER);
        }

        drawDecoration(canvas);
        imp.drawOverlay(canvas);
        canvas.resetTransformations();
    }

    /**
     * Invoked after the image of the body has been drawn. Override this method to add new behaviour.
     *
     * @param canvas the canvas to draw on
     *
     * @since 2.0
     */
    protected void drawDecoration(final Canvas canvas) {
    }

    /**
     * Invoked for every step in the physics simulation. Override this method to add new behaviour.
     *
     * @param dt the time of this step in seconds
     *
     * @since 2.0
     */
    protected void step(final double dt) {
    }

    boolean setPhysics(final Physics physics) {
        if (imp.belongsTo(physics)) {
            return false;
        }

        final BodyImp oldImp = imp;
        if (physics == null) {
            imp = new DetachedBodyImp(oldImp);
        }
        else {
            imp = new PhysicsBodyImp(physics, this, oldImp);
        }

        oldImp.destroy();
        return true;
    }

    void internalBeginContact(final Body other) {
        if (imp.getPhysics() != null) {
            beginContact(other);
        }
    }

    void internalEndContact(final Body other) {
        if (imp.getPhysics() != null) {
            endContact(other);
        }
    }

    BodyImp getImp() {
        return imp;
    }

    Physics getPhysics() {
        return imp.getPhysics();
    }
}
