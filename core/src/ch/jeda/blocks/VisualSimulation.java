/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.blocks;

import ch.jeda.Simulation;
import ch.jeda.Size;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Events;
import ch.jeda.ui.Window;

/**
 * This class represents a visual simulation, which is a simulation with a 
 * graphical representation. This class extends the {@link ch.jeda.Simulation}
 * class and implements a simulation loop that renders to a
 * {@link ch.jeda.ui.Window}.
 * 
 * @since 1
 */
abstract class VisualSimulation extends Simulation {

    private final Window window;

    /**
     * Creates a new visual simulation with a default-sized window.
     *
     * @see Window#Window()
     * @since 1
     */
    protected VisualSimulation() {
        this(new Size());
    }

    /**
     * Creates a new visual simulation with a window that has a drawing area of
     * the specified size.
     *
     * @param width the width of the window's drawing area
     * @param height the height of the window's drawing area
     * @throws IllegalArgumentException if width or height are smaller than 1
     * 
     * @since 1
     */
    public VisualSimulation(int width, int height) {
        this(new Size(width, height));
    }

    /**
     * Creates a new visual simulation with a window that has a drawing area of
     * the specified size.
     *
     * @param size the size of the window's drawing area
     *
     * @throws NullPointerException if size is <code>null</code>
     * @throws IllegalArgumentException if size is empty
     * 
     * @since 1
     */
    public VisualSimulation(Size size) {
        this.window = new Window(size, Window.Feature.DoubleBuffered);
    }

    /**
     * Returns the title of this simulation's window.
     *
     * @return current window title
     * 
     * @see #setTitle(java.lang.String)
     * @since 1
     */
    public final String getTitle() {
        return this.window.getTitle();
    }

    public final Size getCanvasSize() {
        return this.window.getSize();
    }

    /**
     * Checks whether this simulation's window is in fullscreen mode.
     *
     * @return <code>true</code> if the window is in fullscreen mode
     * 
     * @see #setFullscreen(boolean)
     * @since 1
     */
    public final boolean isFullscreen() {
        return this.window.hasFeature(Window.Feature.Fullscreen);
    }

    /**
     * Enables/disables the fullscreen mode.
     *
     * @param fullscreen <code>true</code> to enable fullscreen mode,
     *                   <code>false</code> to disable it
     * 
     * @see #isFullscreen()
     * @since 1
     */
    public final void setFullscreen(boolean fullscreen) {
        this.window.setFeature(Window.Feature.Fullscreen, fullscreen);
    }

    /**
     * Sets the title of the simulations's window.
     *
     * @param title new title of the window
     * @throws NullPointerException if title is null
     *
     * @see #getTitle()
     * @since 1
     */
    public final void setTitle(String title) {
        this.window.setTitle(title);
    }

    /**
     * This method is overridden to implement the simulation step for a visual
     * simulation.
     */
    @Override
    protected final void step() {
        this.beforeUpdate();
        this.update(this.window.getEvents());
        this.afterUpdate();
        this.beforeDraw();
        this.drawBackground(this.window);
        this.drawForeground(this.window);
        this.afterDraw();
        this.window.update();
    }

    /**
     * This method is called after the
     * {@link #drawForeground(ch.jeda.ui.Canvas)} method.
     * It is intended as a hook for Jeda framework classes and may not be
     * available to override.
     */
    protected void afterDraw() {
    }

    /**
     * This method is called after the {@link #update(ch.jeda.ui.Events)}
     * method.
     * It is intended as a hook for Jeda framework classes and may not be
     * available to override.
     */
    protected void afterUpdate() {
    }

    /**
     * This method is called before the
     * {@link #drawBackground(ch.jeda.ui.Canvas)} method.
     * It is intended as a hook for Jeda framework classes and may not be
     * available to override.
     */
    protected void beforeDraw() {
    }

    /**
     * This method is called before the {@link #update(ch.jeda.ui.Events)}
     * method.
     * It is intended as a hook for Jeda framework classes and may not be
     * available to override.
     */
    protected void beforeUpdate() {
    }

    /**
     * This method is called in every simulation step to draw the background of
     * the simulation. The default implementation fills the canvas with the
     * color white.
     * 
     * @param canvas the canvas on which the background is drawn
     * 
     * @since 1
     */
    protected void drawBackground(Canvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.fill();
    }

    /**
     * This method is called in every simulation step to draw the foreground of
     * the simulation.
     * 
     * @param canvas the canvas on which the foreground is drawn
     * 
     * @since 1
     */
    protected abstract void drawForeground(Canvas canvas);

    /**
     * This method is called once every simulation step to let the simulation
     * update it's state and react to user input events.
     * 
     * @param events the user input events that occured since the last call to
     *               this method
     * 
     * @since 1
     */
    protected void update(Events events) {
    }
}
