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
package ch.jeda.platform.java;

import ch.jeda.Engine;
import ch.jeda.Size;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import ch.jeda.ui.Window.Feature;
import java.awt.DisplayMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WindowManager {

    private final Engine engine;
    private final InputWindow inputWindow;
    private final LogWindow logWindow;
    private final SelectionWindow selectionWindow;
    private final Set<JedaWindow> windows;
    private boolean finished;
    private ViewWindow fullscreenWindow;

    public WindowManager(Engine engine) {
        this.engine = engine;
        this.inputWindow = new InputWindow(this);
        this.logWindow = new LogWindow(this);
        this.selectionWindow = new SelectionWindow(this);
        this.windows = new HashSet();

        this.windows.add(this.inputWindow);
        this.windows.add(this.logWindow);
        this.windows.add(this.selectionWindow);
    }

    void finish() {
        this.finished = true;

        for (JedaWindow window : new ArrayList<JedaWindow>(this.windows)) {
            if (!window.isVisible()) {
                window.dispose();
                this.windows.remove(window);
            }
        }
    }

    ViewImp createViewImp(ViewInfo viewInfo) {
        ViewWindow window = this.createViewWindow(viewInfo);
        this.windows.add(window);
        window.setVisible(true);
        return JavaViewImp.create(window, viewInfo.hasFeature(Feature.DoubleBuffered));
    }

    private ViewWindow createViewWindow(ViewInfo viewInfo) {
        Size size = viewInfo.getSize();
        if (size == null) {
            size = Size.from(800, 600);
        }

        if (viewInfo.hasFeature(Feature.Fullscreen) && this.fullscreenWindow == null) {
            DisplayMode displayMode = GUI.findDisplayMode(size);
            size = Size.from(displayMode.getWidth(), displayMode.getHeight());
            this.fullscreenWindow = new ViewWindow(this, size, true);
            GUI.setFullscreenMode(this.fullscreenWindow, displayMode);
            return this.fullscreenWindow;
        }
        else {
            return new ViewWindow(this, size, false);
        }
    }

    void log(String text) {
        this.logWindow.log(text);
    }

    void showInputRequest(InputRequest inputRequest) {
        this.inputWindow.setRequest(inputRequest);
        this.inputWindow.setVisible(true);
    }

    void showLog() {
        this.logWindow.setVisible(true);
    }

    void showSelectionRequest(SelectionRequest selectionRequest) {
        this.selectionWindow.setListInfo(selectionRequest);
        this.selectionWindow.setVisible(true);
    }

    boolean isShuttingDown() {
        return this.finished;
    }

    void notifyDisposing(JedaWindow window) {
        if (window.equals(this.fullscreenWindow)) {
            this.fullscreenWindow = null;
            GUI.finishFullscreenMode();
        }
        this.windows.remove(window);
    }

    void notifyHidden(JedaWindow window) {
        if (window.equals(this.fullscreenWindow)) {
            this.fullscreenWindow = null;
            GUI.finishFullscreenMode();
        }

        // If no windows are visible, then we request Jeda to stop.
        for (JedaWindow w : this.windows) {
            if (w.isVisible()) {
                return;
            }
        }

        this.engine.requestStop();
    }
}
