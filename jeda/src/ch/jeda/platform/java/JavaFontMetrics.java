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
package ch.jeda.platform.java;

import ch.jeda.platform.FontMetrics;

public class JavaFontMetrics implements FontMetrics {

    private final java.awt.FontMetrics imp;

    public JavaFontMetrics(java.awt.FontMetrics imp) {
        this.imp = imp;
    }

    @Override
    public int getAscent() {
        return imp.getAscent();
    }

    @Override
    public int getDescent() {
        return imp.getDescent();
    }

    @Override
    public int getLeading() {
        return imp.getLeading();
    }

    @Override
    public int getLineHeight() {
        return imp.getMaxAscent() + imp.getMaxDescent();
    }
}
