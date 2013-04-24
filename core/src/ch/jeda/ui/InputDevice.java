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
package ch.jeda.ui;

import ch.jeda.platform.InputDeviceImp;

/**
 * <b>Experimental.</b> Represents an input device such as a gamepad or a mouse.
 * An input device has two kinds of sensors: axes and buttons.
 *
 * Supported devices are:
 * <ul>
 * <li>Xbox or compatible controller (Java platform)</li>
 * </ul>
 */
public class InputDevice {

    private final InputDeviceImp imp;

    /**
     * Returns all available axes of this input device.
     *
     * @return available axes
     */
    public Iterable<Axis> getAxes() {
        return this.imp.getAxes();
    }

    public double getAxisValue(final Axis axis) {
        if (axis == null) {
            throw new NullPointerException("axis");
        }

        return this.imp.getAxisValue(axis);
    }

    public Iterable<Button> getButtons() {
        return this.imp.getButtons();
    }

    public boolean isButtonPressed(final Button button) {
        if (button == null) {
            throw new NullPointerException("button");
        }

        return this.imp.isButtonPressed(button);
    }

    @Override
    public String toString() {
        return this.imp.getName();
    }

    InputDevice(final InputDeviceImp imp) {
        this.imp = imp;
    }

    void update() {
        this.imp.poll();
    }
}
