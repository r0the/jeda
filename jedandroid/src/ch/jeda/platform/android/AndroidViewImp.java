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

public class AndroidViewImp extends AndroidCanvasImp implements ViewImp {

    private final AndroidEventsImp eventsImp;
    private final ViewManager viewManager;

    static AndroidViewImp create(ViewManager viewManager, boolean doubleBuffered) {
        if (doubleBuffered) {
            return new AndroidViewImp(viewManager);
        }
        else {
            return new SingleBufferedAndroidViewImp(viewManager);
        }
    }

    private AndroidViewImp(ViewManager viewManager) {
        this.eventsImp = new AndroidEventsImp();
        this.viewManager = viewManager;
        this.setSize(this.viewManager.getSize());
    }

    @Override
    public void close() {
    }

    public EnumSet<Window.Feature> getFeatures() {
        return EnumSet.noneOf(Window.Feature.class);
    }

    @Override
    public EventsImp getEventsImp() {
        return this.eventsImp;
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

    private static class SingleBufferedAndroidViewImp extends AndroidViewImp {

        SingleBufferedAndroidViewImp(ViewManager viewManager) {
            super(viewManager);
        }

        @Override
        void modified() {
            this.update();
        }
    }
}
