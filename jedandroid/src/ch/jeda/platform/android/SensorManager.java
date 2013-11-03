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
package ch.jeda.platform.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import ch.jeda.SensorType;
import ch.jeda.event.SensorEvent;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

class SensorManager extends Fragment {

    private final EnumSet<SensorType> enabledSensors;
    private final EnumMap<SensorType, Sensor> sensorMap;
    private final EnumMap<SensorType, SensorEventListener> sensorListenerMap;
    private final Map<Sensor, SensorInfo> sensorInfoMap;
    private boolean paused;
    private android.hardware.SensorManager imp;

    SensorManager() {
        this.enabledSensors = EnumSet.noneOf(SensorType.class);
        this.sensorMap = new EnumMap<SensorType, Sensor>(SensorType.class);
        this.sensorListenerMap = new EnumMap<SensorType, SensorEventListener>(SensorType.class);
        this.sensorInfoMap = new HashMap<Sensor, SensorInfo>();
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.sensorMap.clear();
        this.sensorInfoMap.clear();
        this.sensorListenerMap.clear();
        this.imp = (android.hardware.SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        this.checkAdd(SensorType.ACCELERATION, this.imp.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        this.checkAdd(SensorType.GRAVITY, this.imp.getDefaultSensor(Sensor.TYPE_GRAVITY));
        this.checkAdd(SensorType.LINEAR_ACCELERATION, this.imp.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        this.checkAdd(SensorType.MAGNETIC_FIELD, this.imp.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        for (final SensorType sensorType : this.sensorListenerMap.keySet()) {
            this.deactivateSensor(sensorType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (final SensorType sensorType : this.enabledSensors) {
            this.activateSensor(sensorType);
        }
    }

    boolean isAvailable(final SensorType sensorType) {
        return this.sensorMap.containsKey(sensorType);
    }

    boolean isEnabled(final SensorType sensorType) {
        return this.enabledSensors.contains(sensorType);
    }

    void setEnabled(final SensorType sensorType, final boolean enabled) {
        // Enable sensor only if it is available
        if (enabled && this.sensorMap.containsKey(sensorType)) {
            this.enabledSensors.add(sensorType);
        }
        else {
            this.enabledSensors.remove(sensorType);
        }

        this.updateListeners();
    }

    private void activateSensor(final SensorType sensorType) {
        final SensorEventListener listener = this.createEventListener(sensorType);
        if (listener != null) {
            this.imp.registerListener(listener,
                                      this.sensorMap.get(sensorType),
                                      android.hardware.SensorManager.SENSOR_DELAY_UI);
            this.sensorListenerMap.put(sensorType, listener);
        }
    }

    private void checkAdd(final SensorType sensorType, final Sensor sensor) {
        if (sensor != null) {
            this.sensorMap.put(sensorType, sensor);
            this.sensorInfoMap.put(sensor, new SensorInfo(sensorType, sensor));
        }
    }

    private SensorEventListener createEventListener(final SensorType sensorType) {
        switch (sensorType) {
            case ACCELERATION:
            case GRAVITY:
            case LINEAR_ACCELERATION:
            case MAGNETIC_FIELD:
                return new ThreeDeeSensorHandler(this.getActivity(), this.sensorInfoMap);
            default:
                return null;
        }
    }

    private void deactivateSensor(final SensorType sensorType) {
        final SensorEventListener listener = this.sensorListenerMap.get(sensorType);
        if (listener != null) {
            this.imp.unregisterListener(listener);
            this.sensorListenerMap.remove(sensorType);
        }
    }

    private void updateListeners() {
        for (final SensorType sensorType : this.enabledSensors) {
            if (!this.sensorListenerMap.containsKey(sensorType)) {
                this.activateSensor(sensorType);
            }
        }

        for (final SensorType sensorType : this.sensorListenerMap.keySet()) {
            if (!this.enabledSensors.contains(sensorType)) {
                this.deactivateSensor(sensorType);
            }
        }
    }

    private static class ThreeDeeSensorHandler implements SensorEventListener {

        private final Activity activity;
        private final Map<Sensor, SensorInfo> sensorReserveMap;

        public ThreeDeeSensorHandler(final Activity activity, final Map<Sensor, SensorInfo> sensorReserveMap) {
            this.activity = activity;
            this.sensorReserveMap = sensorReserveMap;
        }

        public void onSensorChanged(final android.hardware.SensorEvent event) {
            final SensorInfo sensorInfo = this.sensorReserveMap.get(event.sensor);
            float x = 0f;
            float y = 0f;
            final float z = event.values[2];
            switch (this.activity.getWindowManager().getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_0:
                    x = -event.values[0];
                    y = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    x = event.values[1];
                    y = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    x = event.values[0];
                    y = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    x = -event.values[1];
                    y = -event.values[0];
                    break;
            }

            ((Main) this.activity).addEvent(new SensorEvent(sensorInfo, sensorInfo.getSensorType(), x, y, z));
        }

        public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        }
    }

    private static class SensorInfo {

        private final Sensor sensor;
        private final SensorType sensorType;

        public SensorInfo(final SensorType sensorType, final Sensor sensor) {
            this.sensor = sensor;
            this.sensorType = sensorType;
        }

        SensorType getSensorType() {
            return this.sensorType;
        }

        @Override
        public String toString() {
            return this.sensor.getName();
        }
    }
}
