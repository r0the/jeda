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
package ch.jeda.platform.java;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

class LogWindow extends AutoCloseWindow {

    LogWindow(final WindowManager manager) {
        super(manager);
        this.initComponents();
        this.setTitle("Jeda Console");
        this.closeButton.setText("Schliessen");
        this.setDefaultButton(this.closeButton);
        this.init();
        System.setErr(new PrintStream(new ConsoleOutputStream(this, Color.RED, System.err)));
        System.setOut(new PrintStream(new ConsoleOutputStream(this, Color.BLACK, System.out)));
    }

    private void append(final String content, final AttributeSet textAttributes) {
        try {
            final Document document = this.logTextArea.getDocument();
            document.insertString(document.getLength(), content, textAttributes);
        }
        catch (final BadLocationException ex) {
            // ignore
        }

        this.logTextArea.setCaretPosition(this.logTextArea.getDocument().getLength());
        this.setVisible(true);
    }

    private void changeFontSize(float delta) {
        final Font font = this.logTextArea.getFont();
        this.logTextArea.setFont(font.deriveFont(font.getSize() + delta));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        closeButton = new javax.swing.JButton();
        increaseSizeButton = new javax.swing.JButton();
        decreaseSizeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setFont(new java.awt.Font("Sans Serif", 0, 16)); // NOI18N
        logTextArea.setRows(5);
        jScrollPane1.setViewportView(logTextArea);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        increaseSizeButton.setText("+");
        increaseSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increaseSizeButtonActionPerformed(evt);
            }
        });

        decreaseSizeButton.setText("-");
        decreaseSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decreaseSizeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(increaseSizeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decreaseSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {decreaseSizeButton, increaseSizeButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(increaseSizeButton)
                    .addComponent(decreaseSizeButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.cancel();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void increaseSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_increaseSizeButtonActionPerformed
        this.changeFontSize(+3);
    }//GEN-LAST:event_increaseSizeButtonActionPerformed

    private void decreaseSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decreaseSizeButtonActionPerformed
        this.changeFontSize(-3);
    }//GEN-LAST:event_decreaseSizeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton decreaseSizeButton;
    private javax.swing.JButton increaseSizeButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    // End of variables declaration//GEN-END:variables

    private static class ConsoleOutputStream extends ByteArrayOutputStream {

        private final LogWindow logWindow;
        private final PrintStream printStream;
        private final SimpleAttributeSet textAttributes;

        ConsoleOutputStream(final LogWindow logWindow, final Color textColor, final PrintStream printStream) {
            this.logWindow = logWindow;
            this.printStream = printStream;
            this.textAttributes = new SimpleAttributeSet();
            StyleConstants.setForeground(this.textAttributes, textColor);
        }

        @Override
        public void flush() {
            final String content = this.toString();
            if (content.isEmpty()) {
                return;
            }

            this.logWindow.append(content, this.textAttributes);
            this.printStream.print(content);
            this.printStream.flush();
            this.reset();
        }
    }
}
