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
package ch.jeda.cute;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

public class CuteObject {

    private Box box;
    private String message;
    private double messageTimeout;
    private CuteObjectType objectType;
    private double radius;
    private CuteWorld world;
    private double vx;
    private double vy;
    private double vz;
    private double x;
    private double y;
    private double z;

    public CuteObject(final CuteObjectType objectType) {
        this(objectType, 0f, 0f, 0f);
    }

    public CuteObject(final CuteObjectType objectType, final double x, final double y, final double z) {
        if (objectType == null) {
            this.objectType = CuteObjectType.EMPTY;
        }
        else {
            this.objectType = objectType;
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double distanceTo(final CuteObject object) {
        if (object == null) {
            throw new NullPointerException("object");
        }

        final double dx = object.x - this.x;
        final double dy = object.y - this.y;
        final double dz = object.z - this.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public final int getIntX() {
        return (int) Math.round(this.x);
    }

    public final int getIntY() {
        return (int) Math.round(this.y);
    }

    public final int getIntZ() {
        return (int) Math.round(this.z);
    }

    public final String getMessage() {
        return this.message;
    }

    public final CuteObjectType getObjectType() {
        return this.objectType;
    }

    public final double getRadius() {
        return this.radius;
    }

    public final double getVx() {
        return this.vx;
    }

    public final double getVy() {
        return this.vy;
    }

    public final double getVz() {
        return this.vz;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public final void remove() {
        this.world.removeObject(this);
    }

    public final void setMessage(final String message) {
        this.setMessage(message, -1f);
    }

    public final void setMessage(final String message, final double seconds) {
        this.message = message;
        this.messageTimeout = seconds;
    }

    public final void setPosition(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.changed();
    }

    public final void setRadius(final double radius) {
        if (radius < 0) {
            this.radius = 0;
        }
        else {
            this.radius = radius;
        }
    }

    public final void setVx(final double vx) {
        this.vx = vx;
    }

    public final void setVy(final double vy) {
        this.vy = vy;
    }

    public final void setVz(final double vz) {
        this.vz = vz;
    }

    protected void collideWith(final CuteObject other) {
    }

    protected void draw(final Canvas canvas, final double x, final double y) {
        if (this.objectType != null) {
            canvas.drawImage(x, y, this.objectType.getImage(), Alignment.BOTTOM_CENTER);
        }
    }

    protected void moveTo(final double newX, final double newY, final double newZ) {
        this.setPosition(newX, newY, newZ);
    }

    protected void update(final double dt) {
    }

    void internalUpdate(final double dt) {
        if (this.messageTimeout > 0f) {
            this.messageTimeout -= dt;
            if (this.messageTimeout < 0f) {
                this.message = null;
            }
        }

        this.update(dt);
        double newX = getX() + this.vx * dt;
        double newY = getY() + this.vy * dt;
        double newZ = getZ() + this.vz * dt;
        this.moveTo(newX, newY, newZ);
    }

    void setRenderer(final CuteWorld renderer) {
        this.world = renderer;
        this.changed();
    }

    private void changed() {
        if (this.box != null) {
            this.box.removeObject(this);
        }

        if (this.world != null) {
            this.box = this.world.getBox(this.getIntX(), this.getIntY(), this.getIntZ());
            if (this.box != null) {
                this.box.addObject(this);
            }
        }
    }
}
