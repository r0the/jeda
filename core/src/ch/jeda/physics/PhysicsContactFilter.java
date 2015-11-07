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

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.dynamics.Fixture;

class PhysicsContactFilter extends ContactFilter {

    @Override
    public boolean shouldCollide(final Fixture fixtureA, final Fixture fixtureB) {
        if (fixtureA.m_isSensor || fixtureB.m_isSensor) {
            return true;
        }
        else {
            final Body bodyA = (Body) fixtureA.m_body.m_userData;
            final Body bodyB = (Body) fixtureB.m_body.m_userData;
            return bodyA.shouldCollide(bodyB) && bodyB.shouldCollide(bodyA);
        }
    }
}
