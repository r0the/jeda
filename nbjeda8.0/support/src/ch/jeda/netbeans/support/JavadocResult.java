/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.netbeans.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.queries.JavadocForBinaryQuery;
import org.openide.util.Exceptions;

public final class JavadocResult implements JavadocForBinaryQuery.Result {

    private final URL[] roots;

    public JavadocResult(String... urls) {
        this.roots = new URL[urls.length];
        for (int i = 0; i < urls.length; ++i) {
            try {
                this.roots[0] = new URL(urls[i]);
            }
            catch (final MalformedURLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void addChangeListener(final ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(final ChangeListener listener) {
    }

    @Override
    public URL[] getRoots() {
        return Arrays.copyOf(this.roots, this.roots.length);
    }
}
