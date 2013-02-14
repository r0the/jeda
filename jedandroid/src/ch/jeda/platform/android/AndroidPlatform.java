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
import android.app.Application;
import android.graphics.BitmapFactory;
import ch.jeda.Engine;
import ch.jeda.Size;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import dalvik.system.DexFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class AndroidPlatform implements Platform {

    private final Activity activity;
    private final Engine engine;
    private final ViewManager viewManager;

    @Override
    public CanvasImp createCanvasImp(Size size) {
        final AndroidCanvasImp result = new AndroidCanvasImp();
        result.setSize(size);
        return result;
    }

    /**
     * Iterates through the entries of the application's DEX file to find all
     * classes.
     */
    @Override
    public Iterable<String> listClassNames() throws Exception {
        final List<String> result = new ArrayList();
        final Application app = this.activity.getApplication();
        final String apkName = app.getPackageManager().getApplicationInfo(
                app.getPackageName(), 0).sourceDir;
        final DexFile dx = new DexFile(apkName);
        final Enumeration<String> e = dx.entries();
        while (e.hasMoreElements()) {
            String resourceName = e.nextElement();
            if (!resourceName.contains("$")) {
                result.add(resourceName);
            }
        }

        return result;
    }

    @Override
    public ImageImp loadImageImp(InputStream in) throws Exception {
        return new AndroidImageImp(BitmapFactory.decodeStream(in));
    }

    @Override
    public void log(String text) {
        this.activity.runOnUiThread(new LogTask(this.viewManager, text));
    }

    @Override
    public void showInputRequest(InputRequest inputRequest) {
        this.activity.runOnUiThread(new ShowInputRequestTask(
                this.viewManager, inputRequest));
    }

    @Override
    public void showLog() {
        this.activity.runOnUiThread(new ShowLogTask(this.viewManager));
    }

    @Override
    public void showSelectionRequest(SelectionRequest selectionRequest) {
        this.activity.runOnUiThread(new ShowSelectionRequestTask(
                this.viewManager, selectionRequest));
    }

    @Override
    public void showWindow(WindowRequest windowRequest) {
        this.activity.runOnUiThread(new ShowWindowTask(
                this.viewManager, windowRequest));
    }

    @Override
    public void stop() {
        this.activity.runOnUiThread(new StopTask(this.viewManager));
    }

    AndroidPlatform(Activity activity) {
        this.activity = activity;
        this.engine = new Engine(this);
        this.viewManager = new ViewManager(activity);
    }

    void start() {
        this.engine.start();
    }

    private static class LogTask implements Runnable {

        private final ViewManager viewManager;
        private final String text;

        @Override
        public void run() {
            this.viewManager.log(this.text);
        }

        LogTask(ViewManager viewManager, String text) {
            this.viewManager = viewManager;
            this.text = text;
        }
    }

    private static class ShowInputRequestTask implements Runnable {

        private final ViewManager viewManager;
        private final InputRequest inputRequest;

        @Override
        public void run() {
            this.viewManager.showInputRequest(this.inputRequest);
        }

        ShowInputRequestTask(ViewManager viewManager, InputRequest inputRequest) {
            this.viewManager = viewManager;
            this.inputRequest = inputRequest;
        }
    }

    private static class ShowLogTask implements Runnable {

        private final ViewManager viewManager;

        @Override
        public void run() {
            this.viewManager.showLog();
        }

        ShowLogTask(ViewManager viewManager) {
            this.viewManager = viewManager;
        }
    }

    private static class ShowSelectionRequestTask implements Runnable {

        private final ViewManager viewManager;
        private final SelectionRequest selectionRequest;

        @Override
        public void run() {
            this.viewManager.showSelectionRequest(this.selectionRequest);
        }

        ShowSelectionRequestTask(ViewManager viewManager, SelectionRequest selectionRequest) {
            this.viewManager = viewManager;
            this.selectionRequest = selectionRequest;
        }
    }

    private static class ShowWindowTask implements Runnable {

        private final ViewManager viewManager;
        private final WindowRequest windowRequest;

        @Override
        public void run() {
            this.viewManager.showWindow(this.windowRequest);
        }

        ShowWindowTask(ViewManager viewManager, WindowRequest windowRequest) {
            this.viewManager = viewManager;
            this.windowRequest = windowRequest;
        }
    }

    private static class StopTask implements Runnable {

        private final ViewManager viewManager;

        @Override
        public void run() {
            this.viewManager.stop();
        }

        StopTask(ViewManager viewManager) {
            this.viewManager = viewManager;
        }
    }
}
