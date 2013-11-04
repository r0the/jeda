/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.ui;

/**
 * @deprecated Use event system instead.
 */
public final class Pointer {

    private final int id;
    private int deltaX;
    private int deltaY;
    private int x;
    private int y;

    @Override
    /**
     * @deprecated Use event system instead.
     */
    public boolean equals(Object object) {
        if (object instanceof Pointer) {
            return this.id == ((Pointer) object).id;
        }
        else {
            return false;
        }
    }

    /**
     * @deprecated Use event system instead.
     */
    public int getDeltaX() {
        return this.deltaX;
    }

    /**
     * @deprecated Use event system instead.
     */
    public int getDeltaY() {
        return this.deltaY;
    }

    /**
     * @deprecated Use event system instead.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @deprecated Use event system instead.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @deprecated Use event system instead.
     */
    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * @deprecated Use event system instead.
     */
    public boolean isAvailable() {
        return this.x != -1;
    }

    Pointer(int id) {
        this.id = id;
        this.x = -1;
        this.y = -1;
        this.deltaX = 0;
        this.deltaY = 0;
    }

    void prepare() {
        this.deltaX = 0;
        this.deltaY = 0;
    }

    void setLocation(int x, int y) {
        if (this.x == -1) {
            this.deltaX = 0;
            this.deltaY = 0;
        }
        else {
            this.deltaX = this.deltaX + x - this.x;
            this.deltaY = this.deltaY + y - this.y;
        }

        this.x = x;
        this.y = y;
    }

    void setUnavailable() {
        this.x = -1;
        this.y = -1;
        this.deltaX = 0;
        this.deltaY = 0;
    }
}
