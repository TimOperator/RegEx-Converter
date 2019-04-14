/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hamp_it.regex_converter;

import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author Tim
 */
public class WordExporter {

    private final String filename;
    private XWPFDocument document;
    private XWPFParagraph paragraph;

    public WordExporter(String filename) {
        this.filename = filename;
    }

    public void open() {
        document = new XWPFDocument();
        paragraph = document.createParagraph();
    }
    
    public void addText(String text, WordFormater wf) {
        this.addText(text, wf, false);
    }

    public void addText(String text, WordFormater wf, boolean addBreak) {
        XWPFRun paragraphOne = paragraph.createRun();
        paragraphOne.setFontFamily(wf.getFontFamily());
        paragraphOne.setFontSize(wf.getFontSize());
        paragraphOne.setBold(wf.getBold());
        paragraphOne.setItalic(wf.getItalic());
        paragraphOne.setColor(wf.getColor());
        paragraphOne.setText(text);
        if (addBreak) {
            paragraphOne.addBreak();
        }
    }

    public void close() throws Exception {
        try (FileOutputStream output = new FileOutputStream(filename)) {
            document.write(output);
        }
    }
}