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
package ch.jeda.platform.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;
import java.io.IOException;
import java.io.OutputStream;

class AndroidImageImp implements ImageImp {

    final Bitmap bitmap;

    public ImageImp createRotatedImage(double angle) {
        final int width = this.bitmap.getWidth();
        final int height = this.bitmap.getHeight();
        final int diameter = (int) Math.ceil(Math.sqrt(width * width + height * height));
        final Bitmap result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        canvas.rotate((float) angle, diameter / 2, diameter / 2);
        canvas.drawBitmap(this.bitmap, (diameter - width) / 2, (diameter - height) / 2, null);
        return new AndroidImageImp(result);
    }

    @Override
    public ImageImp createScaledImage(final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return new AndroidImageImp(Bitmap.createScaledBitmap(this.bitmap, width, height, false));
    }

    @Override
    public ImageImp createSubImage(final int x, final int y, final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return new AndroidImageImp(Bitmap.createBitmap(this.bitmap, x, y, width, height));
    }

    @Override
    public int getHeight() {
        return this.bitmap.getHeight();
    }

    @Override
    public int[] getPixels(final int x, final int y, final int width, final int height) {
        final int[] result = new int[width * height];
        this.bitmap.getPixels(result, 0, width, x, y, width, height);
        return result;
    }

    @Override
    public int getWidth() {
        return this.bitmap.getWidth();
    }

    @Override
    public ImageImp replacePixels(final Color oldColor, final Color newColor) {
        assert oldColor != null;
        assert newColor != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean write(final OutputStream out, final Encoding encoding)
        throws IOException {
        return this.bitmap.compress(convertEncoding(encoding), 100, out);
    }

    AndroidImageImp(final Bitmap bitmap) {
        this.bitmap = bitmap;
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
