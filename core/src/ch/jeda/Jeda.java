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

import static ch.jeda.IO.err;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.WindowImp;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.TickListener;
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

    private Jeda() {
    }

    /**
     * Adds a tick listener to the Jeda engine. The listener will receive {@link ch.jeda.event.EventType#TICK} events in
     * approximately the frequency set with {@link ch.jeda.Jeda#setFrameRate(float)}. Has no effect if <tt>listener</tt>
     * is <tt>null</tt>.
     *
     * @param listener the listener to add
     *
     * @see #removeTickListener(ch.jeda.ui.TickListener)
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
        ENGINE.getPlatform().setSensorEnabled(sensorType, false);
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
        ENGINE.getPlatform().setSensorEnabled(sensorType, true);
    }

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
        return ENGINE.getPlatform().isSensorAvailable(sensorType);
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
        return ENGINE.getPlatform().isSensorEnabled(sensorType);
    }

    public static String[] loadTextFile(final String filePath) {
        final InputStream in = ENGINE.getPlatform().openResource(filePath);
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
            err("jeda.file.error.read", filePath, ex);
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

    public static void removeTickListener(final TickListener listener) {
        ENGINE.removeTickListener(listener);
    }

    /**
     * Sets the target tick frequency in Hertz. This is the frequency in which windows will be refreshed and
     * {@link TickEvent} events will be emitted.
     *
     * @param hertz new frame rate in hertz
     *
     * @see #getFrameRate()
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

    static WindowImp createWindowImp(final int width, final int height, final EnumSet<WindowFeature> features) {
        if (features == null) {
            throw new NullPointerException("features");
        }

        final WindowRequest request = new WindowRequest(width, height, features);
        ENGINE.getPlatform().showWindow(request);
        request.waitForResult();
        return request.getResult();
    }

    static InputStream openResource(final String path) {
        return ENGINE.getPlatform().openResource(path);
    }

    static void showInputRequest(final InputRequest inputRequest) {
        ENGINE.getPlatform().showInputRequest(inputRequest);
    }
}
