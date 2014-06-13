/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

import ch.jeda.ui.Color;
import java.io.IOException;
import java.io.OutputStream;

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

    int[] getPixels(int x, int y, int width, int height);

    int getWidth();

    ImageImp replacePixels(Color oldColor, Color newColor);

    ImageImp rotate(double angle);

    ImageImp scale(int width, int height);

    ImageImp subImage(int x, int y, int width, int height);

    boolean write(OutputStream out, Encoding encoding) throws IOException;
}
