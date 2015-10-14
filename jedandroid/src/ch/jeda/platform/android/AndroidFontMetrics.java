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
package ch.jeda.platform.android;

import ch.jeda.platform.FontMetrics;

public class AndroidFontMetrics implements FontMetrics {

    private final android.graphics.Paint.FontMetricsInt imp;

    public AndroidFontMetrics(android.graphics.Paint.FontMetricsInt imp) {
        this.imp = imp;
    }

    @Override
    public int getAscent() {
        return imp.ascent;
    }

    @Override
    public int getDescent() {
        return imp.descent;
    }

    @Override
    public int getLeading() {
        return imp.leading;
    }

    @Override
    public int getLineHeight() {
        return imp.bottom - imp.top;
    }
}
