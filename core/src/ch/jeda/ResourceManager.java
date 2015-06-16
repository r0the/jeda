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
package ch.jeda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

class ResourceManager {

    private static final String HTTP_PREFIX = "http://";
    private static final String RESOURCE_PREFIX = "res:";

    private ResourceManager() {
    }

    static InputStream openInputStream(final String path) {
        if (path == null) {
            throw new NullPointerException("path");
        }
        else if (path.startsWith(RESOURCE_PREFIX)) {
            return openResourceInputStream(path, RESOURCE_PREFIX.length());
        }
        else if (path.startsWith(HTTP_PREFIX)) {
            return openRemoteInputStream(path);
        }
        else {
            return openFileInputStream(path);
        }
    }

    private static InputStream openFileInputStream(final String path) {
        try {
            return new FileInputStream(path);
        }
        catch (final FileNotFoundException ex) {
            Log.e(ex, "File '", path, "' not found.");
        }

        return null;
    }

    private static InputStream openRemoteInputStream(final String path) {
        try {
            return new URL(path).openStream();
        }
        catch (final MalformedURLException ex) {
            Log.e(ex, "Cannot open invalid path '", path, "'.");
            return null;
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading remote file '", path, "'.");
        }
        return null;
    }

    private static InputStream openResourceInputStream(final String path, final int prefixLength) {
        final String resourcePath = path.substring(prefixLength);
        URL url = findResource("res/" + resourcePath);
        if (url == null) {
            url = findResource(resourcePath);
        }

        if (url == null) {
            Log.e("Resource file '", path, "' not found.");
            return null;
        }

        try {
            return url.openStream();
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading resource file '", path, "'.");
        }

        return null;
    }

    private static URL findResource(final String resourcePath) {
        final URL result = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        if (result == null) {
            return Log.class.getClassLoader().getResource(resourcePath);
        }
        else {
            return result;
        }
    }
}
