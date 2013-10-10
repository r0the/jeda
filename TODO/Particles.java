package ch.jeda.demo;

import ch.jeda.*;
import ch.jeda.ui.*;
import ch.jeda.world.*;

public class Particles extends World implements PointerDownListener,
                                                PointerMovedListener {

    private static final int PARTICLE_COUNT = 1000;
    private static final float MASS = 0.01f;
    float damping = 1.0f;
    // Coordinates of the mass
    float x;
    float y;
    float force;
    Circle center;
    boolean showHelp;
    Button incForce;
    Button decForce;
    Particle[] particles;

    @Override
    protected void init() {
        setTitle("Physics Engine: Animating " + PARTICLE_COUNT + " Particles");

        setFeature(WorldFeature.SHOW_WORLD_INFO, true);
        //setFeature(Window.Feature.HoveringPointer, true);
        x = getCanvasWidth() / 2;
        y = getCanvasHeight() / 2;
        force = 50;
        damping = 0.8f;
        incForce = new Button(10, 50, 200, 50, Key.UP);
        incForce.setText("UP: Increase Force");
        decForce = new Button(10, 130, 200, 50, Key.DOWN);
        decForce.setText("DOWN: Decrease Force");
        addObject(incForce);
        addObject(decForce);

        center = new Circle(20);
        center.setDraggable(true);
        center.setFillColor(Color.RED);
        center.move(getCanvasWidth() / 2, getCanvasHeight() / 2);
        addObject(center);
        reset();
    }

    @Override
    protected void update() {
        if (incForce.isClicked()) {
            force = Math.min(force + 10, 100);
        }
        if (decForce.isClicked()) {
            force = Math.max(force - 10, -100);
        }

        for (int i = 0; i < particles.length; ++i) {
            float dx = center.getCenterX() - particles[i].getX();
            float dy = center.getCenterY() - particles[i].getY();
            float d = (float) Math.sqrt(dx * dx + dy * dy);
            if (d > 20) {
                //particles[i].applyForceToCenter(dx * force / d, dy * force / d);
            }
        }
    }

    @Override
    protected void drawOverlay(Canvas canvas) {
        canvas.setColor(Color.BLACK);
        canvas.drawText(10, 200, "Force: " + force);
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        x = event.getX();
        y = event.getY();
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        x = event.getX();
        y = event.getY();
    }

    private void reset() {
        if (particles != null) {
            for (int i = 0; i < particles.length; ++i) {
                removeObject(particles[i]);
            }
        }

        particles = new Particle[PARTICLE_COUNT];
        for (int i = 0; i < PARTICLE_COUNT; ++i) {
            particles[i] = new Particle();
            particles[i].setPosition(Util.randomInt(getCanvasWidth()), Util.randomInt(getCanvasHeight()));
            //particles[i].setLinearDamping(0.0f);
            particles[i].setMass(MASS);
            particles[i].setColor(Color.BLACK);
            addObject(particles[i]);
        }
    }
}
