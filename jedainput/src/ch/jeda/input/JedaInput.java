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
package ch.jeda.input;

import ch.jeda.Plugin;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import net.java.games.input.*;

public class JedaInput implements Plugin {

    public JavaGamepad[] getControllers() {
        final PrintStream oldErr = System.err;
        try {
            final PrintStream nul = new PrintStream(new ByteArrayOutputStream());
            System.setErr(nul);
            // Outputs nasty stuff
            final Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            final JavaGamepad[] result = new JavaGamepad[controllers.length];
            for (int i = 0; i < controllers.length; ++i) {
                result[i] = new JavaGamepad(controllers[i]);
            }

            return result;
        }
        finally {
            System.setErr(oldErr);
        }
    }

    @Override
    public void initialize() throws Exception {
        loadNativeLibrary("jinput-dx8.dll");
        loadNativeLibrary("jinput-dx8_64.dll");
        loadNativeLibrary("jinput-raw_64.dll");
        loadNativeLibrary("jinput-raw.dll");
        loadNativeLibrary("jinput-wintab.dll");
        loadNativeLibrary("libjinput-linux.so");
        loadNativeLibrary("libjinput-linux64.so");
        loadNativeLibrary("libjinput-osx.jnilib");
    }

    private static void loadNativeLibrary(final String libraryName) {
        final InputStream inputStream = JedaInput.class.getClassLoader().getResourceAsStream(libraryName);
        try {
            final String tempDir = System.getProperty("java.io.tmpdir");
            System.setProperty("net.java.games.input.librarypath", tempDir);
            final File libraryFile = new File(tempDir, libraryName);
            libraryFile.deleteOnExit();
            try (final FileOutputStream fileOutputStream = new FileOutputStream(libraryFile)) {
                final byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            inputStream.close();
        }
        catch (final Exception ex) {
            //ex.printStackTrace();
        }
    }
}
