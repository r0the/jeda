/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
package ch.jeda.platform.java;

import ch.jeda.Jeda;

/**
 * This class is the main entry point for Jeda applications on the Java platform. Be sure to specify this class as main
 * class.
 *
 * @since 1.0
 */
public class Main {

    /**
     * Entry point for Jeda framework.
     *
     * @param args the arguments
     *
     * @since 1.0
     */
    public static void main(final String[] args) {
        Jeda.startProgram();
    }
}
