/*
 * Copyright (C) 2012 - 2014 by Stefan Rothe
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

import android.view.WindowManager;
import ch.jeda.LogLevel;
import ch.jeda.event.SensorType;
import ch.jeda.platform.AudioManagerImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.PlatformCallback;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.SoundImp;
import ch.jeda.platform.WindowRequest;
import java.io.InputStream;

class AndroidPlatform implements Platform {

    private static AndroidPlatform INSTANCE;
    private final PlatformCallback callback;

    static AndroidPlatform getInstance() {
        return INSTANCE;
    }

    public AndroidPlatform(final PlatformCallback callback) {
        INSTANCE = this;
        this.callback = callback;
        final Main activity = Main.getInstance();
        // Adjust window when soft keyboard is shown.
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        final AndroidCanvasImp result = new AndroidCanvasImp();
        result.init(width, height);
        return result;
    }

    @Override
    public ImageImp createImageImp(final String path) {
        return Main.getInstance().createImageImp(path);
    }

    @Override
    public SoundImp createSoundImp(final String path) {
        return Main.getInstance().createSoundImp(path);
    }

    @Override
    public AudioManagerImp getAudioManagerImp() {
        return Main.getInstance().getAudioManagerImp();
    }

    @Override
    public boolean isSensorAvailable(final SensorType sensorType) {
        return Main.getInstance().isSensorAvailable(sensorType);
    }

    @Override
    public boolean isSensorEnabled(final SensorType sensorType) {
        return Main.getInstance().isSensorEnabled(sensorType);
    }

    @Override
    public Class<?>[] loadClasses() throws Exception {
        return Main.getInstance().loadClasses();
    }

    @Override
    public void log(final LogLevel logLevel, String message) {
        Main.getInstance().log(logLevel, message);
    }

    @Override
    public InputStream openResource(final String path) {
        return Main.getInstance().openResource(path);
    }

    @Override
    public void setSensorEnabled(final SensorType sensorType, boolean enabled) {
        Main.getInstance().setSensorEnabled(sensorType, enabled);
    }

    @Override
    public void showInputRequest(final InputRequest inputRequest) {
        Main.getInstance().showInputRequest(inputRequest);
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        Main.getInstance().showSelectionRequest(selectionRequest);
    }

    @Override
    public void showWindow(final WindowRequest windowRequest) {
        Main.getInstance().showWindow(windowRequest);
    }

    @Override
    public void shutdown() {
        Main.getInstance().shutdown();
    }

    void onPause() {
        this.callback.pause();
    }

    void onResume() {
        this.callback.resume();
    }
}
