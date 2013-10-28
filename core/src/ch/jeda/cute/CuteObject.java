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
package ch.jeda.cute;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

public class CuteObject {

    private Box box;
    private String message;
    private float messageTimeout;
    private CuteObjectType objectType;
    private CuteWorld world;
    private float vx;
    private float vy;
    private float vz;
    private float x;
    private float y;
    private float z;

    public CuteObject(final CuteObjectType objectType) {
        this(objectType, 0f, 0f, 0f);
    }

    public CuteObject(final CuteObjectType objectType, final float x, final float y, final float z) {
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

    public void draw(final Canvas canvas, final float x, final float y) {
        if (this.objectType != null) {
            canvas.drawImage(x, y, this.objectType.getImage(), Alignment.BOTTOM_CENTER);
        }
        if (this.message != null) {
            SpeechBubble.draw(canvas, x, y, this.message);
        }
    }

    public final int getIntX() {
        return Math.round(this.x);
    }

    public final int getIntY() {
        return Math.round(this.y);
    }

    public final int getIntZ() {
        return Math.round(this.z);
    }

    public final String getMessage() {
        return this.message;
    }

    public final CuteObjectType getObjectType() {
        return this.objectType;
    }

    public final float getVx() {
        return this.vx;
    }

    public final float getVy() {
        return this.vy;
    }

    public final float getVz() {
        return this.vz;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getZ() {
        return this.z;
    }

    public final void setMessage(final String message) {
        this.setMessage(message, -1f);
    }

    public final void setMessage(final String message, final float seconds) {
        this.message = message;
        this.messageTimeout = seconds;
    }

    public final void setPosition(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.changed();
    }

    public final void setVx(final float vx) {
        this.vx = vx;
    }

    public final void setVy(final float vy) {
        this.vy = vy;
    }

    public final void setVz(final float vz) {
        this.vz = vz;
    }

    public void update(final float dt) {
    }

    protected void moveTo(final float newX, final float newY, final float newZ) {
        this.setPosition(newX, newY, newZ);
    }

    void internalUpdate(final float dt) {
        if (this.messageTimeout > 0f) {
            this.messageTimeout -= dt;
            if (this.messageTimeout < 0f) {
                this.message = null;
            }
        }

        this.update(dt);
        float newX = getX() + this.vx * dt;
        float newY = getY() + this.vy * dt;
        float newZ = getZ() + this.vz * dt;
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
