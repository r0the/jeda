package ch.jeda.test;

import ch.jeda.Program;
import ch.jeda.event.*;
import ch.jeda.physics.PhysicsView;
import ch.jeda.tiled.TiledMap;
import ch.jeda.ui.*;

public class TiledTest extends Program implements TickListener {

    private PhysicsView view;
    private TiledMap map;
    private double offsetX;
    private double offsetY;
    private double maxOffsetX;
    private double maxOffsetY;

    @Override
    public void run() {
        view = new PhysicsView(700, 700, ViewFeature.USER_SCROLL);
        view.createWalls();
        view.setGravity(0, 0);
        view.setDebugging(true);
        map = new TiledMap("res:raw/test_xml.tmx");
        view.addEventListener(this);
        map.addTo(view);
//        maxOffsetX = map.getWidth() * map.getTileWidth() - window.getWidth();
//        maxOffsetY = map.getHeight() * map.getTileHeight() - window.getHeight();
    }

    @Override
    public void onTick(TickEvent event) {
    }

}
