package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.ui.*;

public class PhysicsTest extends Program implements TickListener, PointerMovedListener, ActionListener {

    PhysicsView view;
    // Daten der Spielfigur
    Player player;
    PointerEvent pe;

    @Override
    public void run() {
        Jeda.setTickFrequency(60);
        view = new PhysicsView(1400, 700, ViewFeature.USER_SCROLL, ViewFeature.USER_SCALE);
        view.getBackground().setColor(Color.LIGHT_GREEN_50);
        view.getBackground().fill();
        view.setGravity(0, 0);

        view.setDebugging(true);
        view.add(new Box(view));
        view.add(new Box(2, 2, 1, 1, 0.1));

        Body body = new Body();
        body.addShape(new Circle(0, 0, 1));
        body.addShape(new Rectangle(-1, -1, 2, 1));
        body.setPosition(10, 10);
        body.setAngleDeg(90);
        view.add(body);

        player = new Player();
        player.setPosition(10, 10);

///        view.add(player);
        Body ground = new Body();
        ground.setType(BodyType.STATIC);
        ground.setPosition(0, 1);
        ground.addShape(new Rectangle(0, 0, 20, 0.2));
        view.add(ground);
//        physics.loadMap("res:level-1.tmx");
        view.addEventListener(this);
        view.setPaused(false);
    }

    @Override
    public void onTick(TickEvent event) {
//        fenster.setColor(Color.WHITE);
//        fenster.fill();
        view.step(event.getDuration());
//        if (this.contact != null && this.contact.involves(player)) {
//            fenster.setColor(Color.RED);
//            fenster.drawText(10, 10, "contact!");
//            this.contact = null;
//        }
    }

    @Override
    public void onPointerMoved(PointerEvent pe) {
        this.pe = pe;
        System.out.println("x=" + pe.getWorldX() + ", y=" + pe.getWorldY());
    }

    @Override
    public void onAction(ActionEvent event) {
        if (event.getName().equals("CHEVRON_DOWN")) {
            view.scale(1.1, 0, 0);
        }
        else if (event.getName().equals("CHEVRON_UP")) {
            view.scale(1 / 1.1, 0, 0);
        }
    }
}

class Player extends Body implements KeyDownListener, KeyUpListener, TickListener {

    private boolean links;
    private boolean rechts;
    private boolean springen;

    public Player() {
        addShape(new Circle(0, 0, 1));
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        updateKey(event.getKey(), true);
    }

    @Override
    public void onKeyUp(KeyEvent event) {
        updateKey(event.getKey(), false);
    }

    @Override
    protected void drawDecoration(Canvas canvas) {
        canvas.setLineWidth(0.01);
        canvas.setColor(Color.AQUA);
        canvas.fillCircle(0, 0, 1);
    }

    void updateKey(Key key, boolean pressed) {
        if (key == Key.UP) {
            springen = pressed;
        }
        if (key == Key.LEFT) {
            links = pressed;
        }
        if (key == Key.RIGHT) {
            rechts = pressed;
        }
    }

    @Override
    public void onTick(TickEvent te) {
        if (links) {
            applyForce(-5, 0);
        }

        if (rechts) {
            applyForce(5, 0);
        }

        if (springen) {
            applyForce(0, 100);
        }
    }
}
