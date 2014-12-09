/*
 * Copyright (C) 2014 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an movable, rotatable object represented by an image. A sprite has the following properties:
 * <ul>
 * <li><b>Position</b>: The pixel coordinates of the sprite's center. The position can be set with
 * {@link #setPosition(double, double)} and be retrieved with {@link #getX()} and {@link #getY()}.
 * <li><b>Speed</b>: The current speed of the sprite in pixels per second.
 * <li><b>Direction</b>: The movement direction of the sprite in radians. The sprite's direction is independent of it's
 * rotation. The direction 0 points to the right, the direction <tt>Math.PI/2</tt> points down.
 * <li><b>Acceleration</b></li>: The current acceleration of the sprite in pixels per second per second.
 * <li><b>Rotation</b>: The current rotation of the sprite's image in radians. The sprite's rotation is independent of
 * the direction of its movement. The rotation 0 points to the right, the rotation <tt>Math.PI/2</tt> points down.
 * <li><b>Radius</b>: The radius of the sprite's collision circle.
 * </ul>
 *
 * @since 1.0
 * @version 3
 */
public class Sprite extends Element implements TickListener {

    private static final double THRESHHOLD = 1E-3;
    private double ax;
    private double ay;
    private double direction;
    private Image image;
    private double radius;
    private double rotation;
    private double speed;
    private double vx;
    private double vy;
    private double x;
    private double y;

    /**
     * Constructs a new sprite located at the origin of the window.
     *
     * @since 1.0
     */
    public Sprite() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructs a new sprite located at the specified coordinates.
     *
     * @param x x coordinate of the sprite's center
     * @param y x coordinate of the sprite's center
     *
     * @since 1.0
     */
    public Sprite(final double x, final double y) {
        this(x, y, 0.0, 0.0);
    }

    /**
     * Constructs a new sprite located at the specified coordinates with the specified initial speed.
     *
     * @param x x coordinate of the sprite's center
     * @param y x coordinate of the sprite's center
     * @param speed the sprite's initial speed in pixels per second
     * @param direction the sprite's initial movement direction.
     *
     * @since 1.0
     */
    public Sprite(final double x, final double y, final double speed, final double direction) {
        this.rotate(direction);
        this.setPosition(x, y);
        this.direction = MathUtil.normalizeAngle(direction);
        this.speed = speed;
        this.velocityChanged();
    }

    /**
     * Returns an array of all other sprites in the same window that collide with the sprite. The collision algorithm
     * works as follows:
     * <ul>
     * <li>Get a list of all sprites and do for every one:
     * <li>Check if the collision circles of this sprite and the other sprite overlap.
     * <li>If so, perform a pixel-by-pixel collision based on the images returned by {@link #getRotatedImage()} of the
     * two sprites.
     * </ul>
     *
     * <b>Warning:</b>The collision algorithm is no sophisticated and may be very slow with a large number of sprites.
     *
     * @return an array of all other sprites in the same view that collide with the sprite
     *
     * @since 1.1
     */
    public final Sprite[] getCollidingSprites() {
        return this.getCollidingSprites(Sprite.class);
    }

    /**
     * Returns an array of all other sprites of the specified class in the same window that collide with the sprite. The
     * collision algorithm works as follows:
     * <ul>
     * <li>Get a list of all sprites of the specified class and do for every one:
     * <li>Check if the collision circles of this sprite and the other sprite overlap.
     * <li>If so, perform a pixel-by-pixel collision based on the images returned by {@link #getRotatedImage()} of the
     * two sprites.
     * </ul>
     *
     * <b>Warning:</b>The collision algorithm is no sophisticated and may be very slow with a large number of sprites.
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public final <T extends Sprite> T[] getCollidingSprites(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        if (this.getView() != null) {
            for (final T item : this.getView().getElements(clazz)) {
                if (this.collidesWith(item)) {
                    result.add(item);
                }
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    /**
     * Returns the movement direction of the sprite in radians. The sprite's direction is independent of it's rotation.
     * The direction 0 points to the right, the direction <tt>Math.PI/2</tt> points down.
     *
     * @return the movement direction of the sprite in radians
     *
     * @see #setDirection(double)
     * @since 1.0
     */
    public final double getDirection() {
        return this.direction;
    }

    /**
     * Returns the image of the sprite.
     *
     * @return the image of the sprite.
     *
     * @since 1.6
     */
    public final Image getImage() {
        return this.image;
    }

    /**
     * Returns the radius of the sprite's collision circle.
     *
     * @return the radius of the sprite's collision circle
     *
     * @see #setRadius(double)
     * @since 1.0
     */
    public final double getRadius() {
        return this.radius;
    }

    /**
     * Returns the rotated image of the sprite.
     *
     * @return the rotated image of the sprite.
     *
     * @see #getRotation()
     * @see #setRotation(double)
     * @since 1.0
     */
    public final Image getRotatedImage() {
        if (this.image == null) {
            return null;
        }
        else {
            return this.image.rotate(this.rotation);
        }
    }

    /**
     * Returns the current rotation of the sprite in radians. The sprite's rotation is independent of the direction of
     * its movement. The rotation 0 points to the right, the rotation <tt>Math.PI/2</tt> points down.
     *
     * @return the current rotation of the sprite in radians.
     *
     * @see #rotate(double)
     * @since 1.0
     */
    public final double getRotation() {
        return this.rotation;
    }

    /**
     * Returns the absolute speed of the sprite in pixels per second. The absolute speed can never be negative.
     *
     * @return absolute speed of the sprite
     *
     * @since 1.0
     */
    public final double getSpeed() {
        return this.speed;
    }

    /**
     * Returns the current x coordinate of this sprite.
     *
     * @return the current x coordinate of this sprite
     *
     * @see #getY()
     * @see #setPosition(double, double)
     * @since 1.0
     */
    public final double getX() {
        return this.x;
    }

    /**
     * Returns the current y coordinate of this sprite.
     *
     * @return the current y coordinate of this sprite
     *
     * @see #getX()
     * @see #setPosition(double, double)
     * @since 1.0
     */
    public final double getY() {
        return this.y;
    }

    @Override
    public final void onTick(final TickEvent event) {
        final double dt = event.getDuration();
        if (!MathUtil.isZero(this.ax, THRESHHOLD) || !MathUtil.isZero(this.ay, THRESHHOLD)) {
            this.vx = this.vx + this.ax * dt;
            this.vy = this.vy + this.ay * dt;
            this.speed = Math.sqrt(this.vx * this.vx + this.vy * this.vy);
            if (!MathUtil.isZero(this.speed, THRESHHOLD)) {
                this.direction = MathUtil.normalizeAngle(Math.atan2(this.vy, this.vx));
            }
        }

        final double newX = this.x + this.vx * dt;
        final double newY = this.y + this.vy * dt;
        this.update(dt, newX, newY);
    }

    /**
     * Changes the rotation of the sprite by the specified angle.
     *
     * @param angle angle to rotate
     *
     * @see #getRotation()
     * @see #setRotation(double)
     * @since 1.0
     */
    public final void rotate(final double angle) {
        this.setRotation(this.rotation + angle);
    }

    /**
     * Sets the acceleration of the sprite component-wise. The acceleration is measured in pixels per second per second.
     *
     * @param ax the horizontal component of the acceleration
     * @param ay the vertical component of the acceleration
     *
     * @since 1.0
     */
    public final void setAcceleration(final double ax, final double ay) {
        this.ax = ax;
        this.ay = ay;
    }

    /**
     * Sets the movement direction of the sprite in radians. The sprite's direction is independent of it's rotation. The
     * direction 0 points to the right, the direction <tt>Math.PI/2</tt> points down.
     *
     * @param direction the movement direction of the sprite in radians
     *
     * @see #getDirection()
     * @since 1.1
     */
    public final void setDirection(final double direction) {
        this.direction = MathUtil.normalizeAngle(direction);
        this.velocityChanged();
    }

    /**
     * Sets the image representing the sprite.
     *
     * @param image image representing the sprite
     *
     * @see #setImage(java.lang.String)
     * @see #setImage(ch.jeda.ui.Image, int)
     * @see #setImage(java.lang.String, int)
     * @since 1.1
     */
    public final void setImage(final Image image) {
        this.image = image;
    }

    /**
     * Sets the image representing the sprite.
     *
     * @param path the file or resource path of the image representing the sprite
     *
     * @see #setImage(ch.jeda.ui.Image)
     * @see #setImage(ch.jeda.ui.Image, int)
     * @see #setImage(java.lang.String, int)
     * @since 1.0
     */
    public final void setImage(final String path) {
        this.setImage(new Image(path));
    }

    /**
     * @deprecated Use {@link #setImage(ch.jeda.ui.Image)} instead
     */
    public final void setImage(final Image image, final int steps) {
        this.setImage(image);
    }

    /**
     * @deprecated Use {@link #setImage(java.lang.String)} instead
     */
    public final void setImage(final String path, final int steps) {
        this.setImage(path);
    }

    /**
     * Sets the position of the sprite.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     *
     * @see #getX()
     * @see #getY()
     * @since 1.0
     */
    public final void setPosition(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the radius of the sprite's collision circle.
     *
     * @param radius the radius of the sprite's collision circle
     *
     * @since 1.0
     */
    public final void setRadius(final double radius) {
        this.radius = radius;
    }

    /**
     * Set the current rotation of the sprite in radians. The sprite's rotation is independent of the direction of its
     * movement. The rotation 0 points to the right, the rotation <tt>Math.PI/2</tt> points down.
     *
     * @param rotation the current rotation of the sprite in radians.
     *
     * @since 1.1
     */
    public final void setRotation(final double rotation) {
        this.rotation = MathUtil.normalizeAngle(rotation);
    }

    /**
     * Set the speed of the sprite in pixels per second.
     *
     * @param speed the speed of the sprite in pixels per second
     *
     * @see #getSpeed()
     * @since 1.1
     */
    public final void setSpeed(final double speed) {
        this.speed = speed;
        this.velocityChanged();
    }

    /**
     * Changes the movement direction of the sprite by the specified angle.
     *
     * @param angle angle to turn
     *
     * @see #getDirection()
     * @see #setDirection(double)
     * @since 1.1
     */
    public final void turn(final double angle) {
        this.setDirection(this.direction + angle);
    }

    @Override
    protected void draw(final Canvas canvas) {
        canvas.setTranslation(this.x, this.y);
        canvas.setRotation(this.rotation);
        canvas.drawImage(0, 0, this.getImage(), Alignment.CENTER);
        canvas.resetTransformations();
    }

    /**
     * @since 1.0
     */
    protected void update(final double dt, final double newX, final double newY) {
        this.setPosition(newX, newY);
    }

    private boolean collidesWith(final Sprite other) {
        if (this == other || this.radius <= 0 || other.radius <= 0) {
            return false;
        }

        final double dx = this.x - other.x;
        final double dy = this.y - other.y;
        final double r = this.radius + other.radius;
        return (dx * dx + dy * dy < r * r) && this.pixelCollisionWith(other);
    }

    // Performs pixel collision
    private boolean pixelCollisionWith(final Sprite other) {
        final Image image1 = this.getRotatedImage();
        final Image image2 = other.getRotatedImage();
        if (image1 == null || image2 == null) {
            return false;
        }

        final int w1 = image1.getWidth() / 2;
        final int h1 = image1.getHeight() / 2;
        final int w2 = image2.getWidth() / 2;
        final int h2 = image2.getHeight() / 2;

        final int left = (int) Math.max(this.x - w1, other.x - w2);
        final int width = (int) Math.min(this.x + w1, other.x + w2) - left - 1;
        final int top = (int) Math.max(this.y - h1, other.y - h2);
        final int height = (int) Math.min(this.y + h1, other.y + h2) - top - 1;

        if (height < 1 || width < 1) {
            return false;
        }

        final int[] pixels1 = image1.getPixels((int) (left - this.x + w1), (int) (top - this.y + h1), width, height);
        final int[] pixels2 = image2.getPixels((int) (left - other.x + w2), (int) (top - other.y + h2), width, height);
        for (int i = 0; i < pixels1.length; ++i) {
            if ((pixels1[i] & 0xFF000000) != 0 && (pixels2[i] & 0xFF000000) != 0) {
                return true;
            }
        }

        return false;
    }

    private void velocityChanged() {
        this.vx = this.speed * Math.cos(this.direction);
        this.vy = this.speed * Math.sin(this.direction);
    }
}
