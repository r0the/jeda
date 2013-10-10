/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.ui;

/**
 * Represents an event of the type {@link EventType#TICK}.
 *
 * @since 1
 */
public class TickEvent extends Event {

    private final float duration;
    private final float frameRate;

    public TickEvent(final EventSource source, final EventType type, final float duration, final float frameRate) {
        super(source, type);
        this.duration = duration;
        this.frameRate = frameRate;
    }

    /**
     * Returns the duration of the last frame in seconds. This value can be used to calculate smooth movements.
     *
     * @return duration of last frame in seconds
     * @since 1
     */
    public float getDuration() {
        return this.duration;
    }

    /**
     * Returns the current frame rate.
     *
     * @return the current frame rate
     * @since 1
     */
    public float getFrameRate() {
        return this.frameRate;
    }
}
