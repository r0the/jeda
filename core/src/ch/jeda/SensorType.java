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
 *
 * @since 1
 */
public enum SensorType {

    /**
     * Acceleration sensor. An acceleration sensor measures the acceleration applied to the device, including the force
     * of gravity.
     *
     * @since 1
     */
    ACCELERATION,
    /**
     * Gravity sensor. The gravity sensor provides a three dimensional vector indicating the direction and magnitude of
     * gravity.
     */
    GRAVITY,
    /**
     * Linear acceleration sensor. The linear acceleration sensor provides you with a three-dimensional vector
     * representing acceleration along each device axis, excluding gravity.
     */
    LINEAR_ACCELERATION,
    /**
     * Magnetic field sensor. The magnetic field sensor measures the magnetic field.
     */
    MAGNETIC_FIELD
}
