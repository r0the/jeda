package ch.jeda.demo;

import ch.jeda.*;
import ch.jeda.ui.*;

public class Pong extends Simulation {

    Window window;
    // Höhe des Zeichenbereichs
    int h;
    // Breite des Zeichenbereichs
    int w;
    Player left;
    Player right;
    Ball ball;

    @Override
    protected void init() {
        window = new Window(600, 400, Window.Feature.DoubleBuffered,
                            Window.Feature.OrientationLandscape);
        window.setTitle("Pong");
        window.setFontSize(40);
        w = window.getWidth();
        h = window.getHeight();
        ball = new Ball(w, h, 0);
        int paddleWidth = 15;
        int paddleHeight = h / 5;
        left = new LeftPlayer(0, (h - paddleHeight) / 2, paddleWidth, paddleHeight);
        right = new RightPlayer(w - paddleWidth, (h - paddleHeight) / 2, paddleWidth, paddleHeight);
    }

    @Override
    protected void step() {
        ball.move(w, h);

        left.handleUserInput(window);
        right.handleUserInput(window);

        left.checkHit(ball);
        right.checkHit(ball);

        if (ball.getX() < 0) {
            right.score();
            ball.reset(w, h, 0);
        }

        if (ball.getX() > w) {
            left.score();
            ball.reset(w, h, Math.PI);
        }

        // Hintergrund zeichnen
        window.setColor(Color.BLACK);
        window.fill();

        ball.draw(window);
        left.drawPaddle(window);
        right.drawPaddle(window);
        // Punkte darstellen
        window.setColor(Color.WHITE);
        window.drawText(50, 10, "" + left.getPoints());
        window.drawText(w - 50, 10, "" + right.getPoints());

        window.update();
    }

    private static class Ball {

        private double x;
        private double y;
        private int radius;
        private double speed;
        private double direction;

        public Ball(int w, int h, double dir) {
            reset(w, h, dir);
            radius = 15;
            speed = 5;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getRadius() {
            return radius;
        }

        public void bounceHorizontal(int newX) {
            direction = Math.PI - direction;
            x = newX;
        }

        public final void reset(int w, int h, double dir) {
            x = w / 2;
            y = h / 2;
            direction = (0.5 * Math.random() - 0.25) * Math.PI + dir;
        }

        public void move(int w, int h) {
            // Ball bewegen
            x = x + speed * Math.cos(direction);
            y = y - speed * Math.sin(direction);

            // Abprallen am oberen Rand
            // Wenn die y-Koordinate kleiner als der Radius ist, 
            // hat der Ball den Rand erreicht.
            if (y < radius) {
                direction = -direction;
                // Den Ball aufs Spielfeld zurück setzen, damit er sich nicht am
                // Rand verfängt.
                y = radius;
            }

            // Abprallen am unteren Rand
            // Wenn die y-Koordinate grösser als die Höhe des Spielfelds minus
            // Radius ist, hat der Ball den Rand erreicht.
            if (y > h - radius) {
                direction = -direction;
                // Den Ball aufs Spielfeld zurück setzen, damit er sich nicht am
                // Rand verfängt.
                y = h - radius;
            }
        }

        public void draw(Window window) {
            window.setColor(Color.WHITE);
            window.fillCircle((int) x, (int) y, radius);
        }
    }

    private static abstract class Player {

        // Schläger: x-Koordinate der linken Kante
        protected int paddleX;
        // Schläger: y-Koordinate der oberen Kante
        protected int paddleY;
        // Dicke des Schlägers
        protected int paddleWidth;
        // Höhe des Schlägers
        protected int paddleHeight;
        // Geschwindigkeit des Schläger
        protected int paddleSpeed;
        // Punkte des Spielers
        private int points;
        // Zeiger des Spielers
        private Pointer pointer;

        public Player(int paddleX, int paddleY, int paddleWidth, int paddleHeight) {
            this.paddleX = paddleX;
            this.paddleY = paddleY;
            this.paddleWidth = paddleWidth;
            this.paddleHeight = paddleHeight;
            this.paddleSpeed = 5;
        }

        public void handleUserInput(Window window) {
            // Benutzereingaben bearbeiten. Mit Math.max und Math.min wird
            // verhindert, dass sich die Schläger aus dem Spielfeld bewegen.
            Events events = window.getEvents();
            if (pointer != null && !pointer.isAvailable()) {
                pointer = null;
            }

            for (Pointer p : events.getNewPointers()) {
                if (pointer == null && isValidPointer(p)) {
                    pointer = p;
                }
            }

            int move = 0;

            if (pointer != null) {
                if (pointer.getY() < paddleY + paddleHeight / 2 - 20) {
                    move = -paddleSpeed;
                }

                if (pointer.getY() > paddleY + paddleHeight / 2 + 20) {
                    move = paddleSpeed;
                }
            }

            if (events.isKeyPressed(Key.Q)) {
                move = -paddleSpeed;
            }

            if (events.isKeyPressed(Key.A)) {
                move = paddleSpeed;
            }

            paddleY = paddleY + move;

            // Sicherstellen, dass die Schläger im Spielfeld bleiben
            paddleY = Math.min(Math.max(paddleY, 0), window.getHeight() - paddleHeight);

        }

        public void score() {
            points = points + 1;
        }

        public abstract void checkHit(Ball ball);

        public void drawPaddle(Window window) {
            // Schläger zeichnen
            window.setColor(Color.WHITE);
            window.fillRectangle(paddleX, paddleY, paddleWidth, paddleHeight);
        }

        public int getPoints() {
            return points;
        }

        protected abstract boolean isValidPointer(Pointer pointer);
    }

    private static class LeftPlayer extends Player {

        public LeftPlayer(int paddleX, int paddleY, int paddleWidth, int paddleHeight) {
            super(paddleX, paddleY, paddleWidth, paddleHeight);
        }

        @Override
        public void checkHit(Ball ball) {
            if (ball.getX() < paddleX + paddleWidth + ball.getRadius() && paddleY < ball.getY() && ball.getY() < paddleY + paddleHeight) {
                ball.bounceHorizontal(paddleX + paddleWidth + ball.getRadius());
            }
        }

        @Override
        protected boolean isValidPointer(Pointer pointer) {
            return pointer.getX() < paddleX + paddleWidth + 40;
        }
    }

    private static class RightPlayer extends Player {

        public RightPlayer(int paddleX, int paddleY, int paddleWidth, int paddleHeight) {
            super(paddleX, paddleY, paddleWidth, paddleHeight);
        }

        @Override
        public void checkHit(Ball ball) {
            if (ball.getX() > paddleX - ball.getRadius() && paddleY < ball.getY() && ball.getY() < paddleY + paddleHeight) {
                ball.bounceHorizontal(paddleX - ball.getRadius());
            }
        }

        @Override
        protected boolean isValidPointer(Pointer pointer) {
            return pointer.getX() > paddleX - 40;
        }
    }
}
