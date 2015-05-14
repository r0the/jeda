/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
        return targetFrequency;
    }

    double getLastStepDuration() {
        return lastStepDuration / 1000.0;
    }

    void setTargetFrequency(final double hertz) {
        if (targetFrequency != hertz) {
            targetFrequency = hertz;
            refresh();
        }
    }

    void start() {
        refresh();
    }

    void tick() {
        final long end = System.currentTimeMillis();
        final long sleepTime = period - end + start - adjustment;
        if (sleepTime > 0) {
            sleep(sleepTime);
            adjustment = (System.currentTimeMillis() - end) - sleepTime;
        }
        else {
            sleep(1);
            adjustment = 0;
        }

        final long now = System.currentTimeMillis();
        lastStepDuration = now - start;
        start = now;
    }

    void refresh() {
        start = System.currentTimeMillis();
        period = (long) (1000f / targetFrequency);
        adjustment = 0;
    }

    private void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (final InterruptedException e) {
        }
    }
}
