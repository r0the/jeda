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
package ch.jeda.platform.android;

import android.app.Application;
import android.graphics.BitmapFactory;
import ch.jeda.IO;
import ch.jeda.platform.ImageImp;
import dalvik.system.DexFile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class Resources {

    private static final String HTTP_PREFIX = "http://";
    private static final String NEW_RESOURCE_PREFIX = "res:";
    private static final String OLD_RESOURCE_PREFIX = ":";

    static Class<?>[] loadClasses(final Application application) throws Exception {
        final List<Class<?>> result = new ArrayList<Class<?>>();
        final String apkName = application.getPackageManager().
            getApplicationInfo(application.getPackageName(), 0).sourceDir;
        final DexFile dx = new DexFile(apkName);
        final Enumeration<String> e = dx.entries();
        while (e.hasMoreElements()) {
            final String resourceName = e.nextElement();
            if (!resourceName.contains("$")) {
                try {
                    // Try to load class with system class loader
                    result.add(ClassLoader.getSystemClassLoader().loadClass(resourceName));
                }
                catch (ClassNotFoundException ex) {
                    try {
                        // Try to load class with class loader of current context
                        result.add(Thread.currentThread().getContextClassLoader().loadClass(resourceName));
                    }
                    catch (ClassNotFoundException ex2) {
                        // Ignore
                    }
                }
            }
        }

        return result.toArray(new Class<?>[result.size()]);
    }

    static ImageImp openImage(final String filePath) {
        final InputStream in = openInputStream(filePath);
        if (in == null) {
            return null;
        }

        try {
            return new AndroidImageImp(BitmapFactory.decodeStream(in));
        }
        catch (final Exception ex) {
            IO.err(ex, "jeda.image.error.read", filePath);
            return null;
        }
        finally {
            try {
                in.close();
            }
            catch (final IOException ex) {
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
            IO.err(ex, "jeda.file.error.not-found", filePath);
            return null;
        }
    }

    private static InputStream openRemoteInputStream(final String filePath) {
        try {
            return new URL(filePath).openStream();
        }
        catch (final MalformedURLException ex) {
            IO.err(ex, "jeda.file.error.open", filePath);
            return null;
        }
        catch (final IOException ex) {
            IO.err(ex, "jeda.file.error.open", filePath);
            return null;
        }

    }

    private static InputStream openResourceInputStream(final String filePath, final int prefixLength) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(filePath.substring(prefixLength));
        if (url == null) {
            url = IO.class.getClassLoader().getResource(filePath.substring(prefixLength));
        }

        if (url == null) {
            IO.err("jeda.file.error.not-found", filePath);
            return null;
        }
        else {
            try {
                return url.openStream();
            }
            catch (final IOException ex) {
                IO.err(ex, "jeda.file.error.open", filePath);
                return null;
            }
        }
    }
}
