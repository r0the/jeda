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

class FrequencyMeter {

    private static final int MAX_VALUES = 60;
    private final long[] durations;
    private double frequency;
    private int index;
    private long startTime;

    FrequencyMeter() {
        durations = new long[MAX_VALUES];
        frequency = 0f;
        index = 0;
        startTime = System.currentTimeMillis();
    }

    void count() {
        final long now = System.currentTimeMillis();
        durations[index] = now - startTime;
        startTime = now;
        if (index < MAX_VALUES - 1) {
            index = index + 1;
        }
        else {
            index = 0;
        }

        frequency = 0f;
        for (int i = 0; i < durations.length; ++i) {
            frequency = frequency + durations[i];
        }

        frequency = frequency / MAX_VALUES;
    }

    double getFrequency() {
        return 1000.0 / frequency;
    }
}
