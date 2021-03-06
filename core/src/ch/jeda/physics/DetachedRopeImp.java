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

class DetachedRopeImp extends DetachedJointImp implements RopeImp {

    private float maxLength;

    DetachedRopeImp(final RopeImp oldImp) {
        super(oldImp);
        this.maxLength = oldImp.getMaxLength();
    }

    DetachedRopeImp(float anchorAx, float anchorAy, float anchorBx, float anchorBy) {
        super(anchorAx, anchorAy, anchorBx, anchorBy);
        this.maxLength = 0f;
    }

    @Override
    public float getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(final float maxLength) {
        this.maxLength = maxLength;
    }
}
