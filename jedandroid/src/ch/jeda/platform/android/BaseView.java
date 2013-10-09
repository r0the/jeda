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
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Base class of all views used in the jeda activity. Base views are managed by the view manager.
 */
class BaseView extends RelativeLayout {

    private final ViewManager manager;
    private String title;

    BaseView(final ViewManager manager) {
        super(manager.getContext());
        this.manager = manager;
        this.setLayoutParams(createFillLayout());
    }

    int getOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    }

    final String getTitle() {
        return this.title;
    }

    final void setTitle(final String value) {
        this.title = value;
        this.manager.titleChanged(this);
    }

    protected final void show() {
        this.manager.show(this);
    }

    protected final void accept() {
        this.manager.closing(this);
        this.onAccept();
    }

    protected final void cancel() {
        this.manager.closing(this);
        this.onCancel();
    }

    protected void onAccept() {
    }

    protected void onCancel() {
    }

    protected static ViewGroup.LayoutParams createFillLayout() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }
}
