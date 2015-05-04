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
package ch.jeda.platform.java;

import ch.jeda.LogLevel;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.ViewRequest;
import ch.jeda.ui.ViewFeature;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.HashSet;
import java.util.Set;

class WindowManager {

    private static final GraphicsDevice GRAPHICS_DEVICE =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final Platform.Callback callback;
    private final InputWindow inputWindow;
    private final LogWindow logWindow;
    private final SelectionWindow selectionWindow;
    private final Set<BaseWindow> windows;
    private BaseWindow fullscreenWindow;
    private boolean shutdown;

    WindowManager(final Platform.Callback callback) {
        this.callback = callback;
        this.inputWindow = new InputWindow(this);
        this.logWindow = new LogWindow(this);
        this.selectionWindow = new SelectionWindow(this);
        this.windows = new HashSet();

        this.windows.add(this.inputWindow);
        this.windows.add(this.logWindow);
        this.windows.add(this.selectionWindow);
    }

    public void log(final LogLevel logLevel, final String message) {
        this.logWindow.log(logLevel, message);
    }

    void showInputRequest(final InputRequest inputRequest) {
        this.inputWindow.setRequest(inputRequest);
        this.inputWindow.setVisible(true);
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.selectionWindow.setListInfo(selectionRequest);
        this.selectionWindow.setVisible(true);
    }

    void showViewRequest(final ViewRequest viewRequest) {
        int width = viewRequest.getWidth();
        int height = viewRequest.getHeight();
        final BaseWindow window = new BaseWindow(this);
        if (viewRequest.getFeatures().contains(ViewFeature.FULLSCREEN) && this.fullscreenWindow == null) {
            DisplayMode displayMode = findDisplayMode(width, height);
            width = displayMode.getWidth();
            height = displayMode.getHeight();
            this.fullscreenWindow = window;
            GRAPHICS_DEVICE.setFullScreenWindow(this.fullscreenWindow);
            GRAPHICS_DEVICE.setDisplayMode(displayMode);
        }

        this.windows.add(window);
        viewRequest.setResult(new JavaViewImp(window, width, height, viewRequest.getFeatures()));
    }

    void shutdown() {
        this.shutdown = true;
        this.checkWindowsClosed();
    }

    void windowClosed(final BaseWindow window) {
        if (window.equals(this.fullscreenWindow)) {
            this.fullscreenWindow = null;
            GRAPHICS_DEVICE.setFullScreenWindow(window);
        }

        this.checkWindowsClosed();
    }

    private void checkWindowsClosed() {
        // If no windows are visible, then we request Jeda to stop.
        for (BaseWindow w : this.windows) {
            if (w.isVisible()) {
                return;
            }
        }

        if (this.shutdown) {
            for (BaseWindow window : this.windows) {
                window.dispose();
            }

            System.exit(0);
        }
        else {
            this.callback.stop();
        }
    }

    private static DisplayMode findDisplayMode(final int width, final int height) {
        DisplayMode result = null;
        int fdx = Integer.MAX_VALUE;
        int fdy = Integer.MAX_VALUE;
        int fcolor = Integer.MIN_VALUE;
        for (DisplayMode candidate : GRAPHICS_DEVICE.getDisplayModes()) {
            final int dx = candidate.getWidth() - width;
            final int dy = candidate.getHeight() - height;
            if (dx >= 0 && dy >= 0 && (dx < fdx || dy < fdy || candidate.getBitDepth() > fcolor)) {
                result = candidate;
                fdx = dx;
                fdy = dy;
                fcolor = result.getBitDepth();
            }
        }

        if (result == null) {
            result = GRAPHICS_DEVICE.getDisplayMode();
        }

        return result;
    }
}
