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
package ch.jeda.platform.android;

import android.graphics.Bitmap;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;
import java.io.IOException;
import java.io.OutputStream;

public class AndroidImageImp implements ImageImp {

    private final Bitmap bitmap;
    private final Size size;

    public AndroidImageImp(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.size = new Size(this.bitmap.getWidth(), this.bitmap.getHeight());
    }

    @Override
    public ImageImp createScaledImage(Size newSize) {
        assert newSize != null;

        return new AndroidImageImp(Bitmap.createScaledBitmap(
                this.bitmap, newSize.width, newSize.height, false));
    }

    @Override
    public ImageImp createSubImage(Location topLeft, Size size) {
        return new AndroidImageImp(Bitmap.createBitmap(
                this.bitmap, topLeft.x, topLeft.y, size.width, size.height));
    }

    @Override
    public Size getSize() {
        return this.size;
    }

    @Override
    public ImageImp replacePixels(Color oldColor, Color newColor) {
        assert oldColor != null;
        assert newColor != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean write(OutputStream out, Encoding encoding) throws IOException {
        return this.bitmap.compress(convertEncoding(encoding), 100, out);
    }

    Bitmap getBitmap() {
        return this.bitmap;
    }

    private static Bitmap.CompressFormat convertEncoding(Encoding encoding) {
        switch (encoding) {
            case JPEG:
                return Bitmap.CompressFormat.JPEG;
            case PNG:
                return Bitmap.CompressFormat.PNG;
            default:
                return null;
        }
    }
}
