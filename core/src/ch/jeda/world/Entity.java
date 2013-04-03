/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import ch.jeda.Transformation;
import ch.jeda.geometry.ProxyShape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * This class represents an object in a simulated world. An object has a
 * location and an orientation. It may have a visual representation, can change
 * it's location and orientation. It has a bounding shape that determines
 * whether it collides with other entities.
 */
public abstract class Entity extends ProxyShape {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;

    public Entity() {
    }

    public Entity(double x, double y) {
        this.setLocation(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(this.getLocation());
        return result.toString();
    }

    /**
     * Returns the paint order of this entity. Actors with a lower paint order
     * are painted first, i.e. below actors with a higher paint order. Override
     * this method to change the paint order of your actor subclass. Each
     * implementation of this method should always return the same value.
     *
     * @return paint order of this actor class
     */
    protected int paintOrder() {
        return 0;
    }

    protected void update(World world) {
    }

    protected int updateOrder() {
        return 0;
    }

    void drawDebugOverlay(Canvas canvas) {
        if (this.getCollisionShape() != null) {
            final Transformation oldTransformation = canvas.getTransformation();
            canvas.setTransformation(this.worldTransformation());
            this.getCollisionShape().setFillColor(DEBUG_FILL_COLOR);
            this.getCollisionShape().setOutlineColor(DEBUG_OUTLINE_COLOR);
            this.getCollisionShape().draw(canvas);
            canvas.setTransformation(oldTransformation);
        }
    }
}
