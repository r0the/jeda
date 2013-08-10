/*
 * Copyright (C) 2013 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.world;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Body extends PhysicsObject {

    private final FixtureDef fixtureDef;
    private Fixture fixture;
    private Image image;
    private float offsetX;
    private float offsetY;

    protected Body() {
        this.fixtureDef = new FixtureDef();
    }

    @Override
    public void draw(final Canvas canvas) {
        if (this.image != null) {
            canvas.drawImage(this.getX() + this.offsetX, this.getY() + this.offsetY, this.image, Alignment.CENTER);
        }
    }

    public final Image getImage() {
        return this.image;
    }

    public final float getRestitution() {
        if (this.fixture == null) {
            return this.fixtureDef.restitution;
        }
        else {
            return this.fixture.getRestitution();
        }
    }

    public final void setImage(final Image image) {
        this.image = image;
    }

    public final void setOffset(final float x, final float y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public final void setRestitution(final float restitution) {
        if (restitution < 0f || 1f < restitution) {
            throw new IllegalArgumentException("restitution");
        }

        if (this.fixture == null) {
            this.fixtureDef.restitution = restitution;
        }
        else {
            this.fixture.setRestitution(restitution);
        }
    }

    public final void setShape(final Shape shape) {
        this.fixtureDef.shape = convertShape(shape);
        if (this.fixture != null) {
            this.body.destroyFixture(this.fixture);
            this.fixture = this.body.createFixture(this.fixtureDef);
        }
    }

    @Override
    void addToWorld(final org.jbox2d.dynamics.World physics) {
        super.addToWorld(physics);
        if (this.fixture == null) {
            this.fixture = this.body.createFixture(this.fixtureDef);
        }
    }

    @Override
    void removeFromWorld(final org.jbox2d.dynamics.World physics) {
        if (this.fixture != null) {
            this.fixtureDef.restitution = this.fixture.getRestitution();
            this.body.destroyFixture(this.fixture);
            this.fixture = null;
        }

        super.removeFromWorld(physics);
    }

    private static org.jbox2d.collision.shapes.Shape convertShape(Shape shape) {
        if (shape instanceof Circle) {
            final Circle orig = (Circle) shape;
            final org.jbox2d.collision.shapes.CircleShape result = new org.jbox2d.collision.shapes.CircleShape();
            result.setRadius(orig.getRadius());
            result.m_p.x = orig.getCenterX();
            result.m_p.y = orig.getCenterY();
            return result;
        }
        else if (shape instanceof Rectangle) {
            final Rectangle orig = (Rectangle) shape;
            final org.jbox2d.collision.shapes.PolygonShape result = new org.jbox2d.collision.shapes.PolygonShape();
            final float halfWidth = orig.getWidth() / 2f;
            final float halfHeight = orig.getHeight() / 2f;
            result.setAsBox(halfWidth, halfHeight, new Vec2(halfWidth, halfHeight), 0f);
            return result;
        }
        else {
            return null;
        }
    }
}
