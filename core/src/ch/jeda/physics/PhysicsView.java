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
        physics = new Physics();
        physics.setScale(1);
        addEventListener(this);
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
        return physics.isDebugging();
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
        physics.setDebugging(debugging);
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
        physics.setGravity(ax, ay);
    }

    public final void setPaused(final boolean paused) {
        physics.setPaused(paused);
    }

    public void step(final double seconds) {
        physics.step(seconds);
    }

    @Override
    protected void elementAdded(final Element element) {
        if (element instanceof Body) {
            physics.add((Body) element);
        }
    }

    @Override
    protected void elementRemoved(final Element element) {
        if (element instanceof Body) {
            physics.remove((Body) element);
        }
    }

    @Override
    public void onTick(final TickEvent event) {
        step(event.getDuration());
    }
}
