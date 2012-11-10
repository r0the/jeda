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

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;

public class AndroidViewImp extends AndroidCanvasImp implements ViewImp {

    private final MainActivity mainActivity;
    private final SurfaceHolder surfaceHolder;

    AndroidViewImp(MainActivity mainActivity, SurfaceHolder surfaceHolder) {
        this.mainActivity = mainActivity;
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();
        this.setSize(canvas.getWidth(), canvas.getHeight());
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void close() {
    }

    public boolean isDoubleBuffered() {
        return false;
    }

//    public InputImp getInputImp() {
//        return new AndroidInputImp();
//    }
    public boolean isFullscreen() {
        return false;
    }

    public void setMouseCursor(MouseCursor mouseCursor) {
        // ignore
    }

    public void setTitle(final String title) {
        this.mainActivity.runOnUiThread(new Runnable() {

            public void run() {
                mainActivity.setTitle(title);
            }
        });
    }

    public void update() {
        Canvas canvas = this.surfaceHolder.lockCanvas();
        canvas.drawBitmap(this.getBitmap(), 0f, 0f, null);
        this.surfaceHolder.unlockCanvasAndPost(canvas);

    }

    @Override
    void modified() {
        this.update();
    }
}
