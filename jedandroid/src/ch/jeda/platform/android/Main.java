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

import android.app.Service;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import ch.jeda.Jeda;
import ch.jeda.LogLevel;
import ch.jeda.event.Event;
import ch.jeda.event.SensorType;
import ch.jeda.platform.AudioManagerImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.SoundImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.WindowFeature;
import java.io.InputStream;

public final class Main extends FragmentActivity {

    private static final int CONTENT_ID = 4242;
    private static Main INSTANCE;
    private final AudioManager audioManager;
    private final LogFragment logFragment;
    private final ResourceManager resourceManager;
    private final SensorManager sensorManager;
    private JedaView contentView;
    private CanvasFragment topWindow;
    private boolean virtualKeyboardVisible;

    static Main getInstance() {
        return INSTANCE;
    }

    public Main() {
        INSTANCE = this;
        this.audioManager = new AudioManager();
        this.logFragment = new LogFragment();
        this.resourceManager = new ResourceManager(this);
        this.sensorManager = new SensorManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Jeda", "onBackPressed");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView = new JedaView(this);
        this.contentView.setId(CONTENT_ID);
        setContentView(this.contentView, new ViewGroup.LayoutParams(
                       ViewGroup.LayoutParams.FILL_PARENT,
                       ViewGroup.LayoutParams.FILL_PARENT));
        Log.d("Jeda", "onCreate");
        if (savedInstanceState == null) {
            this.addManager(this.audioManager, "AudioManager");
            this.addManager(this.sensorManager, "SensorManager");
            Jeda.startProgram();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Jeda", "onPause");
        AndroidPlatform.getInstance().onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Jeda", "onRestart");
        Jeda.startProgram();
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Jeda", "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Jeda", "onResume");
        AndroidPlatform.getInstance().onResume();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Jeda", "onSaveInstanceState");
    }

    /**
     * Adds an event to be dispatched by the top view, if available.
     *
     * @param event
     */
    void addEvent(final Event event) {
        if (this.topWindow != null) {
            this.topWindow.addEvent(event);
        }
    }

    ImageImp createImageImp(final String path) {
        return this.resourceManager.openImage(path);
    }

    SoundImp createSoundImp(final String path) {
        return this.audioManager.createSoundImp(path);
    }

    TypefaceImp createTypefaceImp(final String path) {
        return this.resourceManager.openTypeface(path);
    }

    AudioManagerImp getAudioManagerImp() {
        return this.audioManager;
    }

    boolean isSensorAvailable(final SensorType sensorType) {
        return this.sensorManager.isAvailable(sensorType);
    }

    boolean isSensorEnabled(final SensorType sensorType) {
        return this.sensorManager.isEnabled(sensorType);
    }

    boolean isVirtualKeyboardVisible() {
        return this.virtualKeyboardVisible;
    }

    Class<?>[] loadClasses() throws Exception {
        return this.resourceManager.loadClasses();
    }

    void log(final LogLevel logLevel, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doLog(logLevel, message);
            }
        });
    }

    InputStream openResource(final String path) {
        return this.resourceManager.openInputStream(path);
    }

    void setSensorEnabled(final SensorType sensorType, final boolean enabled) {
        this.sensorManager.setEnabled(sensorType, enabled);
    }

    void setVirtualKeyboardVisible(final boolean visible) {
        this.virtualKeyboardVisible = visible;

        if (visible) {
            this.getInputMethodManager().showSoftInput(this.contentView, InputMethodManager.SHOW_FORCED);
        }
        else {
            this.getInputMethodManager().hideSoftInputFromWindow(this.contentView.getWindowToken(), 0);
        }
    }

    void showInputRequest(final InputRequest inputRequest) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowInputRequest(inputRequest);
            }
        });
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowSelectionRequest(selectionRequest);
            }
        });
    }

    void showWindow(final WindowRequest request) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowWindow(request);
            }
        });
    }

    void shutdown() {
    }

    private void addManager(final Fragment fragment, final String tag) {
        final FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(fragment, tag);
        ft.commit();
    }

    private void doLog(final LogLevel logLevel, final String message) {
        switch (logLevel) {
            case DEBUG:
                Log.d("Jeda", message);
                break;
            case ERROR:
                Log.e("Jeda", message);
                this.logFragment.append(message);
                this.showFragment(this.logFragment);
                break;
            case INFO:
                System.out.println(message);
                this.logFragment.append(message);
                this.showFragment(this.logFragment);
                break;
        }
    }

    void doShowInputRequest(final InputRequest request) {
        this.setTitle(request.getTitle());
        InputDialogFragment dialog = new InputDialogFragment(request);
        dialog.show(this.getSupportFragmentManager(), "InputDialog");
    }

    private void doShowSelectionRequest(final SelectionRequest request) {
        this.setTitle(request.getTitle());
        this.showFragment(new SelectionDialogFragment(request));
    }

    private void doShowWindow(final WindowRequest request) {
        this.topWindow = new CanvasFragment(request);
        final int orientation = this.getScreenOrientation(request);
        Log.d("Jeda", "Setting screen orientation to " + orientation);
        this.setRequestedOrientation(orientation);
        this.showFragment(this.topWindow);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
    }

    private void showFragment(final Fragment fragment) {
        final FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(CONTENT_ID, fragment);
        ft.commit();
    }

    private int getScreenOrientation(final WindowRequest request) {
        if (request.getFeatures().contains(WindowFeature.ORIENTATION_LANDSCAPE)) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else if (request.getFeatures().contains(WindowFeature.ORIENTATION_PORTRAIT)) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
            final int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
            }
            else {
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                }
            }
        }
    }
}
