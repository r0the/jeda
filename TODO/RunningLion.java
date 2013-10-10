package ch.jeda.demo;

import ch.jeda.*;
import ch.jeda.ui.*;
import ch.jeda.world.*;

public class RunningLion extends World {

    int horizonY;
    private static Color GROUND_COLOR;
    private static Color SKY_COLOR;

    @Override
    protected void init() {
        SKY_COLOR = new Color(20, 9, 70);
        GROUND_COLOR = new Color(245, 190, 99);

        horizonY = 2 * getCanvasWidth() / 3;

        addObject(new Lion(0, horizonY));
        setFeature(WorldFeature.SHOW_WORLD_INFO, true);
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        canvas.setColor(SKY_COLOR);
        canvas.fillRectangle(0, 0, canvas.getWidth(), horizonY);
        canvas.setColor(GROUND_COLOR);
        canvas.fillRectangle(0, horizonY, canvas.getWidth(), canvas.getHeight() - horizonY);
    }

    private class Lion extends MovableBody {

        TileSet images;
        int imageIndex;
        double duration;

        public Lion(float x, float y) {
            setPosition(x, y);
            images = new TileSet(":images/lion-running-128x128.png", 128, 128);
            setSpeed(150);
            setDirection(0);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawImage(getX(), getY(), images.getTileAt(imageIndex, 0), Alignment.CENTER);
        }

//        @Override
//        public void update(float dt) {
//            duration = duration + dt;
//            if (duration > 0.05) {
//                duration = 0;
//                imageIndex = imageIndex + 1;
//                if (imageIndex >= images.getWidth()) {
//                    imageIndex = 0;
//                }
//
//                if (getX() > getCanvasWidth()) {
//                    setPosition(0, this.getY());
//                }
//            }
//        }
    }
}
