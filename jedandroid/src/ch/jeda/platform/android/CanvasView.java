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
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ch.jeda.Size;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.Window;

class CanvasView extends BaseView implements SurfaceHolder.Callback {

    private final AndroidEventsImp eventsImp;
    private WindowRequest request;
    private boolean surfaceAvailable;
    private SurfaceHolder surfaceHolder;
    private MySurfaceView surfaceView;
    private Size surfaceSize;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceAvailable = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.surfaceSize = Size.from(width, height);
        if (this.request != null) {
            this.request.setResult(AndroidWindowImp.create(
                    this, this.request.hasFeature(Window.Feature.DoubleBuffered)));
            this.request = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceAvailable = false;
    }

    private void addMotionEvent(MotionEvent event) {
        this.eventsImp.addMotionEvent(event);
    }

    CanvasView(ViewManager manager, WindowRequest request) {
        super(manager);
        this.eventsImp = new AndroidEventsImp();
        this.request = request;
        this.surfaceView = new MySurfaceView(this);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.addView(this.surfaceView);
    }

    AndroidEventsImp getEventsImp() {
        return this.eventsImp;
    }

    Size getSize() {
        return this.surfaceSize;
    }

    void setBitmap(Bitmap bitmap) {
        if (this.surfaceAvailable) {
            Canvas canvas = this.surfaceHolder.lockCanvas();
            canvas.drawBitmap(bitmap, 0f, 0f, null);
            this.surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private static class MySurfaceView extends SurfaceView {

        private final CanvasView canvasView;

        MySurfaceView(CanvasView canvasView) {
            super(canvasView.getContext());
            this.canvasView = canvasView;
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            //this.eventsImp.addKeyEvent(event);
            return super.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            this.canvasView.addMotionEvent(event);
            return true;
        }
    }
}
