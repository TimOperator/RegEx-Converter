/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hamp_it.regex_converter;

import javax.swing.UIManager;

/**
 *
 * @author Tim
 */
public class RegExConverter {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //Doesn't matter
        }
        MainJFrame userFrame = new MainJFrame();
        userFrame.setVisible(true);
    }
    
}
