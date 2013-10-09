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
import android.content.Context;
import ch.jeda.Engine;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.util.Stack;

class ViewManager {

    private final Activity activity;
    private final InputView inputView;
    private final LogView logView;
    private final SelectionView selectionView;
    private final Stack<BaseView> visibleViews;
    private boolean shutdown;

    ViewManager(final Activity activity) {
        this.activity = activity;
        // Stack must be instantiated before any views
        this.visibleViews = new Stack();
        this.inputView = new InputView(this);
        this.logView = new LogView(this);
        this.selectionView = new SelectionView(this);
    }

    /**
     * Closes a view. If it is the last closed view, signal the Jeda engine to stop.
     */
    void closeView() {
        if (!this.visibleViews.isEmpty()) {
            final BaseView view = this.visibleViews.pop();
            view.cancel();
            if (this.visibleViews.isEmpty()) {
                Engine.stop();
            }
        }
    }

    Context getContext() {
        return this.activity;
    }

    void closing(final BaseView view) {
        this.visibleViews.remove(view);
        if (this.visibleViews.empty()) {
            this.checkStop();
        }
        else {
            this.activateView(this.visibleViews.peek());
        }
    }

    void show(final BaseView view) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                doShow(view);
            }
        });
    }

    void showInputRequest(final InputRequest inputRequest) {
        this.inputView.setInputRequest(inputRequest);
        this.doShow(this.inputView);
    }

    void showLog() {
        this.doShow(this.logView);
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.selectionView.setSelectionRequest(selectionRequest);
        this.doShow(this.selectionView);
    }

    void showWindow(final WindowRequest request) {
        CanvasView displayView = new CanvasView(this, request);
        this.doShow(displayView);
    }

    void shutdown() {
        this.shutdown = true;
        this.checkStop();
    }

    void titleChanged(final BaseView view) {
        if (this.isTopView(view)) {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setTitle(view.getTitle());
                }
            });
        }
    }

    private void activateView(final BaseView view) {
        this.activity.setContentView(view);
        this.activity.setRequestedOrientation(view.getOrientation());
        this.activity.setTitle(view.getTitle());
        view.requestFocus();
    }

    private void checkStop() {
        if (this.visibleViews.empty() && this.shutdown) {
            this.activity.finish();
        }
    }

    private boolean isTopView(final BaseView view) {
        return !this.visibleViews.isEmpty() && view == this.visibleViews.peek();
    }

    private void doShow(final BaseView view) {
        if (!this.isTopView(view)) {
            this.visibleViews.push(view);
            this.activateView(view);
        }
    }
}
