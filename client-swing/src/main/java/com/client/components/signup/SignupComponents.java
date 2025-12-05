package com.client.components.signup;

import javax.swing.JLabel;

import com.client.constants.UIColors;
import com.client.constants.UIFonts;

public class SignupComponents {

    public static JLabel statusLabel() {
        JLabel lbl = new JLabel("", JLabel.LEFT);
        lbl.setFont(UIFonts.TEXT);
        lbl.setForeground(UIColors.ERROR);
        return lbl;
    }
}
