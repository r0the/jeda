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
 * Represents an event of the type {@link EventType#POINTER_DOWN}, {@link EventType#POINTER_MOVED}, or
 * {@link EventType#POINTER_UP}.
 *
 * @since 1.0
 */
public final class PointerEvent extends Event {

    private final int pointerId;
    private final float x;
    private final float y;

    /**
     * Constructs a pointer event.
     *
     * @param source the event source that generates the event
     * @param type the event type
     * @param pointerId the id of the pointer
     *
     * @since 1.0
     */
    public PointerEvent(final Object source, final EventType type, final int pointerId) {
        this(source, type, pointerId, -1, -1);
    }

    /**
     * Constructs a pointer event.
     *
     * @param source the event source that generates the event
     * @param type the event type
     * @param pointerId the id of the pointer
     * @param x the x coordinate of the pointer
     * @param y the y coordinate of the pointer
     *
     * @since 1.0
     */
    public PointerEvent(final Object source, final EventType type, final int pointerId,
                        final float x, final float y) {
        super(source, type);
        this.pointerId = pointerId;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the id of the pointer.
     *
     * @return id of the pointer
     *
     * @since 1.0
     */
    public final int getPointerId() {
        return pointerId;
    }

    /**
     * Returns the x coordinate of the pointer.
     *
     * @return x coordinate of the pointer
     *
     * @since 1.0
     */
    public final float getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the pointer.
     *
     * @return y coordinate of the pointer
     *
     * @since 1.0
     */
    public final float getY() {
        return y;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("PointerEvent(type=");
        result.append(getType());
        result.append(", pointerId=");
        result.append(pointerId);
        result.append(", x=");
        result.append(x);
        result.append(", y=");
        result.append(y);
        result.append(")");
        return result.toString();
    }
}
