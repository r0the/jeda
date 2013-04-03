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

import ch.jeda.Vector;
import ch.jeda.geometry.Collision;

public class Contact {

    private final Entity entity1;
    private final Entity entity2;
    private final Vector contactNormal;
    private double penetration;
    private Vector movement1;
    private Vector movement2;

    public Contact(Entity entity1, Entity entity2, Collision collision) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.contactNormal = collision.normal;
        this.contactNormal.normalize();
        this.penetration = collision.normal.length();
    }

    public double getPenetration() {
        return this.penetration;
    }

    public void updatePenetration(Contact contact) {
        if (this.entity1 == contact.entity1) {
            this.penetration -= contact.movement1.dot(this.contactNormal);
        }
        if (this.entity1 == contact.entity2) {
            this.penetration -= contact.movement2.dot(this.contactNormal);
        }
        if (this.entity2 == contact.entity1) {
            this.penetration += contact.movement1.dot(this.contactNormal);
        }
        if (this.entity2 == contact.entity2) {
            this.penetration += contact.movement2.dot(this.contactNormal);
        }
    }
}
