/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import de.hamp_it.EasyuseServerConnector.MainServerConnector;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

/**
 *
 * @author Tim
 */
public class ReportBugJFrame extends javax.swing.JFrame {

    private String originalText;
    private String convertedText;
    private String programTitle;
    /**
     * Creates new form reportBugJFrame
     * @param programTitle Title
     */
    public ReportBugJFrame(String programTitle) {
        initComponents();
        setIcon();
        this.programTitle = programTitle;
    }

    private ReportBugJFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("convicon.png")));
    }
    
    public void setConverterContent(String originalText, String convertedText) {
        this.originalText = originalText;
        this.convertedText = convertedText;
        originalTextArea.setText(originalText);
        convertedTextArea.setText(convertedText);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        commentLabel = new javax.swing.JLabel();
        originalTextLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        originalTextArea = new javax.swing.JTextArea();
        convertedTextLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        convertedTextArea = new javax.swing.JTextArea();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("interfaces"); // NOI18N
        setTitle(bundle.getString("title_report_error")); // NOI18N
        setMinimumSize(new java.awt.Dimension(270, 510));

        reportButton.setText(bundle.getString("button_send")); // NOI18N
        reportButton.setEnabled(false);
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        commentTextArea.setColumns(20);
        commentTextArea.setRows(5);
        commentTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                commentTextAreaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(commentTextArea);

        commentLabel.setText(bundle.getString("label_comment")); // NOI18N

        originalTextLabel.setText(bundle.getString("label_original_text")); // NOI18N

        originalTextArea.setEditable(false);
        originalTextArea.setColumns(20);
        originalTextArea.setRows(5);
        jScrollPane2.setViewportView(originalTextArea);

        convertedTextLabel.setText(bundle.getString("label_converted_text")); // NOI18N

        convertedTextArea.setEditable(false);
        convertedTextArea.setColumns(20);
        convertedTextArea.setRows(5);
        jScrollPane3.setViewportView(convertedTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(reportButton))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(originalTextLabel)
                            .addComponent(convertedTextLabel)
                            .addComponent(commentLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(originalTextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(convertedTextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        // Send bugreport to server
        String text = programTitle + ":\r\n" + "|original Text:\r\n" + originalText + "\r\n|converted Text:\r\n" + convertedText + "|Comment:\r\n" + commentTextArea.getText();
        String response = MainServerConnector.reportBug(text);
        JOptionPane.showMessageDialog(null, response);
        this.setVisible(false);
    }//GEN-LAST:event_reportButtonActionPerformed

    private void commentTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commentTextAreaKeyReleased
        if (commentTextArea.getText().length() > 0) {
            reportButton.setEnabled(true);
        } else {
            reportButton.setEnabled(false);
        }
    }//GEN-LAST:event_commentTextAreaKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel commentLabel;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JTextArea convertedTextArea;
    private javax.swing.JLabel convertedTextLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea originalTextArea;
    private javax.swing.JLabel originalTextLabel;
    private javax.swing.JButton reportButton;
    // End of variables declaration//GEN-END:variables
}
