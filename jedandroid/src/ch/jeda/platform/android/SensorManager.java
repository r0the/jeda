/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda.platform.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import ch.jeda.event.SensorEvent;
import ch.jeda.event.SensorType;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

class SensorManager extends Fragment {

    private final EnumSet<SensorType> enabledSensors;
    private final EnumMap<SensorType, Sensor> sensorMap;
    private final EnumMap<SensorType, SensorEventListener> sensorListenerMap;
    private final Map<Sensor, SensorInfo> sensorInfoMap;
    private android.hardware.SensorManager imp;

    SensorManager() {
        enabledSensors = EnumSet.noneOf(SensorType.class);
        sensorMap = new EnumMap<SensorType, Sensor>(SensorType.class);
        sensorListenerMap = new EnumMap<SensorType, SensorEventListener>(SensorType.class);
        sensorInfoMap = new HashMap<Sensor, SensorInfo>();
        setRetainInstance(true);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        sensorMap.clear();
        sensorInfoMap.clear();
        sensorListenerMap.clear();
        imp = (android.hardware.SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        checkAdd(SensorType.ACCELERATION, 1f, 0f, imp.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        checkAdd(SensorType.GRAVITY, 1f, 0f, imp.getDefaultSensor(Sensor.TYPE_GRAVITY));
        checkAdd(SensorType.LIGHT, 1f, 0f, imp.getDefaultSensor(Sensor.TYPE_LIGHT));
        checkAdd(SensorType.LINEAR_ACCELERATION, 1f, 0f, imp.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        // Android returns values in micro Tesla
        checkAdd(SensorType.MAGNETIC_FIELD, 1e-6f, 0f, imp.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        // Android returns value in hekto Pascal
        checkAdd(SensorType.PRESSURE, 100f, 0f, imp.getDefaultSensor(Sensor.TYPE_PRESSURE));
        // Android returns value in centimeter
        checkAdd(SensorType.PROXIMITY, 0.01f, 0f, imp.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        // Android returns value in degrees Celcius
        checkAdd(SensorType.TEMPERATURE, 1f, -273.15f, imp.getDefaultSensor(Sensor.TYPE_TEMPERATURE));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        for (final SensorType sensorType : sensorListenerMap.keySet()) {
            deactivateSensor(sensorType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (final SensorType sensorType : enabledSensors) {
            activateSensor(sensorType);
        }
    }

    boolean isAvailable(final SensorType sensorType) {
        return sensorMap.containsKey(sensorType);
    }

    boolean isEnabled(final SensorType sensorType) {
        return enabledSensors.contains(sensorType);
    }

    void setEnabled(final SensorType sensorType, final boolean enabled) {
        // Enable sensor only if it is available
        if (enabled && sensorMap.containsKey(sensorType)) {
            enabledSensors.add(sensorType);
        }
        else {
            enabledSensors.remove(sensorType);
        }

        updateListeners();
    }

    private void activateSensor(final SensorType sensorType) {
        final SensorEventListener listener = createEventListener(sensorType);
        if (listener != null) {
            imp.registerListener(listener,
                                 sensorMap.get(sensorType),
                                 android.hardware.SensorManager.SENSOR_DELAY_UI);
            sensorListenerMap.put(sensorType, listener);
        }
    }

    private void checkAdd(final SensorType sensorType, final float factor, final float shift, final Sensor sensor) {
        if (sensor != null) {
            sensorMap.put(sensorType, sensor);
            sensorInfoMap.put(sensor, new SensorInfo(sensorType, sensor, factor, shift));
        }
    }

    private SensorEventListener createEventListener(final SensorType sensorType) {
        switch (sensorType) {
            case ACCELERATION:
            case GRAVITY:
            case LINEAR_ACCELERATION:
            case MAGNETIC_FIELD:
                return new ThreeDeeSensorHandler((Main) getActivity(), sensorInfoMap);
            case LIGHT:
            case PRESSURE:
            case PROXIMITY:
            case TEMPERATURE:
                return new ValueSensorHandler((Main) getActivity(), sensorInfoMap);
            default:
                return null;
        }
    }

    private void deactivateSensor(final SensorType sensorType) {
        final SensorEventListener listener = sensorListenerMap.get(sensorType);
        if (listener != null) {
            imp.unregisterListener(listener);
            sensorListenerMap.remove(sensorType);
        }
    }

    private void updateListeners() {
        for (final SensorType sensorType : enabledSensors) {
            if (!sensorListenerMap.containsKey(sensorType)) {
                activateSensor(sensorType);
            }
        }

        for (final SensorType sensorType : sensorListenerMap.keySet()) {
            if (!enabledSensors.contains(sensorType)) {
                deactivateSensor(sensorType);
            }
        }
    }

    private static class ThreeDeeSensorHandler implements SensorEventListener {

        private final Main activity;
        private final Map<Sensor, SensorInfo> sensorReserveMap;

        ThreeDeeSensorHandler(final Main activity, final Map<Sensor, SensorInfo> sensorReserveMap) {
            this.activity = activity;
            this.sensorReserveMap = sensorReserveMap;
        }

        public void onSensorChanged(final android.hardware.SensorEvent event) {
            final SensorInfo sensorInfo = sensorReserveMap.get(event.sensor);
            float x = 0f;
            float y = 0f;
            final float z = event.values[2] * sensorInfo.factor;
            switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_0:
                    x = -event.values[0] * sensorInfo.factor;
                    y = -event.values[1] * sensorInfo.factor;
                    break;
                case Surface.ROTATION_90:
                    x = event.values[1] * sensorInfo.factor;
                    y = -event.values[0] * sensorInfo.factor;
                    break;
                case Surface.ROTATION_180:
                    x = event.values[0] * sensorInfo.factor;
                    y = event.values[1] * sensorInfo.factor;
                    break;
                case Surface.ROTATION_270:
                    x = -event.values[1] * sensorInfo.factor;
                    y = event.values[0] * sensorInfo.factor;
                    break;
            }

            activity.postEvent(new SensorEvent(sensorInfo, sensorInfo.sensorType, x, y, z));
        }

        public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        }
    }

    private static class ValueSensorHandler implements SensorEventListener {

        private final Main activity;
        private final Map<Sensor, SensorInfo> sensorReserveMap;

        ValueSensorHandler(final Main activity, final Map<Sensor, SensorInfo> sensorReserveMap) {
            this.activity = activity;
            this.sensorReserveMap = sensorReserveMap;
        }

        public void onSensorChanged(final android.hardware.SensorEvent event) {
            final SensorInfo sensorInfo = sensorReserveMap.get(event.sensor);
            final boolean maxiumum = event.values[0] >= event.sensor.getMaximumRange() ||
                                     event.values[0] <= -event.sensor.getMaximumRange();
            final float value = event.values[0] * sensorInfo.factor + sensorInfo.shift;
            activity.postEvent(new SensorEvent(sensorInfo, sensorInfo.sensorType, maxiumum, value));
        }

        public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        }
    }

    private static class SensorInfo {

        final float factor;
        final float shift;
        final SensorType sensorType;
        private final Sensor sensor;

        public SensorInfo(final SensorType sensorType, final Sensor sensor, final float factor, final float shift) {
            this.factor = factor;
            this.sensor = sensor;
            this.sensorType = sensorType;
            this.shift = shift;
        }

        @Override
        public String toString() {
            return sensor.getName();
        }
    }
}
