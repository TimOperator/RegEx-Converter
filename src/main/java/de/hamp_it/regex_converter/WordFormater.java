/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

/**
 *
 * @author Tim
 */
public class WordFormater {
    private String fontFamily;
    private int fontSize;
    private boolean fontBold;
    private boolean fontItalic;
    private String color;
    
    public WordFormater(String fontFamily, int fontSize) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontBold = false;
        this.fontItalic = false;
        this.color = "000000";
    }
    public WordFormater(String fontFamily, int fontSize, boolean fontBold, boolean fontItalic) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontBold = fontBold;
        this.fontItalic = fontItalic;
        this.color = "000000";
    }
    public void setFontSize(int size) {
        this.fontSize = size;
    }
    public void setFontFamily(String family) {
        this.fontFamily = family;
    }
    public void setBold(boolean bold) {
        this.fontBold = bold;
    }
    public void setItalic(boolean italic) {
        this.fontItalic = italic;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setStyle(String fontFamily, int fontSize, boolean bold, boolean italic) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontBold = bold;
        this.fontItalic = italic;
    }
    
    public String getFontFamily() {
        return this.fontFamily;
    }
    public int getFontSize() {
        return this.fontSize;
    }
    public boolean getBold() {
        return this.fontBold;
    }
    public boolean getItalic() {
        return this.fontItalic;
    }
    public String getColor() {
        return this.color;
    }
}
