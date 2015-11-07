/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.jeda.ex4;

import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;
import ch.jeda.geometry.Circle;
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Element;

/**
 *
 * @author stefan
 */
public class InteractiveElement extends Element implements PointerListener {

    private Color backgroundColor;
    private Color borderColor;
    private final Shape shape;
    private PointerEvent drag;

    InteractiveElement() {
        shape = new Circle(0, 0, 100);
        backgroundColor = Color.WHITE;
        borderColor = Color.BLACK;
        setDrawOrder(1);
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        if (drag == null && contains(event.getViewX(), event.getViewY())) {
            drag = event;
        }
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        if (drag != null && drag.getPointerId() == event.getPointerId()) {
            float dx = event.getViewX() - drag.getViewX();
            float dy = event.getViewY() - drag.getViewY();
            move(dx, dy);
            drag = event;
        }
    }

    @Override
    public void onPointerUp(PointerEvent event) {
        if (drag != null && drag.getPointerId() == event.getPointerId()) {
            drag = null;
        }
    }

    public boolean contains(final double x, final double y) {
        return containsLocal(x - this.getX(), y - this.getY());
    }

    public boolean containsLocal(final double x, final double y) {
        return shape.contains(x, y);
    }

    public void move(final double dx, final double dy) {
        setPosition(getX() + dx, getY() + dy);
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.setColor(backgroundColor);
        shape.fill(canvas);
        canvas.setColor(borderColor);
        shape.draw(canvas);
    }
}
