/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

class PhysicsContactListener implements ContactListener {

    @Override
    public void beginContact(final Contact contact) {
        final Fixture fixA = contact.m_fixtureA;
        final Fixture fixB = contact.m_fixtureB;
        if (fixA.m_isSensor) {
            final Sensor sensor = (Sensor) fixA.m_userData;
            if (!fixB.m_isSensor) {
                sensor.internalBeginContact(fixB);
            }
        }
        else if (fixB.m_isSensor) {
            final Sensor sensor = (Sensor) fixB.m_userData;
            if (!fixA.m_isSensor) {
                sensor.internalBeginContact(fixA);
            }
        }
        else {
            final Body bodyA = (Body) fixA.m_body.m_userData;
            final Body bodyB = (Body) fixB.m_body.m_userData;
            if (bodyA != null && bodyB != null) {
                bodyA.internalBeginContact(bodyB);
                bodyB.internalBeginContact(bodyA);
            }
        }
    }

    @Override
    public void endContact(final Contact contact) {
        final Fixture fixA = contact.m_fixtureA;
        final Fixture fixB = contact.m_fixtureB;
        if (fixA.m_isSensor) {
            final Sensor sensor = (Sensor) fixA.m_userData;
            if (!fixB.m_isSensor) {
                sensor.internalEndContact(fixB);
            }
        }
        else if (fixB.m_isSensor) {
            final Sensor sensor = (Sensor) fixB.m_userData;
            if (!fixA.m_isSensor) {
                sensor.internalEndContact(fixA);
            }
        }
        else {
            final Body bodyA = (Body) fixA.m_body.m_userData;
            final Body bodyB = (Body) fixB.m_body.m_userData;
            if (bodyA != null && bodyB != null) {
                bodyA.internalEndContact(bodyB);
                bodyB.internalEndContact(bodyA);
            }
        }
    }

    @Override
    public void preSolve(final Contact contact, final Manifold manifold) {
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {
    }
}
