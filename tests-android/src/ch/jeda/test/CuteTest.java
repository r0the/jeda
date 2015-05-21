package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.cute.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CuteTest extends Program implements TickListener, WheelListener {

    Window window;
    CuteWorld world;
    int x;
    int y;

    @Override
    public void run() {
        window = new Window(WindowFeature.DOUBLE_BUFFERED);
        world = new CuteWorld(40, 40, 4);
        world.fill(0, Block.GRASS);
        world.setBlockAt(0, 0, 1, Block.STONE);
        world.setBlockAt(0, 0, 2, Block.STONE);
        world.setBlockAt(0, 0, 3, Block.STONE);

        x = 1;
        y = 1;
        addBlock(Block.BROWN);
        addBlock(Block.DIRT);
        addBlock(Block.EMPTY);
        addBlock(Block.GRASS);
        addBlock(Block.ICE);
        addBlock(Block.PLAIN);
        addBlock(Block.STONE);
        addBlock(Block.WALL);
        addBlock(Block.WATER);
        addBlock(Block.WOOD);

        x = 3;
        y = 5;
        addObject(CuteObjectType.BOY);
        addObject(CuteObjectType.CAT_GIRL);
        addObject(CuteObjectType.DEMON_GIRL);
        addObject(CuteObjectType.PINK_GIRL);
        addObject(CuteObjectType.PRINCESS_GIRL);

        x = 3;
        y = 8;
        addObject(CuteObjectType.BLUE_GEM);
        addObject(CuteObjectType.GREEN_GEM);
        addObject(CuteObjectType.ORANGE_GEM);
        addObject(CuteObjectType.HEART);
        addObject(CuteObjectType.STAR);
        addObject(CuteObjectType.SELECTOR);

        x = 3;
        y = 11;
        addObject(CuteObjectType.KEY);
        addObject(CuteObjectType.CLOSED_CHEST);
        addObject(CuteObjectType.OPEN_CHEST);
        addObject(CuteObjectType.CLOSED_DOOR);
        addObject(CuteObjectType.OPEN_DOOR);

        x = 3;
        y = 14;
        addObject(CuteObjectType.ROCK);
        addObject(CuteObjectType.SHORT_TREE);
        addObject(CuteObjectType.TALL_TREE);
        addObject(CuteObjectType.UGLY_TREE);
        addObject(CuteObjectType.BIG_BUG);

        window.addEventListener(this);
    }

    @Override
    public void onWheel(WheelEvent event) {
        //world.scroll(event.getDx(), event.getDy());
    }

    @Override
    public void onTick(TickEvent event) {
        // Update world
        world.update(event.getDuration());
        // Draw background
        window.setColor(Color.BLUE);
        window.fill();
        // Draw world
//        world.draw(window);
    }

    private void addBlock(Block block) {
        world.setBlockAt(x, y, 1, block);
        ++x;
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
