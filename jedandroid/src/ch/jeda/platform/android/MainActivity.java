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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ch.jeda.Engine;
import ch.jeda.Log;
import ch.jeda.Size;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.ListInfo;
import ch.jeda.platform.LogInfo;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import java.net.URL;
import java.util.Stack;

public class MainActivity extends Activity implements SurfaceHolder.Callback,
                                                      Platform {

    private static final int SELECT_FROM_LIST = 1;
    private final Engine engine;
    private final ResourceFinder resourceFinder;
    private final Object surfaceLock;
    private final Stack<ViewImp> viewStack;
    private boolean surfaceAvailable;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private ListInfo listInfo;

    public MainActivity() {
        this.engine = new Engine(this);
        this.resourceFinder = new ResourceFinder(this);
        this.surfaceLock = new Object();
        this.viewStack = new Stack();
    }

    public CanvasImp createCanvasImp(Size size) {
        return new AndroidCanvasImp(size);
    }

    public Iterable<String> listClassNames() throws Exception {
        return this.resourceFinder.findClassNames();
    }

    public Iterable<String> listPropertyFiles() throws Exception {
        return this.resourceFinder.findPropertyFiles();
    }

    public ImageImp loadImageImp(URL url) throws Exception {
        return new AndroidImageImp(BitmapFactory.decodeStream(url.openStream()));
    }

    public void showInputRequest(InputRequest inputRequest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> void showList(ListInfo<T> listInfo) {
        this.listInfo = listInfo;
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.TITLE, listInfo.getTitle());
        intent.putExtra(ListActivity.ITEMS, listInfo.getDisplayItems());
        this.startActivityForResult(intent, SELECT_FROM_LIST);
    }

    public void showLog(LogInfo logInfo) {
        Intent intent = new Intent(this, LogActivity.class);
        intent.putExtra(LogActivity.PARAM_BUTTON_TEXT, logInfo.getButtonText());
        intent.putExtra(LogActivity.PARAM_LOG_CONTENT, logInfo.getLog());
        intent.putExtra(LogActivity.PARAM_TITLE, logInfo.getTitle());
        this.startActivity(intent);
    }

    public ViewImp showView(ViewInfo viewInfo) {
        synchronized (this.surfaceLock) {
            while (!this.surfaceAvailable) {
                Log.debug("Waiting for surface");
                try {
                    this.surfaceLock.wait();
                }
                catch (InterruptedException ex) {
                }
            }

            Log.debug("Surface received");
            if (viewInfo.isDoubleBuffered()) {
                return new DoubleBufferedViewImp(this, this.surfaceHolder);
            }
            else {
                return new AndroidViewImp(this, this.surfaceHolder);
            }
        }
    }

    @Override
    public void stop() {
        // ignore
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.surfaceView = new SurfaceView(this);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.setContentView(this.surfaceView);
        this.surfaceHolder.addCallback(this);
        this.engine.start();
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
            this.listInfo.done(data.getIntExtra(ListActivity.SELECTED_INDEX, -1));
        }
        else {
            this.listInfo.done(-1);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this.surfaceLock) {
            Log.debug("Surface create");
            this.surfaceAvailable = true;
            this.surfaceLock.notify();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //this.viewImp.resize(width, height);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this.surfaceLock) {
            Log.debug("Surface destroyed");
            this.surfaceAvailable = false;
            this.surfaceLock.notify();
        }
    }
}
