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
package ch.jeda.platform.java;

import ch.jeda.platform.InputDeviceImp;
import ch.jeda.ui.Axis;
import ch.jeda.ui.Button;
import java.util.HashMap;
import java.util.Map;
import net.java.games.input.Component;

public class JavaGamepad implements InputDeviceImp {

    private static final Map<Component.Identifier, Axis> AXIS_MAP = initAxisMap();
    private static final Map<Component.Identifier, Button> BUTTON_MAP = initButtonMap();
    private final net.java.games.input.Controller imp;
    private Map<Axis, Component> axisMap;
    private Map<Button, Component> buttonMap;

    @Override
    public Iterable<Axis> getAxes() {
        return this.axisMap.keySet();
    }

    @Override
    public double getAxisValue(Axis axis) {
        return this.axisMap.get(axis).getPollData();
    }

    @Override
    public Iterable<Button> getButtons() {
        return this.buttonMap.keySet();
    }

    @Override
    public String getName() {
        return this.imp.getName();
    }

    @Override
    public boolean isButtonPressed(Button button) {
        return this.buttonMap.get(button).getPollData() == 1f;
    }

    @Override
    public void poll() {
        this.imp.poll();
    }

    JavaGamepad(net.java.games.input.Controller imp) {
        this.imp = imp;
        this.axisMap = new HashMap();
        this.buttonMap = new HashMap();

        for (Component component : this.imp.getComponents()) {
            if (AXIS_MAP.containsKey(component.getIdentifier())) {
                this.axisMap.put(AXIS_MAP.get(component.getIdentifier()), component);
            }
            else if (BUTTON_MAP.containsKey(component.getIdentifier())) {
                this.buttonMap.put(BUTTON_MAP.get(component.getIdentifier()), component);
            }
            else {
                System.out.println(component.getIdentifier());
            }
        }
    }

    private static Map<Component.Identifier, Axis> initAxisMap() {
        Map<Component.Identifier, Axis> result = new HashMap();
        result.put(Component.Identifier.Axis.X, Axis.LEFT_X);
        result.put(Component.Identifier.Axis.Y, Axis.LEFT_Y);
        result.put(Component.Identifier.Axis.Z, Axis.LEFT_Z);
        result.put(Component.Identifier.Axis.RX, Axis.RIGHT_X);
        result.put(Component.Identifier.Axis.RY, Axis.RIGHT_Y);
        result.put(Component.Identifier.Axis.RZ, Axis.RIGHT_Z);
        result.put(Component.Identifier.Axis.POV, Axis.POV);
        return result;
    }

    private static Map<Component.Identifier, Button> initButtonMap() {
        Map<Component.Identifier, Button> result = new HashMap();
        result.put(Component.Identifier.Button.A, Button.A);
        result.put(Component.Identifier.Button.B, Button.B);
        result.put(Component.Identifier.Button.LEFT_THUMB, Button.LEFT_INDEX);
        result.put(Component.Identifier.Button.LEFT_THUMB3, Button.LEFT_THUMB);
        result.put(Component.Identifier.Button.MODE, Button.MODE);
        result.put(Component.Identifier.Button.RIGHT_THUMB, Button.RIGHT_INDEX);
        result.put(Component.Identifier.Button.RIGHT_THUMB3, Button.RIGHT_THUMB);
        result.put(Component.Identifier.Button.SELECT, Button.SELECT);
        result.put(Component.Identifier.Button.START, Button.START);
        result.put(Component.Identifier.Button.X, Button.X);
        result.put(Component.Identifier.Button.Y, Button.Y);
        return result;
    }
}
