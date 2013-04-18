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
package ch.jeda;

/**
 * Represents the state of a Jeda Program.
 */
public enum ProgramState {

    /**
     * The program is being created. A Jeda {@link Program} has this state while
     * being created, that is, while the constructor is executed.
     *
     * @since 1
     */
    Creating,
    /**
     * The program is running. A Jeda {@link Program} is in this state while it
     * is being executed. An implementation should perform it's operations while
     * the program is in this state.
     * <p>
     * For example, a Jeda {@link Simulation} repeadedly calls the
     * {@link Simulation#step()} method while in this state.
     *
     * @since 1
     */
    Running,
    /**
     * The program is paused. This state indicates that a Jeda {@link Program}
     * should temporaryly cease to execute but be ready to resume execution at a
     * later time when it's state returns to {@link #Running}.
     * <p>
     * For example, a Jeda {@link Simulation} ceases to call
     * {@link  Simulation#step()} while in this state.
     *
     * @since 1
     */
    Paused,
    /**
     * The program is stopped. This state indicates that the Jeda
     * {@link Program} has beend requested to stop and should return from the
     * {@link Program#run()} method as soon as possible. This state is also
     * reached if a program returns from the {@link Program#run()} method on its
     * own.
     * <p>
     * For example, a Jeda {@link Simulation} returns from the
     * {@link Simulation#run()} method as soon as a possibly ongoing call to
     * {@link Simulation#step()} has returned.
     *
     * @since 1
     */
    Stopped
}
