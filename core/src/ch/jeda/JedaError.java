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
 * <b>Internal</b>. Do not use this class.
 */
public final class JedaError extends RuntimeException {

    public static final int XML_READER_CREATION_FAILED = 1000;

    private final int type;

    public JedaError(final int type, String message) {
        super(message);
        this.type = type;
    }

    public JedaError(final int type, Throwable cause) {
        super(lookupMessage(type), cause);
        this.type = type;
    }

    private static String lookupMessage(int type) {
        switch (type) {
            case XML_READER_CREATION_FAILED:
                return "Internal error while creating XML reader.";
            default:
                return "Unknown internal error.";
        }
    }
}
