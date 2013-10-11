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
package ch.jeda.platform.java;

import ch.jeda.IO;
import ch.jeda.platform.ImageImp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.imageio.ImageIO;

class Resources {

    private static final String HTTP_PREFIX = "http://";
    private static final String NEW_RESOURCE_PREFIX = "res:";
    private static final String OLD_RESOURCE_PREFIX = ":";
    private static final ResourceFinder RESOURCE_FINDER = new ResourceFinder();

    static Class<?>[] loadClasses() throws Exception {
        return RESOURCE_FINDER.loadClasses();
    }

    static ImageImp openImage(final String filePath) {
        final InputStream in = openInputStream(filePath);
        if (in == null) {
            return null;
        }

        try {
            return new JavaImageImp(ImageIO.read(in));
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

    private static class ResourceFinder {

        private final Set<Class<?>> classesSet;
        private Class<?>[] classes;

        ResourceFinder() {
            this.classesSet = new HashSet<Class<?>>();
        }

        Class<?>[] loadClasses() throws Exception {
            if (this.classes == null) {
                findResources();
            }

            return this.classes;
        }

        private void checkDirectory(final File directory, final String directoryName) {
            for (File file : directory.listFiles()) {
                this.checkResource(file, directoryName);
            }
        }

        private void checkResource(final File file, final String directoryName) {
            if (!file.exists()) {
                return;
            }

            String fullName = file.getName();
            if (!directoryName.isEmpty()) {
                fullName = directoryName + '/' + fullName;
            }

            if (file.isDirectory()) {
                this.checkDirectory(file, fullName);
            }
            else {
                this.checkResource(fullName);
            }
        }

        private void checkResource(final String resourceName) {
            // Collect only global classes.
            // Inner classes have a $ in their name.
            if (resourceName.endsWith(".class") && !resourceName.contains("$")) {
                // Remove ".class" from the name
                String className = resourceName.substring(0, resourceName.length() - 6);
                // Convert path to packages
                className = className.replace("/", ".");
                try {
                    // Try to load class with system class loader
                    this.classesSet.add(ClassLoader.getSystemClassLoader().loadClass(className));
                }
                catch (ClassNotFoundException ex) {
                    try {
                        // Try to load class with class loader of current context
                        this.classesSet.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    }
                    catch (ClassNotFoundException ex2) {
                        // Ignore
                    }
                }
            }
        }

        private void findResources() throws Exception {
            this.findResources(getClass().getProtectionDomain().getCodeSource().getLocation());
            final String[] classPaths = System.getProperty("java.class.path").split(File.pathSeparator);
            for (String classPath : classPaths) {
                this.findResources(classPath);
            }

            this.classes = this.classesSet.toArray(new Class<?>[this.classesSet.size()]);
        }

        private void findResources(final String classPath) throws Exception {
            if (classPath.endsWith(".jar")) {
                try {
                    this.findJarResources(new URL(classPath).openStream());
                }
                catch (Exception ex) {
                    // No URL, so probably a file name.
                    this.findJarResources(new FileInputStream(classPath));
                }
            }
            else {
                this.checkDirectory(new File(classPath), "");
            }
        }

        private void findResources(final URL url) throws IOException {
            if (url.getFile().endsWith(".jar")) {
                this.findJarResources(url.openStream());
            }
        }

        private void findJarResources(final InputStream in) throws IOException {
            final JarInputStream jarStream = new JarInputStream(in);
            JarEntry element = jarStream.getNextJarEntry();
            while (element != null) {
                this.checkResource(element.getName());
                element = jarStream.getNextJarEntry();
            }
        }
    }
}
