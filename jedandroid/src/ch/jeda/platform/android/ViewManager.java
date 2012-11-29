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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ch.jeda.Size;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import ch.jeda.ui.Window.Feature;

class ViewManager implements SurfaceHolder.Callback {

    private final Activity activity;
    private final Object surfaceLock;
    private boolean surfaceAvailable;
    private SurfaceHolder surfaceHolder;
    private JedaSurfaceView surfaceView;
    private Size surfaceSize;

    public ViewManager(Activity activity) {
        this.activity = activity;
        this.surfaceLock = new Object();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        synchronized (this.surfaceLock) {
            this.surfaceAvailable = true;
            this.surfaceSize = new Size(width, height);
            this.surfaceLock.notify();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this.surfaceLock) {
            this.surfaceAvailable = false;
            this.surfaceLock.notify();
        }
    }

    ViewImp createViewImp(ViewInfo viewInfo) {
        synchronized (this.surfaceLock) {
            while (!this.surfaceAvailable) {
                try {
                    this.surfaceLock.wait();
                }
                catch (InterruptedException ex) {
                }
            }

            return AndroidViewImp.create(this, viewInfo.hasFeature(Feature.DoubleBuffered));
        }
    }

    AndroidEventsImp getEventsImp() {
        return this.surfaceView.getEventsImp();
    }

    Size getSize() {
        return this.surfaceSize;
    }

    void onCreate() {
        this.surfaceView = new JedaSurfaceView(this.activity);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.activity.setContentView(this.surfaceView);
        this.surfaceHolder.addCallback(this);
    }

    void setTitle(final String title) {
        this.activity.runOnUiThread(new Runnable() {

            public void run() {
                activity.setTitle(title);
            }
        });
    }

    void setBitmap(Bitmap bitmap) {
        if (this.surfaceAvailable) {
            Canvas canvas = this.surfaceHolder.lockCanvas();
            canvas.drawBitmap(bitmap, 0f, 0f, null);
            this.surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
