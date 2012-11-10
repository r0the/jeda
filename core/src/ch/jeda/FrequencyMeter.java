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

class FrequencyMeter {

    private static final int MAX_VALUES = 60;
    private final long[] durations;
    private long frequency;
    private int index;
    private long startTime;

    FrequencyMeter() {
        this.durations = new long[MAX_VALUES];
        this.frequency = 0;
        this.index = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void count() {
        long now = System.currentTimeMillis();
        this.durations[this.index] = now - this.startTime;
        this.startTime = now;
        if (this.index < MAX_VALUES - 1) {
            this.index = this.index + 1;
        }
        else {
            this.index = 0;
        }

        this.frequency = 0;
        for (long time : this.durations) {
            this.frequency = this.frequency + time;
        }

        this.frequency = this.frequency / MAX_VALUES;
    }

    int getFrequency() {
        if (this.frequency == 0) {
            return 0;
        }
        else {
            return 1000 / (int) this.frequency;
        }
    }
}
