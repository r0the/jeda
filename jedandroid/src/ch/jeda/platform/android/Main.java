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
import android.util.Log;
import ch.jeda.Jeda;

public final class Main extends Activity {

    private static Main INSTANCE;

    static Main getInstance() {
        return INSTANCE;
    }

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Jeda", "onBackPressed");
        AndroidPlatform.getInstance().onBackPressed();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Jeda", "onCreate");
        Jeda.startProgram();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Jeda", "onPause");
        AndroidPlatform.getInstance().onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Jeda", "onRestart");
        Jeda.startProgram();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Jeda", "onResume");
        AndroidPlatform.getInstance().onResume();
    }
}
