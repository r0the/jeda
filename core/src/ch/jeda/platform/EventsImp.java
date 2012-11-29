/*
 * Copyright (C) 2012 by Stefan Rothe
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

import ch.jeda.Location;
import ch.jeda.ui.Key;
import java.util.List;
import java.util.Set;

public interface EventsImp {

    Location getPointerLocation();

    Location getPointerMovement();

    Set<Key> getPressedKeys();

    String getTypedChars();

    List<Key> getTypedKeys();

    boolean isClicked();

    boolean isDragging();
}
