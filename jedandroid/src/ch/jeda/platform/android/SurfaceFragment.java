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

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.EventType;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.PointerEvent;
import ch.jeda.platform.ViewRequest;
import ch.jeda.ui.ViewFeature;
import java.util.EnumSet;

class SurfaceFragment extends Fragment implements SurfaceHolder.Callback,
                                                  View.OnKeyListener,
                                                  View.OnTouchListener {

    private final EnumSet<ViewFeature> features;
    private EventQueue eventQueue;
    private ViewRequest request;
    private boolean surfaceAvailable;
    private SurfaceHolder surfaceHolder;

    SurfaceFragment(final ViewRequest request) {
        super();
        this.features = request.getFeatures();
        this.request = request;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final SurfaceView result = new SurfaceView(this.getActivity());
        result.setFocusable(true);
        result.setFocusableInTouchMode(true);
        result.setOnKeyListener(this);
        result.setOnTouchListener(this);
        this.surfaceHolder = result.getHolder();
        this.surfaceHolder.addCallback(this);
        return result;
    }

    @Override
    public boolean onKey(final View view, final int keyCode, final android.view.KeyEvent event) {
        final KeyEvent jedaEvent = EventMapper.mapEvent(event);
        if (jedaEvent != null) {
            this.postEvent(jedaEvent);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent event) {
        int index;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                index = event.getActionIndex();
                this.postEvent(new PointerEvent(EventMapper.mapDevice(event), EventType.POINTER_DOWN, event.getPointerId(index),
                                                (int) event.getX(index), (int) event.getY(index)));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                index = event.getActionIndex();
                this.postEvent(new PointerEvent(EventMapper.mapDevice(event), EventType.POINTER_UP, event.getPointerId(index),
                                                (int) event.getX(index), (int) event.getY(index)));
                break;
            case MotionEvent.ACTION_MOVE:
                for (index = 0; index < event.getPointerCount(); ++index) {
                    this.postEvent(new PointerEvent(EventMapper.mapDevice(event), EventType.POINTER_MOVED, event.getPointerId(index),
                                                    (int) event.getX(index), (int) event.getY(index)));
                }

                break;
        }

        return true;
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        this.surfaceAvailable = true;
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
        if (this.request != null) {
            this.request.setResult(new AndroidViewImp(this, width, height));
            this.request = null;
        }
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        this.surfaceAvailable = false;
    }

    EnumSet<ViewFeature> getFeatures() {
        return this.features;
    }

    int getOrientation(final int currentOrientation) {
        if (this.features.contains(ViewFeature.ORIENTATION_LANDSCAPE)) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else if (this.features.contains(ViewFeature.ORIENTATION_PORTRAIT)) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
            return currentOrientation;
        }
    }

    void postEvent(final Event event) {
        if (this.eventQueue != null) {
            this.eventQueue.addEvent(event);
        }
    }

    void setBitmap(final Bitmap bitmap) {
        if (this.surfaceAvailable) {
            final Canvas canvas = this.surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(bitmap, 0f, 0f, null);
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    void setEventQueue(final EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    void setFeature(final ViewFeature feature, final boolean enabled) {
        if (enabled) {
            this.features.add(feature);
        }
        else {
            this.features.remove(feature);
        }
    }

    void setTitle(final String title) {
        this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                getActivity().setTitle(title);
            }
        });
    }
}
