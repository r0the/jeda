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
package ch.jeda.tiled;

import ch.jeda.physics.Body;
import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a Tiled object layer.
 *
 * @since 2.0
 */
public final class ObjectLayer extends Layer {

    private final Color color;
    private final TiledObject[] objects;

    ObjectLayer(final TiledMap map, final ElementWrapper element) {
        super(map, element);
        color = element.getColorAttribute(Const.COLOR, Color.RED);
        final List<ElementWrapper> objectElements = element.getChildren(Const.OBJECT);
        objects = new TiledObject[objectElements.size()];
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = new TiledObject(map, objectElements.get(i));
        }
    }

    /**
     * Adds the contents of this object group to a physics view. The Tiled objects of this layer are added by creating a
     * {@link ch.jeda.physics.Body} for each Tiled object in this layer.
     *
     * @param view the physics view
     *
     * @since 2.0
     */
    @Override
    public void addTo(final PhysicsView view) {
        if (this.isVisible()) {
            for (int i = 0; i < objects.length; ++i) {
                final Body body = objects[i].toBody();
                body.setDebugColor(color);
                view.add(body);
            }
        }
    }

    /**
     * Returns the color of this object group. The color is used to draw the debug overlay of bodies created based on
     * this layer.
     *
     * @return the color of this object group
     *
     * @since 2.0
     */
    public Color getColor() {
        return color;
    }

    @Override
    public TiledObject[] getObjects() {
        return Arrays.copyOf(objects, objects.length);
    }
}
