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

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import ch.jeda.Engine;
import ch.jeda.Size;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.Request;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import java.net.URL;

public class MainActivity extends Activity implements Platform {

    private static final int LOG_ACTIVITY = 1;
    private static final int SELECT_FROM_LIST_ACTIVITY = 2;
    private final Engine engine;
    private final StringBuilder log;
    private final ResourceFinder resourceFinder;
    private final ViewManager viewManager;
    private Request currentRequest;
    private boolean logVisible;

    public MainActivity() {
        this.engine = new Engine(this);
        this.log = new StringBuilder();
        this.resourceFinder = new ResourceFinder(this);
        this.viewManager = new ViewManager(this);
    }

    @Override
    public CanvasImp createCanvasImp(Size size) {
        AndroidCanvasImp result = new AndroidCanvasImp();
        result.setSize(size);
        return result;
    }

    @Override
    public Iterable<String> listClassNames() throws Exception {
        return this.resourceFinder.findClassNames();
    }

    @Override
    public ImageImp loadImageImp(URL url) throws Exception {
        return new AndroidImageImp(BitmapFactory.decodeStream(url.openStream()));
    }

    @Override
    public void log(String text) {
        this.log.append(text);
        this.log.append('\n');
        if (this.logVisible) {
            Intent intent = new Intent(LogActivity.class.getName());
            intent.putExtra(LogActivity.PARAM_LOG_CONTENT, text);
            MainActivity.this.sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewManager.onCreate();
        this.engine.start();
    }

    @Override
    public void showInputRequest(InputRequest inputRequest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showSelectionRequest(SelectionRequest selectionRequest) {
        if (this.currentRequest != null) {
            return;
        }

        this.currentRequest = selectionRequest;
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(SelectionActivity.TITLE, selectionRequest.getTitle());
        intent.putExtra(SelectionActivity.ITEMS, selectionRequest.getDisplayItems());
        this.startActivityForResult(intent, SELECT_FROM_LIST_ACTIVITY);
    }

    @Override
    public void showLog() {
        if (!this.logVisible) {
            this.logVisible = true;
            Intent intent = new Intent(MainActivity.this, LogActivity.class);
            intent.putExtra(LogActivity.PARAM_LOG_CONTENT, this.log.toString());
            MainActivity.this.startActivityForResult(intent, LOG_ACTIVITY);
        }
    }

    public ViewImp showView(ViewInfo viewInfo) {
        return this.viewManager.createViewImp(viewInfo);
    }

    @Override
    public void stop() {
        // ignore
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_FROM_LIST_ACTIVITY:
                this.onSelectedFromList(resultCode, data);
                return;
            case LOG_ACTIVITY:
                this.logVisible = false;
        }
    }

    private void onSelectedFromList(int resultCode, Intent data) {
        if (this.currentRequest instanceof SelectionRequest) {
            SelectionRequest selectionRequest = (SelectionRequest) this.currentRequest;
            // data or this.selectionRequest might be null (Occured during testing,
            // not sure why.
            if (resultCode == RESULT_OK && data != null) {
                selectionRequest.setSelectedIndex(data.getIntExtra(SelectionActivity.SELECTED_INDEX, -1));
            }
            else {
                selectionRequest.cancelRequest();
            }
        }
    }
}
