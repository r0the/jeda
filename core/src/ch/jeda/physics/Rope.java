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

/**
 * A joint that connects two bodies widh a rope. Both bodies can rotate around their attachment point to the rope.
 *
 * @since 2.2
 */
public final class Rope extends Joint {

    private RopeImp imp;

    /**
     * Constructs a rope that connects the centers of two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Rope(final Body bodyA, final Body bodyB) {
        this(bodyA, bodyB, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Construct a rope that connects two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @param anchorAx the horizontal local coordinate of the connection of the rope to the first body
     * @param anchorAy the vertical local coordinate of the connection of the rope to the first body
     * @param anchorBx the horizontal local coordinate of the connection of the rope to the second body
     * @param anchorBy the vertical local coordinate of the connection of the rope to the second body
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Rope(final Body bodyA, final Body bodyB, final double anchorAx, final double anchorAy,
                final double anchorBx, final double anchorBy) {
        super(bodyA, bodyB);
        imp = new DetachedRopeImp((float) anchorAx, (float) anchorAy, (float) anchorBx, (float) anchorBy);
        bodyA.addJoint(this);
        bodyB.addJoint(this);
    }

    /**
     * Returns the horizontal local coordinate of the connection point between the rope and the first body.
     *
     * @return the horizontal local coordinate of the connection point between the rope and the first body
     *
     * @since 2.2
     */
    public float getAnchorAx() {
        return imp.getAnchorAx();
    }

    /**
     * Returns the vertical local coordinate of the connection point between the rope and the first body.
     *
     * @return the vertical local coordinate of the connection point between the rope and the first body
     *
     * @since 2.2
     */
    public float getAnchorAy() {
        return imp.getAnchorAy();
    }

    /**
     * Returns the horizontal local coordinate of the connection point between the rope and the second body.
     *
     * @return the horizontal local coordinate of the connection point between the rope and the second body
     *
     * @since 2.2
     */
    public float getAnchorBx() {
        return imp.getAnchorBx();
    }

    /**
     * Returns the vertical local coordinate of the connection point between the rope and the second body.
     *
     * @return the vertical local coordinate of the connection point between the rope and the second body
     *
     * @since 2.2
     */
    public float getAnchorBy() {
        return imp.getAnchorBy();
    }

    /**
     * Returns the length of the rope. This is equal to the maximal distance of the two anchor points.
     *
     * @return the length of the rope
     *
     * @since 2.2
     */
    public float getMaxLength() {
        return imp.getMaxLength();
    }

    /**
     * Sets the length of the rope. This is equal to the maximal distance of the two anchor points.
     *
     * @param maxLength the length of the rope
     *
     * @since 2.2
     */
    public void setMaxLength(final double maxLength) {
        imp.setMaxLength((float) maxLength);
    }

    @Override
    boolean setPhysics(final Physics physics) {
        if (imp.belongsTo(physics)) {
            return false;
        }

        final RopeImp oldImp = imp;
        if (physics == null) {
            imp = new DetachedRopeImp(oldImp);
        }
        else {
            imp = new PhysicsRopeImp(physics, this, oldImp);
        }

        oldImp.destroy();
        return true;
    }
}
