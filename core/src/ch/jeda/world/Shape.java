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
package ch.jeda.world;

import ch.jeda.ui.Color;

public abstract class Shape extends Figure {

    private Color outlineColor;
    private Color fillColor;

    public final Color getFillColor() {
        return this.fillColor;
    }

    public final Color getOutlineColor() {
        return this.outlineColor;
    }

    public final void setFillColor(final Color value) {
        this.fillColor = value;
    }

    public final void setOutlineColor(final Color value) {
        this.outlineColor = value;
    }
}
