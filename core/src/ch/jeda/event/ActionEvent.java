/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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

import ch.jeda.ui.Widget;

/**
 * @since 1.0
 * @version 3
 */
public final class ActionEvent extends Event {

    private final int id;
    private final Widget widget;

    /**
     * Constructs an action event.
     *
     * @param widget the widget that caused this action.
     * @param id the id of this action
     *
     * @since 1.0
     */
    public ActionEvent(final Widget widget, final int id) {
        super(widget, EventType.ACTION);
        this.id = id;
        this.widget = widget;
    }

    /**
     * Returns the id of this action.
     *
     * @return the id of this action
     *
     * @since 2.1
     */
    public int getId() {
        return id;
    }

    /**
     * @deprecated Use {@link #getId()} to determine the kind of action.
     */
    public String getName() {
        return widget.getName();
    }

    /**
     * Returns the widget that caused this action.
     *
     * @return the widget that caused this action
     *
     * @since 2.0
     */
    public Widget getWidget() {
        return widget;
    }
}
