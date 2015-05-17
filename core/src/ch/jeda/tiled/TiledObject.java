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

import ch.jeda.Data;
import ch.jeda.geometry.Circle;
import ch.jeda.geometry.Ellipse;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

/**
 * Represents a Tiled object.
 *
 * @since 2.0
 */
public final class TiledObject {

    private final double height;
    private final String name;
    private final Data properties;
    private final double rotation;
    private final Shape shape;
    private final Tile tile;
    private final String type;
    private final boolean visible;
    private final double width;
    private final double x;
    private final double y;

    TiledObject(final TiledMap map, final ElementWrapper element) {
        height = element.getDoubleAttribute(Const.HEIGHT, 0.0);
        name = element.getStringAttribute(Const.NAME);
        properties = element.parsePropertiesChild();
        rotation = element.getDoubleAttribute(Const.ROTATION, 0.0);
        shape = Parser.parseShape(element, 0.0, 0.0);
        type = element.getStringAttribute(Const.TYPE);
        visible = element.getBooleanAttribute(Const.VISIBLE, true);
        width = element.getDoubleAttribute(Const.WIDTH, 0.0);
        double cx = element.getDoubleAttribute(Const.X);
        double cy = element.getDoubleAttribute(Const.Y);
        if (shape instanceof Circle || shape instanceof Ellipse || shape instanceof Rectangle) {
            cx = cx + width / 2.0;
            cy = cy + height / 2.0;
        }

        x = cx;
        y = cy;
        if (element.hasAttribute(Const.GID)) {
            tile = map.lookupTile(element.getIntAttribute(Const.GID));
        }
        else {
            tile = null;
        }
    }

    /**
     * Draws this tile with the offset. Has no effect if the object is not visible.
     *
     * @param canvas the canvas to draw on
     * @param offsetX the horizontal offset
     * @param offsetY the vertical offset
     *
     * @since 2.0
     */
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        if (!visible) {
            return;
        }

        canvas.pushTransformations();
        canvas.resetTransformations();
        canvas.setTranslation(x + offsetX, y + offsetY);
        canvas.setRotationDeg(rotation);
        if (tile != null) {
            canvas.drawImage(0, 0, tile.getImage(), Alignment.BOTTOM_LEFT);
        }

        if (shape != null) {
            shape.draw(canvas);
        }

        canvas.popTransformations();
    }

    /**
     * Returns the height of this object.
     *
     * @return the height of this object
     *
     * @since 2.0
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the name of this object.
     *
     * @return the name of this object
     *
     * @since 2.0
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the properties of this object.
     *
     * @return the properties of this object
     *
     * @since 2.0
     */
    public Data getProperties() {
        return properties;
    }

    /**
     * Returns the rotation of this object in degrees.
     *
     * @return the rotation of this object in degrees
     *
     * @since 2.0
     */
    public double getRotationDeg() {
        return rotation;
    }

    /**
     * Returns the rotation of this object in radians.
     *
     * @return the rotation of this object in radians
     *
     * @since 2.0
     */
    public double getRotationRad() {
        return Math.toRadians(rotation);
    }

    /**
     * Returns the shape of this object.
     *
     * @return the shape of this object
     *
     * @since 2.0
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Returns the tile representing this object.
     *
     * @return the tile representing this object
     *
     * @since 2.0
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Returns the type of this object.
     *
     * @return the type of this object
     *
     * @since 2.0
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the width of this object.
     *
     * @return the width of this object
     *
     * @since 2.0
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the x coordinate of this object.
     *
     * @return the x coordinate of this object
     *
     * @since 2.0
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this object.
     *
     * @return the y coordinate of this object
     *
     * @since 2.0
     */
    public double getY() {
        return y;
    }

    /**
     * Converts this Tiled object to a body.
     *
     * @return the body
     *
     * @since 2.0
     */
    public Body toBody() {
        final Body result = Body.create(getType());
        result.addShape(shape);
        result.setName(name);
        result.setPosition(x, y);
        result.setType(BodyType.DYNAMIC);
        if (tile != null) {
            result.setImage(tile.getImage());
            Shape[] shapes = tile.getShapes();
            for (int i = 0; i < shapes.length; ++i) {
                result.addShape(shapes[i]);
            }
        }

        return result;
    }
}
