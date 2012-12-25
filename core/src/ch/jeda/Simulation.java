/*
 * Copyright (C) 2011 by Stefan Rothe
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

/**
 * A Simulation is a program that executes a method at a certain frequency.
 * 
 * @since 1
 */
public abstract class Simulation extends Program {

    private final FrequencyMeter frequencyMeter;
    private final Timer timer;

    /**
     * Initializes a new Simulation object. The target frame rate is set to 60
     * Hertz.
     * 
     * @since 1
     */
    protected Simulation() {
        this.frequencyMeter = new FrequencyMeter();
        this.timer = new Timer();
    }

    /**
     * Returns the measured simulation frequency in Hertz. This is the frequency
     * in which the method {@link #step()} has beend called during the last
     * second.
     *
     * @return measured simulation frequency
     * 
     * @see #getFrequency()
     * @see #setFrequency(int)
     * @since 1
     */
    public final int getCurrentFrequency() {
        return this.frequencyMeter.getFrequency();
    }

    /**
     * Returns the duration of the last simulation step in seconds. This
     * value can be used to calculate smooth movements.
     *
     * @return duration of last simulation step in seconds
     */
    public final double getLastStepDuration() {
        return this.timer.getLastStepDuration();
    }

    /**
     * Returns the desired simulation frequency in Hertz.
     *
     * @return the desired frequency
     * 
     * @see #getCurrentFrequency()
     * @see #setFrequency(int)
     * @since 1
     */
    public final int getFrequency() {
        return this.timer.getFrequency();
    }

    /**
     * Calls {@link #init()} and then starts the simulation. Returns
     * immediately after the simulation has been started.
     * 
     * @since 1
     */
    @Override
    public final void run() {
        this.init();
        this.timer.start();
        while (!this.stopRequested()) {
            this.frequencyMeter.count();
            this.step();
            this.timer.tick();
        }
    }

    /**
     * Sets the desired simulation frequency in Hertz. This is the frequency
     * in which the method {@link #step()} will be called.
     *
     * @param hertz new frequency in hertz
     * 
     * @see #getCurrentFrequency()
     * @see #getFrequency()
     * @since 1
     */
    public final void setFrequency(int hertz) {
        this.timer.setFrequency(hertz);
    }

    /**
     * Override this method to initialize the simulation.
     * 
     * @since 1
     */
    protected abstract void init();

    /**
     * Override this method to perform a simulation step.
     * 
     * @since 1
     */
    protected abstract void step();
}
