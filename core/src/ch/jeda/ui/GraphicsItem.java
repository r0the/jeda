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

import java.util.Comparator;

public abstract class GraphicsItem {

    static final Comparator<GraphicsItem> DRAW_ORDER = new DrawOrder();
    GraphicsItems owner;
    private boolean dirty;
    private float x;
    private float y;
    private float drawOrder;

    protected GraphicsItem() {
    }

    public final float getDrawOrder() {
        return this.drawOrder;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final void setDrawOrder(final float drawOrder) {
        this.drawOrder = drawOrder;
        if (this.owner != null) {
            this.owner.setDirty();
        }
    }

    public final void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.dirty = true;
    }

    protected abstract boolean contains(float x, float y);

    protected abstract void draw(Canvas canvas);

    private static class DrawOrder implements Comparator<GraphicsItem> {

        @Override
        public int compare(final GraphicsItem object1, final GraphicsItem object2) {
            return (int) Math.signum(object1.drawOrder - object2.drawOrder);
        }
    }
}
