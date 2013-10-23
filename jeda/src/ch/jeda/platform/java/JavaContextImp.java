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

import ch.jeda.SensorType;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ContextImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.io.InputStream;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class JavaContextImp implements ContextImp {

    private final String defaultProgramName;
    private final WindowManager windowManager;

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        return new JavaCanvasImp(width, height);
    }

    @Override
    public String defaultProgramName() {
        return this.defaultProgramName;
    }

    @Override
    public boolean isSensorAvailable(final SensorType sensorType) {
        return false;
    }

    @Override
    public boolean isSensorEnabled(final SensorType sensorType) {
        return false;
    }

    @Override
    public Class<?>[] loadClasses() throws Exception {
        return ResourceManager.loadClasses();
    }

    @Override
    public ImageImp loadImageImp(final String path) {
        return ResourceManager.openImage(path);
    }

    @Override
    public InputStream openResource(final String path) {
        return ResourceManager.openInputStream(path);
    }

    @Override
    public void setSensorEnabled(final SensorType sensorType, final boolean enabled) {
        // ignore
    }

    @Override
    public void showInputRequest(final InputRequest inputRequest) {
        this.windowManager.showInputRequest(inputRequest);
    }

    @Override
    public void showLog() {
        this.windowManager.showLog();
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.windowManager.showSelectionRequest(selectionRequest);
    }

    @Override
    public void showWindow(final WindowRequest windowRequest) {
        this.windowManager.showWindow(windowRequest);
    }

    @Override
    public void shutdown() {
        this.windowManager.shutdown();
    }

    JavaContextImp(final String[] args) {
        setLookAndFeel();
        if (args.length > 0) {
            this.defaultProgramName = args[0];
        }
        else {
            this.defaultProgramName = null;
        }

        this.windowManager = new WindowManager();
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
