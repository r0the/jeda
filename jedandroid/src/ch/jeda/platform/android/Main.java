/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
import android.app.Service;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ViewRequest;
import ch.jeda.ui.ViewFeature;
import java.io.InputStream;

public final class Main extends Activity {

    private static final int CONTENT_ID = 4242;
    private static Main INSTANCE;
    private final LogFragment logFragment;
    private final ResourceManager resourceManager;
    private final SensorManager sensorManager;
    private AndroidAudioManagerImp audioManager;
    private JedaView contentView;
    private boolean virtualKeyboardVisible;

    static Main getInstance() {
        return INSTANCE;
    }

    public Main() {
        INSTANCE = this;
        logFragment = new LogFragment();
        resourceManager = new ResourceManager(this);
        sensorManager = new SensorManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Jeda", "onBackPressed");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = new AndroidAudioManagerImp(this);
        contentView = new JedaView(this);
        contentView.setId(CONTENT_ID);
        setContentView(contentView, new ViewGroup.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT,
                       ViewGroup.LayoutParams.MATCH_PARENT));
        Log.d("Jeda", "onCreate");
        addManager(sensorManager, "SensorManager");
        Log.i("Jeda", "Starting Jeda " + Jeda.getProperties().getProperty("jeda.version"));
        if (savedInstanceState == null) {
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

    ImageImp createImageImp(final String path) {
        return resourceManager.openImage(path);
    }

    TypefaceImp createTypefaceImp(final String path) {
        return resourceManager.openTypeface(path);
    }

    AudioManagerImp getAudioManagerImp() {
        return audioManager;
    }

    boolean isSensorAvailable(final SensorType sensorType) {
        return sensorManager.isAvailable(sensorType);
    }

    boolean isSensorEnabled(final SensorType sensorType) {
        return sensorManager.isEnabled(sensorType);
    }

    boolean isVirtualKeyboardVisible() {
        return virtualKeyboardVisible;
    }

    Class<?>[] loadClasses() throws Exception {
        return resourceManager.loadClasses();
    }

    void log(final LogLevel logLevel, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doLog(logLevel, message);
            }
        });
    }

    InputStream openResource(final String path) {
        return resourceManager.openInputStream(path);
    }

    void postEvent(final Event event) {
        AndroidPlatform.getInstance().postEvent(event);
    }

    void setSensorEnabled(final SensorType sensorType, final boolean enabled) {
        sensorManager.setEnabled(sensorType, enabled);
    }

    void setVirtualKeyboardVisible(final boolean visible) {
        virtualKeyboardVisible = visible;

        if (visible) {
            getInputMethodManager().showSoftInput(contentView, InputMethodManager.SHOW_FORCED);
        }
        else {
            getInputMethodManager().hideSoftInputFromWindow(contentView.getWindowToken(), 0);
        }
    }

    void showInputRequest(final InputRequest inputRequest) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowInputRequest(inputRequest);
            }
        });
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowSelectionRequest(selectionRequest);
            }
        });
    }

    void showViewRequest(final ViewRequest request) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doShowViewRequest(request);
            }
        });
    }

    void shutdown() {
    }

    private void addManager(final Fragment fragment, final String tag) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
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
                logFragment.append(message);
                showFragment(logFragment);
                break;
            case INFO:
                System.out.println(message);
                logFragment.append(message);
                showFragment(logFragment);
                break;
        }
    }

    void doShowInputRequest(final InputRequest request) {
        setTitle(request.getTitle());
        InputDialogFragment dialog = new InputDialogFragment(request);
        dialog.show(getFragmentManager(), "InputDialog");
    }

    private void doShowSelectionRequest(final SelectionRequest request) {
        setTitle(request.getTitle());
        showFragment(new SelectionDialogFragment(request));
    }

    private void doShowViewRequest(final ViewRequest request) {
        final SurfaceFragment topView = new SurfaceFragment(request);
        contentView.setTopView(topView);
        final int orientation = getScreenOrientation(request);
        Log.i("Jeda", "Setting screen orientation to " + orientation);
        setRequestedOrientation(orientation);
        showFragment(topView);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
    }

    private void showFragment(final Fragment fragment) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(CONTENT_ID, fragment);
        ft.commit();
    }

    private int getScreenOrientation(final ViewRequest request) {
        if (request.getFeatures().contains(ViewFeature.ORIENTATION_LANDSCAPE)) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else if (request.getFeatures().contains(ViewFeature.ORIENTATION_PORTRAIT)) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
            final int rotation = getWindowManager().getDefaultDisplay().getRotation();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
