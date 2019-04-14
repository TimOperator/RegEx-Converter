/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import de.hamp_it.EasyuseServerConnector.MainServerConnector;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Tim
 */
public class MainJFrame extends javax.swing.JFrame {

    private Component frame;
    private boolean fileNameIsElevated;
    private int fileNumber;
    private String originalFileName;
    protected Settings settings;
    public SettingsJFrame settingsFrame;
    public ReportBugJFrame reportBugFrame;
    private final String steam_History ="Steam_History";
    private final String skype_History ="Skype_History";
    private final String discord_History ="Discord_History";
    private final String whatsapp_History ="WhatsApp_History";

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        setIcon();
        setLocationRelativeTo(null);
        fileNumber = 1;
        fileNameIsElevated = false;
        settings = new Settings();
        settingsFrame = new SettingsJFrame(settings, this.getTitle());
        reportBugFrame = new ReportBugJFrame(this.getTitle());
        setFileName(fileNameTextField.getText());
        originalFileName = fileNameTextField.getText();
        if (settings.getAutoUpdateCheck()) {
            // TODO check for updates
            String response = MainServerConnector.checkForUpdates(this.getTitle());
            System.out.println(response);
            if (response.startsWith("Die Version ") && response.endsWith("ist nun verfügbar.")) {
                JOptionPane.showMessageDialog(frame, "Update verfügbar!\r\n" + response);
            }
        }
    }

    private void setFileName(String name) {
        Date date = new Date();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime curDate = LocalDateTime.now();
        fileNameTextField.setText(name + " " + curDate.format(format));
    }

    private void markFileName() {
        if (exportCheckBox.isSelected()) {
            if (allowOverwriteCheckBox.isSelected()) {
                File f = new File(fileNameTextField.getText() + ".docx");
                if (f.exists()) {
                    fileNameTextField.setBackground(Color.red);
                } else {
                    fileNameTextField.setBackground(Color.green);
                }
            } else {
                checkFileName();
            }
        }
    }

    private void checkFileName() {
        Integer i = fileNumber;
        if (fileNameIsElevated) {
            fileNameTextField.setText(originalFileName);
        }
        while (true) {
            String outputFileName = fileNameTextField.getText();
            File f = new File(outputFileName + ".docx");
            if (!f.exists()) {
                if (outputFileName.equalsIgnoreCase(originalFileName)) {
                    fileNameIsElevated = false;
                    fileNameTextField.setBackground(Color.GREEN);
                }
                break;
            } else {
                fileNameTextField.setBackground(Color.YELLOW);
                fileNameIsElevated = true;
                fileNameTextField.setText(originalFileName + "_" + i.toString());
                i++;
            }
        }
    }

    private void convert(String type) {
        
        // Auswahl des Modus
        String tag;
        String skypeTag = "(?:\\[(.+)?\\]\\s(.+?)?:\\s)?(.+)";
        String steamTag = "(\\d\\d:\\d\\d)\\s-\\s(.+?):\\s(.*)";
        String discordTag = "(?:(.+)-(?:.*)\\sum\\s(.+?)\\sUhr)?(.+)?";
        String whatsappTag = "\\[(\\d\\d:\\d\\d)\\,\\s\\d+\\.\\d+.\\d\\d\\d\\d\\]\\s(.+?):\\s(.*)|(.+)";    //Group 4 is line without intro
        int timeNo=1;
        int userNo=2;

        switch (type) {
            case "Skype":
                tag = skypeTag;
                break;
            case "Steam":
                tag = steamTag;
                break;
            case "Discord":
                tag = discordTag;
                // Time and user reversed
                timeNo=2;
                userNo=1;
                break;
            case "WhatsApp":
                tag = whatsappTag;
                break;
            default:
                return;
        }

        WordExporter we = new WordExporter(fileNameTextField.getText() + ".docx");
        we.open();
        
        

        try {
            //Text auslesen
            String text = Original_TextArea.getText();
            String[] lines = Original_TextArea.getText().split("\\n");
            Pattern pattern = Pattern.compile(tag);
            int process = 0;
            jProgressBar.setMaximum(lines.length);
            jProgressBar.setValue(process);
            //RegEx catchen
            for (String line : lines) {     //Iteriert über alle Zeilen
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String time = matcher.group(timeNo);            //Zeitstempel
                    String user = matcher.group(userNo);            //Userstempel
                    String match = matcher.group(3);                //Inhalt
                    String match2 = "";
                    try {
                        match2 = matcher.group(4);               //Content, if no intro (only used for whatsapp
                    } catch (IndexOutOfBoundsException e) {
                        //No group 4
                    }
                    
                    
                    if (match == null) {
                        match = match2;
                    } else {
                        
                        match+="\r\n";
                    }
                    
                    
                    if (time != null && user != null) {
                        if (type.equals("Skype")) {
                            int start = time.length() - 8;
                            int end = time.length() - 3;
                            time = time.substring(start, end);
                        }
                        String headline = user + " " + time + "\r\n";
                        Converted_TextArea.append(headline);

                        we.addText(user + " ", settings.getUserWF());
                        we.addText(time, settings.getTimeWF(), true);
                    }
                    String toTextArea = "";
                    LineEditor lineEditor = new LineEditor(settings.getContentWF(), settings.getNameWF());
                    lineEditor.setParams(settings.getNameStartChar(), settings.getNameEndChar(), settings.getColorStartChar(), settings.getColorSplitChar(), settings.getColorEndChar());
                    lineEditor.setLine(match);
                    lineEditor.setColorMap(settingsFrame.getColorMap());
                    String part;
                    boolean addBreak;
                    while (lineEditor.hastContent()) {
                        String textPart = lineEditor.getContent();
                        addBreak = !lineEditor.hastContent();
                        toTextArea = toTextArea + textPart;
                        //JOptionPane.showMessageDialog(frame, textPart);
                        we.addText(textPart, lineEditor.getCurrentWF(), addBreak);
                    }        
                    Converted_TextArea.append(toTextArea);
                }
                process++;
                jProgressBar.setValue(process);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Regex conversion error: " + e.toString());
        }

        //Save as word (.docx) document
        if (exportCheckBox.isSelected()) {
            try {
                we.close();
            } catch (Exception e) {
                jProgressBar.setValue(0);
                JOptionPane.showMessageDialog(frame, "Word error: " + e.toString());
            }
        }
        this.markFileName();
        fileNameTextField.setBackground(Color.GREEN);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        buttonGroup = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSplitPane = new javax.swing.JSplitPane();
        upperPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Original_TextArea = new javax.swing.JTextArea();
        originalTextLabel = new javax.swing.JLabel();
        lowerPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Converted_TextArea = new javax.swing.JTextArea();
        convertedTextLabel = new javax.swing.JLabel();
        Skype_RadioButton = new javax.swing.JRadioButton();
        convertButton = new javax.swing.JButton();
        regExTextLabel = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        exportCheckBox = new javax.swing.JCheckBox();
        allowOverwriteCheckBox = new javax.swing.JCheckBox();
        settingsButton = new javax.swing.JButton();
        Steam_RadioButton = new javax.swing.JRadioButton();
        jProgressBar = new javax.swing.JProgressBar();
        errorButton = new javax.swing.JButton();
        Discord_RadioButton = new javax.swing.JRadioButton();
        WhatsApp_RadioButton = new javax.swing.JRadioButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        buttonGroup.add(Skype_RadioButton);
        buttonGroup.add(Steam_RadioButton);

        jMenuItem1.setText("Copy");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Paste");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("Copy");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        jMenuItem4.setText("Paste");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RegEx-Converter v5.3.1");
        setFocusable(false);
        setMinimumSize(new java.awt.Dimension(925, 400));

        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        Original_TextArea.setColumns(20);
        Original_TextArea.setRows(5);
        Original_TextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Original_TextAreaMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(Original_TextArea);

        originalTextLabel.setText("Orginal Text");

        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 952, Short.MAX_VALUE)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addComponent(originalTextLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upperPanelLayout.createSequentialGroup()
                .addComponent(originalTextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
        );

        jSplitPane.setTopComponent(upperPanel);

        lowerPanel.setPreferredSize(new java.awt.Dimension(940, 188));

        Converted_TextArea.setColumns(20);
        Converted_TextArea.setRows(5);
        Converted_TextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Converted_TextAreaMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(Converted_TextArea);

        convertedTextLabel.setText("Konvertierter Text");

        Skype_RadioButton.setSelected(true);
        Skype_RadioButton.setText("Skype");
        Skype_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Skype_RadioButtonActionPerformed(evt);
            }
        });

        convertButton.setText("Konvertieren");
        convertButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                convertButtonMouseMoved(evt);
            }
        });
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });

        regExTextLabel.setText("RegEx");

        fileNameTextField.setText("Skype_History");
        fileNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNameTextFieldActionPerformed(evt);
            }
        });
        fileNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fileNameTextFieldKeyReleased(evt);
            }
        });

        exportCheckBox.setSelected(true);
        exportCheckBox.setText("Dateiausgabe nach");
        exportCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCheckBoxActionPerformed(evt);
            }
        });

        allowOverwriteCheckBox.setText("allow Overwrite");
        allowOverwriteCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowOverwriteCheckBoxActionPerformed(evt);
            }
        });

        settingsButton.setText("Einstellungen");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        Steam_RadioButton.setText("Steam");
        Steam_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Steam_RadioButtonActionPerformed(evt);
            }
        });

        jProgressBar.setStringPainted(true);

        errorButton.setText("Fehler melden");
        errorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(Discord_RadioButton);
        Discord_RadioButton.setText("Discord");
        Discord_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Discord_RadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(WhatsApp_RadioButton);
        WhatsApp_RadioButton.setText("WhatsApp");
        WhatsApp_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WhatsApp_RadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lowerPanelLayout = new javax.swing.GroupLayout(lowerPanel);
        lowerPanel.setLayout(lowerPanelLayout);
        lowerPanelLayout.setHorizontalGroup(
            lowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lowerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lowerPanelLayout.createSequentialGroup()
                        .addComponent(Skype_RadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Steam_RadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Discord_RadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(WhatsApp_RadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exportCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(allowOverwriteCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(settingsButton)
                        .addGap(18, 18, 18)
                        .addComponent(convertButton))
                    .addGroup(lowerPanelLayout.createSequentialGroup()
                        .addComponent(regExTextLabel)
                        .addGap(852, 852, 852)))
                .addContainerGap())
            .addGroup(lowerPanelLayout.createSequentialGroup()
                .addComponent(convertedTextLabel)
                .addGap(18, 18, 18)
                .addComponent(errorButton)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        lowerPanelLayout.setVerticalGroup(
            lowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lowerPanelLayout.createSequentialGroup()
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regExTextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Skype_RadioButton)
                    .addComponent(convertButton)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportCheckBox)
                    .addComponent(allowOverwriteCheckBox)
                    .addComponent(settingsButton)
                    .addComponent(Steam_RadioButton)
                    .addComponent(Discord_RadioButton)
                    .addComponent(WhatsApp_RadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(convertedTextLabel)
                    .addComponent(errorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
        );

        convertButton.getAccessibleContext().setAccessibleName("Convert_Button");

        jSplitPane.setRightComponent(lowerPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
        Converted_TextArea.setText("");
        if (Skype_RadioButton.isSelected()) {
            convert("Skype");
        } else if (Steam_RadioButton.isSelected()) {
            convert("Steam");
        } else if (Discord_RadioButton.isSelected()) {
            convert("Discord");
        } else if (WhatsApp_RadioButton.isSelected()) {
            convert("WhatsApp");
        }
    }//GEN-LAST:event_convertButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String result = this.Original_TextArea.getText();
        try {
            StringSelection selection = new StringSelection(result);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void Original_TextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Original_TextAreaMouseReleased
        if (evt.isPopupTrigger()) {
            jPopupMenu1.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_Original_TextAreaMouseReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            this.Original_TextArea.setText(result);
        } catch (HeadlessException | UnsupportedFlavorException | IOException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String result = this.Converted_TextArea.getText();
        try {
            StringSelection selection = new StringSelection(result);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            this.Converted_TextArea.setText(result);
        } catch (HeadlessException | UnsupportedFlavorException | IOException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void Converted_TextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Converted_TextAreaMouseReleased
        if (evt.isPopupTrigger()) {
            jPopupMenu2.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_Converted_TextAreaMouseReleased

    private void exportCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCheckBoxActionPerformed
        if (exportCheckBox.isSelected()) {
            allowOverwriteCheckBox.setEnabled(true);
            settingsButton.setEnabled(true);
        } else {
            allowOverwriteCheckBox.setEnabled(false);
            settingsButton.setEnabled(false);
        }
        this.markFileName();
    }//GEN-LAST:event_exportCheckBoxActionPerformed

    private void convertButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_convertButtonMouseMoved
        this.markFileName();
    }//GEN-LAST:event_convertButtonMouseMoved

    private void allowOverwriteCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowOverwriteCheckBoxActionPerformed
        if (!originalFileName.equals(skype_History) && !originalFileName.equals(steam_History) && !originalFileName.equals(discord_History)) {
            fileNameTextField.setText(originalFileName);
        }
        this.markFileName();
    }//GEN-LAST:event_allowOverwriteCheckBoxActionPerformed

    private void fileNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fileNameTextFieldKeyReleased
        originalFileName = fileNameTextField.getText();
        fileNumber = 1;
    }//GEN-LAST:event_fileNameTextFieldKeyReleased

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        settingsFrame.setVisible(true);
        settingsFrame.setLocationRelativeTo(frame);

    }//GEN-LAST:event_settingsButtonActionPerformed

    private void Steam_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Steam_RadioButtonActionPerformed
        filenameReplacement(steam_History);
        /*if (fileNameTextField.getText().startsWith(skype_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(skype_History, steam_History);
            fileNameTextField.setText(fileName); //Replace Skype_History with Steam_History
        } else if (fileNameTextField.getText().startsWith(discord_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(discord_History, steam_History);
            fileNameTextField.setText(fileName);
        }
        originalFileName = fileNameTextField.getText();
        */
    }//GEN-LAST:event_Steam_RadioButtonActionPerformed

    private void Skype_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Skype_RadioButtonActionPerformed
        filenameReplacement(skype_History);
        /*if (fileNameTextField.getText().startsWith(steam_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(steam_History, skype_History);
            fileNameTextField.setText(fileName);
        } else if (fileNameTextField.getText().startsWith(discord_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(discord_History, skype_History);
            fileNameTextField.setText(fileName);
        }
        originalFileName = fileNameTextField.getText();
        */
    }//GEN-LAST:event_Skype_RadioButtonActionPerformed

    private void errorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorButtonActionPerformed
        reportBugFrame.setConverterContent(Original_TextArea.getText(), Converted_TextArea.getText());
        reportBugFrame.setLocationRelativeTo(frame);
        reportBugFrame.setVisible(true);
    }//GEN-LAST:event_errorButtonActionPerformed

    private void fileNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileNameTextFieldActionPerformed

    private void Discord_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Discord_RadioButtonActionPerformed
        filenameReplacement(discord_History);
        /*if (fileNameTextField.getText().startsWith(skype_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(skype_History, discord_History);
            fileNameTextField.setText(fileName);
        } else if (fileNameTextField.getText().startsWith(steam_History)) {
            String fileName = fileNameTextField.getText();
            fileName = fileName.replace(steam_History, discord_History);
            fileNameTextField.setText(fileName);
        }
        originalFileName = fileNameTextField.getText();
        */
    }//GEN-LAST:event_Discord_RadioButtonActionPerformed

    private void WhatsApp_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WhatsApp_RadioButtonActionPerformed
        filenameReplacement(whatsapp_History);
    }//GEN-LAST:event_WhatsApp_RadioButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainJFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Converted_TextArea;
    private javax.swing.JRadioButton Discord_RadioButton;
    private javax.swing.JTextArea Original_TextArea;
    private javax.swing.JRadioButton Skype_RadioButton;
    private javax.swing.JRadioButton Steam_RadioButton;
    private javax.swing.JRadioButton WhatsApp_RadioButton;
    private javax.swing.JCheckBox allowOverwriteCheckBox;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton convertButton;
    private javax.swing.JLabel convertedTextLabel;
    private javax.swing.JButton errorButton;
    private javax.swing.JCheckBox exportCheckBox;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel lowerPanel;
    private javax.swing.JLabel originalTextLabel;
    private javax.swing.JLabel regExTextLabel;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("convicon.png")));
    }

    private void filenameReplacement(String replacementString) {
        String fileName = fileNameTextField.getText();
        if (fileName.startsWith(skype_History)) {
            fileName = fileName.replace(skype_History, replacementString);
        } else if (fileName.startsWith(discord_History)) {
            fileName = fileName.replace(discord_History, replacementString);
        } else if (fileName.startsWith(steam_History)) {
            fileName = fileName.replace(steam_History, replacementString);
        } else if (fileName.startsWith(whatsapp_History)) {
            fileName = fileName.replace(whatsapp_History, replacementString);
        }
        fileNameTextField.setText(fileName);
        originalFileName = fileNameTextField.getText();
    }
}
