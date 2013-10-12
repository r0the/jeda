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
import ch.jeda.SensorType;
import ch.jeda.event.EventSource;
import ch.jeda.event.SensorEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

class SensorManager {

    private static final EventSource SENSOR = new EventSource(10, "Sensor");
    private final android.hardware.SensorManager imp;
    private final EnumMap<SensorType, Sensor> sensorMap;
    private final EnumMap<SensorType, SensorEventListener> sensorListenerMap;
    private final Map<Sensor, SensorInfo> sensorReserveMap;
    private final ViewManager viewManager;

    SensorManager(final Activity activity, final ViewManager viewManager) {
        this.imp = (android.hardware.SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        this.sensorMap = new EnumMap<SensorType, Sensor>(SensorType.class);
        this.sensorListenerMap = new EnumMap<SensorType, SensorEventListener>(SensorType.class);
        this.sensorReserveMap = new HashMap<Sensor, SensorInfo>();
        this.viewManager = viewManager;

        this.checkAdd(SensorType.Acceleration, this.imp.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        this.checkAdd(SensorType.Gravity, this.imp.getDefaultSensor(Sensor.TYPE_GRAVITY));
        this.checkAdd(SensorType.LinearAcceleration, this.imp.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        this.checkAdd(SensorType.MagneticField, this.imp.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    boolean isAvailable(final SensorType sensorType) {
        return this.sensorMap.containsKey(sensorType);
    }

    boolean isEnabled(final SensorType sensorType) {
        return this.sensorListenerMap.containsKey(sensorType);
    }

    void setEnabled(final SensorType sensorType, final boolean enable) {
        if (!isAvailable(sensorType) || enable == isEnabled(sensorType)) {
            return;
        }

        if (enable) {
            final SensorEventListener listener = this.createEventListener(sensorType);
            if (listener != null) {
                this.imp.registerListener(listener, this.sensorMap.get(sensorType), android.hardware.SensorManager.SENSOR_DELAY_UI);
                this.sensorListenerMap.put(sensorType, listener);
            }
        }
        else {
            final SensorEventListener listener = this.sensorListenerMap.get(sensorType);
            if (listener != null) {
                this.imp.unregisterListener(listener);
            }
        }
    }

    private void checkAdd(final SensorType sensorType, final Sensor sensor) {
        if (sensor != null) {
            this.sensorMap.put(sensorType, sensor);
            this.sensorReserveMap.put(sensor, new SensorInfo(sensorType, sensor));
        }
    }

    private SensorEventListener createEventListener(final SensorType sensorType) {
        switch (sensorType) {
            case Acceleration:
            case Gravity:
            case LinearAcceleration:
            case MagneticField:
                return new ThreeDeeSensorHandler(this.viewManager, this.sensorReserveMap);
            default:
                return null;
        }
    }

    private static class ThreeDeeSensorHandler implements SensorEventListener {

        private final ViewManager viewManager;
        private final Map<Sensor, SensorInfo> sensorReserveMap;

        public ThreeDeeSensorHandler(final ViewManager viewManager, final Map<Sensor, SensorInfo> sensorReserveMap) {
            this.viewManager = viewManager;
            this.sensorReserveMap = sensorReserveMap;
        }

        public void onSensorChanged(final android.hardware.SensorEvent event) {
            final SensorInfo sensorInfo = this.sensorReserveMap.get(event.sensor);
            this.viewManager.addEvent(new SensorEvent(sensorInfo, sensorInfo.getSensorType(),
                -event.values[0], event.values[1], event.values[2]));
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
