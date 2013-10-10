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
package ch.jeda.netbeans;

import java.io.File;
import java.io.IOException;

public class AndroidCommand {

    private static final File WIN_64_PATH = new File("c:/program files (x86)/Android/android-sdk/tools/android.bat");
    private static final File WIN_32_PATH = new File("c:/program files/Android/android-sdk/tools/android.bat");
    private static final String DEFAULT_PATH = "android";

    public static void updateProject(final String projectDir) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(androidExecutablePath(), "update", "project", "-p", projectDir);
        final Process p = pb.start();
        try {
            p.waitFor();
        }
        catch (InterruptedException ex) {
            // ignore
        }
    }

    private static String androidExecutablePath() {
        if (WIN_32_PATH.exists()) {
            return WIN_32_PATH.getAbsolutePath();
        }
        else if (WIN_64_PATH.exists()) {
            return WIN_64_PATH.getAbsolutePath();
        }
        else {
            return DEFAULT_PATH;
        }
    }
}
