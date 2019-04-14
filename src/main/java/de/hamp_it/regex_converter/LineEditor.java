/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import static java.lang.Math.min;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Tim
 */
public class LineEditor {

    private String line;
    private WordFormater currentWF;
    private final WordFormater contentWF;
    private final WordFormater nameWF;
    private String nameStart = "@";
    private String nameEnd = ":";
    private String colorStart = "%&";
    private String colorSplit = ":";
    private String colorEnd = "&%";
    private HashMap colorMap;
    private int colorNameLength;

    public LineEditor(WordFormater contWF, WordFormater nWF) {
        this.contentWF = contWF;
        this.nameWF = nWF;
        this.setColor("000000");
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setColorMap(HashMap colors) {
        this.colorMap = colors;
    }

    private int getNoSpacePos(String s, int no) {
        int pos = 0;
        for (int i = 0; i < no; i++) {
            if (s.substring(pos).contains(" ")) {
                pos = s.indexOf(s, pos);
            } else  {
                if (i == 0) {
                    pos = 100000;
                }
                break;
            }
        }
        return pos;
    }

    public void setParams(String nameStartString, String nameEndString, String colorStartString, String colorSplitString, String colorEndString) {
        nameStart = nameStartString;
        nameEnd = nameEndString;
        colorStart = colorStartString;
        colorSplit = colorSplitString;
        colorEnd = colorEndString;
    }

    public String getContent() {
        currentWF = contentWF;
        String result;

        //Wenn kein Prüfzeichen (mehr) vorkommt
        if (!line.contains(nameStart) && !line.contains(colorStart) && !line.contains(colorEnd)) {
            result = line;
            line = "";
            currentWF = contentWF;
        } //Wenn erstes Zeichen Namen-Prüfzeichen ist
        else if (line.substring(0, nameStart.length()).equals(nameStart)) {
            int startPos = nameStart.length();
            int endPos = line.indexOf(nameEnd) + nameEnd.length();
            int aendPos = getNoSpacePos(line, 3);
            endPos = min(endPos, aendPos);
            if (endPos == -1 + nameEnd.length() && line.contains(" ")) {
                endPos = line.indexOf(" ");
            } else if (endPos == -1 + nameEnd.length()) {
                endPos = line.length();
            }
            result = line.substring(startPos, endPos);
            line = line.substring(endPos);
            currentWF = nameWF;
        } //Wenn erstes Zeichen Farben-Setz-Prüfzeichen ist
        else if (line.substring(0, colorStart.length()).equals(colorStart)) {
            int startPos = colorStart.length();
            int splitPos = line.indexOf(colorSplit);
            int asplitPos = getNoSpacePos(line, 2);
            //splitPos = min(splitPos, asplitPos);
            if (splitPos == -1) {
                if (line.contains(" ")) {
                    splitPos = line.indexOf(" ");
                } else {
                    splitPos = line.length();
                }
            }
            String color = line.substring(startPos, splitPos);
            if (color.contains(nameStart)) {
                splitPos = line.indexOf(nameStart);
                color = line.substring(startPos, splitPos);
                splitPos = splitPos - 1;
            }
            color = colorToRGB(color);
            this.setColor(color);
            splitPos = splitPos - colorNameLength;
            line = line.substring(splitPos + 1);
            result = "";
        } //Wenn erstes Zeichen Farben-Rücksetz-Prüfzeichen ist
        else if (line.substring(0, colorEnd.length()).equals(colorEnd)) {
            this.setColor("000000");
            line = line.substring(colorEnd.length());
            result = "";
        } //Wenn später erst ein Prüfzeichen kommt
        else {
            int startPos = 0;
            int endPos;
            int nameStartPos = line.indexOf(nameStart);
            int colorStartPos = line.indexOf(colorStart);
            int colorEndPos = line.indexOf(colorEnd);
            if (nameStartPos == -1) {
                nameStartPos = 10000;
            }
            if (colorStartPos == -1) {
                colorStartPos = 10000;
            }
            if (colorEndPos == -1) {
                colorEndPos = 10000;
            }
            endPos = min(colorStartPos, colorEndPos);
            endPos = min(endPos, nameStartPos);
            result = line.substring(startPos, endPos);
            line = line.substring(endPos);
            currentWF = contentWF;
        }
        return result;
    }

    private String colorToRGB(String colorName) {
        String rgb;
        rgb = (String) colorMap.get(colorName);
        if (rgb == null) {
            // Set color to default black, if color is not found
            rgb = "000000";
        }
        colorNameLength = 0;
        if (rgb == null) {
            Set<String> set = colorMap.keySet();
            rgb = "8C8C8C";
            colorNameLength = colorName.length() + 1;
            for (String s : set) {
                if (colorName.contains(s)) {
                    rgb = (String) colorMap.get(s);
                    colorNameLength = colorName.length() - s.length() + 1;
                    break;
                }
            }
        }
        return rgb;
    }

    public boolean hastContent() {
        return line.length() > 0;
    }

    private void setColor(String color) {
        contentWF.setColor(color);
        nameWF.setColor(color);
    }

    public WordFormater getCurrentWF() {
        return currentWF;
    }
}
