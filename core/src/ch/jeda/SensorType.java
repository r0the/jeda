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

/**
 * Defines available sensor types.
 * <p>
 * <img src="../../../windows.png"> <img src="../../../linux.png"> Sensors are not supported.
 * <p>
 * <img src="../../../android.png"> Sensors may be available depending on the device.
 *
 * @since 1
 */
public enum SensorType {

    /**
     * Acceleration sensor. An acceleration sensor measures the acceleration applied to the device, including the force
     * of gravity.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    ACCELERATION,
    /**
     * Gravity sensor. The gravity sensor provides a three dimensional vector indicating the direction and magnitude of
     * gravity.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    GRAVITY,
    /**
     * Light sensor. The light sensor measures the ambient light level.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    LIGHT,
    /**
     * Linear acceleration sensor. The linear acceleration sensor provides you with a three-dimensional vector
     * representing acceleration along each device axis, excluding gravity.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    LINEAR_ACCELERATION,
    /**
     * Magnetic field sensor. The magnetic field sensor measures the magnetic field.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    MAGNETIC_FIELD,
    /**
     * Pressure sensor. The sensor measures the air pressure.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    PRESSURE,
    /**
     * Proximity sensor. The sensor measures the distance to the nearest object, usually the user.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    PROXIMITY,
    /**
     * Temperature sensor. The sensor measures the air temperature.
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The sensor is not available.
     * <p>
     * <img src="../../../android.png"> The sensor may be available depending on the device.
     *
     * @see ch.jeda.event.SensorEvent
     * @since 1
     */
    TEMPERATURE
}
