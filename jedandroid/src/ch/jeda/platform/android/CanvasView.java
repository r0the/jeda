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

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import ch.jeda.Size;
import ch.jeda.platform.Event;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.Key;
import ch.jeda.ui.Window;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CanvasView extends BaseView implements SurfaceHolder.Callback,
                                             View.OnKeyListener,
                                             View.OnTouchListener {

    private static final Map<Integer, Key> KEY_MAP = initKeyMap();
    private final EnumSet<Window.Feature> features;
    private List<Event> eventsIn;
    private List<Event> eventsOut;
    private WindowRequest request;
    private boolean surfaceAvailable;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private Size surfaceSize;

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        Key key = KEY_MAP.get(keyCode);
        if (key == null) {
            return false;
        }

        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                this.eventsIn.add(Event.createKeyPressed(key));
                break;
            case KeyEvent.ACTION_UP:
                this.eventsIn.add(Event.createKeyReleased(key));
                break;
        }

        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int index = 0;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                index = event.getActionIndex();
                this.eventsIn.add(Event.createPointerAvailable(
                        event.getPointerId(index),
                        (int) event.getX(index), (int) event.getY(index)));
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                index = event.getActionIndex();
                this.eventsIn.add(Event.createPointerUnavailable(
                        event.getPointerId(index)));
                break;

            case MotionEvent.ACTION_MOVE:
                for (index = 0; index < event.getPointerCount(); ++index) {
                    this.eventsIn.add(Event.createPointerMoved(
                            event.getPointerId(index),
                            (int) event.getX(index), (int) event.getY(index)));
                }

                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceAvailable = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.surfaceSize = new Size(width, height);
        if (this.request != null) {
            this.request.setResult(AndroidWindowImp.create(this));
            this.request = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.surfaceAvailable = false;
    }

    CanvasView(ViewManager manager, WindowRequest request) {
        super(manager);
        this.features = request.getFeatures();
        this.eventsIn = new ArrayList();
        this.eventsOut = new ArrayList();
        this.request = request;
        this.surfaceView = new SurfaceView(this.getContext());
        this.surfaceView.setFocusable(true);
        this.surfaceView.setFocusableInTouchMode(true);
        this.surfaceView.setOnKeyListener(this);
        this.surfaceView.setOnTouchListener(this);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);

        this.addView(this.surfaceView);
    }

    Iterable<Event> fetchEvents() {
        List<Event> temp = this.eventsIn;
        this.eventsIn = this.eventsOut;
        this.eventsOut = temp;
        this.eventsIn.clear();
        return this.eventsOut;
    }

    EnumSet<Window.Feature> getFeatures() {
        return this.features;
    }

    @Override
    int getOrientation() {
        if (this.features.contains(Window.Feature.OrientationLandscape)) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else if (this.features.contains(Window.Feature.OrientationPortrait)) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
            return ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
        }
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

    void setFeature(Window.Feature feature, boolean enabled) {
        if (enabled) {
            this.features.add(feature);
        }
        else {
            this.features.remove(feature);
        }
    }

    private static Map<Integer, Key> initKeyMap() {
        Map<Integer, Key> result = new HashMap();
        result.put(KeyEvent.KEYCODE_A, Key.A);
        result.put(KeyEvent.KEYCODE_ALT_LEFT, Key.ALT_LEFT);
        result.put(KeyEvent.KEYCODE_ALT_RIGHT, Key.ALT_RIGHT);
        result.put(KeyEvent.KEYCODE_APOSTROPHE, Key.APOSTROPHE);
        result.put(KeyEvent.KEYCODE_B, Key.B);
        result.put(KeyEvent.KEYCODE_DEL, Key.BACKSPACE);
        result.put(KeyEvent.KEYCODE_C, Key.C);
        result.put(KeyEvent.KEYCODE_D, Key.D);
        result.put(KeyEvent.KEYCODE_0, Key.DIGIT_0);
        result.put(KeyEvent.KEYCODE_1, Key.DIGIT_1);
        result.put(KeyEvent.KEYCODE_2, Key.DIGIT_2);
        result.put(KeyEvent.KEYCODE_3, Key.DIGIT_3);
        result.put(KeyEvent.KEYCODE_4, Key.DIGIT_4);
        result.put(KeyEvent.KEYCODE_5, Key.DIGIT_5);
        result.put(KeyEvent.KEYCODE_6, Key.DIGIT_6);
        result.put(KeyEvent.KEYCODE_7, Key.DIGIT_7);
        result.put(KeyEvent.KEYCODE_8, Key.DIGIT_8);
        result.put(KeyEvent.KEYCODE_9, Key.DIGIT_9);
        result.put(KeyEvent.KEYCODE_DPAD_DOWN, Key.DOWN);
        result.put(KeyEvent.KEYCODE_E, Key.E);
        result.put(KeyEvent.KEYCODE_ENTER, Key.ENTER);
        result.put(KeyEvent.KEYCODE_EQUALS, Key.EQUALS);
        result.put(KeyEvent.KEYCODE_F, Key.F);
        result.put(KeyEvent.KEYCODE_G, Key.G);
        result.put(KeyEvent.KEYCODE_GRAVE, Key.GRAVE);
        result.put(KeyEvent.KEYCODE_H, Key.H);
        result.put(KeyEvent.KEYCODE_I, Key.I);
        result.put(KeyEvent.KEYCODE_J, Key.J);
        result.put(KeyEvent.KEYCODE_K, Key.K);
        result.put(KeyEvent.KEYCODE_L, Key.L);
        result.put(KeyEvent.KEYCODE_DPAD_LEFT, Key.LEFT);
        result.put(KeyEvent.KEYCODE_M, Key.M);
        result.put(KeyEvent.KEYCODE_MENU, Key.MENU);
        result.put(KeyEvent.KEYCODE_MINUS, Key.MINUS);
        result.put(KeyEvent.KEYCODE_N, Key.N);
        result.put(KeyEvent.KEYCODE_O, Key.O);
        result.put(KeyEvent.KEYCODE_P, Key.P);
        result.put(KeyEvent.KEYCODE_PAGE_DOWN, Key.PAGE_DOWN);
        result.put(KeyEvent.KEYCODE_PAGE_UP, Key.PAGE_UP);
        result.put(KeyEvent.KEYCODE_Q, Key.Q);
        result.put(KeyEvent.KEYCODE_R, Key.R);
        result.put(KeyEvent.KEYCODE_DPAD_RIGHT, Key.RIGHT);
        result.put(KeyEvent.KEYCODE_S, Key.S);
        result.put(KeyEvent.KEYCODE_SHIFT_LEFT, Key.SHIFT_LEFT);
        result.put(KeyEvent.KEYCODE_SHIFT_RIGHT, Key.SHIFT_RIGHT);
        result.put(KeyEvent.KEYCODE_SPACE, Key.SPACE);
        result.put(KeyEvent.KEYCODE_T, Key.T);
        result.put(KeyEvent.KEYCODE_TAB, Key.TAB);
        result.put(KeyEvent.KEYCODE_U, Key.U);
        result.put(KeyEvent.KEYCODE_DPAD_UP, Key.UP);
        result.put(KeyEvent.KEYCODE_V, Key.V);
        result.put(KeyEvent.KEYCODE_VOLUME_DOWN, Key.VOLUME_DOWN);
        result.put(KeyEvent.KEYCODE_VOLUME_UP, Key.VOLUME_UP);
        result.put(KeyEvent.KEYCODE_W, Key.W);
        result.put(KeyEvent.KEYCODE_X, Key.X);
        result.put(KeyEvent.KEYCODE_Y, Key.Y);
        result.put(KeyEvent.KEYCODE_Z, Key.Z);

// Not supported
//        result.put(KeyEvent.KEYCODE_, Key.END);
//        result.put(KeyEvent.KEYCODE_, Key.HOME);
//        result.put(KeyEvent.KEYCODE_, Key.PAUSE);
//        result.put(KeyEvent.KEYCODE_, Key.PRINT_SCREEN);
//        result.put(KeyEvent.KEYCODE_, Key.INSERT);
//        result.put(KeyEvent.KEYCODE_, Key.DELETE);


// Only since API level 11
//        result.put(KeyEvent.KEYCODE_ALT_GRAPH, Key.ALT_GRAPH);
//        result.put(KeyEvent.KEYCODE_CAPS_LOCK, Key.CAPS_LOCK);
//        result.put(KeyEvent.KEYCODE_CTRL_LEFT, Key.CONTROL_LEFT);
//        result.put(KeyEvent.KEYCODE_CTRL_RIGHT, Key.CONTROL_RIGHT);
//        result.put(KeyEvent.KEYCODE_ESCAPE, Key.ESCAPE);
//        result.put(KeyEvent.KEYCODE_F1, Key.F1);
//        result.put(KeyEvent.KEYCODE_F2, Key.F2);
//        result.put(KeyEvent.KEYCODE_F3, Key.F3);
//        result.put(KeyEvent.KEYCODE_F4, Key.F4);
//        result.put(KeyEvent.KEYCODE_F5, Key.F5);
//        result.put(KeyEvent.KEYCODE_F6, Key.F6);
//        result.put(KeyEvent.KEYCODE_F7, Key.F7);
//        result.put(KeyEvent.KEYCODE_F8, Key.F8);
//        result.put(KeyEvent.KEYCODE_F9, Key.F9);
//        result.put(KeyEvent.KEYCODE_F10, Key.F10);
//        result.put(KeyEvent.KEYCODE_F11, Key.F11);
//        result.put(KeyEvent.KEYCODE_F12, Key.F12);
//        result.put(KeyEvent.KEYCODE_NUM_LOCK, Key.NUM_LOCK);
//        result.put(KeyEvent.KEYCODE_NUMPAD_0, Key.NUMPAD_0);
//        result.put(KeyEvent.KEYCODE_NUMPAD_1, Key.NUMPAD_1);
//        result.put(KeyEvent.KEYCODE_NUMPAD_2, Key.NUMPAD_2);
//        result.put(KeyEvent.KEYCODE_NUMPAD_3, Key.NUMPAD_3);
//        result.put(KeyEvent.KEYCODE_NUMPAD_4, Key.NUMPAD_4);
//        result.put(KeyEvent.KEYCODE_NUMPAD_5, Key.NUMPAD_5);
//        result.put(KeyEvent.KEYCODE_NUMPAD_6, Key.NUMPAD_6);
//        result.put(KeyEvent.KEYCODE_NUMPAD_7, Key.NUMPAD_7);
//        result.put(KeyEvent.KEYCODE_NUMPAD_8, Key.NUMPAD_8);
//        result.put(KeyEvent.KEYCODE_NUMPAD_9, Key.NUMPAD_9);
//        result.put(KeyEvent.KEYCODE_NUMPAD_ADD, Key.NUMPAD_ADD);
//        result.put(KeyEvent.KEYCODE_NUMPAD_DIVIDE, Key.NUMPAD_DIVIDE);
//        result.put(KeyEvent.KEYCODE_NUMPAD_DOT, Key.NUMPAD_DOT);
//        result.put(KeyEvent.KEYCODE_NUMPAD_ENTER, Key.NUMPAD_ENTER);
//        result.put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, Key.NUMPAD_MULTIPLY);
//        result.put(KeyEvent.KEYCODE_NUMPAD_SUBTRACT, Key.NUMPAD_SUBTRACT);
//        result.put(KeyEvent.KEYCODE_SCROLL_LOCK, Key.SCROLL_LOCK);
        return result;
    }
}
