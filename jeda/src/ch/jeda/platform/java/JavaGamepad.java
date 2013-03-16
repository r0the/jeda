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
import net.java.games.input.Component.Identifier;

public class JavaGamepad implements InputDeviceImp {

    private static final Map<Component.Identifier, Axis> DEFAULT_AXIS_MAP = initDefaultAxisMap();
    private static final Map<String, Profile> PROFILE_MAP = initProfileMap();
    private final Map<Axis, Component> axisMap;
    private final Map<Button, Component> buttonMap;
    private final net.java.games.input.Controller imp;

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
        Profile profile = PROFILE_MAP.get(imp.getName());

        for (Component component : this.imp.getComponents()) {
            if (profile.hasAxis(component.getIdentifier())) {
                this.axisMap.put(profile.getAxis(component.getIdentifier()), component);
            }
            else if (profile.hasButton(component.getIdentifier())) {
                this.buttonMap.put(profile.getButton(component.getIdentifier()), component);
            }
            else {
                System.out.println(component.getIdentifier());
            }
        }
    }

    private static class Profile {

        private final Map<Component.Identifier, Axis> axisMap;
        private final Map<Component.Identifier, Button> buttonMap;

        public Profile(Map<Identifier, Axis> axisMap, Map<Identifier, Button> buttonMap) {
            this.axisMap = axisMap;
            this.buttonMap = buttonMap;
        }

        Axis getAxis(Component.Identifier identifier) {
            return axisMap.get(identifier);
        }

        Button getButton(Component.Identifier identifier) {
            return buttonMap.get(identifier);
        }

        boolean hasAxis(Component.Identifier identifier) {
            return axisMap.containsKey(identifier);
        }

        boolean hasButton(Component.Identifier identifier) {
            return buttonMap.containsKey(identifier);
        }
    }

    private static Map<Component.Identifier, Axis> initDefaultAxisMap() {
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

    private static Map<String, Profile> initProfileMap() {
        final Map<String, Profile> result = new HashMap();
        result.put("Generic X-Box pad", createXBoxPadProfile());
        result.put("Gamepad F310 (Controller)", createLogitechF310Profile());
        return result;
    }

    private static Profile createXBoxPadProfile() {
        Map<Component.Identifier, Button> buttonMap = new HashMap();
        buttonMap.put(Component.Identifier.Button.A, Button.A);
        buttonMap.put(Component.Identifier.Button.B, Button.B);
        buttonMap.put(Component.Identifier.Button.LEFT_THUMB, Button.LEFT_INDEX);
        buttonMap.put(Component.Identifier.Button.LEFT_THUMB3, Button.LEFT_THUMB);
        buttonMap.put(Component.Identifier.Button.RIGHT_THUMB, Button.RIGHT_INDEX);
        buttonMap.put(Component.Identifier.Button.RIGHT_THUMB3, Button.RIGHT_THUMB);
        buttonMap.put(Component.Identifier.Button.SELECT, Button.SELECT);
        buttonMap.put(Component.Identifier.Button.START, Button.START);
        buttonMap.put(Component.Identifier.Button.X, Button.X);
        buttonMap.put(Component.Identifier.Button.Y, Button.Y);
        return new Profile(DEFAULT_AXIS_MAP, buttonMap);
    }

    private static Profile createLogitechF310Profile() {
        Map<Component.Identifier, Button> buttonMap = new HashMap();
        buttonMap.put(Component.Identifier.Button._0, Button.A);
        buttonMap.put(Component.Identifier.Button._1, Button.B);
        buttonMap.put(Component.Identifier.Button._2, Button.X);
        buttonMap.put(Component.Identifier.Button._3, Button.Y);
        buttonMap.put(Component.Identifier.Button._4, Button.LEFT_INDEX);
        buttonMap.put(Component.Identifier.Button._5, Button.RIGHT_INDEX);
        buttonMap.put(Component.Identifier.Button._6, Button.SELECT);
        buttonMap.put(Component.Identifier.Button._7, Button.START);
        buttonMap.put(Component.Identifier.Button._8, Button.LEFT_THUMB);
        buttonMap.put(Component.Identifier.Button._9, Button.RIGHT_THUMB);
        return new Profile(DEFAULT_AXIS_MAP, buttonMap);
    }
}
