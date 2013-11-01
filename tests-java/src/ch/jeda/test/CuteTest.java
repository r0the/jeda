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
    int y;

    @Override
    public void run() {
        window = new Window(WindowFeature.DOUBLE_BUFFERED);
        world = new CuteWorld(40, 40, 4);
        world.fill(0, Block.GRASS);
        x = 3;
        y = 5;
        world.setBlockAt(0, 0, 1, Block.STONE);
        world.setBlockAt(0, 0, 2, Block.STONE);
        world.setBlockAt(0, 0, 3, Block.STONE);

        addObject(CuteObjectType.BOY);
        addObject(CuteObjectType.CAT_GIRL);
        addObject(CuteObjectType.DEMON_GIRL);
        addObject(CuteObjectType.PINK_GIRL);
        addObject(CuteObjectType.PRINCESS_GIRL);

        dz = new DragAndZoom();

        window.addEventListener(dz);
        window.addEventListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        world.scroll(-dz.getDx(), -dz.getDy());
        System.out.println(-dz.getDx());
        // Update world
        world.update(event.getDuration());
        // Draw background
        window.setColor(Color.BLUE);
        window.fill();
        // Draw world
        world.draw(window);
    }

    private void addObject(CuteObjectType type) {
        CuteObject object = new CuteObject(type, x, y, 1);
        world.addObject(object);
        object.setMessage(type.toString());
        ++x;
        if (x > world.getSizeX() - 3) {
            x = 3;
            y = y + 2;
        }
    }
}
