/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
 * <b>Internal</b>. Do not use this interface.
 */
public interface ImageImp {

    enum Encoding {

        JPEG, PNG
    }

    ImageImp flipHorizontally();

    ImageImp flipVertically();

    int getHeight();

    int getPixel(int x, int y);

    int[] getPixels(int x, int y, int width, int height);

    int getWidth();

    boolean isAvailable();

    ImageImp rotateRad(double angle);

    boolean save(final String path, final Encoding encoding);

    ImageImp scale(int width, int height);

    ImageImp subImage(int x, int y, int width, int height);
}
