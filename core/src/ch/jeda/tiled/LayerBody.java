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
package ch.jeda.tiled;

import ch.jeda.Data;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

public class LayerBody extends Body {

    private final TileLayer layer;

    public LayerBody(final TileLayer layer) {
        this.layer = layer;
        final Data properties = layer.getProperties();

        setName(layer.getName());
        setType(BodyType.STATIC);
        setOpacity(layer.getOpacity());
        setPosition(0, 0);
        setAngularDamping(properties.readFloat(Const.ANGULAR_DAMPING, 0f));
        setDamping(properties.readFloat(Const.DAMPING, 0f));
        setDensity(properties.readFloat(Const.DENSITY, 1f));
        setFriction(properties.readFloat(Const.FRICTION, 0f));
        setRotationFixed(properties.readBoolean(Const.ROTATION_FIXED, false));
        final int endX = layer.getMap().getWidth();
        final int endY = layer.getMap().getHeight();
        for (int x = 0; x < endX; ++x) {
            for (int y = 0; y < endY; ++y) {
                final Tile tile = layer.getTile(x, y);
                if (tile != null) {
                    addShapesFor(x, y, tile);
                }
            }
        }
    }

    private void addShapesFor(final int x, final int y, final Tile tile) {
        final Shape[] shapes = tile.getShapes();
        final float width = tile.getWidth();
        final float height = tile.getHeight();
        final int endY = layer.getMap().getHeight();
        if (shapes.length > 0) {
            for (final Shape shape : shapes) {
                addShape(shape.translate(x + width / 2f, endY - y - height / 2f));
            }
        }
        else {
            addShape(new Rectangle(x, endY - y - 1f, width, height));
        }
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        canvas.setAlignment(Alignment.TOP_LEFT);
        final int endX = layer.getMap().getWidth();
        final int endY = layer.getMap().getHeight();
        for (int x = 0; x < endX; ++x) {
            for (int y = 0; y < endY; ++y) {
                final Tile tile = layer.getTile(x, y);
                if (tile != null) {
                    canvas.drawImage(x, endY - y, tile.getImage());
                }
            }
        }
    }
}
