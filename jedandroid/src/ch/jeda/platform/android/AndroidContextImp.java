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
import android.view.WindowManager;
import ch.jeda.SensorType;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ContextImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.io.InputStream;

class AndroidContextImp implements ContextImp {

    private final Activity activity;
    private final SensorManager sensorManager;
    private final ViewManager viewManager;

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        final AndroidCanvasImp result = new AndroidCanvasImp();
        result.init(width, height);
        return result;
    }

    @Override
    public String defaultProgramName() {
        return null;
    }

    public boolean isSensorAvailable(final SensorType sensorType) {
        return this.sensorManager.isAvailable(sensorType);
    }

    public boolean isSensorEnabled(final SensorType sensorType) {
        return this.sensorManager.isEnabled(sensorType);
    }

    @Override
    public Class<?>[] loadClasses() throws Exception {
        return Resources.loadClasses(this.activity.getApplication());
    }

    public ImageImp loadImageImp(final String path) {
        return Resources.openImage(path);
    }

    public InputStream openResource(final String path) {
        return Resources.openInputStream(path);
    }

    public void setSensorEnabled(final SensorType sensorType, boolean enabled) {
        this.sensorManager.setEnabled(sensorType, enabled);
    }

    @Override
    public void showInputRequest(final InputRequest inputRequest) {
        this.activity.runOnUiThread(new ShowInputRequestTask(this.viewManager, inputRequest));
    }

    @Override
    public void showLog() {
        this.activity.runOnUiThread(new ShowLogTask(this.viewManager));
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.activity.runOnUiThread(new ShowSelectionRequestTask(this.viewManager, selectionRequest));
    }

    @Override
    public void showWindow(final WindowRequest windowRequest) {
        this.activity.runOnUiThread(new ShowWindowTask(this.viewManager, windowRequest));
    }

    @Override
    public void shutdown() {
        this.activity.runOnUiThread(new ShutdownTask(this.viewManager));
    }

    AndroidContextImp(Activity activity) {
        this.activity = activity;
        this.viewManager = new ViewManager(activity);
        this.sensorManager = new SensorManager(activity, this.viewManager);
        // Adjust window when soft keyboard is shown.
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    void closeView() {
        this.viewManager.closeView();
    }

    private static class ShowInputRequestTask implements Runnable {

        private final ViewManager viewManager;
        private final InputRequest inputRequest;

        @Override
        public void run() {
            this.viewManager.showInputRequest(this.inputRequest);
        }

        ShowInputRequestTask(final ViewManager viewManager, final InputRequest inputRequest) {
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

        ShowLogTask(final ViewManager viewManager) {
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

        ShowSelectionRequestTask(final ViewManager viewManager, final SelectionRequest selectionRequest) {
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

        ShowWindowTask(final ViewManager viewManager, final WindowRequest windowRequest) {
            this.viewManager = viewManager;
            this.windowRequest = windowRequest;
        }
    }

    private static class ShutdownTask implements Runnable {

        private final ViewManager viewManager;

        @Override
        public void run() {
            this.viewManager.shutdown();
        }

        ShutdownTask(final ViewManager viewManager) {
            this.viewManager = viewManager;
        }
    }
}
