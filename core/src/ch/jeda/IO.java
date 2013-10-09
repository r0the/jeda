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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Internal</b>. Do not use this class.
 * <p>
 * Provides a low-level input/output interface for Jeda.
 */
public class IO {

    private static final String HTTP_PREFIX = "http://";
    private static final String NEW_RESOURCE_PREFIX = "res:";
    private static final String OLD_RESOURCE_PREFIX = ":";

    public static void err(final String messageKey, Object... args) {
        System.err.format(Helper.getMessage(messageKey), args);
        System.err.println();
        System.err.flush();
    }

    public static void err(final Throwable throwable, final String messageKey, Object... args) {
        System.err.format(Helper.getMessage(messageKey), args);
        System.err.println();
        if (throwable != null) {
            System.err.println("  " + throwable);
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < stackTrace.length; ++i) {
                System.err.println("   " + stackTrace[i].toString());
            }
        }

        System.err.flush();
    }

    static String[] loadTextFile(final String filePath) {
        final InputStream in = openInputStream(filePath);
        if (in == null) {
            return null;
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            final List<String> result = new ArrayList<String>();
            while (reader.ready()) {
                result.add(reader.readLine());
            }

            return result.toArray(new String[result.size()]);
        }
        catch (final IOException ex) {
            err("jeda.file.error.read", filePath, ex);
            return null;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                // ignore
            }
        }
    }

    static InputStream openInputStream(final String filePath) {
        if (filePath == null) {
            throw new NullPointerException("filePath");
        }

        if (filePath.startsWith(NEW_RESOURCE_PREFIX)) {
            return openResourceInputStream(filePath, NEW_RESOURCE_PREFIX.length());
        }
        else if (filePath.startsWith(OLD_RESOURCE_PREFIX)) {
            return openResourceInputStream(filePath, OLD_RESOURCE_PREFIX.length());
        }
        else if (filePath.startsWith(HTTP_PREFIX)) {
            return openRemoteInputStream(filePath);
        }
        else {
            return openFileInputStream(filePath);
        }
    }

    private static InputStream openFileInputStream(final String filePath) {
        try {
            return new FileInputStream(filePath);
        }
        catch (final FileNotFoundException ex) {
            err("jeda.file.error.not-found", filePath);
            return null;
        }
    }

    private static InputStream openRemoteInputStream(final String filePath) {
        try {
            return new URL(filePath).openStream();
        }
        catch (final MalformedURLException ex) {
            err("jeda.file.error.open", filePath);
            return null;
        }
        catch (final IOException ex) {
            err("jeda.file.error.open", filePath);
            return null;
        }

    }

    private static InputStream openResourceInputStream(final String filePath, final int prefixLength) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(filePath.substring(prefixLength));
        if (url == null) {
            url = IO.class.getClassLoader().getResource(filePath.substring(prefixLength));
        }

        if (url == null) {
            err("jeda.file.error.not-found", filePath);
            return null;
        }
        else {
            try {
                return url.openStream();
            }
            catch (final IOException ex) {
                err("jeda.file.error.open", filePath);
                return null;
            }
        }
    }
}
