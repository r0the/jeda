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
package ch.jeda.platform;

import ch.jeda.DisplayMetrics;
import ch.jeda.event.Event;
import ch.jeda.event.SensorType;
import java.io.InputStream;
import org.xml.sax.XMLReader;

/**
 * <b>Internal</b>. Do not use this interface.
 */
public interface Platform {

    enum StandardTypeface {

        MONOSPACED, SANS_SERIF, SERIF
    }

    CanvasImp createCanvasImp(int width, int height);

    TypefaceImp createTypefaceImp(String path);

    ImageImp createImageImp(String path);

    XMLReader createXmlReader();

    byte[] decodeBase64(final String base64);

    String encodeBase64(final byte[] data);

    AudioManagerImp getAudioManagerImp();

    DisplayMetrics getDisplayMetrics();

    TypefaceImp getStandardTypefaceImp(final StandardTypeface standardTypeface);

    boolean isSensorAvailable(SensorType sensorType);

    boolean isSensorEnabled(SensorType sensorType);

    boolean isVirtualKeyboardVisible();

    Class<?>[] loadClasses() throws Exception;

    void setSensorEnabled(SensorType sensorType, boolean enabled);

    void setVirtualKeyboardVisible(boolean visible);

    void showInputRequest(InputRequest inputRequest);

    void showSelectionRequest(SelectionRequest selectionRequest);

    void showViewRequest(ViewRequest viewRequest);

    void shutdown();

    void writeln(final String message);

    interface Callback {

        void pause();

        void postEvent(Event event);

        void resume();

        void stop();

    }
}
