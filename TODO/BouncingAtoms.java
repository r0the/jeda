package ch.jeda.demo;

import ch.jeda.*;
import ch.jeda.ui.*;
import ch.jeda.world.*;

public class BouncingAtoms extends World {

    static int WALL_WIDTH = 50;

    @Override
    protected void init() {
        float r = 20f;
        for (int i = 0; i < 50; ++i) {
            MovableBody e = new Atom((float) Math.random() * getCanvasWidth(), (float) Math.random() * getCanvasHeight(), r);
            addObject(e);
        }

        addObject(new Wall(-WALL_WIDTH, -WALL_WIDTH, WALL_WIDTH, getCanvasHeight() + 2 * WALL_WIDTH));
        addObject(new Wall(getCanvasWidth(), -WALL_WIDTH, WALL_WIDTH, getCanvasHeight() + 2 * WALL_WIDTH));
        addObject(new Wall(0, -WALL_WIDTH, getCanvasWidth(), WALL_WIDTH));
        addObject(new Wall(0, getCanvasHeight(), getCanvasWidth(), WALL_WIDTH));
        setFeature(WorldFeature.SHOW_WORLD_INFO, true);
        setFeature(WorldFeature.SHOW_COLLISION_SHAPES, true);
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.fill();
    }

    private static class Wall extends FixedBody {

        Wall(float x, float y, float width, float height) {
            setShape(new Rectangle(width, height));
            setRestitution(1f);
            setPosition(x, y);
        }

        @Override
        public void draw(Canvas canvas) {
        }
    }

    private static class Atom extends MovableBody {

        private final float radius;

        Atom(float x, float y, float radius) {
            this.radius = radius;
            setSpeed(0.1f);
            //setMass(10);

            setDirection(Math.random() * Math.PI * 2);
            setShape(new Circle(radius));
            setRestitution(1f);
            setPosition(x, y);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.setColor(Color.BLACK);
            canvas.fillCircle(getX(), getY(), radius);
            canvas.drawText(getX(), getY(), toString());
        }
    }
}
