/*
 * Copyright (C) 2010 - 2013 by Stefan Rothe
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
package ch.jeda.world;

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Events;

/**
 * EXPERIMENTAL - API MAY CHANGE.
 */
public class WorldState {

    public static final WorldState NULL = new WorldState();
    private World world;

    protected WorldState() {
    }

    protected void enterState() {
    }

    protected void exitState() {
    }

    protected World getWorld() {
        return this.world;
    }

    protected void init() {
    }

    /**
     * This method is called in every simulation step to draw the background of
     * this state. The default implementation fills the canvas with the color
     * white.
     *
     * @param canvas the canvas on which the background is drawn
     * @since 1
     */
    protected void drawBackground(Canvas canvas) {
        this.world.drawBackground(canvas);
    }

    /**
     * This method is called in every simulation step to draw the foreground of
     * this state.
     *
     * @param canvas the canvas on which the foreground is drawn
     * @since 1
     */
    protected void drawOverlay(Canvas canvas) {
        this.world.drawOverlay(canvas);
    }

    protected void update(Events events) {
        this.world.update(events);
    }

    void notifyEnter(World world) {
        if (this.world == null) {
            this.world = world;
            this.init();
        }

        this.enterState();
    }

    void notifyExit() {
        this.exitState();
    }
}
