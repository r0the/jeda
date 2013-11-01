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
 * @deprecated Use {@link Jeda} instead.
 */
public class Device {

    /**
     * @deprecated Use {@link Jeda#disableSensor(ch.jeda.SensorType)} instead.
     */
    public static void disableSensor(final SensorType sensorType) {
        Jeda.disableSensor(sensorType);
    }

    /**
     * @deprecated Use {@link Jeda#enableSensor(ch.jeda.SensorType)} instead.
     */
    public static void enableSensor(final SensorType sensorType) {
        Jeda.enableSensor(sensorType);
    }

    /**
     * @deprecated Use {@link Jeda#isSensorAvailable(ch.jeda.SensorType)} instead.
     */
    public static boolean isSensorAvailable(final SensorType sensorType) {
        return Jeda.isSensorAvailable(sensorType);
    }

    /**
     * @deprecated Use {@link Jeda#isSensorEnabled(ch.jeda.SensorType)} instead.
     */
    public static boolean isSensorEnabled(final SensorType sensorType) {
        return Jeda.isSensorEnabled(sensorType);
    }
}
