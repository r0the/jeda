/*
 * Copyright (C) 2014 by Stefan Rothe
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
 * Represents the state of a media playback.
 *
 * @since 1.0
 */
public enum PlaybackState {

    /**
     * The playback has been paused.
     *
     * @since 1.0
     */
    PAUSED,
    /**
     * The playback is currently in progress.
     *
     * @since 1.0
     */
    PLAYING,
    /**
     * The playback is not in progress. It has either not started yet or has already finished.
     *
     * @since 1.0
     */
    STOPPED
}
