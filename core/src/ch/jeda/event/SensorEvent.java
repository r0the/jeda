/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
package ch.jeda.event;

/**
 * Represents an event of the type {@link ch.jeda.event.EventType#SENSOR}. All sensor values are given in SI base units.
 * <p>
 * <img src="../../../windows.png"> <img src="../../../linux.png"> Sensors are not supported.
 * <p>
 * <img src="../../../android.png"> Sensors may be available depending on the device.
 *
 * @since 1
 */
public class SensorEvent extends Event {

    private final boolean maximum;
    private final SensorType sensorType;
    private final double value;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Constructs a sensor event.
     *
     * @param source the event source that generates the event
     * @param sensorType the type of sensor that generated the event
     * @param maximum is the reported value at the sensor's maximum range
     * @param value the currently sensed value
     *
     * @since 1
     */
    public SensorEvent(final Object source, final SensorType sensorType, final boolean maximum, final double value) {
        this(source, sensorType, maximum, value, 0f, 0f, 0f);
    }

    /**
     * Constructs a sensor event.
     *
     * @param source the event source that generates the event
     * @param sensorType the type of sensor that generated the event
     * @param x the x-axis component of the currently sensed value
     * @param y the y-axis component of the currently sensed value
     * @param z the z-axis component of the currently sensed value
     *
     * @since 1
     */
    public SensorEvent(final Object source, final SensorType sensorType,
                       final double x, final double y, final double z) {
        this(source, sensorType, false, Math.sqrt(x * x + y * y + z * z), x, y, z);
    }

    /**
     * Returns the type of sensor that generated the event.
     *
     * @return the type of sensor that generated the event
     *
     * @since 1
     */
    public final SensorType getSensorType() {
        return this.sensorType;
    }

    /**
     * Returns the currently sensed value. Depending on the sensor type, this is:
     * <ul>
     * <li>For {@link ch.jeda.event.SensorType#ACCELERATION}: The absolute value of the acceleration in meter per square
     * second [m/s<sup>2</sup>].
     * <li>For {@link ch.jeda.event.SensorType#GRAVITY}: The absolute value of the gravity in meter per square second.
     * <li>For {@link ch.jeda.event.SensorType#LINEAR_ACCELERATION}: The absolute value of the acceleration in meter per
     * square second [m/s<sup>2</sup>].
     * <li>For {@link ch.jeda.event.SensorType#MAGNETIC_FIELD}: The absolute value of the magnetic field in Tesla [T].
     * <li>For {@link ch.jeda.event.SensorType#LIGHT}: The abmient light level in lux [lx].
     * <li>For {@link ch.jeda.event.SensorType#PRESSURE}: The air pressure in Pascal [Pa].
     * <li>For {@link ch.jeda.event.SensorType#PROXIMITY}: The proximity distance in meter [m].
     * <li>For {@link ch.jeda.event.SensorType#TEMPERATURE}: The air temperature in Kelvin [K].
     * </ul>
     *
     * @return the currently sensed value
     */
    public final double getValue() {
        return this.value;
    }

    /**
     * Returns the x-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link ch.jeda.event.SensorType#ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive x-axis of the {@link ch.jeda.ui.Window}, i.e. to the right.
     * <li>For {@link ch.jeda.event.SensorType#GRAVITY}: The gravity component in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive x-axis of the {@link ch.jeda.ui.Window}, i.e. to the right.
     * <li>For {@link ch.jeda.event.SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive x-axis of the {@link ch.jeda.ui.Window}, i.e. to the right.
     * <li>For {@link ch.jeda.event.SensorType#MAGNETIC_FIELD}: The magnetic field component in Tesla [T] in direction
     * of the positive x-axis of the {@link ch.jeda.ui.Window}, i.e. to the right.
     * </ul>
     * For other sensor types, the method always returns zero.
     *
     * @return the x-axis component of the currently sensed value
     *
     * @since 1
     */
    public final double getX() {
        return this.x;
    }

    /**
     * Returns the y-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link ch.jeda.event.SensorType#ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive y-axis of the {@link ch.jeda.ui.Window}, i.e. downwards on the
     * screen.
     * <li>For {@link ch.jeda.event.SensorType#GRAVITY}: The gravity component in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive y-axis of the {@link ch.jeda.ui.Window}, i.e. downwards on the
     * screen.
     * <li>For {@link ch.jeda.event.SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] in direction of the positive y-axis of the {@link ch.jeda.ui.Window}, i.e. downwards on the
     * screen.
     * <li>For {@link ch.jeda.event.SensorType#MAGNETIC_FIELD}: The magnetic field component in Tesla [T] in direction
     * of the positive y-axis of the {@link ch.jeda.ui.Window}, i.e. downwards on the screen.
     * </ul>
     * For other sensor types, the method always returns zero.
     *
     * @return the y-axis component of the currently sensed value
     *
     * @since 1
     */
    public final double getY() {
        return this.y;
    }

    /**
     * Returns the z-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link ch.jeda.event.SensorType#ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] upwards from the screen.
     * <li>For {@link ch.jeda.event.SensorType#GRAVITY}: The gravity component in meter per square second
     * [m/s<sup>2</sup>] upwards from the screen.
     * <li>For {@link ch.jeda.event.SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second
     * [m/s<sup>2</sup>] upwards from the screen.
     * <li>For {@link ch.jeda.event.SensorType#MAGNETIC_FIELD}: The magnetic field component in Tesla [T] upwards from
     * the screen.
     * </ul>
     * For other sensor types, the method always returns zero.
     *
     * @return the z-axis component of the currently sensed value
     *
     * @since 1
     */
    public final double getZ() {
        return this.z;
    }

    /**
     * Returns <tt>true</tt> if the reported value is the maximum range of the sensor. This means that the actual value
     * might be bigger, but the sensor is not capable of detecting it.
     *
     * @return <tt>true</tt> if the reported value is the maximum range of the sensor, otherwise <tt>false</tt>
     *
     * @since 1
     */
    public final boolean isMaximum() {
        return this.maximum;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("SensorEvent(type=");
        result.append(this.getType());
        result.append(", maxiumum=");
        result.append(this.maximum);
        result.append(", sensorType=");
        result.append(this.sensorType);
        result.append(", x=");
        result.append(this.x);
        result.append(", y=");
        result.append(this.y);
        result.append(", z=");
        result.append(this.z);
        result.append(")");
        return result.toString();
    }

    private SensorEvent(final Object source, final SensorType sensorType, final boolean maxiumum, final double value,
                        final double x, final double y, final double z) {
        super(source, EventType.SENSOR);
        this.maximum = maxiumum;
        this.sensorType = sensorType;
        this.value = value;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
