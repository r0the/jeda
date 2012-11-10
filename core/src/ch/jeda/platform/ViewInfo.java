/*
 * Copyright (C) 2012 by Stefan Rothe
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

import ch.jeda.Size;

public class ViewInfo {

    private final Size size;
    private boolean doubleBuffered;
    private boolean fullscreen;

    public ViewInfo(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return this.size;
    }

    public boolean isDoubleBuffered() {
        return this.doubleBuffered;
    }

    public boolean isFullscreen() {
        return this.fullscreen;
    }

    public void setDoubleBuffered(boolean value) {
        this.doubleBuffered = value;
    }

    public void setFullscreen(boolean value) {
        this.fullscreen = value;
    }
}
