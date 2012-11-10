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
import ch.jeda.Log;
import ch.jeda.Size;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;
import java.io.FileOutputStream;

public class AndroidImageImp implements ImageImp {

    private final Bitmap bitmap;
    private final Size size;

    public AndroidImageImp(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.size = new Size(this.bitmap.getWidth(), this.bitmap.getHeight());
    }

    public ImageImp createScaledImage(Size newSize) {
        assert newSize != null;

        return new AndroidImageImp(
                Bitmap.createScaledBitmap(this.bitmap,
                newSize.width, newSize.height, false));
    }

    public Size getSize() {
        return this.size;
    }

    public ImageImp replacePixels(Color oldColor, Color newColor) {
        assert oldColor != null;
        assert newColor != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean save(String filePath) {
        boolean result = false;
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            result = this.bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            if (!result) {
                Log.warning("jeda.gui.image.format.error", filePath, "png");
            }
        }
        catch (Exception ex) {
            Log.warning("jeda.fs.image.write.error", filePath);
        }

        return result;
    }
}
