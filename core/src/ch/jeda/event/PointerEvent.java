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

import java.util.EnumSet;

/**
 * Represents an event of the type {@link EventType#POINTER_DOWN}, {@link EventType#POINTER_MOVED},
 * {@link EventType#POINTER_UP}, or {@link EventType#WHEEL}.
 *
 * @version 2
 * @since 1.0
 */
public final class PointerEvent extends Event {

    private final int pointerId;
    private final EnumSet<Button> pressedButtons;
    private final float viewX;
    private final float viewY;
    private final float wheel;
    private final float worldX;
    private final float worldY;

    /**
     * Constructs a pointer event.
     *
     * @param source the event source that generates the event
     * @param type the event type
     * @param pointerId the id of the pointer
     * @param pressedButtons the currently pressed buttons
     * @param wheel the movement of the wheel
     * @param viewX the horizontal view coordinate of the pointer
     * @param viewY the vertical view coordinate of the pointer
     * @param worldX the horizontal world coordinate of the pointer
     * @param worldY the vertical world coordinate of the pointer
     *
     * @since 2.0
     */
    public PointerEvent(final Object source, final EventType type, final int pointerId,
                        final EnumSet<Button> pressedButtons, final float wheel, final float viewX,
                        final float viewY, final float worldX, final float worldY) {
        super(source, type);
        this.pointerId = pointerId;
        this.pressedButtons = pressedButtons;
        this.viewX = viewX;
        this.viewY = viewY;
        this.wheel = wheel;
        this.worldX = worldX;
        this.worldY = worldY;
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
     * @deprecated Use {@link #getViewX()} instead.
     */
    public final float getCanvasX() {
        return viewX;
    }

    /**
     * @deprecated Use {@link #getViewY()} instead.
     */
    public final float getCanvasY() {
        return viewY;
    }

    /**
     * Returns the horizontal view coordinate of the pointer.
     *
     * @return horizontal view coordinate of the pointer
     *
     * @since 2.1
     */
    public final float getViewX() {
        return viewX;
    }

    /**
     * Returns the vertical view coordinate of the pointer.
     *
     * @return vertical view coordinate of the pointer
     *
     * @since 2.1
     */
    public final float getViewY() {
        return viewY;
    }

    /**
     * Returns the wheel rotation of the pointing device.
     *
     * @return the wheel rotation of the pointing device
     *
     * @since 2.0
     */
    public float getWheel() {
        return wheel;
    }

    /**
     * Returns the horizontal world coordinate of the pointer.
     *
     * @return horizontal world coordinate of the pointer
     *
     * @since 2.0
     */
    public final float getWorldX() {
        return worldX;
    }

    /**
     * Returns the vertical world coordinate of the pointer.
     *
     * @return vertical world coordinate of the pointer
     *
     * @since 2.0
     */
    public final float getWorldY() {
        return worldY;
    }

    /**
     * Returns the x coordinate of the pointer.
     *
     * @return x coordinate of the pointer
     *
     * @since 1.0
     */
    public final int getX() {
        return (int) viewX;
    }

    /**
     * Returns the y coordinate of the pointer.
     *
     * @return y coordinate of the pointer
     *
     * @since 1.0
     */
    public final int getY() {
        return (int) viewY;
    }

    /**
     * Checks if a button is currently pressed.
     *
     * @param button the button to check
     * @return <code>true</code> if the specified button is pressed, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public final boolean isPressed(final Button button) {
        return pressedButtons.contains(button);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("PointerEvent(type=");
        result.append(getType());
        result.append(", pointerId=");
        result.append(pointerId);
        result.append(", x=");
        result.append(viewX);
        result.append(", y=");
        result.append(viewY);
        result.append(")");
        return result.toString();
    }
}
