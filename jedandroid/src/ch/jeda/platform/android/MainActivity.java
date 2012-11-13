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
import ch.jeda.platform.LogInfo;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import java.net.URL;

public class MainActivity extends Activity implements Platform {

    private static final int SELECT_FROM_LIST = 1;
    private final Engine engine;
    private final ResourceFinder resourceFinder;
    private final ViewManager viewManager;
    private SelectionRequest selectionRequest;

    public MainActivity() {
        this.engine = new Engine(this);
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
    public Iterable<String> listPropertyFiles() throws Exception {
        return this.resourceFinder.findPropertyFiles();
    }

    @Override
    public ImageImp loadImageImp(URL url) throws Exception {
        return new AndroidImageImp(BitmapFactory.decodeStream(url.openStream()));
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
        this.selectionRequest = selectionRequest;
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(SelectionActivity.TITLE, selectionRequest.getTitle());
        intent.putExtra(SelectionActivity.ITEMS, selectionRequest.getDisplayItems());
        this.startActivityForResult(intent, SELECT_FROM_LIST);
    }

    @Override
    public void showLog(LogInfo logInfo) {
        Intent intent = new Intent(this, LogActivity.class);
        intent.putExtra(LogActivity.PARAM_BUTTON_TEXT, logInfo.getButtonText());
        intent.putExtra(LogActivity.PARAM_LOG_CONTENT, logInfo.getLog());
        intent.putExtra(LogActivity.PARAM_TITLE, logInfo.getTitle());
        this.startActivity(intent);
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
            case SELECT_FROM_LIST:
                this.onSelectedFromList(resultCode, data);
                return;
        }
    }

    private void onSelectedFromList(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.selectionRequest.setSelectedIndex(data.getIntExtra(SelectionActivity.SELECTED_INDEX, -1));
        }
        else {
            this.selectionRequest.cancelRequest();
        }
    }
}
