/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.event;

public class TurnEvent extends Event {

    private final float amount;
    private final TurnAxis axis;

    public TurnEvent(final Object source, final float amount, final TurnAxis axis) {
        super(source, EventType.TURN);
        this.amount = amount;
        this.axis = axis;
    }

    public float getAmount() {
        return this.amount;
    }

    public TurnAxis getAxis() {
        return this.axis;
    }
}
