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
package ch.jeda.ui;

class DisplayAdaption {

    final int iconSize;
    final int pixelsY;
    final float screenScale;
    final int shadowThickness;

    DisplayAdaption(final int dpi, final int pixelsY) {
        this.pixelsY = pixelsY;
        this.screenScale = 1; //dpi / 2.54f;
        if (dpi > 300) {
            this.shadowThickness = 8;
            this.iconSize = 48;
        }
        else if (dpi > 200) {
            this.shadowThickness = 5;
            this.iconSize = 36;
        }
        else if (dpi > 100) {
            this.shadowThickness = 5;
            this.iconSize = 24;
        }
        else {
            this.shadowThickness = 3;
            this.iconSize = 18;
        }
    }
}
