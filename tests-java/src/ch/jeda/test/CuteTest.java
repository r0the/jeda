package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.cute.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CuteTest extends Program implements TickListener {

    Window window;
    CuteWorld world;

    @Override
    public void run() {
        window = new Window(WindowFeature.DOUBLE_BUFFERED);
        world = new CuteWorld(40, 4, 40);
        world.fill(0, Block.GRASS);
        // Initialize world here
        window.addEventListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        // Update world
        world.update(event.getDuration());
        // Draw background
        window.setColor(Color.BLUE);
        window.fill();
        // Draw world
        world.draw(window);
    }
}
