package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.ui.*;

public class PhysicsJointsTest extends Program implements KeyDownListener {

    private PhysicsView view;
    private Rod rod;
    private Body bodyA;
    private Body bodyB;

    @Override
    public void run() {
        view = new PhysicsView(ViewFeature.USER_SCALE, ViewFeature.USER_SCROLL);
        view.setGravity(0, 0);
        view.setDebugging(true);
        //view.setGravity(0, 0);
        view.add(new Box(view));
        bodyA = new Body();
        bodyA.addShape(new Circle(0, 0, 1));
        bodyA.setPosition(10, 10);
        bodyB = new Body();
        bodyB.addShape(new Rectangle(0, 0, 2, 1));
        bodyB.setPosition(10, 5);
        view.add(bodyA, bodyB);
        rod = new Rod(bodyA, bodyB);
        rod.setLength(3);
        view.addEventListener(this);
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (event.getKey() == Key.D) {
            rod.destroy();
        }

        if (event.getKey() == Key.DIGIT_1) {
            rod.setLength(1);
        }

        if (event.getKey() == Key.DIGIT_2) {
            rod.setLength(5);
        }

        if (event.getKey() == Key.N) {
            rod = new Rod(bodyA, bodyB);
            rod.setLength(4);
        }
    }
}
