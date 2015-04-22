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
package ch.jeda.ui;

import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;

public abstract class Shape extends Element implements PointerListener {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    private Color fillColor;
    private PointerEvent lastEvent;
    private Color outlineColor;

    public final Collision collideWith(final Shape other) {
        if (this.getBoundingBox().intersects(other.getBoundingBox())) {
            if (this instanceof Polygon && other instanceof Polygon) {
                return ((Polygon) this).collideWithPolygon((Polygon) other);
            }
        }

        return null;
    }

    public boolean contains(final double x, final double y) {
        return this.getBoundingBox().contains(x, y) && this.doesContain(x, y);
    }

    public abstract BoundingBox getBoundingBox();

    public abstract float getCenterX();

    public abstract float getCenterY();

    public final Color getFillColor() {
        return this.fillColor;
    }

    public final Color getOutlineColor() {
        return this.outlineColor;
    }

    /**
     * @since 1.0
     */
    public boolean isDragging() {
        return this.lastEvent != null;
    }

    public abstract void move(double dx, double dy);

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.lastEvent == null) {
            this.lastEvent = event;
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if ((this.lastEvent != null) && (event.getPointerId() == this.lastEvent.getPointerId())) {
            this.move(event.getX() - this.lastEvent.getX(), event.getY() - this.lastEvent.getY());
            this.lastEvent = event;
        }
    }

    @Override
    public void onPointerUp(final PointerEvent event) {
        if ((this.lastEvent != null) && (event.getPointerId() == this.lastEvent.getPointerId())) {
            this.lastEvent = null;
        }
    }

    public final void setFillColor(final Color value) {
        this.fillColor = value;
    }

    public final void setOutlineColor(final Color value) {
        this.outlineColor = value;
    }

    protected Shape() {
        this.fillColor = null;
        this.outlineColor = Color.BLACK;
    }

    abstract boolean doesContain(double x, double y);
}
