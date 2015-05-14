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

import ch.jeda.Data;
import ch.jeda.geometry.Ellipse;
import ch.jeda.geometry.Polygon;
import ch.jeda.geometry.PolygonalChain;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

/**
 * Represents a TMX object.
 *
 * @since 2.0
 */
public final class TmxObject {

    private final double height;
    private final String name;
    private final Data properties;
    private final double rotation;
    private final Shape shape;
    private final TmxTile tile;
    private final String type;
    private final boolean visible;
    private final double width;
    private final double x;
    private final double y;

    TmxObject(final TmxMap map, final Element element) {
        height = element.getDoubleAttribute(Const.HEIGHT, 0.0);
        name = element.getStringAttribute(Const.NAME);
        properties = element.parsePropertiesChild();
        rotation = element.getDoubleAttribute(Const.ROTATION, 0.0);
        shape = parseShape(element);
        type = element.getStringAttribute(Const.TYPE);
        visible = element.getBooleanAttribute(Const.VISIBLE, true);
        width = element.getDoubleAttribute(Const.WIDTH, 0.0);
        x = element.getDoubleAttribute(Const.X);
        y = element.getDoubleAttribute(Const.Y);
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
    public TmxTile getTile() {
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
     * Converts this TMX object to a body.
     *
     * @return the body
     *
     * @since 2.0
     */
    public Body toBody() {
        final Body result = new Body();
        result.addShape(getShape());
        result.setName(name);
        result.setPosition(x, y);
        result.setType(BodyType.DYNAMIC);
        if (tile != null) {
            result.setImage(tile.getImage());
        }

        return result;
    }

    private static Shape parseShape(final Element element) {
        if (element.hasChild(Const.ELLIPSE)) {
            final double height = element.getDoubleAttribute(Const.HEIGHT, 0.0);
            final double width = element.getDoubleAttribute(Const.WIDTH, 0.0);
            return new Ellipse(width / 2.0, height / 2.0);
        }
        else if (element.hasChild(Const.POLYGON)) {
            final String points = element.getChild(Const.POLYGON).getStringAttribute(Const.POINTS);
            return new Polygon(parsePoints(points));
        }
        else if (element.hasChild(Const.POLYLINE)) {
            final String points = element.getChild(Const.POLYLINE).getStringAttribute(Const.POINTS);
            return new PolygonalChain(parsePoints(points));
        }
        else {
            final double height = element.getDoubleAttribute(Const.HEIGHT, 0.0);
            final double width = element.getDoubleAttribute(Const.WIDTH, 0.0);
            return new Rectangle(width, height);
        }
    }

    private static double[] parsePoints(final String points) {
        String[] parts = points.split(" |,");
        final double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            result[i] = Double.parseDouble(parts[i]);
        }

        return result;
    }
}
