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
package ch.jeda.netbeans.support;

public class Version implements Comparable<Version> {

    public final int major;
    public final int minor;
    public final int release;

    public Version(final Object version) {
        final int[] parts = split(version);
        if (parts == null) {
            major = 0;
            minor = 0;
            release = 0;
        }
        else {
            switch (parts.length) {
                case 0:
                    major = 0;
                    minor = 0;
                    release = 0;
                    break;
                case 1:
                    major = parts[0];
                    minor = 0;
                    release = 0;
                    break;
                case 2:
                    major = parts[0];
                    minor = parts[1];
                    release = 0;
                    break;
                default:
                    major = parts[0];
                    minor = parts[1];
                    release = parts[2];
                    break;
            }
        }
    }

    public Version(final int major, final int minor, final int release) {
        this.major = major;
        this.minor = minor;
        this.release = release;
    }

    @Override
    public int compareTo(final Version other) {
        int result = this.major - other.major;
        if (result == 0) {
            result = this.minor - other.minor;
        }

        if (result == 0) {
            result = this.release - other.release;
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.major);
        result.append('.');
        result.append(this.minor);
        result.append('.');
        result.append(this.release);
        return result.toString();
    }

    private static int[] split(final Object version) {
        if (version == null) {
            return null;
        }

        final String text = version.toString();
        if (text == null) {
            return null;
        }

        final String[] parts = text.split("\\.");
        final int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            try {
                result[i] = Integer.parseInt(parts[i]);
            }
            catch (final NumberFormatException ex) {
                result[i] = -1;
            }
        }

        return result;
    }

}
