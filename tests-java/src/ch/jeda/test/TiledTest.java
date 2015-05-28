package ch.jeda.test;

import ch.jeda.Program;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.tiled.TiledMap;
import ch.jeda.ui.*;

public class TiledTest extends Program {

    private PhysicsView view;
    private TiledMap map;

    @Override
    public void run() {
        view = new PhysicsView(700, 700, ViewFeature.USER_SCROLL, ViewFeature.USER_SCALE);
        view.add(new Box(view));
        view.setGravity(0, 0);
        view.setDebugging(true);
//        view.setPaused(true);
        map = new TiledMap("res:raw/racetrack.tmx");
        view.addEventListener(this);
        map.addTo(view);
    }
}
