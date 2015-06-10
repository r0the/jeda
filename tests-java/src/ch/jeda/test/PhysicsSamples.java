package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.ui.*;

public class PhysicsSamples extends Program {

    PhysicsView view;
    // Daten der Spielfigur
    PointerEvent pe;

    @Override
    public void run() {
        view = new PhysicsView(1400, 700, ViewFeature.USER_SCROLL, ViewFeature.USER_SCALE);
        view.getBackground().setColor(Color.LIGHT_GREEN_200);
        view.getBackground().fill();
        view.setGravity(0, 0);

        view.setDebugging(true);

        // addShape
        Body body = new Body();
        body.addShape(new Circle(-2, 0, 1));
        body.addShape(new Rectangle(-2, -0.5, 4, 1));
        body.addShape(new Circle(2, 0, 1));
        body.setPosition(20, 10);
        view.add(body);

        // setAngle
        body = new Body();
        body.addShape(new Rectangle(-1.5, -0.5, 3, 1));
        view.add(body);

        body = new Body();
        body.addShape(new Rectangle(-1.5, -0.5, 3, 1));
        body.setPosition(4, 0);
        body.setAngleDeg(45);
        view.add(body);

        // setImage
        body = new Body();
        body.setName("Body A");
        body.setImage(new Image("res:drawable/asteroid.png"), 2, 2);
        body.addShape(new Rectangle(-1, -1, 2, 2));
        body.setPosition(7, 2);
        view.add(body);

        body = new Body();
        body.setName("Body B");
        body.setImage(new Image("res:drawable/asteroid.png"), 4, 2);
        body.addShape(new Rectangle(-2, -1, 4, 2));
        body.setPosition(11, 2);
        view.add(body);

        // setOpacity
        body = new Body();
        body.setName("Body A");
        body.setImage(new Image("res:drawable/asteroid.png"), 2, 2);
        body.addShape(new Rectangle(-1, -1, 2, 2));
        body.setPosition(15, 2);
        body.setOpacity(255);
        view.add(body);

        body = new Body();
        body.setName("Body B");
        body.setImage(new Image("res:drawable/asteroid.png"), 2, 2);
        body.addShape(new Rectangle(-1, -1, 2, 2));
        body.setPosition(17.5, 2);
        body.setOpacity(150);
        view.add(body);

        body = new Body();
        body.setName("Body C");
        body.setImage(new Image("res:drawable/asteroid.png"), 2, 2);
        body.addShape(new Rectangle(-1, -1, 2, 2));
        body.setPosition(20, 2);
        body.setOpacity(100);
        view.add(body);

        body = new DecoratedBody();
        body.setPosition(14, 5);
        view.add(body);

        body = new DecoratedBody();
        body.setAngleDeg(45);
        body.setPosition(18, 5);
        view.add(body);
    }
}

class DecoratedBody extends Body {

    @Override
    protected void drawDecoration(Canvas canvas) {
        canvas.setColor(Color.LIGHT_GREEN_900);
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.fillRectangle(0, 0, 2, 1);
    }
}
