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

abstract class DetachedJointImp implements JointImp {

    private final float anchorAx;
    private final float anchorAy;
    private final float anchorBx;
    private final float anchorBy;

    DetachedJointImp(final JointImp oldImp) {
        this.anchorAx = oldImp.getAnchorAx();
        this.anchorAy = oldImp.getAnchorAy();
        this.anchorBx = oldImp.getAnchorBx();
        this.anchorBy = oldImp.getAnchorBy();
    }

    DetachedJointImp(float anchorAx, float anchorAy, float anchorBx, float anchorBy) {
        this.anchorAx = anchorAx;
        this.anchorAy = anchorAy;
        this.anchorBx = anchorBx;
        this.anchorBy = anchorBy;
    }

    @Override
    public final boolean belongsTo(final Physics physics) {
        return false;
    }

    @Override
    public final void destroy() {
        // ignore
    }

    @Override
    public final float getAnchorAx() {
        return anchorAx;
    }

    @Override
    public final float getAnchorAy() {
        return anchorAy;
    }

    @Override
    public final float getAnchorBx() {
        return anchorBx;
    }

    @Override
    public final float getAnchorBy() {
        return anchorBy;
    }
}
