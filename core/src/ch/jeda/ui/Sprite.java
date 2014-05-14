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
 * <li><b>Position</b>: The coordinates of the sprite's center in pixels. The position can be set with
 * {@link #setPosition(double, double)} and be retrieved with {@link #getX()} and {@link #getY()}.
 * <li><b>Speed</b>: The current speed of the sprite in pixels per second. The speed can be retrieved with
 * {@link #getSpeed()}.
 * <li><b>Direction</b>: The direction in which the sprite moves in radians. The direction 0 points to the left, the
 * direction <tt>Math.PI/2</tt> points down.
 * <li><b>Acceleration</b></li>: The current acceleration of the sprite in pixels per second per second. The
 * acceleration can be set with {@link #setAcceleration(double, double)}.
 * <li><b>Rotation</b>: The current rotation of the sprite's image in radians. The sprite's rotation is independent of
 * it's direction.
 * <li><b>Radius</b>: The radius of the sprite's collision circle.
 * </ul>
 *
 * @since 1
 */
public class Sprite extends GraphicsItem implements TickListener {

    private double ax;
    private double ay;
    private RotatedImage image;
    private double radius;
    private double rotation;
    private double vx;
    private double vy;
    private double x;
    private double y;

    /**
     * Constructs a new sprite located at the origin of the window.
     *
     * @since 1
     */
    public Sprite() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructs a new sprite located at the specified coordinates.
     *
     * @since 1
     */
    public Sprite(final double x, final double y) {
        this(x, y, 0.0, 0.0);
    }

    /**
     * Constructs a new sprite located at the specified coordinates with the specified initial speed.
     *
     * @since 1
     */
    public Sprite(final double x, final double y, final double speed, final double direction) {
        this.rotate(direction);
        this.setPosition(x, y);
        this.vx = speed * Math.cos(direction);
        this.vy = speed * Math.sin(direction);
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
     * @since 1
     */
    @SuppressWarnings("unchecked")
    public final <T extends Sprite> T[] getCollidingSprites(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        if (this.getWindow() != null) {
            for (final T item : this.getWindow().getGraphicsItems(clazz)) {
                if (this.collidesWith(item)) {
                    result.add(item);
                }
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    /**
     * Returns the current direction of the sprite. The direction influences the movement of the sprite.
     *
     * @since 1
     */
    public final double getDirection() {
        return Math.atan2(this.vy, this.vx);
    }

    /**
     * @since 1
     */
    public final Image getRotatedImage() {
        if (this.image == null) {
            return null;
        }
        else {
            return this.image.getImage(this.rotation);
        }
    }

    /**
     * @since 1
     */
    public final double getRadius() {
        return this.radius;
    }

    /**
     * Returns the current rotation of the sprite. The rotation influences the graphical representation of the sprite.
     *
     * @see #rotate(double)
     * @since 1
     */
    public final double getRotation() {
        return this.rotation;
    }

    /**
     * Returns the current speed of the sprite in pixels per second.
     *
     * @since 1
     */
    public final double getSpeed() {
        return Math.sqrt(this.vx * this.vx + this.vy * this.vy);
    }

    /**
     * Returns the current x coordinate of this sprite.
     *
     * @return the current x coordinate of this sprite
     *
     * @see #getY()
     * @see #setPosition(double, double)
     * @since 1
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
     * @since 1
     */
    public final double getY() {
        return this.y;
    }

    @Override
    public final void onTick(final TickEvent event) {
        this.vx = this.vx + this.ax * event.getDuration();
        this.vy = this.vy + this.ay * event.getDuration();
        final double newX = this.x + this.vx * event.getDuration();
        final double newY = this.y + this.vy * event.getDuration();
        this.update(event.getDuration(), newX, newY);
    }

    /**
     * @since 1
     */
    public final void rotate(final double angle) {
        this.rotation = this.rotation + angle;
        while (this.rotation < 0) {
            this.rotation = this.rotation + 2.0 * Math.PI;
        }

        while (this.rotation >= 2.0 * Math.PI) {
            this.rotation = this.rotation - 2.0 * Math.PI;
        }
    }

    /**
     * @since 1
     */
    public final void setAcceleration(final double ax, final double ay) {
        this.ax = ax;
        this.ay = ay;
    }

    /**
     * @since 1
     */
    public final void setImage(final String path) {
        this.setImage(path, 1);
    }

    /**
     * @since 1
     */
    public final void setImage(final String path, final int steps) {
        this.image = new RotatedImage(new Image(path), steps);
    }

    /**
     * @since 1
     */
    public final void setPosition(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @since 1
     */
    public final void setRadius(final double radius) {
        this.radius = radius;
    }

    @Override
    protected void draw(final Canvas canvas) {
        canvas.drawImage(this.x, this.y, this.getRotatedImage(), Alignment.CENTER);
    }

    /**
     * @since 1
     */
    protected void update(final double dt, final double newX, final double newY) {
        this.setPosition(newX, newY);
    }

    private boolean collidesWith(final Sprite other) {
        final double dx = this.x - other.x;
        final double dy = this.y - other.y;
        final double r = this.radius + other.radius;
        return (dx * dx + dy * dy < r * r) && this.pixelCollisionWith(other);
    }

    // Performs pixel collision
    private boolean pixelCollisionWith(final Sprite other) {
        Image image1 = this.getRotatedImage();
        Image image2 = other.getRotatedImage();
        if (image1 == null || image2 == null) {
            return false;
        }

        int w1 = image1.getWidth() / 2;
        int h1 = image1.getHeight() / 2;
        int w2 = image2.getWidth() / 2;
        int h2 = image2.getHeight() / 2;

        int left = (int) Math.max(this.x - w1, other.x - w2);
        int width = (int) Math.min(this.x + w1, other.x + w2) - left - 1;
        int top = (int) Math.max(this.y - h1, other.y - h2);
        int height = (int) Math.min(this.y + h1, other.y + h2) - top - 1;

        if (height < 1 || width < 1) {
            return false;
        }

        int[] pixels1 = image1.getPixels((int) (left - this.x + w1), (int) (top - this.y + h1), width, height);
        int[] pixels2 = image2.getPixels((int) (left - other.x + w2), (int) (top - other.y + h2), width, height);
        for (int i = 0; i < pixels1.length; ++i) {
            if ((pixels1[i] & 0xFF000000) != 0 && (pixels2[i] & 0xFF000000) != 0) {
                return true;
            }
        }

        return false;
    }
}
