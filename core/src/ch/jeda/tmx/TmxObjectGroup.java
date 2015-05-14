/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.tmx;

import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a TMX object group.
 *
 * @since 2.0
 */
public final class TmxObjectGroup extends TmxLayer {

    private final Color color;
    private final TmxObject[] objects;

    TmxObjectGroup(final TmxMap map, final Element element) {
        super(map, element);
        color = element.getColorAttribute(Const.COLOR, Color.RED);
        final List<Element> objectElements = element.getChildren(Const.OBJECT);
        objects = new TmxObject[objectElements.size()];
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = new TmxObject(map, objectElements.get(i));
        }
    }

    /**
     * Adds the contents of this object group to a physics view.
     *
     * @param view the physics view
     *
     * @since 2.0
     */
    @Override
    public void addTo(final PhysicsView view) {
        for (int i = 0; i < objects.length; ++i) {
            view.add(objects[i].toBody());
        }
    }

    @Override
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        if (!isVisible()) {
            return;
        }

        canvas.setLineWidth(3);
        canvas.setColor(color);
        for (int i = 0; i < objects.length; ++i) {
            objects[i].draw(canvas, offsetX, offsetY);
        }
    }

    /**
     * Returns the color of this object group.
     *
     * @return the color of this object group
     *
     * @since 2.0
     */
    public Color getColor() {
        return color;
    }

    @Override
    public TmxObject[] getObjects() {
        return Arrays.copyOf(objects, objects.length);
    }

    @Override
    public final TmxLayerType getType() {
        return TmxLayerType.OBJECT;
    }
}
