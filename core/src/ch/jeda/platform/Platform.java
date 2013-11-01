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
package ch.jeda.platform;

import ch.jeda.SensorType;
import java.io.InputStream;

/**
 * <b>Internal</b>. Do not use this interface.
 */
public interface Platform {

    CanvasImp createCanvasImp(int width, int height);

    ImageImp createImageImp(String path);

    SoundImp createSoundImp(String path);

    boolean isSensorAvailable(SensorType sensorType);

    boolean isSensorEnabled(SensorType sensorType);

    Class<?>[] loadClasses() throws Exception;

    InputStream openResource(String path);

    void setSensorEnabled(SensorType sensorType, boolean enabled);

    void showInputRequest(InputRequest inputRequest);

    void showLog();

    void showSelectionRequest(SelectionRequest selectionRequest);

    void showWindow(WindowRequest windowRequest);

    void shutdown();
}
