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
package ch.jeda.physics;

import ch.jeda.geometry.Rectangle;

/**
 * A hollow box. This is a convienience class for physical worlds.
 *
 * @since 2.0
 */
public class Box extends Body {

    /**
     * Constructs a box.
     *
     * @param x the horizontal coordinate of the center of the box
     * @param y the vertical coordinate of the center of the box
     * @param width the width of the box
     * @param height the height of the box
     * @param thickness the thickness of the box
     */
    public Box(final double x, final double y, final double width, final double height, final double thickness) {
        this((float) x, (float) y, (float) width, (float) height, (float) thickness);
    }

    /**
     * Constructs a box.
     *
     * @param x the horizontal coordinate of the center of the box
     * @param y the vertical coordinate of the center of the box
     * @param width the width of the box
     * @param height the height of the box
     * @param thickness the thickness of the box
     */
    public Box(final float x, final float y, final float width, final float height, final float thickness) {
        setPosition(x, y);
        setType(BodyType.STATIC);
        float hw = width / 2f;
        float hh = height / 2f;
        addShape(new Rectangle(-hw - thickness, -hh - thickness, width + 2f * thickness, thickness));
        addShape(new Rectangle(-hw - thickness, hh, width + 2f * thickness, thickness));
        addShape(new Rectangle(-hw - thickness, -hh, thickness, height));
        addShape(new Rectangle(hw, -hh, thickness, height));
    }

    /**
     * Constructs a box that surrounds the view.
     *
     * @param view the view
     */
    public Box(final PhysicsView view) {
        this(view.getCenterX(), view.getCenterY(), view.getWidthM(), view.getHeightM(), 0.5f);
    }
}
