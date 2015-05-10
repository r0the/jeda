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
import org.jbox2d.dynamics.contacts.Contact;

class PhysicsContactListener implements ContactListener {

    @Override
    public void beginContact(final Contact contact) {
        final org.jbox2d.dynamics.Body jbBodyA = contact.m_fixtureA.m_body;
        final org.jbox2d.dynamics.Body jbBodyB = contact.m_fixtureB.m_body;
        final Body bodyA = (Body) jbBodyA.m_userData;
        final Body bodyB = (Body) jbBodyB.m_userData;
        if (bodyA != null && bodyB != null) {
            bodyA.internalBeginContact(bodyB);
            bodyB.internalBeginContact(bodyA);
        }
//        if (bodyA.isDestructible() || bodyB.isDestructible()) {
//            final Vec2 va = jbBodyA.getLinearVelocity();
//            final Vec2 vb = jbBodyB.getLinearVelocity();
//            final double rvx = va.x - vb.x;
//            final double rvy = va.y - vb.y;
//            final double rv = Math.sqrt(rvx * rvx + rvy * rvy);
//            final double pa = rv * jbBodyB.m_mass;
//            final double pb = rv * jbBodyA.m_mass;
//            if (bodyA.isDestructible() && bodyA.getFatigueThreshold() <= pa) {
//                bodyA.fatigue += pa;
//            }
//
//            if (bodyB.isDestructible() && bodyB.getFatigueThreshold() <= pb) {
//                bodyB.fatigue += pb;
//            }
//        }
    }

    @Override
    public void endContact(final Contact contact) {
        final Body bodyA = (Body) contact.m_fixtureA.m_body.m_userData;
        final Body bodyB = (Body) contact.m_fixtureB.m_body.m_userData;
        bodyA.internalEndContact(bodyB);
        bodyB.internalEndContact(bodyA);
    }

    @Override
    public void preSolve(final Contact contact, final Manifold manifold) {
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {
//        final Body bodyA = (Body) contact.m_fixtureA.m_body.m_userData;
//        final Body bodyB = (Body) contact.m_fixtureB.m_body.m_userData;
//        final double i = impulse.normalImpulses[0];
//        if (impulse.normalImpulses.length > 1) {
//            System.out.println("LENGTH: " + impulse.normalImpulses.length);
//        }
//
//        if (i >= bodyA.fatigueThreshold) {
//            bodyA.fatigue += i;
//        }
//
//        if (i >= bodyB.fatigueThreshold) {
//            bodyB.fatigue += i;
//        }
    }
}
