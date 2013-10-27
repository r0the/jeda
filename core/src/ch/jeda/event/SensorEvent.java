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
package ch.jeda.event;

import ch.jeda.SensorType;

/**
 * Represents an event of the type {@link EventType#SENSOR}.
 *
 * @since 1
 */
public class SensorEvent extends Event {

    private SensorType sensorType;
    private float x;
    private float y;
    private float z;

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
    public SensorEvent(final Object source, final SensorType sensorType, float x, final float y, final float z) {
        super(source, EventType.SENSOR);
        this.sensorType = sensorType;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the type of sensor that generated the event.
     *
     * @return the type of sensor that generated the event
     *
     * @since 1
     */
    public SensorType getSensorType() {
        return this.sensorType;
    }

    /**
     * Returns the x-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link SensorType#ACCELERATION}: The acceleration in meter per square second in direction of the positive
     * x-axis of the {@link ch.jeda.Window}, i.e. to the right.
     * <li>For {@link SensorType#GRAVITY}: The gravity component in meter per square second in direction of the positive
     * x-axis of the {@link ch.jeda.Window}, i.e. to the right.
     * <li>For {@link SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second in direction of the
     * positive x-axis of the {@link ch.jeda.Window}, i.e. to the right.
     * <li>For {@link SensorType#MAGNETIC_FIELD}: The magnetic field component in Microtesla in direction of the
     * positive x-axis of the {@link ch.jeda.Window}, i.e. to the right.
     * </ul>
     *
     * @return the x-axis component of the currently sensed value
     *
     * @since 1
     */
    public float getX() {
        return this.x;
    }

    /**
     * Returns the y-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link SensorType#ACCELERATION}: The acceleration in meter per square second in direction of the positive
     * y-axis of the {@link ch.jeda.Window}, i.e. downwards on the screen.
     * <li>For {@link SensorType#GRAVITY}: The gravity component in meter per square second in direction of the positive
     * y-axis of the {@link ch.jeda.Window}, i.e. downwards on the screen.
     * <li>For {@link SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second in direction of the
     * positive y-axis of the {@link ch.jeda.Window}, i.e. downwards on the screen.
     * <li>For {@link SensorType#MAGNETIC_FIELD}: The magnetic field component in Microtesla in direction of the
     * positive y-axis of the {@link ch.jeda.Window}, i.e. downwards on the screen.
     * </ul>
     *
     * @return the y-axis component of the currently sensed value
     *
     * @since 1
     */
    public float getY() {
        return this.y;
    }

    /**
     * Returns the z-axis component of the currently sensed value. Depending on the sensor type, these are:
     * <ul>
     * <li>For {@link SensorType#ACCELERATION}: The acceleration in meter per square second upwards from the screen.
     * <li>For {@link SensorType#GRAVITY}: The gravity component in meter per square second upwards from the screen.
     * <li>For {@link SensorType#LINEAR_ACCELERATION}: The acceleration in meter per square second upwards from the
     * screen.
     * <li>For {@link SensorType#MAGNETIC_FIELD}: The magnetic field component in Microtesla upwards from the screen.
     * </ul>
     *
     * @return the z-axis component of the currently sensed value
     *
     * @since 1
     */
    public float getZ() {
        return this.z;
    }
}
