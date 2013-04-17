/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import ch.jeda.geometry.Collision;
import ch.jeda.geometry.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ContactGenerator {

    private final List<Contact> contacts;

    protected ContactGenerator() {
        this.contacts = new ArrayList<Contact>();
    }

    public final Collection<Contact> detect(World world) {
        this.contacts.clear();
        this.doDetect(world);
        return contacts;
    }

    protected abstract void doDetect(World world);

    protected final void checkParticles(Entity e1, Entity e2) {
        Shape s1 = e1.getCollisionShape();
        Shape s2 = e2.getCollisionShape();
        if (s1 == null || s2 == null) {
            return;
        }

        Collision c = s1.collideWith(s2);
        if (c.isNull()) {
            return;
        }

        this.contacts.add(new Contact(e1, e2, c));
    }
}
