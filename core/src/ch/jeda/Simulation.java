/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda;

import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;

/**
 * @deprecated Use {@link Program} with an {@link TickListener} instead.
 */
@Deprecated
public abstract class Simulation extends Program implements TickListener {

    private int currentFrequency;
    private double lastStepDuration;

    protected Simulation() {
    }

    public final int getCurrentFrequency() {
        return this.currentFrequency;
    }

    public final double getLastStepDuration() {
        return this.lastStepDuration;
    }

    public final int getFrequency() {
        return 0;
    }

    @Override
    public void onTick(final TickEvent event) {
        this.lastStepDuration = event.getDuration();
        this.currentFrequency = (int) event.getFrameRate();
        this.step();
    }

    @Override
    public final void run() {
        this.init();
    }

    public final void setFrequency(final int hertz) {
    }

    protected abstract void init();

    protected abstract void step();
}
