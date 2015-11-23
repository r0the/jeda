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
package ch.jeda.netbeans.java;

import ch.jeda.netbeans.support.JavadocResult;
import java.net.URL;
import org.netbeans.api.java.queries.JavadocForBinaryQuery.Result;
import org.netbeans.spi.java.queries.JavadocForBinaryQueryImplementation;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = JavadocForBinaryQueryImplementation.class, position = 0)
public class JedaJavadocQuery implements JavadocForBinaryQueryImplementation {

    private static final Result RESULT = new JavadocResult(
        "http://www.jeda.ch/api/",
        "http://jeda.ch/api/"
    );

    @Override
    public Result findJavadoc(final URL binaryRoot) {
        if (binaryRoot.toExternalForm().contains("jeda.jar")) {
            return RESULT;
        }
        else {
            return null;
        }
    }
}
