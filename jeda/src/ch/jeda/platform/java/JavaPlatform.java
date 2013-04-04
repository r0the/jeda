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
package ch.jeda.platform.java;

import ch.jeda.Engine;
import ch.jeda.Size;
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class JavaPlatform implements Platform {

    private final Engine engine;
    private final ResourceFinder resourceFinder;
    private final WindowManager windowManager;

    @Override
    public CanvasImp createCanvasImp(Size size) {
        return new JavaCanvasImp(size);
    }

    @Override
    public Transformation createTransformation() {
        return new JavaTransformation();
    }

    @Override
    public Iterable<String> listClassNames() throws Exception {
        return this.resourceFinder.findClassNames();
    }

    @Override
    public ImageImp loadImageImp(InputStream in) throws Exception {
        return new JavaImageImp(ImageIO.read(in));
    }

    @Override
    public void log(String text) {
        this.windowManager.log(text);
        System.out.print(text);
    }

    @Override
    public void showInputRequest(InputRequest inputRequest) {
        this.windowManager.showInputRequest(inputRequest);
    }

    @Override
    public void showLog() {
        this.windowManager.showLog();
    }

    @Override
    public void showSelectionRequest(SelectionRequest selectionRequest) {
        this.windowManager.showSelectionRequest(selectionRequest);
    }

    @Override
    public void showWindow(WindowRequest windowRequest) {
        this.windowManager.showWindow(windowRequest);
    }

    @Override
    public void stop() {
        this.windowManager.stop();
    }

    JavaPlatform(String[] args) {
        setLookAndFeel();
        this.engine = new Engine(this);
        this.resourceFinder = new ResourceFinder();
        this.windowManager = new WindowManager(this.engine);
        loadNativeLibrary("jinput-dx8.dll");
        loadNativeLibrary("jinput-dx8_64.dll");
        loadNativeLibrary("jinput-raw_64.dll");
        loadNativeLibrary("jinput-raw.dll");
        loadNativeLibrary("jinput-wintab.dll");
        loadNativeLibrary("libjinput-linux.so");
        loadNativeLibrary("libjinput-linux64.so");
        loadNativeLibrary("libjinput-osx.jnilib");
    }

    void start() {
        this.engine.start();
    }

    private static void loadNativeLibrary(String libraryName) {
        final InputStream inputStream = JavaPlatform.class.getClassLoader().getResourceAsStream(libraryName);
        try {
            final String tempDir = System.getProperty("java.io.tmpdir");
            System.setProperty("net.java.games.input.librarypath", tempDir);
            final File libraryFile = new File(tempDir, libraryName);
            libraryFile.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(libraryFile);
            final byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void setLookAndFeel() {
        try {
            final String defaultLaf = System.getProperty("swing.defaultlaf");
            if (defaultLaf != null) {
                UIManager.setLookAndFeel(defaultLaf);
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (InstantiationException ex) {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
