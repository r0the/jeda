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
import ch.jeda.ui.Color;
import ch.jeda.ui.Element;
import ch.jeda.ui.Image;

/**
 * Represents a body.
 *
 * @since 2.0
 */
public class Body extends Element {

    private Color debugColor;
    private Image image;
    private BodyImp imp;
    private int opacity;

    /**
     * Creates a new object of a subclass of {@link ch.jeda.physics.Body}. If <code>className</code> does not meet all
     * of the following criteria, a new {@link ch.jeda.physics.Body} object is created and returned.
     * <ul>
     * <li><code>className</code> must be a valid class name
     * <li>The class must be a subclass of {@link ch.jeda.physics.Body}
     * <li>The class must provide a public default constructor
     * </ul>
     *
     * @param className the name of the class
     * @return the created object
     *
     * @since 2.0
     */
    public static Body create(final String className) {
        if (className == null || className.isEmpty()) {
            return new Body();
        }

        try {
            final Class clazz = Class.forName(className);
            if (Body.class.isAssignableFrom(clazz)) {
                return (Body) clazz.newInstance();
            }
        }
        catch (final ClassNotFoundException ex) {
        }
        catch (InstantiationException ex) {
        }
        catch (IllegalAccessException ex) {
        }

        return new Body();
    }

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
     * Adds a shape to this body. Has no effect if <code>shape</code> is <code>null</code>.
     *
     * @param shape the shape to add
     *
     * @since 2.0
     */
    public final void addShape(final Shape shape) {
        if (shape != null) {
            imp.addShape(shape);
        }
    }

    /**
     * Applies a force to this body. The force is applied to the body's center of mass in the direction of the body's
     * current angle. The unit of the force is Newton.
     *
     * @param f the force in Newton
     *
     * @since 2.0
     */
    public final void applyForce(final double f) {
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
    public final void applyForce(final double fx, final double fy) {
        imp.applyForce(fx, fy);
    }

    /**
     * Applies a torque to this body. The unit of the torque is Newton meter.
     *
     * @param torque the torque in Newton meter
     *
     * @since 2.0
     */
    public final void applyTorque(final double torque) {
        imp.applyTorque(torque);
    }

    /**
     * Returns the current angle of this body in degrees.
     *
     * @return the current angle of this body in degrees
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
     * Returns the current angle of this body in radians.
     *
     * @return the current angle of this body in radians
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
     * Returns the angular damping of this body.
     *
     * @return the angular damping of this body
     *
     * @see #setAngularDamping(double)
     * @since 2.0
     */
    public final double getAngularDamping() {
        return imp.getAngularDamping();
    }

    /**
     * Returns the current angular velocity of this body in radians per second.
     *
     * @return the current angular velocity of this body in radians per second
     *
     * @see #setAngularVelocity(double)
     * @since 2.0
     */
    public final double getAngularVelocity() {
        return imp.getAngularVelocity();
    }

    /**
     * Returns the linear damping of this body.
     *
     * @return the linear damping of this body
     *
     * @see #setDamping(double)
     * @since 2.0
     */
    public final double getDamping() {
        return imp.getDamping();
    }

    /**
     * Returns the debug color of this body. The debug color is used for drawing the debug overlay of this body.
     *
     * @return the debug color of this body
     *
     * @since 2.0
     */
    public Color getDebugColor() {
        return debugColor;
    }

    /**
     * Returns the density of this body. The unit of the density is kilograms per square meter
     *
     * @return the density of this body in kilograms per square meter
     *
     * @see #setDensity(double)
     * @since 2.0
     */
    public final double getDensity() {
        return imp.getDensity();
    }

    /**
     * Returns the friction of this body.
     *
     * @return the friction of this body
     *
     * @see #setFriction(double)
     * @since 2.0
     */
    public final double getFriction() {
        return imp.getFriction();
    }

    /**
     * Returns the image of this body.
     *
     * @return the image of this body
     *
     * @see #setImage(ch.jeda.ui.Image)
     * @since 2.0
     */
    public final Image getImage() {
        return image;
    }

    /**
     * Returns the mass of this body in kilograms. The body's mass is determined by it's density and the areas of it's
     * shapes.
     *
     * @return the mass of this body in kilograms
     *
     * @see #addShape(ch.jeda.geometry.Shape)
     * @see #getDensity()
     * @see #getShapes()
     * @see #setDensity(double)
     * @since 2.0
     */
    public final double getMass() {
        return imp.getMass();
    }

    /**
     * Returns the opacity of this body. The opacity is a number between 0 and 255 where 0 means totally transparent and
     * 255 means totally opaque.
     *
     * @return the opacity of this body
     *
     * @see #setOpacity(int)
     * @since 2.0
     */
    public final int getOpacity() {
        return opacity;
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
     * Returns the type of this body.
     *
     * @return the type of this body
     *
     * @see #setType(ch.jeda.physics.BodyType)
     * @since 2.0
     */
    public final BodyType getType() {
        return imp.getType();
    }

    /**
     * Returns the horizontal component of this body's current linear velocity in meters per second.
     *
     * @return the horizontal component of this body's current linear velocity in meters per second
     *
     * @see #setVelocity(double, double)
     * @see #getVy()
     * @since 2.0
     */
    public final double getVx() {
        return imp.getVx();
    }

    /**
     * Returns the vertical component of this body's current linear velocity in meters per second.
     *
     * @return the vertical component of this body's current linear velocity in meters per second
     *
     * @see #setVelocity(double, double)
     * @see #getVx()
     * @since 2.0
     */
    public final double getVy() {
        return imp.getVy();
    }

    /**
     * Returns the x coordinate of this body in pixels.
     *
     * @return the x coordinate of the body in pixels
     *
     * @see #setPosition(double, double)
     * @see #getY()
     * @since 2.0
     */
    public final double getX() {
        return imp.getX();
    }

    /**
     * Returns the y coordinate of this body in pixels.
     *
     * @return the y coordinate of this body in pixels
     *
     * @see #setPosition(double, double)
     * @see #getX()
     * @since 2.0
     */
    public final double getY() {
        return imp.getY();
    }

    /**
     * Sets the rotation angle of this body in degrees. Has no effect if the body currenly is in a physics simulation.
     *
     * @param angle the angle of this body in degrees
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public final void setAngleDeg(final double angle) {
        imp.setAngleRad(Math.toRadians(angle));
    }

    /**
     * Sets the rotation angle of this body in radians. Has no effect if the body currenly is in a physics simulation.
     *
     * @param angle the angle of this body in radians
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleDeg(double)
     * @since 2.0
     */
    public final void setAngleRad(final double angle) {
        imp.setAngleRad(angle);
    }

    /**
     * Sets the angular damping of this body.
     *
     * @param angularDamping the angular damping
     *
     * @see #getAngularDamping()
     * @since 2.0
     */
    public final void setAngularDamping(final double angularDamping) {
        imp.setAngularDamping(angularDamping);
    }

    /**
     * Sets the angular velocity of this body in radians per second.
     *
     * @param angularVelocity the angular velocity in radians per second
     *
     * @see #getAngularVelocity()
     * @since 2.0
     */
    public final void setAngularVelocity(final double angularVelocity) {
        imp.setAngularVelocity(angularVelocity);
    }

    /**
     * Sets the linear damping of this body.
     *
     * @param damping the linear damping
     *
     * @see #getDamping()
     * @since 2.0
     */
    public final void setDamping(final double damping) {
        imp.setDamping(damping);
    }

    /**
     * Sets the debug color of this body. The debug color is used for drawing the debug overlay of this body.
     *
     * @param debugColor the debug color of this body.
     *
     * @since 2.0
     */
    public void setDebugColor(Color debugColor) {
        this.debugColor = debugColor;
    }

    /**
     * Sets the density of this body in kilogram per square meter.
     *
     * @param density the density of this body in kilogram per square meter
     *
     * @see #getDensity()
     * @since 2.0
     */
    public final void setDensity(final double density) {
        imp.setDensity(density);
    }

    /**
     * Sets the friction of this body.
     *
     * @param friction the friction of this body
     *
     * @see #getFriction()
     * @since 2.0
     */
    public final void setFriction(final double friction) {
        imp.setFriction(friction);
    }

    /**
     * Sets the image for this body. The image has no impact on the physical simulation.
     *
     * @param image the image
     *
     * @see #getImage()
     * @since 2.0
     */
    public final void setImage(final Image image) {
        this.image = image;
    }

    /**
     * Sets the opacity of this body. The opacity is a number between 0 and 255 where 0 means totally transparent and
     * 255 means totally opaque. The opacity has no impact on the physical simulation.
     *
     * @param opacity the opacity of this body
     *
     * @see #getOpacity()
     * @since 2.0
     */
    public final void setOpacity(final int opacity) {
        this.opacity = opacity;
    }

    /**
     * Sets the position of this body. If the body is in a physics simulation, the body will be removed from the
     * simulation, then added to the simulation at the specified coordinates.
     *
     * @param x the x coordinate of this body in pixels
     * @param y the y coordinate of this body in pixels
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
     * Disallows or allows this body to rotate.
     *
     * @param fixed
     *
     * @since 2.0
     */
    public final void setRotationFixed(final boolean fixed) {
        imp.setRotationFixed(fixed);
    }

    /**
     * Sets the type of this body.
     *
     * @param type the type of this body
     *
     * @see #getType()
     * @since 2.0
     */
    public final void setType(final BodyType type) {
        imp.setType(type);
    }

    /**
     * Sets the velocity of this body.
     *
     * @param vx the horizontal component of the velocity
     * @param vy the vertical component of the velocity
     *
     * @see #getVx()
     * @see #getVy()
     * @since 2.0
     */
    public final void setVelocity(final double vx, final double vy) {
        imp.setVelocity(vx, vy);
    }

    /**
     * Invoked when this body just has made contact with another body. Override this method to add new behavior.
     *
     * @param other the other body
     *
     * @since 2.0
     */
    protected void beginContact(final Body other) {
    }

    /**
     * Invoked when the contact with another body ends. Override this method to add new behavior.
     *
     * @param other the other body
     *
     * @since 2.0
     */
    protected void endContact(final Body other) {
    }

    @Override
    protected final void draw(final Canvas canvas) {
        canvas.pushTransformations();
        canvas.rotateRad(getAngleRad());
        canvas.translate(getX(), getY());
        if (image != null) {
            canvas.drawImage(0, 0, image, Alignment.CENTER);
        }

        drawDecoration(canvas);
        imp.drawOverlay(canvas);
        canvas.popTransformations();
    }

    /**
     * Invoked after the image of this body has been drawn. Override this method to add new behaviour.
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
