/*
 * Copyright (C) 2014 by Stefan Rothe
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

import ch.jeda.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ListenerWrapper {

    private final Object object;
    private final Method onAction;
    private final Method onConnectionAccepted;
    private final Method onConnectionClosed;
    private final Method onKeyDown;
    private final Method onKeyUp;
    private final Method onKeyTyped;
    private final Method onMessageReceived;
    private final Method onPointerDown;
    private final Method onPointerMoved;
    private final Method onPointerUp;
    private final Method onSensorChanged;

    ListenerWrapper(final Object object) {
        this.object = object;
        this.onAction = findMethod(object, "onAction", ActionEvent.class);
        this.onConnectionAccepted = findMethod(object, "onConnectionAccepted", ConnectionEvent.class);
        this.onConnectionClosed = findMethod(object, "onConnectionClosed", ConnectionEvent.class);
        this.onKeyDown = findMethod(object, "onKeyDown", KeyEvent.class);
        this.onKeyUp = findMethod(object, "onKeyUp", KeyEvent.class);
        this.onKeyTyped = findMethod(object, "onKeyTyped", KeyEvent.class);
        this.onMessageReceived = findMethod(object, "onMessageReceived", MessageEvent.class);
        this.onPointerDown = findMethod(object, "onPointerDown", PointerEvent.class);
        this.onPointerMoved = findMethod(object, "onPointerMoved", PointerEvent.class);
        this.onPointerUp = findMethod(object, "onPointerUp", PointerEvent.class);
        this.onSensorChanged = findMethod(object, "onSensorChanged", SensorEvent.class);
    }

    void handleEvent(final Event event) {
        switch (event.getType()) {
            case ACTION:
                if (this.onAction != null) {
                    this.invoke(this.onAction, event);
                }

                break;
            case CONNECTION_ACCEPTED:
                if (this.onConnectionAccepted != null) {
                    this.invoke(this.onConnectionAccepted, event);
                }

                break;
            case CONNECTION_CLOSED:
                if (this.onConnectionClosed != null) {
                    this.invoke(this.onConnectionClosed, event);
                }

                break;
            case KEY_DOWN:
                if (this.onKeyDown != null) {
                    this.invoke(this.onKeyDown, event);
                }

                break;
            case KEY_TYPED:
                if (this.onKeyTyped != null) {
                    this.invoke(this.onKeyDown, event);
                }

                break;
            case KEY_UP:
                if (this.onKeyUp != null) {
                    this.invoke(this.onKeyUp, event);
                }

                break;
            case MESSAGE_RECEIVED:
                if (this.onMessageReceived != null) {
                    this.invoke(this.onMessageReceived, event);
                }

                break;
            case POINTER_DOWN:
                if (this.onPointerDown != null) {
                    this.invoke(this.onPointerDown, event);
                }

                break;
            case POINTER_MOVED:
                if (this.onPointerMoved != null) {
                    this.invoke(this.onPointerMoved, event);
                }

                break;
            case POINTER_UP:
                if (this.onPointerUp != null) {
                    this.invoke(this.onPointerUp, event);
                }

                break;
            case SENSOR:
                if (this.onSensorChanged != null) {
                    this.invoke(this.onSensorChanged, event);
                }

                break;
        }
    }

    private void invoke(final Method method, final Event event) {
        try {
            method.invoke(this.object, event);
        }
        catch (final InvocationTargetException ex) {
            Log.err(ex.getCause(), "jeda.event.error");
        }
        catch (final Exception ex) {
            Log.err(ex, "jeda.event.error");
        }
    }

    private Method findMethod(final Object object, final String name, final Class parameter) {
        try {
            final Method method = object.getClass().getMethod(name, parameter);
            method.setAccessible(true);
            return method;
        }
        catch (final Exception ex) {
            return null;
        }
    }
}
