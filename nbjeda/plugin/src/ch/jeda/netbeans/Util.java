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

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.openide.util.ImageUtilities;

public class Util {

    public static final String PROJECT_FILE = "jeda.properties";
    private static final String ICON = "ch/jeda/netbeans/resources/logo-16x16.png";
    private static Image JEDA_IMAGE;
    private static Icon JEDA_ICON;

    public static Icon getJedaIcon() {
        if (JEDA_ICON == null) {
            JEDA_ICON = new ImageIcon(getJedaImage());
        }

        return JEDA_ICON;
    }

    public static Image getJedaImage() {
        if (JEDA_IMAGE == null) {
            JEDA_IMAGE = ImageUtilities.loadImage(ICON);
        }

        return JEDA_IMAGE;
    }

    public static boolean isJedaProject(Project project) {
        return project.getProjectDirectory().getFileObject(PROJECT_FILE) != null;
    }

    public static void log(String message) {
        System.out.println("NBJeda: " + message);
    }
}
