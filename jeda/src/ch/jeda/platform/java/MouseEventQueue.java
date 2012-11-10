/*
 * Copyright (C) 2011 by Stefan Rothe
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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

class MouseEventQueue extends EventQueue<MouseEvent> implements MouseListener,
                                                                MouseMotionListener,
                                                                MouseWheelListener {

    @Override
    public void mouseClicked(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        this.add(event);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        this.add(event);
    }
}
