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

import android.app.Activity;
import android.os.Bundle;
import ch.jeda.Engine;

public final class Main extends Activity {

    private AndroidContextImp contextImp;

    public Main() {
    }

    @Override
    public void onBackPressed() {
        this.contextImp.closeView();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contextImp = new AndroidContextImp(this);
        Engine.init(this.contextImp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Engine.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Engine.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Engine.resume();
    }
}
