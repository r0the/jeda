/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

class ResourceFinder {

    private final List<String> classNames;
    private boolean done;

    ResourceFinder() {
        this.classNames = new ArrayList();
        this.done = false;
    }

    List<String> findClassNames() throws Exception {
        this.ensureResources();
        return this.classNames;
    }

    private void checkDirectory(File directory, String directoryName) {
        for (File file : directory.listFiles()) {
            this.checkResource(file, directoryName);
        }
    }

    private void checkResource(File file, String directoryName) {
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

    private void checkResource(String resourceName) {
        if (resourceName.endsWith(".class") && !resourceName.contains("$")) {
            String className = resourceName.substring(0, resourceName.length() - 6);
            className = className.replace("/", ".");
            this.classNames.add(className);
        }
    }

    private void ensureResources() throws Exception {
        if (!this.done) {
            this.findResources();
            this.done = true;
        }
    }

    private void findResources() throws Exception {
        String[] classPaths = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String classPath : classPaths) {
            this.findResources(classPath);
        }

    }

    private void findResources(String classPath) throws IOException {
        if (classPath.endsWith(".jar")) {
            this.findJarResources(classPath);
        }
        else {
            this.checkDirectory(new File(classPath), "");
        }
    }

    private void findJarResources(String jarFile) throws IOException {
        JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile));
        JarEntry element = jarStream.getNextJarEntry();
        while (element != null) {
            this.checkResource(element.getName());
            element = jarStream.getNextJarEntry();
        }
    }
}
