/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda.event;

/**
 * Represents an event of the type {@link ch.jeda.event.EventType#TICK}.
 *
 * @since 1.0
 */
public class TickEvent extends Event {

    private final double duration;
    private final double frameRate;

    /**
     * Constructs a tick event.
     *
     * @param source the event source that generates the event
     * @param duration the duration since the last frame in seconds
     * @param frameRate the current frame rate
     *
     * @since 1.0
     */
    public TickEvent(final Object source, final double duration, final double frameRate) {
        super(source, EventType.TICK);
        this.duration = duration;
        this.frameRate = frameRate;
    }

    /**
     * Returns the duration of the last frame in seconds. This value can be used to calculate smooth movements.
     *
     * @return duration of last frame in seconds
     *
     * @since 1.0
     */
    public final double getDuration() {
        return duration;
    }

    /**
     * Returns the current frame rate.
     *
     * @return the current frame rate
     *
     * @since 1.0
     */
    public final double getFrameRate() {
        return frameRate;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("TickEvent(type=");
        result.append(getType());
        result.append(", duration=");
        result.append(duration);
        result.append(", frameRate=");
        result.append(frameRate);
        result.append(")");
        return result.toString();
    }
}
