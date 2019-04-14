/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tim
 */
public class Settings {
    private WordFormater userWF;
    private WordFormater timeWF;
    private WordFormater contentWF;
    private WordFormater nameWF;
    private String nameStartChar;
    private String nameEndChar;
    private String colorStartChar;
    private String colorSplitChar;
    private String colorEndChar;
    private Boolean autoUpdateCheck;
    private final String settingsFileName="settings.json";

    public Settings() {
        loadSettingsFromFile(settingsFileName);
    }
    
    public String getNameStartChar() {
        return nameStartChar;
    }
    public void setNameStartChar(String nameStartChar) {
        this.nameStartChar = nameStartChar;
    }

    public String getNameEndChar() {
        return nameEndChar;
    }
    public void setNameEndChar(String nameEndChar) {
        this.nameEndChar = nameEndChar;
    }

    public String getColorStartChar() {
        return colorStartChar;
    }
    public void setColorStartChar(String colorStartChar) {
        this.colorStartChar = colorStartChar;
    }

    public String getColorSplitChar() {
        return colorSplitChar;
    }
    public void setColorSplitChar(String colorSplitChar) {
        this.colorSplitChar = colorSplitChar;
    }

    public String getColorEndChar() {
        return colorEndChar;
    }
    public void setColorEndChar(String colorEndChar) {
        this.colorEndChar = colorEndChar;
    }
    
    public WordFormater getUserWF() {
        return userWF;
    }
    public void setUserWF(WordFormater userWF) {
        this.userWF = userWF;
    }

    public WordFormater getTimeWF() {
        return timeWF;
    }
    public void setTimeWF(WordFormater timeWF) {
        this.timeWF = timeWF;
    }

    public WordFormater getContentWF() {
        return contentWF;
    }
    public void setContentWF(WordFormater contentWF) {
        this.contentWF = contentWF;
    }

    public WordFormater getNameWF() {
        return nameWF;
    }
    public void setNameWF(WordFormater nameWF) {
        this.nameWF = nameWF;
    }
    
    public Boolean getAutoUpdateCheck() {
        return autoUpdateCheck;
    }
    public void setAutoUpdateCheck(Boolean autoUpdateCheck) {
        this.autoUpdateCheck = autoUpdateCheck;
    }
    
    /**
     * set the default values of all fonts (FontFamily does not work)
     */
    private void setDefaultFormating() {
        userWF = new WordFormater("Cambria", 8);
        userWF.setItalic(true);
        timeWF = new WordFormater("Cambria", 7);
        timeWF.setItalic(true);
        contentWF = new WordFormater("Calibri", 11);
        nameWF = new WordFormater("Calibri", 11);
        nameWF.setBold(true);
        nameStartChar = "@";
        nameEndChar = ":";
        colorStartChar = "{";
        colorSplitChar = ":";
        colorEndChar = "}";
        autoUpdateCheck = true;
    }
    
    private void loadSettingsFromFile(String filename) {
        File settingsFile = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(settingsFile);
            String context = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                context += line;
            }
            scanner.close();
            
            JSONObject object = new JSONObject(context);
            JSONObject userJSONObject = object.getJSONObject("user");
            JSONObject timeJSONObject = object.getJSONObject("time");
            JSONObject contentJSONObject = object.getJSONObject("content");
            JSONObject nameJSONObject = object.getJSONObject("name");
            JSONObject nameTagJSONObject = object.getJSONObject("nametag");
            JSONObject colorTagJSONObject = object.getJSONObject("colortag");
            
            userWF = new WordFormater(userJSONObject.getString("font"), userJSONObject.getInt("size"), userJSONObject.getBoolean("bold"), userJSONObject.getBoolean("italic"));
            timeWF = new WordFormater(timeJSONObject.getString("font"), timeJSONObject.getInt("size"), timeJSONObject.getBoolean("bold"), timeJSONObject.getBoolean("italic"));
            contentWF = new WordFormater(contentJSONObject.getString("font"), contentJSONObject.getInt("size"), contentJSONObject.getBoolean("bold"), contentJSONObject.getBoolean("italic"));
            nameWF = new WordFormater(nameJSONObject.getString("font"), nameJSONObject.getInt("size"), nameJSONObject.getBoolean("bold"), nameJSONObject.getBoolean("italic"));
            
            nameStartChar = nameTagJSONObject.getString("start");
            nameEndChar = nameTagJSONObject.getString("end");
            colorStartChar = colorTagJSONObject.getString("start");
            colorSplitChar = colorTagJSONObject.getString("split");
            colorEndChar = colorTagJSONObject.getString("end");
            
            autoUpdateCheck = object.getBoolean("autoupdatecheck");
            
            
        } catch (FileNotFoundException ex) {
            //JOptionPane.showMessageDialog(null, "Neue Datei mit Einstellungen wird erstellt!\r\nErstelle: settings.properties");
            setDefaultFormating();
            saveSettingsFile();
        } catch (JSONException ex) {
            JOptionPane.showMessageDialog(null, "Einstellungen konnten nicht geladen werden!\r\n" + ex.toString());
            setDefaultFormating();
            saveSettingsFile();
        }
    }
    
    public void saveSettingsFile() {
        JSONObject object = new JSONObject();
        JSONObject userJSONObject = new JSONObject();
        JSONObject timeJSONObject = new JSONObject();
        JSONObject contentJSONObject = new JSONObject();
        JSONObject nameJSONObject = new JSONObject();
        JSONObject nameTagJSONObject = new JSONObject();
        JSONObject colorTagJSONObject = new JSONObject();
        
        try {
            // Set user word format
            userJSONObject.put("font", userWF.getFontFamily());
            userJSONObject.put("size", userWF.getFontSize());
            userJSONObject.put("bold", userWF.getBold());
            userJSONObject.put("italic", userWF.getItalic());
            
            // Set time word format
            timeJSONObject.put("font", timeWF.getFontFamily());
            timeJSONObject.put("size", timeWF.getFontSize());
            timeJSONObject.put("bold", timeWF.getBold());
            timeJSONObject.put("italic", timeWF.getItalic());
            
            // Set content word format
            contentJSONObject.put("font", contentWF.getFontFamily());
            contentJSONObject.put("size", contentWF.getFontSize());
            contentJSONObject.put("bold", contentWF.getBold());
            contentJSONObject.put("italic", contentWF.getItalic());
            
            // Set name word format
            nameJSONObject.put("font", nameWF.getFontFamily());
            nameJSONObject.put("size", nameWF.getFontSize());
            nameJSONObject.put("bold", nameWF.getBold());
            nameJSONObject.put("italic", nameWF.getItalic());
            
            // Set name tag
            nameTagJSONObject.put("start", nameStartChar);
            nameTagJSONObject.put("end", nameEndChar);
            
            // Set color tag
            colorTagJSONObject.put("start", colorStartChar);
            colorTagJSONObject.put("split", colorSplitChar);
            colorTagJSONObject.put("end", colorEndChar);
            
            // Add to main JSON
            object.put("user", userJSONObject);
            object.put("time", timeJSONObject);
            object.put("content", contentJSONObject);
            object.put("name", nameJSONObject);
            object.put("nametag", nameTagJSONObject);
            object.put("colortag", colorTagJSONObject);
            object.put("autoupdatecheck", autoUpdateCheck);
            
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFileName), "utf-8"));
            writer.write(object.toString());
            writer.close();
        } catch (JSONException | IOException e) {
            JOptionPane.showMessageDialog(null, "Einstellungen konnten nicht gespeichert werden!\r\n" + e.toString());
        }
    }
}
