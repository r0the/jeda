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

public final class ProjectPropertiesFilter extends TextFileFilter {

    private final String projectName;

    public ProjectPropertiesFilter(final String projectName) {
        this.projectName = projectName;
    }

    @Override
    protected String filterLine(final String line) {
        if (line.startsWith("application.title=")) {
            return "application.title=" + this.projectName;
        }
        else if (line.startsWith("dist.jar=")) {
            return "dist.jar=${dist.dir}/" + this.projectName + ".jar";
        }
        else {
            return line;
        }
    }
}
