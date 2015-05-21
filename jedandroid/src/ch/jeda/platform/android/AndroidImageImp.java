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
package ch.jeda.platform.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import ch.jeda.platform.ImageImp;
import java.io.IOException;
import java.io.OutputStream;

class AndroidImageImp implements ImageImp {

    private static final Matrix FLIP_HORIZONTALLY = initFlipHorizontallyMatrix();
    private static final Matrix FLIP_VERTICALLY = initFlipVerticallyMatrix();
    final Bitmap bitmap;

    AndroidImageImp(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public ImageImp flipHorizontally() {
        return new AndroidImageImp(Bitmap.createBitmap(
            bitmap, 0, 0, getWidth(), getHeight(), FLIP_HORIZONTALLY, false));
    }

    @Override
    public ImageImp flipVertically() {
        return new AndroidImageImp(Bitmap.createBitmap(
            bitmap, 0, 0, getWidth(), getHeight(), FLIP_VERTICALLY, false));
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getPixel(final int x, final int y) {
        return bitmap.getPixel(x, y);
    }

    @Override
    public int[] getPixels(final int x, final int y, final int width, final int height) {
        final int[] result = new int[width * height];
        bitmap.getPixels(result, 0, width, x, y, width, height);
        return result;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public ImageImp rotateRad(double angle) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int diameter = (int) Math.ceil(Math.sqrt(width * width + height * height));
        final Bitmap result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        canvas.rotate((float) (angle * 180 / Math.PI), diameter / 2, diameter / 2);
        canvas.drawBitmap(bitmap, (diameter - width) / 2, (diameter - height) / 2, null);
        return new AndroidImageImp(result);
    }

    @Override
    public ImageImp scale(final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return new AndroidImageImp(Bitmap.createScaledBitmap(bitmap, width, height, false));
    }

    @Override
    public ImageImp subImage(final int x, final int y, final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return new AndroidImageImp(Bitmap.createBitmap(bitmap, x, y, width, height));
    }

    @Override
    public boolean write(final OutputStream out, final Encoding encoding)
        throws IOException {
        return bitmap.compress(convertEncoding(encoding), 100, out);
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

    private static Matrix initFlipHorizontallyMatrix() {
        final Matrix result = new Matrix();
        result.preScale(-1, 1);
        return result;
    }

    private static Matrix initFlipVerticallyMatrix() {
        final Matrix result = new Matrix();
        result.preScale(1, -1);
        return result;
    }
}
