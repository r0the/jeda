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
        inputWindow = new InputWindow(this);
        logWindow = new LogWindow(this);
        selectionWindow = new SelectionWindow(this);
        windows = new HashSet();

        windows.add(inputWindow);
        windows.add(logWindow);
        windows.add(selectionWindow);
    }

    public void writeln(final String message) {
        logWindow.writeln(message);
    }

    void showInputRequest(final InputRequest inputRequest) {
        inputWindow.setRequest(inputRequest);
        inputWindow.setVisible(true);
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        selectionWindow.setListInfo(selectionRequest);
        selectionWindow.setVisible(true);
    }

    void showViewRequest(final ViewRequest viewRequest) {
        int width = viewRequest.getWidth();
        int height = viewRequest.getHeight();
        final BaseWindow window = new BaseWindow(this);
        final boolean fullscreen = viewRequest.getFeatures().contains(ViewFeature.FULLSCREEN) && fullscreenWindow == null;
        if (fullscreen) {
            fullscreenWindow = window;
            width = GRAPHICS_DEVICE.getDisplayMode().getWidth();
            height = GRAPHICS_DEVICE.getDisplayMode().getHeight();
        }

        windows.add(window);
        viewRequest.setResult(new JavaViewImp(window, viewRequest.getCallback(), width, height,
                                              viewRequest.getFeatures()));
        if (fullscreen) {
            GRAPHICS_DEVICE.setFullScreenWindow(fullscreenWindow);
        }
    }

    void shutdown() {
        shutdown = true;
        checkWindowsClosed();
    }

    void windowClosed(final BaseWindow window) {
        if (window.equals(fullscreenWindow)) {
            fullscreenWindow = null;
            GRAPHICS_DEVICE.setFullScreenWindow(window);
        }

        checkWindowsClosed();
    }

    private void checkWindowsClosed() {
        // If no windows are visible, then we request Jeda to stop.
        for (BaseWindow w : windows) {
            if (w.isVisible()) {
                return;
            }
        }

        if (shutdown) {
            for (BaseWindow window : windows) {
                window.dispose();
            }

            System.exit(0);
        }
        else {
            callback.stop();
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
