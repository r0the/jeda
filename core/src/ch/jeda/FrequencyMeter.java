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
    private float frequency;
    private int index;
    private long startTime;

    FrequencyMeter() {
        this.durations = new long[MAX_VALUES];
        this.frequency = 0f;
        this.index = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void count() {
        final long now = System.currentTimeMillis();
        this.durations[this.index] = now - this.startTime;
        this.startTime = now;
        if (this.index < MAX_VALUES - 1) {
            this.index = this.index + 1;
        }
        else {
            this.index = 0;
        }

        this.frequency = 0f;
        for (int i = 0; i < this.durations.length; ++i) {
            this.frequency = this.frequency + this.durations[i];
        }

        this.frequency = this.frequency / MAX_VALUES;
    }

    float getFrequency() {
        return 1000f / this.frequency;
    }
}
