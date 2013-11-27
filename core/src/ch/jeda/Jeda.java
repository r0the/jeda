/*
 * Copyright (C) 2013 by Stefan Rothe
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

import ch.jeda.event.TickListener;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.MusicImp;
import ch.jeda.platform.SoundImp;
import ch.jeda.platform.WindowImp;
import ch.jeda.ui.WindowFeature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents the Jeda engine. Provides methods to control the Jeda engine as well as the platform/device the program
 * ist running on.
 *
 * @since 1
 */
public class Jeda {

    private static final JedaEngine ENGINE = JedaEngine.create();

    /**
     * Adds a tick listener to the Jeda engine. The listener will receive {@link ch.jeda.event.EventType#TICK} events in
     * approximately the frequency set with {@link ch.jeda.Jeda#setTickFrequency(float)}. Has no effect if
     * <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the listener to add
     *
     * @see #removeTickListener(ch.jeda.event.TickListener)
     * @see #setTickFrequency(float)
     * @since 1
     */
    public static void addTickListener(final TickListener listener) {
        ENGINE.addTickListener(listener);
    }

    /**
     * Disables the sensor of the specified type on the device.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to disable
     *
     * @see #enableSensor(ch.jeda.SensorType)
     * @see #isSensorAvailable(ch.jeda.SensorType)
     * @see #isSensorEnabled(ch.jeda.SensorType)
     * @since 1
     */
    public static void disableSensor(final SensorType sensorType) {
        ENGINE.setSensorEnabled(sensorType, false);
    }

    /**
     * Enables the sensor of the specified type on the device.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to enable
     *
     * @see #disableSensor(ch.jeda.SensorType)
     * @see #isSensorAvailable(ch.jeda.SensorType)
     * @see #isSensorEnabled(ch.jeda.SensorType)
     * @since 1
     */
    public static void enableSensor(final SensorType sensorType) {
        ENGINE.setSensorEnabled(sensorType, true);
    }

    /**
     * Returns the name of the currently running Jeda program.
     *
     * @return the name of the currently running Jeda program
     *
     * @since 1
     */
    public static String getProgramName() {
        return ENGINE.getProgramName();
    }

    /**
     * Returns the Jeda system properties.
     *
     * @return the Jeda system properties
     *
     * @since 1
     */
    public static Properties getProperties() {
        return ENGINE.getProperties();
    }

    /**
     * Returns the target target tick frequency in Hertz [Hz].
     *
     * @return the target tick frequency in Hertz [Hz].
     *
     * @see #setTickFrequency(float)
     * @since 1
     */
    public static float getTickFrequency() {
        return ENGINE.getTickFrequency();
    }

    /**
     * Checks if a sensor of the specified type is available on the device.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to check for
     * @return <tt>true</tt> if the specified sensor type is available on the device, otherwise <tt>false</tt>
     *
     * @see #disableSensor(ch.jeda.SensorType)
     * @see #enableSensor(ch.jeda.SensorType)
     * @see #isSensorEnabled(ch.jeda.SensorType)
     * @since 1
     */
    public static boolean isSensorAvailable(final SensorType sensorType) {
        return ENGINE.isSensorAvailable(sensorType);
    }

    /**
     * Checks if the sensor of the specified type is currently enabled on the device.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> No sensors are supported.
     * <p>
     * <img src="../../../android.png"> All available sensor types are supported.
     *
     * @param sensorType the type of sensor to check for
     * @return <tt>true</tt> if the sensor of the specified type is currently enabled on the device, otherwise
     * <tt>false</tt>
     *
     * @see #disableSensor(ch.jeda.SensorType)
     * @see #enableSensor(ch.jeda.SensorType)
     * @see #isSensorAvailable(ch.jeda.SensorType)
     * @since 1
     */
    public static boolean isSensorEnabled(final SensorType sensorType) {
        return ENGINE.isSensorEnabled(sensorType);
    }

    /**
     * Loads a text file and returns the content as a list of Strings. Each line of the text file will be represented by
     * a <tt>String</tt> in the returned array. Returns <tt>null</tt> if the file is not present or cannot be read.
     *
     * To read a resource file, put 'res:' in front of the file path.
     *
     * @param path path to the file
     * @return lines of the file as an array of <tt>String</tt>
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
     * Removes a tick listener from the Jeda engine. The listener will no longer receive
     * {@link ch.jeda.event.EventType#TICK} events. Has no effect if <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the listener to remove
     *
     * @see #addTickListener(ch.jeda.event.TickListener)
     * @see #setTickFrequency(float)
     * @since 1
     */
    public static void removeTickListener(final TickListener listener) {
        ENGINE.removeTickListener(listener);
    }

    /**
     * Sets the target tick frequency in Hertz. This is the frequency in which windows will be refreshed and
     * {@link ch.jeda.event.EventType#TICK} events will be emitted.
     *
     * @param hertz new frame rate in hertz
     *
     * @see #getTickFrequency()
     * @since 1
     */
    public static void setTickFrequency(final float hertz) {
        ENGINE.setTickFrequency(hertz);
    }

    /**
     * Starts a Jeda program. This method is called automatically when a Jeda application is started.
     *
     * @since 1
     */
    public static void startProgram() {
        ENGINE.startProgram(null);
    }

    /**
     * Starts the specified Jeda program.
     *
     * @param programClassName class name of the Jeda program to start.
     * @since 1
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

    static MusicImp createMusicImp(final String path) {
        return ENGINE.createMusicImp(path);
    }

    static SoundImp createSoundImp(final String path) {
        return ENGINE.createSoundImp(path);
    }

    static WindowImp createWindowImp(final int width, final int height, final EnumSet<WindowFeature> features) {
        return ENGINE.createWindowImp(width, height, features);
    }

    static void log(final LogLevel logLevel, final String message) {
        ENGINE.log(logLevel, message);
    }

    static InputStream openResource(final String path) {
        return ENGINE.openResource(path);
    }

    static void showInputRequest(final InputRequest inputRequest) {
        ENGINE.showInputRequest(inputRequest);
    }

    private Jeda() {
    }
}
