/*
 * Copyright (C) 2012 by Stefan Rothe
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
package ch.jeda.netbeans;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Lookup;

public class Util {

    public static void closeProject(Project project) {
        OpenProjects.getDefault().close(new Project[]{project});
    }

    public static void log(String message) {
        Logger.getLogger("NbJeda").log(Level.INFO, message);
    }

    public static InputStream openResource(String resourcePath) throws IOException {
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        URL url = cl.getResource(resourcePath);
        return url.openStream();
    }
}
