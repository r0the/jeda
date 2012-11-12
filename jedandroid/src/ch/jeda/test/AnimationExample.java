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
package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.ui.*;

public class AnimationExample extends Simulation {

    private static final int RADIUS = 20;
    private int x;
    private int y;
    private Window window;

    @Override
    public void init() {
        window = new Window(300, 300, Window.Feature.DoubleBuffered);
        window.setTitle("Animation Example");
        x = window.getWidth() / 2;
        y = window.getHeight() / 2 - RADIUS;
    }

    @Override
    public void step() {
//        if (window.getInput().isKeyTyped(Key.F5)) {
//            window.setFullscreen(!window.isFullscreen());
//        }
        x = x + 3;
        if (x >= window.getWidth()) {
            x = 0;
        }

        // Draw background
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.GREEN);
        window.fillRectangle(0, window.getHeight() / 2, window.getWidth(), window.getHeight());
        // Draw foreground
        window.setColor(Color.BLACK);
        window.setFontSize(16);
        window.drawString(10, 10, "Frame rate: " + getCurrentFrequency());
        window.setFontSize(20);
        window.fillCircle(x, y, RADIUS);
        window.flip();
    }
}
