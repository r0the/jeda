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

import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.geometry.Rectangle;
import ch.jeda.ui.Element;
import ch.jeda.ui.View;
import ch.jeda.ui.ViewFeature;

/**
 * A view with an integraged physics simulation.
 *
 * @since 2.0
 */
public final class PhysicsView extends View implements TickListener {

    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 800;

    private final Physics physics;

    /**
     * Constructs a physics view. The view is shown on the screen.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @since 2.0
     */
    public PhysicsView() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Constructs a physics view. The view is shown on the screen. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @param features the features of the view
     *
     * @since 2.0
     */
    public PhysicsView(final ViewFeature... features) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Constructs a physics view. The view is shown on the screen. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has the specified
     * <tt>width</tt> and <tt>height</tt>.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.</p>
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the view
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 2.0
     */
    public PhysicsView(int width, int height, ViewFeature... features) {
        super(width, height, features);
        this.physics = new Physics();
        this.addEventListener(this);
    }

    /**
     * Adds walls around the visible part of the physics simulation.
     *
     * @since 2.0
     */
    public void addWalls() {
        final double WIDTH = 10.0;
        final Body top = new Body();
        top.setType(BodyType.STATIC);
        final double w = this.getWidth();
        final double h = this.getHeight();
        top.addShape(new Rectangle(w + 2 * WIDTH, WIDTH));
        top.setPosition(w / 2, -WIDTH / 2);
        this.add(top);

        final Body right = new Body();
        right.setType(BodyType.STATIC);
        right.addShape(new Rectangle(WIDTH, h + 2 * WIDTH));
        right.setPosition(w + WIDTH / 2, h / 2);
        this.add(right);

        final Body bottom = new Body();
        bottom.setType(BodyType.STATIC);
        bottom.addShape(new Rectangle(w + 2 * WIDTH, WIDTH));
        bottom.setPosition(w / 2, h + WIDTH / 2);
        this.add(bottom);

        final Body left = new Body();
        left.setType(BodyType.STATIC);
        left.addShape(new Rectangle(WIDTH, h + 2 * WIDTH));
        left.setPosition(-WIDTH / 2, h / 2);
        this.add(left);
    }

    /**
     * Returns the current scale of the physics view. The unit of the scale is meter per pixel, meaning that with a
     * scale of 1.0, the simulation assumes that every pixel corresponds to one meter. The default scale is 10.0.
     *
     * @return the current scale of the physics view in meter per pixel
     *
     * @see #setScale(double)
     * @since 2.0
     */
    public final double getScale() {
        return this.physics.getScale();
    }

    /**
     * Checks if the debugging mode is enabled.
     *
     * @return <tt>true</tt>, if the debugging mode is enabled, otherwise <tt>false</tt>
     *
     * @see #setDebugging(boolean)
     * @since 2.0
     */
    public final boolean isDebugging() {
        return this.physics.isDebugging();
    }

    /**
     * Enables or disabled the debugging mode of the physics view. In debugging mode, the view displays an overlay for
     * bodies showing shapes and similar information.
     *
     * @param debugging enable or disable debugging mode
     *
     * @see #isDebugging()
     * @since 2.0
     */
    public final void setDebugging(final boolean debugging) {
        this.physics.setDebugging(debugging);
    }

    /**
     * Sets the gravity for the physics simulation.
     *
     * @param ax the horizontal component of the gravity
     * @param ay the vertical component of the gravity
     *
     * @since 2.0
     */
    public final void setGravity(final double ax, final double ay) {
        this.physics.setGravity(ax, ay);
    }

    public final void setPaused(final boolean paused) {
        this.physics.setPaused(paused);
    }

    /**
     * Sets the scale for the physics simulation. The unit of the scale is meter per pixel, meaning that with a scale of
     * 1.0, the simulation assumes that every pixel corresponds to one meter. The default scale is 10.0.
     *
     * @param scale the scale in meter per pixel
     *
     * @see #getScale()
     * @since 2.0
     */
    public final void setScale(final double scale) {
        this.physics.setScale(scale);
    }

    public void step(final double seconds) {
        this.physics.step(seconds);
    }

    @Override
    protected void elementAdded(final Element element) {
        if (element instanceof Body) {
            this.physics.add((Body) element);
        }
    }

    @Override
    protected void elementRemoved(final Element element) {
        if (element instanceof Body) {
            this.physics.remove((Body) element);
        }
    }

    @Override
    public void onTick(final TickEvent event) {
        this.step(event.getDuration());
    }
}
