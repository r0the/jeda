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
package ch.jeda.world;

import ch.jeda.ui.PointerEvent;
import ch.jeda.ui.PointerListener;

public abstract class Figure extends WorldObject implements PointerListener {

    private boolean draggable;
    private PointerEvent dragStart;

    public Figure() {
    }

    public final boolean isDraggable() {
        return this.draggable;
    }

    public abstract void move(float dx, float dy);

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.draggable && this.dragStart == null && this.contains(event.getX(), event.getY())) {
            this.dragStart = event;
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if (this.draggable && this.dragStart != null && this.dragStart.getPointerId() == event.getPointerId()) {
            this.move(event.getX() - this.dragStart.getX(), event.getY() - this.dragStart.getY());
            this.dragStart = event;
        }
    }

    @Override
    public void onPointerUp(final PointerEvent event) {
        if (this.draggable && this.dragStart != null && this.dragStart.getPointerId() == event.getPointerId()) {
            this.dragStart = null;
        }
    }

    public final void setDraggable(final boolean draggable) {
        this.draggable = draggable;
    }
}
