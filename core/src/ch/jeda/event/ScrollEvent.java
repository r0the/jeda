/*
 * Copyright (C) 2015 by Stefan Rothe
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
 * Represents an event of the type {@link ch.jeda.event.EventType#SCROLL}.
 *
 * @since 2.0
 */
public class ScrollEvent extends Event {

    private final float dx;
    private final float dy;

    /**
     * Constructs a wheel event.
     *
     * @param source the event source
     * @param dx the horizontal scroll distance
     * @param dy the vertical scroll distance
     *
     * @since 2.0
     */
    public ScrollEvent(final Object source, final float dx, final float dy) {
        super(source, EventType.SCROLL);
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns the horizontal scroll distance.
     *
     * @return the horizontal scroll distance
     *
     * @since 2.0
     */
    public final float getDx() {
        return dx;
    }

    /**
     * Returns the vertical scroll distance.
     *
     * @return the vertical scroll distance
     *
     * @since 2.0
     */
    public final float getDy() {
        return dy;
    }
}
