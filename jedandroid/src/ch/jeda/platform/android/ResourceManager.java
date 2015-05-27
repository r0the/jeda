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
package ch.jeda.platform.android;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import ch.jeda.Log;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.TypefaceImp;
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

class ResourceManager {

    private static final String HTTP_PREFIX = "http://";
    private static final String NEW_RESOURCE_PREFIX = "res:";
    private final Activity activity;

    ResourceManager(final Activity activity) {
        this.activity = activity;
    }

    Class<?>[] loadClasses() throws Exception {
        final List<Class<?>> result = new ArrayList();
        String apkName = activity.getApplication().getPackageManager().
            getApplicationInfo(activity.getApplication().getPackageName(), 0).sourceDir;

        final DexFile dx = new DexFile(apkName);
        final Enumeration<String> e = dx.entries();
        while (e.hasMoreElements()) {
            final String resourceName = (String) e.nextElement();
            if (!resourceName.contains("$")) {
                try {
                    result.add(ClassLoader.getSystemClassLoader().loadClass(resourceName));
                }
                catch (final ClassNotFoundException ex) {
                    try {
                        result.add(Thread.currentThread().getContextClassLoader().loadClass(resourceName));
                    }
                    catch (final ClassNotFoundException ex2) {
                    }
                }
            }
        }
        return (Class[]) result.toArray(new Class[result.size()]);
    }

    ImageImp openImage(final String path) {
        final InputStream in = openInputStream(path);
        if (in == null) {
            return null;
        }
        try {
            return new AndroidImageImp(BitmapFactory.decodeStream(in));
        }
        catch (final Exception ex) {
            Log.e(ex, "Error while reading image file '", path, "'.");
            return null;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
        }
    }

    TypefaceImp openTypeface(final String path) {
        try {
            if (path.startsWith(NEW_RESOURCE_PREFIX)) {
                return new AndroidTypefaceImp(Typeface.createFromAsset(activity.getResources().getAssets(),
                                                                       path.substring(NEW_RESOURCE_PREFIX.length())));

            }
            else {
                return new AndroidTypefaceImp(Typeface.createFromFile(path));
            }
        }
        catch (final RuntimeException ex) {
            return null;
        }
    }

    InputStream openInputStream(final String path) {
        if (path == null) {
            throw new NullPointerException("filePath");
        }
        else if (path.startsWith(NEW_RESOURCE_PREFIX)) {
            return openResourceInputStream(path, NEW_RESOURCE_PREFIX.length());
        }
        else if (path.startsWith(HTTP_PREFIX)) {
            return openRemoteInputStream(path);
        }
        else {
            return openFileInputStream(path);
        }
    }

    private InputStream openFileInputStream(final String path) {
        try {
            return new FileInputStream(path);
        }
        catch (final FileNotFoundException ex) {
            Log.e(ex, "File '", path, "' not found.");
        }

        return null;
    }

    private InputStream openRemoteInputStream(final String path) {
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

    private InputStream openResourceInputStream(final String path, final int prefixLength) {
        final String resourcePath = "res/" + path.substring(prefixLength);
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        if (url == null) {
            url = Log.class.getClassLoader().getResource(resourcePath);
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
}
