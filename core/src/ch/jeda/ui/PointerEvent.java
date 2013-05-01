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

import ch.jeda.ui.Event;
import ch.jeda.ui.InputDevice;
import ch.jeda.ui.EventType;

public final class PointerEvent extends Event {

    private final int pointerId;
    private final int x;
    private final int y;

    public PointerEvent(final InputDevice inputDevice, final EventType type,
                        final int pointerId) {
        this(inputDevice, type, pointerId, -1, -1);
    }

    public PointerEvent(final InputDevice inputDevice, final EventType type,
                        final int pointerId, final int x, final int y) {
        super(inputDevice, type);
        this.pointerId = pointerId;
        this.x = x;
        this.y = y;
    }

    public int getPointerId() {
        return this.pointerId;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
