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
package ch.jeda.platform.android;

import android.graphics.Typeface;
import ch.jeda.platform.TypefaceImp;

class AndroidTypefaceImp implements TypefaceImp {

    final Typeface imp;

    public AndroidTypefaceImp(final Typeface imp) {
        this.imp = imp;
    }

    @Override
    public String getName() {
        return this.imp.toString();
    }

    public boolean isAvailable() {
        return true;
    }
}
