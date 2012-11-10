/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.ListInfo;
import ch.jeda.platform.LogInfo;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * This class is the main entry point for Jeda applications on the Java
 * platform. Be sure to specify this class as main class.
 * 
 * @since 1.0
 */
public class Main implements Platform {

    private final Engine engine;
    private final ResourceFinder resourceFinder;
    private final WindowManager windowManager;

    /**
     * Entry point for Jeda framework.
     * 
     * @since 1.0
     */
    public static void main(String[] args) {
        Main main = new Main(args);
        main.start();
    }

    private Main(String[] args) {
        GUI.setLookAndFeel();
        this.engine = new Engine(this);
        this.resourceFinder = new ResourceFinder();
        this.windowManager = new WindowManager(this.engine);
    }

    @Override
    public CanvasImp createCanvasImp(Size size) {
        return new JavaCanvasImp(size);
    }

    @Override
    public void finish() {
        this.windowManager.finish();
    }

    @Override
    public Iterable<String> listClassNames() throws Exception {
        return this.resourceFinder.findClassNames();
    }

    @Override
    public Iterable<String> listPropertyFiles() throws Exception {
        return this.resourceFinder.findPropertyFiles();
    }

    @Override
    public ImageImp loadImageImp(URL url) throws Exception {
        return new JavaImageImp(ImageIO.read(url));
    }

    @Override
    public <T> void showList(ListInfo<T> listInfo) {
        ListWindow window = this.windowManager.getListWindow();
        window.setListInfo(listInfo);
        window.setVisible(true);
    }

    @Override
    public void showLog(LogInfo logInfo) {
        LogWindow window = this.windowManager.getLogWindow();
        window.setLogInfo(logInfo);
        window.setVisible(true);
    }

    @Override
    public ViewImp showView(ViewInfo viewInfo) {
        return this.windowManager.createViewImp(viewInfo);
    }

    private void start() {
        this.engine.start();
    }
}
