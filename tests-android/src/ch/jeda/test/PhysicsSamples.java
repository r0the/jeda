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

        body = new GlobalForceBody(0, 0);
        body.setPosition(30, 5);
        view.add(body);

        body = new GlobalForceBodyOffset(0.5, -0.2);
        body.setPosition(40, 5);
        view.add(body);

        body = new LocalForceBody(0, 0);
        body.setAngleDeg(45);
        body.setPosition(30, 10);
        view.add(body);

        body = new LocalForceBodyOffset(0.5, -0.2);
        body.setAngleDeg(45);
        body.setPosition(40, 10);
        view.add(body);

        body = new Body();
        body.addShape(new Circle(0, 0, 1));
        body.setPosition(10, 2);
        Body bodyB = new UserBody();
        bodyB.addShape(new Circle(0, 0, 1));
        bodyB.setPosition(10, 6);

        view.add(body, bodyB, new Box(view));
        new Rod(body, bodyB);
        view.setGravity(0, -1);
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

class GlobalForceBody extends Body {

    protected final double x;
    protected final double y;

    public GlobalForceBody(double x, double y) {
        this.x = x;
        this.y = y;
        setName("");
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        canvas.setColor(Color.LIGHT_GREEN_900);
        canvas.setAlignment(Alignment.CENTER);
        canvas.drawRectangle(0, 0, 2, 1);
        canvas.setColor(Color.RED_A700);
        canvas.drawPolyline(x, y, x + 2, y, x + 2, y + 1);
        canvas.setAlignment(Alignment.TOP_CENTER);
        canvas.drawText(x + 1.5, y - 0.1, "fx");
        canvas.setAlignment(Alignment.LEFT);
        canvas.drawText(x + 2 + 0.1, y + 0.5, "fy");
        canvas.setLineWidth(2);
        canvas.drawPolyline(x, y, x + 2, y + 1);
    }
}

class GlobalForceBodyOffset extends GlobalForceBody {

    public GlobalForceBodyOffset(double x, double y) {
        super(x, y);
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        super.drawDecoration(canvas);
        canvas.setColor(Color.RED_A700);
        canvas.fillCircle(x, y, 0.1);
        canvas.setAlignment(Alignment.TOP_RIGHT);
        canvas.drawText(x - 0.1, y - 0.1, "(x, y)");
    }
}

class LocalForceBody extends Body {

    protected final double x;
    protected final double y;

    public LocalForceBody(double x, double y) {
        this.x = x;
        this.y = y;
        setName("");
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        canvas.setColor(Color.LIGHT_GREEN_900);
        canvas.setAlignment(Alignment.CENTER);
        canvas.drawRectangle(0, 0, 2, 1);
        canvas.setColor(Color.RED_A700);
        canvas.setAlignment(Alignment.TOP_LEFT);
        canvas.drawText(x + 0.5, y, "a");
        canvas.setAlignment(Alignment.LEFT);
        canvas.drawText(x + 1.5, y - 0.5, "f");
        canvas.setLineWidth(2);
        canvas.drawPolyline(x, y, x + 2, y - 1);
    }
}

class LocalForceBodyOffset extends LocalForceBody {

    public LocalForceBodyOffset(double x, double y) {
        super(x, y);
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        super.drawDecoration(canvas);
        canvas.setColor(Color.RED_A700);
        canvas.fillCircle(x, y, 0.1);
        canvas.setAlignment(Alignment.TOP_RIGHT);
        canvas.drawText(x - 0.1, y - 0.1, "(x, y)");
    }
}

class UserBody extends Body implements KeyDownListener, KeyUpListener {

    private boolean up;
    private boolean right;

    @Override

    public void onKeyDown(KeyEvent ke) {
        if (ke.getKey() == Key.UP) {
            up = true;
        }
        if (ke.getKey() == Key.RIGHT) {
            right = true;
        }
    }

    @Override
    public void onKeyUp(KeyEvent ke) {
        if (ke.getKey() == Key.UP) {
            up = false;
        }
        if (ke.getKey() == Key.RIGHT) {
            right = false;
        }
    }

    @Override
    protected void step(double dt) {
        if (up) {
            applyLocalForceDeg(20, 90);
        }
        if (right) {
            applyLocalForceDeg(20, 0);
        }
    }

}
