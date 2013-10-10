/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;

@NodeFactory.Registration(projectType = {
    "org-netbeans-modules-java-j2seproject",
    "org-netbeans-modules-android-project"
})
public class ConfigNodeFactory implements NodeFactory {

    @Override
    public NodeList createNodes(final Project project) {
        ProjectWrapper wrapper = ProjectWrapper.forProject(project);
        if (wrapper.isJedaProject()) {
            try {
                return NodeFactorySupport.fixedNodeList(new ConfigNode(wrapper));
            }
            catch (final DataObjectNotFoundException ex) {
                // ignore
            }
        }

        return NodeFactorySupport.fixedNodeList();
    }

    private static class ConfigNode extends FilterNode {

        public ConfigNode(final ProjectWrapper wrapper) throws DataObjectNotFoundException {
            super(DataObject.find(wrapper.getJedaPropertiesFile()).getNodeDelegate());
        }

        @Override
        public String getDisplayName() {
            return "Jeda Configuration";
        }
    }
}
