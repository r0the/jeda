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
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import ch.jeda.ui.Window.Feature;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WindowManager {

    private final Engine engine;
    private final InputWindow inputWindow;
    private final WindowListener listener;
    private final ListWindow listWindow;
    private final LogWindow logWindow;
    private final Set<JedaWindow> windows;
    private boolean finished;

    public WindowManager(Engine engine) {
        this.engine = engine;
        this.inputWindow = new InputWindow();
        this.listener = new WindowListener(this);
        this.listWindow = new ListWindow();
        this.logWindow = new LogWindow();
        this.windows = new HashSet();

        this.registerWindow(this.inputWindow);
        this.registerWindow(this.listWindow);
        this.registerWindow(this.logWindow);
    }

    void finish() {
        this.finished = true;

        for (JedaWindow window : new ArrayList<JedaWindow>(this.windows)) {
            if (!window.isVisible()) {
                window.removeWindowListener(this.listener);
                window.dispose();
                this.windows.remove(window);
            }
        }
    }

    ViewImp createViewImp(ViewInfo viewInfo) {
        ViewWindow window = new ViewWindow(viewInfo.getSize(), false);
        this.registerWindow(window);
        if (viewInfo.hasFeature(Feature.DoubleBuffered)) {
            return new DoubleBufferedViewImp(window);
        }
        else {
            return new DefaultViewImp(window);
        }
    }

    InputWindow getInputWindow() {
        return this.inputWindow;
    }

    ListWindow getListWindow() {
        return this.listWindow;
    }

    LogWindow getLogWindow() {
        return this.logWindow;
    }

    private void registerWindow(JedaWindow window) {
        window.addWindowListener(this.listener);
        this.windows.add(window);
    }

    private void windowClosing(Window window) {
        JedaWindow jw = (JedaWindow) window;
        if (this.finished) {
            jw.removeWindowListener(this.listener);
            jw.dispose();
            this.windows.remove(jw);
        }
        else {
            jw.onHide();
        }

        this.checkStopRequest();
    }

    private void checkStopRequest() {
        for (JedaWindow window : this.windows) {
            if (window.isVisible()) {
                return;
            }
        }

        this.engine.requestStop();
    }

    private static class WindowListener extends WindowAdapter {

        protected WindowManager windowManager;

        public WindowListener(WindowManager windowManager) {
            this.windowManager = windowManager;
        }

        @Override
        public void windowClosing(WindowEvent event) {
            this.windowManager.windowClosing(event.getWindow());
        }
    }
}
