package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.cute.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CuteTest extends Program implements TickListener {

    Window window;
    CuteWorld world;
    DragAndZoom dz;
    int x;
    int z;

    @Override
    public void run() {
        window = new Window(WindowFeature.DOUBLE_BUFFERED);
        world = new CuteWorld(40, 4, 40);
        world.fill(0, Block.GRASS);
        x = 3;
        z = 5;
        world.setBlockAt(0, 1, 0, Block.STONE);
        world.setBlockAt(0, 2, 0, Block.STONE);
        world.setBlockAt(0, 3, 0, Block.STONE);

        addObject(CuteObjectType.BOY);
        addObject(CuteObjectType.CAT_GIRL);
        addObject(CuteObjectType.DEMON_GIRL);
        addObject(CuteObjectType.PINK_GIRL);
        addObject(CuteObjectType.PRINCESS_GIRL);

        dz = new DragAndZoom();
        window.addEventListener(dz);

        // Initialize world here
        window.addEventListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        // Update world
        world.update(event.getDuration());
        world.scroll(-dz.getDx(), -dz.getDy());
        // Draw background
        window.setColor(Color.BLUE);
        window.fill();
        // Draw world
        world.draw(window);
    }

    private void addObject(CuteObjectType type) {
        CuteObject object = new CuteObject(type, x, 1, z);
        world.addObject(object);
        object.setMessage(type.toString());
        ++x;
        if (x > world.getSizeX() - 3) {
            x = 3;
            z = z + 2;
        }
    }
}
