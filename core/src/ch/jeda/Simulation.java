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

/**
 * Represents a simulation. When run, the simulation initializes itself by
 * calling the {@link #init()} method. Then, the simulation calls the
 * {@link #step()} method repeatedly to perform a simulation step. The target
 * frequency with which the {@link #step()} method is called can be adjusted.
 *
 * @since 1
 */
public abstract class Simulation extends Program {

    private final int PAUSE_SLEEP_MILLIS = 200;
    private final FrequencyMeter frequencyMeter;
    private final Timer timer;

    /**
     * Creates a new simulation . The target simulation frequency is set to 60
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
     * Returns the duration of the last simulation step in seconds. This value
     * can be used to calculate smooth movements.
     *
     * @return duration of last simulation step in seconds
     */
    public final double getLastStepDuration() {
        return this.timer.getLastStepDuration();
    }

    /**
     * Returns the target simulation frequency in Hertz.
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
     * Calls {@link #init()} and then starts the simulation. Calls
     * {@link #step()} repeatedly until the simulation has been stopped by
     * calling {@link #requestStop()}.
     *
     * @since 1
     */
    @Override
    public final void run() {
        this.init();
        this.timer.start();
        while (this.getState() != ProgramState.STOPPED) {
            if (this.getState() == ProgramState.PAUSED) {
                this.sleep(PAUSE_SLEEP_MILLIS);
                this.timer.start();
            }
            else {
                this.frequencyMeter.count();
                this.step();
                this.timer.tick();
            }
        }
    }

    /**
     * Sets the target simulation frequency in Hertz. This is the frequency in
     * which the method {@link #step()} will be called.
     *
     * @param hertz new frequency in hertz
     *
     * @see #getCurrentFrequency()
     * @see #getFrequency()
     * @since 1
     */
    public final void setFrequency(final int hertz) {
        this.timer.setFrequency(hertz);
    }

    /**
     * Initializes the simulation. Override this method to initialize the
     * simulation.
     *
     * @since 1
     */
    protected abstract void init();

    /**
     * Performs a simulation step. Override this method to perform a simulation
     * step.
     *
     * @since 1
     */
    protected abstract void step();
}
