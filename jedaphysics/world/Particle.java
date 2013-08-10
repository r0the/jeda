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

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.dynamics.BodyType;

public class Particle extends PhysicsObject {

    private final MassData massData;
    private Color color;

    public Particle() {
        this.bodyDef.type = BodyType.DYNAMIC;
        this.massData = new MassData();
        this.massData.mass = 1f;
        this.color = Color.BLACK;
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.setColor(this.color);
        canvas.fillRectangle(this.getX() - 1, this.getY() - 1, 2, 2);
    }

    public final Color getColor() {
        return this.color;
    }

    public final void setColor(final Color color) {
        this.color = color;
    }

    public final void setMass(final float m) {
        this.massData.mass = m;
        if (this.body != null) {
            this.body.setMassData(this.massData);
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.getClass().getName());
        result.append("(x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(")");
        return result.toString();
    }
}
