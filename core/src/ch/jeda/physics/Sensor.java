/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.physics;

import ch.jeda.geometry.Shape;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jbox2d.dynamics.Fixture;

public final class Sensor {

    private final Body body;
    private final Set<Fixture> contactFixtures;
    private final String name;
    private final Shape shape;
    private Body contacts[];

    public Sensor(final Body body, final Shape shape) {
        this(body, shape, null);
    }

    public Sensor(final Body body, final Shape shape, final String name) {
        if (body == null) {
            throw new NullPointerException("body");
        }

        if (shape == null) {
            throw new NullPointerException("shape");
        }

        this.body = body;
        contactFixtures = new HashSet<Fixture>();
        contacts = new Body[0];
        this.name = name;
        this.shape = shape;
        body.addSensor(this);
    }

    public Body getBody() {
        return body;
    }

    public Body[] getContacts() {
        return Arrays.copyOf(contacts, contacts.length);
    }

    public Shape getShape() {
        return shape;
    }

    public boolean hasContact() {
        return contacts.length > 0;
    }

    void internalBeginContact(final Fixture fixture) {
        final Body contactBody = (Body) fixture.m_body.m_userData;
        if (name == null || name.equals(contactBody.getName())) {
            if (contactFixtures.add(fixture)) {
                updateContacts();
            }
        }
    }

    void internalEndContact(final Fixture fixture) {
        if (contactFixtures.remove(fixture)) {
            updateContacts();
        }
    }

    private void updateContacts() {
        final Set<Body> contactBodies = new HashSet<Body>();
        for (Fixture fixture : contactFixtures) {
            contactBodies.add((Body) fixture.m_body.m_userData);
        }

        contacts = contactBodies.toArray(new Body[contactBodies.size()]);
    }
}
