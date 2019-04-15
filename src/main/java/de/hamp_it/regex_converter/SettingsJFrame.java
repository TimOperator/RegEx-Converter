/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import de.hamp_it.EasyuseServerConnector.MainServerConnector;
import java.awt.Toolkit;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Tim
 */
public final class SettingsJFrame extends javax.swing.JFrame {

    private Settings settings;
    private HashMap colorMap;
    private String programTitle;
    ColorJFrame colorJFrame = new ColorJFrame();

    /**
     * Creates new form SettingsJFrame
     */
    public SettingsJFrame() {
        initComponents();
        setIcon();
    }

    public SettingsJFrame(Settings settings, String programTitle) {
        initComponents();
        setIcon();
        this.settings = settings;
        this.programTitle = programTitle;
        setValuesToBoard();
    }

    private void toggleSaveButton(boolean value) {
        saveButton.setEnabled(value);
        if (value) {
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    private void validateName() {
        String nameStart = nameStartTextField.getText();
        String nameEnd = nameEndTextField.getText();
        if (nameStart.contains(" ") || nameEnd.contains(" ")) {
            toggleSaveButton(false);
            JOptionPane.showMessageDialog(null, "Spaces are not allowed in name");
            return;
        }
        if (nameStart.length() == 0 || nameEnd.length() == 0) {
            toggleSaveButton(false);
            return;
        }
        toggleSaveButton(true);
        setNameExample(nameStart, nameEnd);
    }

    private void validateColor() {
        String colorStart = colorStartTextField.getText();
        String colorSplit = colorSplitTextField.getText();
        String colorEnd = colorEndTextField.getText();
        if (colorStart.contains(" ") || colorSplit.contains(" ") || colorEnd.contains(" ")) {
            toggleSaveButton(false);
            JOptionPane.showMessageDialog(null, "Spaces are not allowed in colors");
            return;
        }
        if (colorStart.equals(colorEnd)) {
            toggleSaveButton(false);
            JOptionPane.showMessageDialog(null, "Color start character matches color end character, which is not allowed");
            return;
        }
        if (colorStart.length() == 0 || colorSplit.length() == 0 || colorEnd.length() == 0) {
            toggleSaveButton(false);
            return;
        }
        toggleSaveButton(true);
        setColorExample(colorStart, colorSplit, colorEnd);
    }

    private void setValuesToBoard() {
        userFontFamily_ComboBox.addItem(settings.getUserWF().getFontFamily());
        addMissingItems(userFontFamily_ComboBox);
        userFontSize_Spinner.setValue(settings.getUserWF().getFontSize());
        userFontBold_CheckBox.setSelected(settings.getUserWF().getBold());
        userFontItalic_CheckBox.setSelected(settings.getUserWF().getItalic());

        timeFontFamily_ComboBox.addItem(settings.getTimeWF().getFontFamily());
        addMissingItems(timeFontFamily_ComboBox);
        timeFontSize_Spinner.setValue(settings.getTimeWF().getFontSize());
        timeFontBold_CheckBox.setSelected(settings.getTimeWF().getBold());
        timeFontItalic_CheckBox.setSelected(settings.getTimeWF().getItalic());

        contentFontFamily_ComboBox.addItem(settings.getContentWF().getFontFamily());
        addMissingItems(contentFontFamily_ComboBox);
        contentFontSize_Spinner.setValue(settings.getContentWF().getFontSize());
        contentFontBold_CheckBox.setSelected(settings.getContentWF().getBold());
        contentFontItalic_CheckBox.setSelected(settings.getContentWF().getItalic());
        
        nameFontFamily_ComboBox.addItem(settings.getNameWF().getFontFamily());
        addMissingItems(nameFontFamily_ComboBox);
        nameFontSize_Spinner.setValue(settings.getNameWF().getFontSize());
        nameFontBold_CheckBox.setSelected(settings.getNameWF().getBold());
        nameFontItalic_CheckBox.setSelected(settings.getNameWF().getItalic());
        
        nameStartTextField.setText(settings.getNameStartChar());
        nameEndTextField.setText(settings.getNameEndChar());
        colorStartTextField.setText(settings.getColorStartChar());
        colorSplitTextField.setText(settings.getColorSplitChar());
        colorEndTextField.setText(settings.getColorEndChar());
        
        setColorExample(settings.getColorStartChar(), settings.getColorSplitChar(), settings.getColorEndChar());
        setNameExample(settings.getNameStartChar(), settings.getNameEndChar());
        
        autoUpdateCheckCheckBox.setSelected(settings.getAutoUpdateCheck());
    }
    
    private void addMissingItems(javax.swing.JComboBox box) {
        // Add fonts to combo box
        String[] list = {"Calibri", "Arial", "Cambria", "Times New Roman"};
        for (String list1 : list) {
            if (!box.getSelectedItem().toString().equalsIgnoreCase(list1)) {
                box.addItem(list1);
            }
        }
        
    }

    private void saveValuesFromBoard() {
        // save word format
        settings.getUserWF().setFontFamily((String) this.userFontFamily_ComboBox.getSelectedItem());
        settings.getUserWF().setFontSize((int) this.userFontSize_Spinner.getValue());
        settings.getUserWF().setBold(this.userFontBold_CheckBox.isSelected());
        settings.getUserWF().setItalic(this.userFontItalic_CheckBox.isSelected());
        settings.getTimeWF().setFontFamily((String) this.timeFontFamily_ComboBox.getSelectedItem());
        settings.getTimeWF().setFontSize((int) this.timeFontSize_Spinner.getValue());
        settings.getTimeWF().setBold(this.timeFontBold_CheckBox.isSelected());
        settings.getTimeWF().setItalic(this.timeFontItalic_CheckBox.isSelected());
        settings.getContentWF().setFontFamily((String) this.contentFontFamily_ComboBox.getSelectedItem());
        settings.getContentWF().setFontSize((int) this.contentFontSize_Spinner.getValue());
        settings.getContentWF().setBold(this.contentFontBold_CheckBox.isSelected());
        settings.getContentWF().setItalic(this.contentFontItalic_CheckBox.isSelected());
        settings.getNameWF().setFontFamily((String) this.nameFontFamily_ComboBox.getSelectedItem());
        settings.getNameWF().setFontSize((int) this.nameFontSize_Spinner.getValue());
        settings.getNameWF().setBold(this.nameFontBold_CheckBox.isSelected());
        settings.getNameWF().setItalic(this.nameFontItalic_CheckBox.isSelected());
        
        //save index characters
        settings.setNameStartChar(nameStartTextField.getText());
        settings.setNameEndChar(nameEndTextField.getText());
        settings.setColorStartChar(colorStartTextField.getText());
        settings.setColorSplitChar(colorSplitTextField.getText());
        settings.setColorEndChar(colorEndTextField.getText());
        
        settings.setAutoUpdateCheck(autoUpdateCheckCheckBox.isSelected());
        
        // save to file
        settings.saveSettingsFile();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("convicon.png")));
    }

    public HashMap getColorMap() {
        return colorJFrame.getColorMap();
    }

    /*public void addColorToMap(String nameKey, String rgbValue) {
        colorMap.put(nameKey, rgbValue);
    }*/

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timeSettingsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        timeFontBold_CheckBox = new javax.swing.JCheckBox();
        timeFontItalic_CheckBox = new javax.swing.JCheckBox();
        timeFontFamily_ComboBox = new javax.swing.JComboBox();
        timeFontSize_Spinner = new javax.swing.JSpinner();
        userSettingsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        userFontBold_CheckBox = new javax.swing.JCheckBox();
        userFontItalic_CheckBox = new javax.swing.JCheckBox();
        userFontFamily_ComboBox = new javax.swing.JComboBox();
        userFontSize_Spinner = new javax.swing.JSpinner();
        contentSettingsPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        contentFontBold_CheckBox = new javax.swing.JCheckBox();
        contentFontItalic_CheckBox = new javax.swing.JCheckBox();
        contentFontFamily_ComboBox = new javax.swing.JComboBox();
        contentFontSize_Spinner = new javax.swing.JSpinner();
        saveButton = new javax.swing.JButton();
        nameSettingsPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        nameFontBold_CheckBox = new javax.swing.JCheckBox();
        nameFontItalic_CheckBox = new javax.swing.JCheckBox();
        nameFontFamily_ComboBox = new javax.swing.JComboBox();
        nameFontSize_Spinner = new javax.swing.JSpinner();
        charPanel = new javax.swing.JPanel();
        nameStartLabel = new javax.swing.JLabel();
        nameStartTextField = new javax.swing.JTextField();
        nameEndLabel = new javax.swing.JLabel();
        nameEndTextField = new javax.swing.JTextField();
        nameExampleLabel1 = new javax.swing.JLabel();
        nameExampleLabel2 = new javax.swing.JLabel();
        colorSettingsPanel = new javax.swing.JPanel();
        colorStartLabel = new javax.swing.JLabel();
        colorStartTextField = new javax.swing.JTextField();
        colorEndTextField = new javax.swing.JTextField();
        colorEndLabel = new javax.swing.JLabel();
        colorSplitTextField = new javax.swing.JTextField();
        colorSplitLabel = new javax.swing.JLabel();
        colorExampleLabel1 = new javax.swing.JLabel();
        colorExampleLabel2 = new javax.swing.JLabel();
        showColorsButton = new javax.swing.JButton();
        checkForUpdatedButton = new javax.swing.JButton();
        autoUpdateCheckCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("interfaces"); // NOI18N
        setTitle(bundle.getString("title_settings")); // NOI18N
        setMinimumSize(new java.awt.Dimension(450, 360));
        setResizable(false);

        timeSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_time"))); // NOI18N

        jLabel1.setText(bundle.getString("label_text_font")); // NOI18N

        jLabel2.setText(bundle.getString("label_text_size")); // NOI18N

        timeFontBold_CheckBox.setText(bundle.getString("label_text_bold")); // NOI18N

        timeFontItalic_CheckBox.setSelected(true);
        timeFontItalic_CheckBox.setText(bundle.getString("label_text_italic")); // NOI18N

        javax.swing.GroupLayout timeSettingsPanelLayout = new javax.swing.GroupLayout(timeSettingsPanel);
        timeSettingsPanel.setLayout(timeSettingsPanelLayout);
        timeSettingsPanelLayout.setHorizontalGroup(
            timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeFontBold_CheckBox)
                    .addComponent(timeFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timeSettingsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(timeFontItalic_CheckBox, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        timeSettingsPanelLayout.setVerticalGroup(
            timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(timeFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(timeFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timeSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeFontItalic_CheckBox)
                    .addComponent(timeFontBold_CheckBox)))
        );

        userSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_user"))); // NOI18N

        jLabel4.setText(bundle.getString("label_text_font")); // NOI18N

        jLabel5.setText(bundle.getString("label_text_size")); // NOI18N

        userFontBold_CheckBox.setText(bundle.getString("label_text_bold")); // NOI18N

        userFontItalic_CheckBox.setText(bundle.getString("label_text_italic")); // NOI18N

        javax.swing.GroupLayout userSettingsPanelLayout = new javax.swing.GroupLayout(userSettingsPanel);
        userSettingsPanel.setLayout(userSettingsPanelLayout);
        userSettingsPanelLayout.setHorizontalGroup(
            userSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userSettingsPanelLayout.createSequentialGroup()
                        .addComponent(userFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userSettingsPanelLayout.createSequentialGroup()
                        .addComponent(userFontBold_CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userFontItalic_CheckBox)))
                .addContainerGap())
        );
        userSettingsPanelLayout.setVerticalGroup(
            userSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(userSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(userFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(userFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(userSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userFontItalic_CheckBox)
                    .addComponent(userFontBold_CheckBox)))
        );

        contentSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_content"))); // NOI18N

        jLabel6.setText(bundle.getString("label_text_font")); // NOI18N

        jLabel7.setText(bundle.getString("label_text_size")); // NOI18N

        contentFontBold_CheckBox.setText(bundle.getString("label_text_bold")); // NOI18N

        contentFontItalic_CheckBox.setText(bundle.getString("label_text_italic")); // NOI18N

        javax.swing.GroupLayout contentSettingsPanelLayout = new javax.swing.GroupLayout(contentSettingsPanel);
        contentSettingsPanel.setLayout(contentSettingsPanelLayout);
        contentSettingsPanelLayout.setHorizontalGroup(
            contentSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentSettingsPanelLayout.createSequentialGroup()
                        .addComponent(contentFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contentFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(contentSettingsPanelLayout.createSequentialGroup()
                        .addComponent(contentFontBold_CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(contentFontItalic_CheckBox)))
                .addContainerGap())
        );
        contentSettingsPanelLayout.setVerticalGroup(
            contentSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentSettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(contentSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(contentFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(contentFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contentFontItalic_CheckBox)
                    .addComponent(contentFontBold_CheckBox)))
        );

        saveButton.setText(bundle.getString("button_save")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        nameSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_content_name"))); // NOI18N

        jLabel8.setText(bundle.getString("label_text_font")); // NOI18N

        jLabel9.setText(bundle.getString("label_text_size")); // NOI18N

        nameFontBold_CheckBox.setSelected(true);
        nameFontBold_CheckBox.setText(bundle.getString("label_text_bold")); // NOI18N

        nameFontItalic_CheckBox.setText(bundle.getString("label_text_italic")); // NOI18N

        javax.swing.GroupLayout nameSettingsPanelLayout = new javax.swing.GroupLayout(nameSettingsPanel);
        nameSettingsPanel.setLayout(nameSettingsPanelLayout);
        nameSettingsPanelLayout.setHorizontalGroup(
            nameSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nameSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(nameSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nameSettingsPanelLayout.createSequentialGroup()
                        .addComponent(nameFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(nameSettingsPanelLayout.createSequentialGroup()
                        .addComponent(nameFontBold_CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nameFontItalic_CheckBox)))
                .addContainerGap())
        );
        nameSettingsPanelLayout.setVerticalGroup(
            nameSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nameSettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(nameSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(nameFontFamily_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(nameFontSize_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nameSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameFontItalic_CheckBox)
                    .addComponent(nameFontBold_CheckBox)))
        );

        charPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_character_name"))); // NOI18N

        nameStartLabel.setText(bundle.getString("label_start_symbol")); // NOI18N

        nameStartTextField.setText("@");
        nameStartTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameStartTextFieldKeyReleased(evt);
            }
        });

        nameEndLabel.setText(bundle.getString("label_end_symbol")); // NOI18N

        nameEndTextField.setText(":");
        nameEndTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameEndTextFieldKeyReleased(evt);
            }
        });

        nameExampleLabel1.setText(bundle.getString("label_example")); // NOI18N

        nameExampleLabel2.setText("@Name:");

        javax.swing.GroupLayout charPanelLayout = new javax.swing.GroupLayout(charPanel);
        charPanel.setLayout(charPanelLayout);
        charPanelLayout.setHorizontalGroup(
            charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(charPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameStartLabel)
                    .addComponent(nameExampleLabel1))
                .addGap(18, 18, 18)
                .addGroup(charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(charPanelLayout.createSequentialGroup()
                        .addComponent(nameStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(nameEndLabel))
                    .addGroup(charPanelLayout.createSequentialGroup()
                        .addComponent(nameExampleLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(nameEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        charPanelLayout.setVerticalGroup(
            charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(charPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nameEndLabel)
                        .addComponent(nameEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nameStartLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(charPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameExampleLabel2)
                    .addComponent(nameExampleLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        colorSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frame_character_color"))); // NOI18N

        colorStartLabel.setText(bundle.getString("label_start_symbol")); // NOI18N

        colorStartTextField.setText("{");
        colorStartTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                colorStartTextFieldKeyReleased(evt);
            }
        });

        colorEndTextField.setText("}");
        colorEndTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                colorEndTextFieldKeyReleased(evt);
            }
        });

        colorEndLabel.setText(bundle.getString("label_end_symbol")); // NOI18N

        colorSplitTextField.setText(":");
        colorSplitTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                colorSplitTextFieldKeyReleased(evt);
            }
        });

        colorSplitLabel.setText(bundle.getString("label_delimiter")); // NOI18N

        colorExampleLabel1.setText(bundle.getString("label_example")); // NOI18N

        colorExampleLabel2.setText("{red: Text }");

        showColorsButton.setText(bundle.getString("button_show_color_palette")); // NOI18N
        showColorsButton.setPreferredSize(new java.awt.Dimension(120, 20));
        showColorsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showColorsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout colorSettingsPanelLayout = new javax.swing.GroupLayout(colorSettingsPanel);
        colorSettingsPanel.setLayout(colorSettingsPanelLayout);
        colorSettingsPanelLayout.setHorizontalGroup(
            colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorStartLabel)
                    .addComponent(colorSplitLabel)
                    .addComponent(colorExampleLabel1))
                .addGap(18, 18, 18)
                .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(colorSplitTextField)
                        .addComponent(colorStartTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                    .addComponent(colorExampleLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(colorSettingsPanelLayout.createSequentialGroup()
                        .addComponent(colorEndLabel)
                        .addGap(18, 18, 18)
                        .addComponent(colorEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showColorsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addContainerGap())
        );
        colorSettingsPanelLayout.setVerticalGroup(
            colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorStartLabel)
                    .addComponent(colorEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorEndLabel))
                .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colorSettingsPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(colorSplitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(colorSplitLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(colorSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(colorExampleLabel1)
                            .addComponent(colorExampleLabel2)))
                    .addGroup(colorSettingsPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(showColorsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        checkForUpdatedButton.setText(bundle.getString("button_check_for_updates")); // NOI18N
        checkForUpdatedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForUpdatedButtonActionPerformed(evt);
            }
        });

        autoUpdateCheckCheckBox.setSelected(true);
        autoUpdateCheckCheckBox.setText(bundle.getString("label_check_for_updates_on_startup")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(autoUpdateCheckCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(userSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(timeSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contentSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(checkForUpdatedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveButton))
                    .addComponent(nameSettingsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(charPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(colorSettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(charPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoUpdateCheckCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(checkForUpdatedButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        //Save settings
        saveValuesFromBoard();
        this.setVisible(false);
    }//GEN-LAST:event_saveButtonActionPerformed

    private void nameStartTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameStartTextFieldKeyReleased
        this.validateName();
    }//GEN-LAST:event_nameStartTextFieldKeyReleased

    private void nameEndTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameEndTextFieldKeyReleased
        this.validateName();
    }//GEN-LAST:event_nameEndTextFieldKeyReleased

    private void colorStartTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colorStartTextFieldKeyReleased
        this.validateColor();
    }//GEN-LAST:event_colorStartTextFieldKeyReleased

    private void colorEndTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colorEndTextFieldKeyReleased
        this.validateColor();
    }//GEN-LAST:event_colorEndTextFieldKeyReleased

    private void colorSplitTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colorSplitTextFieldKeyReleased
        this.validateColor();
    }//GEN-LAST:event_colorSplitTextFieldKeyReleased

    private void showColorsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showColorsButtonActionPerformed
        colorJFrame.setVisible(true);
    }//GEN-LAST:event_showColorsButtonActionPerformed

    private void checkForUpdatedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForUpdatedButtonActionPerformed
        String response = MainServerConnector.checkForUpdates(programTitle);
        JOptionPane.showMessageDialog(null, response);
    }//GEN-LAST:event_checkForUpdatedButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoUpdateCheckCheckBox;
    private javax.swing.JPanel charPanel;
    private javax.swing.JButton checkForUpdatedButton;
    private javax.swing.JLabel colorEndLabel;
    private javax.swing.JTextField colorEndTextField;
    private javax.swing.JLabel colorExampleLabel1;
    private javax.swing.JLabel colorExampleLabel2;
    private javax.swing.JPanel colorSettingsPanel;
    private javax.swing.JLabel colorSplitLabel;
    private javax.swing.JTextField colorSplitTextField;
    private javax.swing.JLabel colorStartLabel;
    private javax.swing.JTextField colorStartTextField;
    private javax.swing.JCheckBox contentFontBold_CheckBox;
    private javax.swing.JComboBox contentFontFamily_ComboBox;
    private javax.swing.JCheckBox contentFontItalic_CheckBox;
    private javax.swing.JSpinner contentFontSize_Spinner;
    private javax.swing.JPanel contentSettingsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel nameEndLabel;
    private javax.swing.JTextField nameEndTextField;
    private javax.swing.JLabel nameExampleLabel1;
    private javax.swing.JLabel nameExampleLabel2;
    private javax.swing.JCheckBox nameFontBold_CheckBox;
    private javax.swing.JComboBox nameFontFamily_ComboBox;
    private javax.swing.JCheckBox nameFontItalic_CheckBox;
    private javax.swing.JSpinner nameFontSize_Spinner;
    private javax.swing.JPanel nameSettingsPanel;
    private javax.swing.JLabel nameStartLabel;
    private javax.swing.JTextField nameStartTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton showColorsButton;
    private javax.swing.JCheckBox timeFontBold_CheckBox;
    private javax.swing.JComboBox timeFontFamily_ComboBox;
    private javax.swing.JCheckBox timeFontItalic_CheckBox;
    private javax.swing.JSpinner timeFontSize_Spinner;
    private javax.swing.JPanel timeSettingsPanel;
    private javax.swing.JCheckBox userFontBold_CheckBox;
    private javax.swing.JComboBox userFontFamily_ComboBox;
    private javax.swing.JCheckBox userFontItalic_CheckBox;
    private javax.swing.JSpinner userFontSize_Spinner;
    private javax.swing.JPanel userSettingsPanel;
    // End of variables declaration//GEN-END:variables

    private void setColorExample(String start, String split, String end) {
        colorExampleLabel2.setText(start + "red" + split + "Text" + end);
    }

    private void setNameExample(String start, String end) {
        nameExampleLabel2.setText(start + "Name" + end);
    }
}
