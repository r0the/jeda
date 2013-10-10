/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.jeda.netbeans;

import org.netbeans.spi.project.ui.ProjectOpenedHook;

public class JedaProjectOpenedHook extends ProjectOpenedHook {

    private final ProjectWrapper projectWrapper;

    JedaProjectOpenedHook(final ProjectWrapper projectWrapper) {
        this.projectWrapper = projectWrapper;
    }

    @Override
    protected void projectClosed() {
    }

    @Override
    protected void projectOpened() {
        this.projectWrapper.onOpen();
    }
}
