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
package ch.jeda.platform.android;

import ch.jeda.platform.EventsImp;
import ch.jeda.platform.ViewImp;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.Window;
import java.util.EnumSet;

public class DoubleBufferedViewImp extends AndroidCanvasImp implements ViewImp {

    private final ViewManager viewManager;

    DoubleBufferedViewImp(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.setSize(this.viewManager.getSize());
    }

    @Override
    public void close() {
    }

    public EnumSet<Window.Feature> getFeatures() {
        return EnumSet.of(Window.Feature.DoubleBuffered);
    }

    @Override
    public EventsImp getEventsImp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMouseCursor(MouseCursor mouseCursor) {
        // ignore
    }

    @Override
    public void setTitle(final String title) {
        this.viewManager.setTitle(title);
    }

    @Override
    public void update() {
        this.viewManager.setBitmap(this.getBitmap());
    }
}
