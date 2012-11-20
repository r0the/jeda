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

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;

class JedaSurfaceView extends SurfaceView {

    private final AndroidEventsImp eventsImp;

    JedaSurfaceView(Context context) {
        super(context);
        this.eventsImp = new AndroidEventsImp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //this.eventsImp.addKeyEvent(event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.eventsImp.addMotionEvent(event);
        return true;
    }

    AndroidEventsImp getEventsImp() {
        return this.eventsImp;
    }
}
