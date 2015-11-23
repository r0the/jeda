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

class DetachedRodImp extends DetachedJointImp implements RodImp {

    private float length;

    DetachedRodImp(final RodImp oldImp) {
        super(oldImp);
        this.length = oldImp.getLength();
    }

    DetachedRodImp(float anchorAx, float anchorAy, float anchorBx, float anchorBy) {
        super(anchorAx, anchorAy, anchorBx, anchorBy);
        this.length = -1f;
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public void setLength(float length) {
        this.length = length;
    }

}
