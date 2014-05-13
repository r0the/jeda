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

class Timer {

    private long adjustment;
    private double targetFrequency;
    private long lastStepDuration;
    private long start;
    private long period;

    Timer(final double targetFrequency) {
        this.targetFrequency = targetFrequency;
    }

    double getTargetFrequency() {
        return this.targetFrequency;
    }

    double getLastStepDuration() {
        return this.lastStepDuration / 1000.0;
    }

    void setTargetFrequency(final double hertz) {
        if (this.targetFrequency != hertz) {
            this.targetFrequency = hertz;
            this.refresh();
        }
    }

    void start() {
        this.refresh();
    }

    void tick() {
        final long end = System.currentTimeMillis();
        final long sleepTime = this.period - end + this.start - this.adjustment;
        if (sleepTime > 0) {
            this.sleep(sleepTime);
            this.adjustment = (System.currentTimeMillis() - end) - sleepTime;
        }
        else {
            this.sleep(1);
            this.adjustment = 0;
        }

        final long now = System.currentTimeMillis();
        this.lastStepDuration = now - this.start;
        this.start = now;
    }

    void refresh() {
        this.start = System.currentTimeMillis();
        this.period = (long) (1000f / this.targetFrequency);
        this.adjustment = 0;
    }

    private void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (final InterruptedException e) {
        }
    }
}
