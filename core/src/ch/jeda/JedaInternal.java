/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
package ch.jeda;

import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.Platform;
import ch.jeda.platform.WindowImp;
import ch.jeda.ui.WindowFeature;
import java.io.InputStream;
import java.util.EnumSet;
import org.xml.sax.XMLReader;

/**
 * <b>Internal</b>. Do not use this class.
 */
public class JedaInternal {

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static CanvasImp createCanvasImp(final int width, final int height) {
        return Jeda.createCanvasImp(width, height);
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static TypefaceImp createTypefaceImp(final String path) {
        return Jeda.createTypefaceImp(path);
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static ImageImp createImageImp(final String path) {
        return Jeda.createImageImp(path);
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static WindowImp createWindowImp(final int width, final int height, final EnumSet<WindowFeature> features) {
        return Jeda.createWindowImp(width, height, features);
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static XMLReader createXmlReader() {
        return Jeda.createXmlReader();
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static TypefaceImp getStandardTypefaceImp(final Platform.StandardTypeface standardTypeface) {
        return Jeda.getStandardTypefaceImp(standardTypeface);
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static InputStream openResource(final String path) {
        return Jeda.openResource(path);
    }
}
