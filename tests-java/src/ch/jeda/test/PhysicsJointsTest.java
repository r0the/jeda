package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.ui.*;

public class PhysicsJointsTest extends Program implements KeyDownListener {

    private PhysicsView view;
    private Rod rod;

    @Override
    public void run() {
        view = new PhysicsView(ViewFeature.USER_SCALE, ViewFeature.USER_SCROLL);
        view.setDebugging(true);
        //view.setGravity(0, 0);
        view.add(new Box(view));
        Body bodyA = new Body();
        bodyA.addShape(new Circle(0, 0, 1));
        bodyA.setPosition(10, 10);
        Body bodyB = new Body();
        bodyB.addShape(new Rectangle(0, 0, 2, 1));
        bodyB.setPosition(10, 5);
        view.add(bodyA, bodyB);
        rod = new Rod(bodyA, bodyB);
        view.addEventListener(this);
    }

    @Override
    public void onKeyDown(KeyEvent ke) {
        rod.destroy();
    }
}
