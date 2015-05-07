/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.jeda.netbeans.support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Bugtracking",
    id = "ch.jeda.netbeans.support.SomeAction"
)
@ActionRegistration(
    displayName = "#CTL_SomeAction"
)
@ActionReference(path = "Menu/File", position = 0)
@Messages("CTL_SomeAction=TEST")
public final class SomeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
