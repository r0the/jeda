package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.Circle;
import ch.jeda.geometry.Rectangle;
import ch.jeda.physics.Body;
import ch.jeda.physics.BodyType;
import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.*;

public class PhysicsTest extends Program implements TickListener, WheelListener, KeyDownListener {

    PhysicsView view;
    // Daten der Spielfigur
    Player player;

    @Override
    public void run() {
        Jeda.setTickFrequency(60);
        view = new PhysicsView(1400, 700, ViewFeature.USER_SCROLL);
        view.getBackground().setColor(Color.AQUA);
        view.getBackground().fill();
        view.setGravity(0, -9);
        view.setDebugging(true);
        view.createWalls();
        System.out.println("width=" + view.getWidth());
        view.createBox(1, 1, 2, 2, 0.5f);

        player = new Player();
        player.setPosition(10, 10);

        view.add(player);

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
    public void onWheel(WheelEvent event) {
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (event.getKey() == Key.DOWN) {
            view.setScale(view.getScale() / 1.1);
        }
        if (event.getKey() == Key.UP) {
            view.setScale(view.getScale() * 1.1);
        }
        System.out.println("scale=" + view.getScale());
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
        if (key == Key.SPACE) {
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
