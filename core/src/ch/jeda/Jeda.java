/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda;

import ch.jeda.event.Event;
import ch.jeda.event.SensorType;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ViewCallback;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.ViewFeature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.xml.sax.XMLReader;

/**
 * Represents the Jeda engine. Provides methods to control the Jeda engine as well as the platform/device the program
 * ist running on.
 *
 * @since 1.0
 * @version 2
 */
public final class Jeda {

    private static final JedaEngine ENGINE = JedaEngine.create();

    /**
     * Adds an event listener to the Jeda engine. The listener will receive the following events:
     * <ul>
     * <li>{@link ch.jeda.event.EventType#ACTION} events from {@link ch.jeda.ui.Widget}s
     * <li>{@link ch.jeda.event.EventType#TICK} events from the Jeda engine
     * <li>{@link ch.jeda.event.EventType#SENSOR} events from the system
     * </ul>
     * Has no effect if <code>listener</code> is <code>null</code>.
     *
     * @param listener the listener to add
     *
     * @see #postEvent(ch.jeda.event.Event)
     * @see #removeEventListener(java.lang.Object)
     * @since 1.4
     */
    public static void addEventListener(final Object listener) {
        ENGINE.addEventListener(listener);
    }

    /**
     * Disables the sensor of the specified type on the device.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to disable
     *
     * @see #enableSensor(ch.jeda.event.SensorType)
     * @see #isSensorAvailable(ch.jeda.event.SensorType)
     * @see #isSensorEnabled(ch.jeda.event.SensorType)
     * @since 1.0
     */
    public static void disableSensor(final SensorType sensorType) {
        ENGINE.setSensorEnabled(sensorType, false);
    }

    /**
     * Enables the sensor of the specified type on the device.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to enable
     *
     * @see #disableSensor(ch.jeda.event.SensorType)
     * @see #isSensorAvailable(ch.jeda.event.SensorType)
     * @see #isSensorEnabled(ch.jeda.event.SensorType)
     * @since 1.0
     */
    public static void enableSensor(final SensorType sensorType) {
        ENGINE.setSensorEnabled(sensorType, true);
    }

    /**
     * Returns the name of the currently running Jeda program.
     *
     * @return the name of the currently running Jeda program
     *
     * @since 1.0
     */
    public static String getProgramName() {
        return ENGINE.getProgramName();
    }

    /**
     * Returns the Jeda system properties.
     *
     * @return the Jeda system properties
     *
     * @since 1.0
     */
    public static Properties getProperties() {
        return ENGINE.getProperties();
    }

    /**
     * Returns the target target tick frequency in Hertz [Hz].
     *
     * @return the target tick frequency in Hertz [Hz].
     *
     * @see #setTickFrequency(double)
     * @since 1.0
     */
    public static double getTickFrequency() {
        return ENGINE.getTickFrequency();
    }

    /**
     * Checks if a sensor of the specified type is available on the device.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to check for
     * @return <code>true</code> if the specified sensor type is available on the device, otherwise <code>false</code>
     *
     * @see #disableSensor(ch.jeda.event.SensorType)
     * @see #enableSensor(ch.jeda.event.SensorType)
     * @see #isSensorEnabled(ch.jeda.event.SensorType)
     * @since 1.0
     */
    public static boolean isSensorAvailable(final SensorType sensorType) {
        return ENGINE.isSensorAvailable(sensorType);
    }

    /**
     * Checks if the sensor of the specified type is currently enabled on the device.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to check for
     * @return <code>true</code> if the sensor of the specified type is currently enabled on the device, otherwise
     * <code>false</code>
     *
     * @see #disableSensor(ch.jeda.event.SensorType)
     * @see #enableSensor(ch.jeda.event.SensorType)
     * @see #isSensorAvailable(ch.jeda.event.SensorType)
     * @since 1.0
     */
    public static boolean isSensorEnabled(final SensorType sensorType) {
        return ENGINE.isSensorEnabled(sensorType);
    }

    /**
     * Checks if the virtual keyboard is currently visible.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> Virtual keyboard is not supported.
     * <p>
     * <img src="../../android.png"> Virtual keyboard is supported.
     *
     * @return <code>true</code> if the virtual keyboard is visible, otherwise <code>false</code>
     *
     * @see #setVirtualKeyboardVisible(boolean)
     * @since 1.3
     */
    public static boolean isVirtualKeyboardVisible() {
        return ENGINE.isVirtualKeyboardVisible();
    }

    /**
     * Loads a text file and returns the content as a list of Strings. Each line of the text file will be represented by
     * a <code>String</code> in the returned array. Returns <code>null</code> if the file is not present or cannot be
     * read.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param path path to the file
     * @return lines of the file as an array of <code>String</code>
     * @since 1.0
     */
    public static String[] loadTextFile(final String path) {
        final InputStream in = ENGINE.openResource(path);
        if (in == null) {
            return null;
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            final List<String> result = new ArrayList<String>();
            while (reader.ready()) {
                result.add(reader.readLine());
            }

            return result.toArray(new String[result.size()]);
        }
        catch (final IOException ex) {
            Log.err("jeda.file.error.read", path, ex);
            return null;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                // ignore
            }
        }
    }

    /**
     * Removes an event listener from the Jeda engine. The listener will no longer receive events. Has no effect if
     * <code>listener</code> is <code>null</code>.
     *
     * @param listener the listener to remove
     *
     * @see #addEventListener(java.lang.Object)
     * @since 1.4
     */
    public static void removeEventListener(final Object listener) {
        ENGINE.removeEventListener(listener);
    }

    /**
     * Sets the target tick frequency in Hertz. This is the frequency in which windows will be refreshed and
     * {@link ch.jeda.event.EventType#TICK} events will be emitted.
     *
     * @param hertz new frame rate in hertz
     *
     * @see #getTickFrequency()
     * @since 1.0
     */
    public static void setTickFrequency(final double hertz) {
        ENGINE.setTickFrequency(hertz);
    }

    /**
     * Shows or hides the virtual keyboard.
     * <p>
     * <img src="../../windows.png"> <img src="../../linux.png"> Virtual keyboard is not supported.
     * <p>
     * <img src="../../android.png"> Virtual keyboard is supported.
     *
     * @param visible indicates if the virtual keyboard should be visible
     *
     * @see #isVirtualKeyboardVisible()
     * @since 1.3
     */
    public static void setVirtualKeyboardVisible(final boolean visible) {
        ENGINE.setVirtualKeyboardVisible(visible);
    }

    /**
     * Starts a Jeda program. This method is called automatically when a Jeda application is started.
     *
     * @since 1.0
     */
    public static void startProgram() {
        ENGINE.startProgram(null);
    }

    /**
     * Starts the specified Jeda program.
     *
     * @param programClassName class name of the Jeda program to start.
     * @since 1.0
     */
    public static void startProgram(final String programClassName) {
        if (programClassName == null) {
            throw new NullPointerException("programClassName");
        }

        ENGINE.startProgram(programClassName);
    }

    static CanvasImp createCanvasImp(final int width, final int height) {
        return ENGINE.createCanvasImp(width, height);
    }

    static ImageImp createImageImp(final String path) {
        return ENGINE.createImageImp(path);
    }

    static TypefaceImp createTypefaceImp(final String path) {
        return ENGINE.createTypefaceImp(path);
    }

    static ViewImp createViewImp(final ViewCallback callback, final int width, final int height,
                                 final EnumSet<ViewFeature> features) {
        return ENGINE.createViewImp(callback, width, height, features);
    }

    static XMLReader createXmlReader() {
        return ENGINE.createXmlReader();
    }

    static AudioManager getAudioManager() {
        return ENGINE.getAudioManager();
    }

    static TypefaceImp getStandardTypefaceImp(final Platform.StandardTypeface standardTypeface) {
        return ENGINE.getStandardTypefaceImp(standardTypeface);
    }

    static void log(final LogLevel logLevel, final String message) {
        ENGINE.log(logLevel, message);
    }

    static InputStream openResource(final String path) {
        return ENGINE.openResource(path);
    }

    static void postEvent(final Event event) {
        ENGINE.postEvent(event);
    }

    static void showInputRequest(final InputRequest inputRequest) {
        ENGINE.showInputRequest(inputRequest);
    }

    private Jeda() {
    }
}
