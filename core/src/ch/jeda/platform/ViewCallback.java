/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.platform;

import ch.jeda.event.PushButton;
import ch.jeda.event.Key;
import java.util.EnumSet;

public interface ViewCallback {

    void postKeyDown(Object source, Key key, int count);

    void postKeyTyped(Object source, Key key);

    void postKeyTyped(Object source, char ch);

    void postKeyUp(Object source, Key key);

    void postPointerDown(Object source, int pointerId, EnumSet<PushButton> pressedButtons, float x, float y);

    void postPointerMoved(Object source, int pointerId, EnumSet<PushButton> pressedButtons, float x, float y);

    void postPointerUp(Object source, int pointerId, EnumSet<PushButton> pressedButtons, float x, float y);

    void postWheel(Object source, float rotation);
}
