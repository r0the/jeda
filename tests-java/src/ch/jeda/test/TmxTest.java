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
package ch.jeda.test;

import ch.jeda.Program;
import ch.jeda.event.ScrollEvent;
import ch.jeda.event.ScrollListener;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.tmx.TmxMap;
import ch.jeda.ui.Window;
import ch.jeda.ui.WindowFeature;

public class TmxTest extends Program implements TickListener, ScrollListener {

    private Window window;
    private TmxMap map;
    private double offsetX;
    private double offsetY;
    private double maxOffsetX;
    private double maxOffsetY;

    @Override
    public void run() {
        window = new Window(500, 500, WindowFeature.DOUBLE_BUFFERED, WindowFeature.SCROLLABLE);
        map = new TmxMap("res:raw/test_xml.tmx");
        window.addEventListener(this);
        maxOffsetX = map.getWidth() * map.getTileWidth() - window.getWidth();
        maxOffsetY = map.getHeight() * map.getTileHeight() - window.getHeight();
    }

    @Override
    public void onTick(TickEvent event) {
        map.draw(window, -offsetX, -offsetY);
    }

    @Override
    public void onScroll(ScrollEvent event) {
        offsetX = Math.max(0.0, Math.min(offsetX + event.getDx(), maxOffsetX));
        offsetY = Math.max(0.0, Math.min(offsetY + event.getDy(), maxOffsetY));
    }

}
