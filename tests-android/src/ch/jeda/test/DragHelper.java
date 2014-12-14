/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
package ch.jeda.test;

import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.ui.Window;

public final class DragHelper implements PointerListener, TickListener {

    private final double maxOffsetX;
    private final double maxOffsetY;
    private PointerEvent lastPos;
    private double offsetX;
    private double offsetY;
    private double dx;
    private double dy;

    public DragHelper(final Window window, final int width, final int height) {
        window.addEventListener(this);
        this.maxOffsetX = -width + window.getWidth();
        this.maxOffsetY = -height + window.getHeight();
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public double getOffsetY() {
        return this.offsetY;
    }

    public boolean isDragging() {
        return this.lastPos != null;
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.lastPos == null) {
            this.lastPos = event;
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if ((this.lastPos != null) && (event.getPointerId() == this.lastPos.getPointerId())) {
            this.dx += event.getX() - this.lastPos.getX();
            this.dy += event.getY() - this.lastPos.getY();
            this.lastPos = event;
        }
    }

    @Override
    public void onPointerUp(final PointerEvent event) {
        if ((this.lastPos != null) && (event.getPointerId() == this.lastPos.getPointerId())) {
            this.lastPos = null;
        }
    }

    @Override
    public void onTick(final TickEvent event) {
        this.offsetX = Math.max(this.maxOffsetX, Math.min(this.offsetX + this.dx, 0.0));
        this.offsetY = Math.max(this.maxOffsetY, Math.min(this.offsetY + this.dy, 0.0));
        this.dx = 0f;
        this.dy = 0f;
    }
}
