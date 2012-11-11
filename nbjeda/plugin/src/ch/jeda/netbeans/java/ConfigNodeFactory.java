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
package ch.jeda.netbeans.java;

import ch.jeda.netbeans.Util;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;

@NodeFactory.Registration(projectType = "org-netbeans-modules-java-j2seproject")
public class ConfigNodeFactory implements NodeFactory {

    @Override
    public NodeList createNodes(Project project) {
        if (Util.isJedaProject(project)) {
            try {
                return NodeFactorySupport.fixedNodeList(new ConfigNode(project));
            }
            catch (DataObjectNotFoundException ex) {
                // ignore
            }
        }

        return NodeFactorySupport.fixedNodeList();
    }

    private static class ConfigNode extends FilterNode {

        public ConfigNode(Project project) throws DataObjectNotFoundException {
            super(DataObject.find(project.getProjectDirectory().getFileObject(Util.PROJECT_FILE)).getNodeDelegate());
        }

        @Override
        public String getDisplayName() {
            return "Jeda Configuration";
        }
//        //Next, we add icons, for the default state, which is
//        //closed, and the opened state; we will make them the same. 
//        //Icons in project logical views are
//        //based on combinations--you must combine the node's own icon
//        //with a distinguishing badge that is merged with it. Here we
//        //first obtain the icon from a data folder, then we add our
//        //badge to it by merging it via a NetBeans API utility method:
//        public Image getIcon(int type) {
//            DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
//            Image original = root.getNodeDelegate().getIcon(type);
//            return ImageUtilities.mergeImages(original, smallImage, 7, 7);
//        }
//
//        public Image getOpenedIcon(int type) {
//            DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
//            Image original = root.getNodeDelegate().getIcon(type);
//            return ImageUtilities.mergeImages(original, smallImage, 7, 7);
//        }
    }
}
