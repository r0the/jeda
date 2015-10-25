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
import ch.jeda.geometry.Shape;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.ui.Image;

/**
 * Represents a Tiled object.
 *
 * @since 2.0
 */
public final class TiledObject {

    private final float height;
    private final String name;
    private final Data properties;
    private final float rotation;
    private final Shape shape;
    private final Tile tile;
    private final String type;
    private final boolean visible;
    private final float width;
    private final float x;
    private final float y;

    TiledObject(final TiledMap map, final ElementWrapper element) {
        final float tileWidth = map.getTileWidth();
        final float tileHeight = map.getTileHeight();
        name = element.getStringAttribute(Const.NAME);
        properties = element.parsePropertiesChild();
        rotation = element.getFloatAttribute(Const.ROTATION);
        type = element.getStringAttribute(Const.TYPE);
        visible = element.getBooleanAttribute(Const.VISIBLE, true);

        final float objectX = element.getFloatAttribute(Const.X) / tileWidth;
        float objectY = element.getFloatAttribute(Const.Y) / tileHeight;
        if (element.hasAttribute(Const.GID)) {
            tile = map.lookupTile(element.getIntAttribute(Const.GID));
            height = tile.getHeight();
            width = tile.getWidth();
            objectY = objectY - height;
        }
        else {
            tile = null;
            height = element.getFloatAttribute(Const.HEIGHT) / tileHeight;
            width = element.getFloatAttribute(Const.WIDTH) / tileWidth;
        }

        final float halfWidth = width / 2f;
        final float halfHeight = height / 2f;
        shape = Parser.parseShape(element, map, -objectX - halfWidth, objectY + halfHeight);
        x = objectX + halfWidth;
        y = map.getHeight() - objectY - halfHeight;
    }

    /**
     * Returns the height of this object.
     *
     * @return the height of this object
     *
     * @since 2.0
     */
    public float getHeight() {
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
    public float getRotationDeg() {
        return rotation;
    }

    /**
     * Returns the rotation of this object in radians.
     *
     * @return the rotation of this object in radians
     *
     * @since 2.0
     */
    public float getRotationRad() {
        return (float) Math.toRadians(rotation);
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
    public float getWidth() {
        return width;
    }

    /**
     * Returns the x coordinate of this object.
     *
     * @return the x coordinate of this object
     *
     * @since 2.0
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this object.
     *
     * @return the y coordinate of this object
     *
     * @since 2.0
     */
    public float getY() {
        return y;
    }

    /**
     * Checks if this object is visible.
     *
     * @return <code>true</code> if this object is visible, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Converts this Tiled object to a body.
     *
     * @return the body
     *
     * @since 2.0
     */
    public Body toBody() {
        final Body result = Body.create(getProperties().readString(Const.CLASS));
        result.addShape(shape);
        result.setName(name);
        result.setPosition(x, y);
        result.setType(BodyType.parse(type, BodyType.DYNAMIC));
        result.setAngularDamping(properties.readFloat(Const.ANGULAR_DAMPING, 0f));
        result.setDamping(properties.readFloat(Const.DAMPING, 0f));
        result.setDensity(properties.readFloat(Const.DENSITY, 1f));
        result.setFriction(properties.readFloat(Const.FRICTION, 0f));
        result.setRotationFixed(properties.readBoolean(Const.ROTATION_FIXED, false));
        if (tile != null) {
            final Image image = tile.getImage();
            result.setImage(image, tile.getWidth(), tile.getHeight());
            Shape[] shapes = tile.getShapes();
            for (int i = 0; i < shapes.length; ++i) {
                result.addShape(shapes[i]);
            }
        }

        return result;
    }
}
