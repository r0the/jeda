/*
 * Copyright (C) 2014 by Stefan Rothe
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
package ch.jeda.platform;

/**
 * <b>Internal</b>. Do not use this class.
 */
public final class CanvasTransformation {

    public double rotation;
    public double scale;
    public double translationX;
    public double translationY;

    public CanvasTransformation() {
        this.reset();
    }

    public CanvasTransformation(final CanvasTransformation other) {
        this.rotation = other.rotation;
        this.scale = other.scale;
        this.translationX = other.translationX;
        this.translationY = other.translationY;
    }

    public void reset() {
        this.rotation = 0.0;
        this.scale = 1.0;
        this.translationX = 0.0;
        this.translationY = 0.0;
    }
}
