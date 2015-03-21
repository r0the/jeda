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
package ch.jeda.netbeans.java;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.queries.JavadocForBinaryQuery;
import org.netbeans.api.java.queries.JavadocForBinaryQuery.Result;
import org.netbeans.spi.java.queries.JavadocForBinaryQueryImplementation;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = JavadocForBinaryQueryImplementation.class, position = 0)
public class JedaJavadocForBinaryQuery implements JavadocForBinaryQueryImplementation {

    private static JedaJavadocResult RESULT = new JedaJavadocResult();

    @Override
    public Result findJavadoc(final URL binaryRoot) {
        if (binaryRoot.toExternalForm().contains("jeda.jar")) {
            return RESULT;
        }
        else {
            return null;
        }
    }

    private static class JedaJavadocResult implements JavadocForBinaryQuery.Result {

        private final URL[] roots;

        public JedaJavadocResult() {
            this.roots = new URL[2];
            try {
                this.roots[0] = new URL("http://www.jeda.ch/api/");
                this.roots[1] = new URL("http://jeda.ch/api/");
            }
            catch (final MalformedURLException ex) {
                // ignore
            }
        }

        @Override
        public void addChangeListener(final ChangeListener l) {
        }

        @Override
        public void removeChangeListener(final ChangeListener l) {
        }

        @Override
        public URL[] getRoots() {
            return Arrays.copyOf(this.roots, this.roots.length);
        }
    }
}
