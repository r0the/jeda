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
package ch.jeda.ui;

import ch.jeda.JedaInternal;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.Platform;

/**
 * Represents a typeface (also known as a font family).
 *
 * @since 1.3
 */
public final class Typeface {

    /**
     * The default Jeda typeface.
     *
     * @since 1.3
     */
    public static Typeface KENVECTOR_FUTURE = new Typeface("res:jeda/fonts/kenvector_future.ttf");
    /**
     * The thin variant of the default Jeda typeface.
     *
     * @since 1.3
     */
    public static Typeface KENVECTOR_FUTURE_THIN = new Typeface("res:jeda/fonts/kenvector_future_thin.ttf");
    /**
     * The default monospaced typeface.
     *
     * @since 1.3
     */
    public static Typeface MONSPACED = new Typeface(JedaInternal.getStandardTypefaceImp(Platform.StandardTypeface.MONOSPACED));
    /**
     * The default sans-serif typeface.
     *
     * @since 1.3
     */
    public static Typeface SANS_SERIF = new Typeface(JedaInternal.getStandardTypefaceImp(Platform.StandardTypeface.SANS_SERIF));
    /**
     * The default serif typeface.
     *
     * @since 1.3
     */
    public static Typeface SERIF = new Typeface(JedaInternal.getStandardTypefaceImp(Platform.StandardTypeface.SERIF));
    final TypefaceImp imp;

    /**
     * Constructs a new typeface based on a font file. To load a font from a project resource, put "res:" in front of
     * the file path. Currently only True-Type Fonts (ttf) are supported.
     *
     * @param path path to the font file
     *
     * @since 1.3
     */
    public Typeface(final String path) {
        this(JedaInternal.createTypefaceImp(path));
    }

    public boolean isAvailable() {
        return this.imp.isAvailable();
    }

    @Override
    public String toString() {
        return this.imp.getName();
    }

    private Typeface(final TypefaceImp imp) {
        this.imp = imp;
    }
}
