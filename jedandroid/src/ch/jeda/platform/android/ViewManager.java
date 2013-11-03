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
import ch.jeda.Log;
import ch.jeda.LogLevel;
import ch.jeda.event.Event;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.PlatformCallback;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.util.Stack;

class ViewManager {

    private final Activity activity;
    private final PlatformCallback callback;
    private final InputView inputView;
    private final LogView logView;
    private final Stack<BaseView> visibleViews;
    private boolean shutdown;

    ViewManager(final Activity activity, final PlatformCallback callback) {
        this.activity = activity;
        this.callback = callback;
        // Stack must be instantiated before any views
        this.visibleViews = new Stack();
        this.inputView = new InputView(this);
        this.logView = new LogView(this);
    }

    /**
     * Adds an event to be dispatched by the top view, if available.
     *
     * @param event
     */
    void addEvent(final Event event) {
        if (this.visibleViews.isEmpty()) {
            return;
        }

        final BaseView topView = this.visibleViews.peek();
        if (topView instanceof CanvasView) {
            ((CanvasView) topView).addEvent(event);
        }
    }

    /**
     * Closes a view. If it is the last closed view, signal the Jeda engine to stop.
     */
    void closeView() {
        if (!this.visibleViews.isEmpty()) {
            final BaseView view = this.visibleViews.pop();
            view.cancel();
            if (this.visibleViews.isEmpty()) {
                this.callback.stop();
            }
        }
    }

    void closing(final BaseView view) {
        Log.dbg("Closing view ", view);
        this.visibleViews.remove(view);
        if (this.visibleViews.empty()) {
            this.checkStop();
        }
        else {
            this.activateView(this.visibleViews.peek());
        }
    }

    Context getContext() {
        return this.activity;
    }

    int getRotation() {
        return this.activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    void log(final LogLevel logLevel, final String message) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logView.log(logLevel, message);
            }
        });
    }

    void show(final BaseView view) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShow(view);
            }
        });
    }

    void showInputRequest(final InputRequest inputRequest) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inputView.setInputRequest(inputRequest);
                doShow(inputView);
            }
        });
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SelectionView view = new SelectionView(ViewManager.this);
                view.setSelectionRequest(selectionRequest);
                doShow(view);
            }
        });
    }

    void showWindow(final WindowRequest request) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CanvasView displayView = new CanvasView(ViewManager.this, request);
                doShow(displayView);
            }
        });
    }

    void shutdown() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shutdown = true;
                checkStop();
            }
        });
    }

    void titleChanged(final BaseView view) {
        if (this.isTopView(view)) {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.dbg("Setting activity title to '" + view.getTitle() + "'.");
                    activity.setTitle(view.getTitle());
                }
            });
        }
    }

    private void activateView(final BaseView view) {
        this.activity.setContentView(view);
        this.activity.setRequestedOrientation(view.getOrientation(this.activity.getResources().getConfiguration().orientation));
        this.activity.setTitle(view.getTitle());
        Log.dbg("Activating view ", view, " with title ", view.getTitle());
        if (!view.requestFocus()) {
            Log.dbg("Request focus failed.");
        }
    }

    private void checkStop() {
        if (this.visibleViews.empty() && this.shutdown) {
            this.activity.finish();
            Log.dbg("Stopping Jeda activity.");
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
