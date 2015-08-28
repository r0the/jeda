/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.RelativeLayout;

class JedaView extends RelativeLayout {

    private JedaInputConnection inputConnection;
    private SurfaceFragment topView;

    JedaView(final Context context) {
        super(context);
        setFocusableInTouchMode(true);
    }

    @Override
    public InputConnection onCreateInputConnection(final EditorInfo outAttrs) {
        if (inputConnection == null) {
            inputConnection = new JedaInputConnection(this, true);
            if (topView != null) {
                inputConnection.setTopView(topView);
            }
        }

        return inputConnection;
    }

    public void setTopView(final SurfaceFragment topView) {
        this.topView = topView;
        if (inputConnection != null) {
            inputConnection.setTopView(topView);
        }
    }
}
