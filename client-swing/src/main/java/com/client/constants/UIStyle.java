package com.client.constants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

// this class is to style the common UI components used across the app like buttons, labels, textfields, dropdowns.
// its just for styling purpose to maintain consistency across the app
public class UIStyle {

    private static final Color DARK_BG = new Color(35, 45, 65);
    private static final Color DARK_BG_SELECTED = new Color(60, 70, 90);
    private static final Color BUTTON_PRIMARY = new Color(255, 100, 90);   // orange red color
    private static final Color BUTTON_SECONDARY = new Color(255, 150, 130); // lighter orange
    private static final int BUTTON_ARC_WIDTH_HEIGHT = 12;

    // function to style any text field, just pass the text field reference
    public static void styleTextField(JTextField field) {

        field.setFont(UIFonts.TEXT); // set font
        field.setForeground(Color.WHITE); // text color
        field.setCaretColor(Color.WHITE); // caret color
        field.setOpaque(false); // allow custom painting
        // padding inside the text field
        field.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        // set UI is used to override the default painting behavior of the text field
        field.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); // 2d graphics
                // smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // rounded background with orange outline
                g2.setColor(DARK_BG);
                // 
                g2.fillRoundRect(0, 0, field.getWidth(), field.getHeight(), BUTTON_ARC_WIDTH_HEIGHT, BUTTON_ARC_WIDTH_HEIGHT);

                // subtle outline
                g2.setColor(DARK_BG_SELECTED);
                g2.drawRoundRect(0, 0, field.getWidth() - 1, field.getHeight() - 1, BUTTON_ARC_WIDTH_HEIGHT, BUTTON_ARC_WIDTH_HEIGHT);
                g2.dispose();

                // paint text, caret, etc.
                super.paintSafely(g);
            }
        });
    }

    // function to style password field, just pass the password field reference
    // its same as text field but with masked characters
    public static void stylePasswordField(JPasswordField field) {

        field.setForeground(Color.WHITE); // text color
        field.setFont(UIFonts.TEXT); // set font
        field.setCaretColor(Color.WHITE);
        field.setEchoChar('•'); // mask password
        field.setOpaque(false); // 
        field.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        // custom paint for password field
        field.setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // rounded background with defailt outline
                g2.setColor(DARK_BG);
                g2.fillRoundRect(0, 0, field.getWidth(), field.getHeight(), BUTTON_ARC_WIDTH_HEIGHT, BUTTON_ARC_WIDTH_HEIGHT);

                // subtle outline
                g2.setColor(DARK_BG_SELECTED);
                g2.drawRoundRect(0, 0, field.getWidth() - 1, field.getHeight() - 1, BUTTON_ARC_WIDTH_HEIGHT, BUTTON_ARC_WIDTH_HEIGHT);
                g2.dispose();

                super.paintSafely(g); // now Swing renders masked characters correctly
            }
        });
    }


    // function to style label used in forms and sections
    public static JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);

        lbl.setFont(UIFonts.TEXT_BOLD);
        lbl.setForeground(Color.WHITE);
        
        return lbl;
    }

    // function to style title label
    public static JLabel titleLabel(String text) {
        JLabel lbl = new JLabel(text);

        lbl.setFont(UIFonts.TITLE);
        lbl.setForeground(Color.WHITE);

        return lbl;
    }

    // function to style subtitle label
    public static JLabel subtitleLabel(String text) {
        JLabel lbl = new JLabel(text);

        lbl.setFont(UIFonts.SUBTITLE);
        lbl.setForeground(new Color(200, 200, 210));  // soft light gray

        return lbl;
    }

    // function to create primary button with standard styling
    public static JButton createPrimaryButton(String text) {
        return createButton(text, BUTTON_PRIMARY);
    }

    // function to create secondary button with standard styling
    public static JButton createSecondaryButton(String text) {
        return createButton(text, BUTTON_SECONDARY);
    }

    // function to create a button with custom background color
    private static JButton createButton(String text, Color bgColor) {

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); // same as usual
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON
                ); // smooth edges

                g2.setColor(bgColor); // bg color
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_ARC_WIDTH_HEIGHT, BUTTON_ARC_WIDTH_HEIGHT); // rounded rect

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        btn.setOpaque(false); // make button background transparent
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); //
        btn.setContentAreaFilled(false); // no content area fill
        btn.setForeground(Color.WHITE);
        btn.setFont(UIFonts.TEXT_BOLD);

        return btn;
    }

    // function to style dropdowns with dark theme for stats and dashboard filtering page
    public static void styleDarkDropdown(JComboBox<?> combo) {
        combo.setFont(UIFonts.TEXT);
        combo.setFocusable(false); // disable focus to prevent focus border
        combo.setEditable(false); // non editable dropdown

        combo.setBackground(DARK_BG); // dark bg
        combo.setForeground(Color.WHITE); // text color
        combo.setBorder(new EmptyBorder(0,0,0,0));

        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                // custom arrow button for dropdown up and down, diff then arrow button
                JButton btn = new JButton("▼");
                
                btn.setForeground(Color.WHITE); // white arrow
                btn.setBackground(DARK_BG); // match bg
                btn.setBorder(BorderFactory.createEmptyBorder()); // no border
                btn.setFocusPainted(false);

                return btn;
            }

            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                g.setColor(DARK_BG); // fill bg
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                
                super.paint(g, c);
            }
        });

        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                // custom rendering for dropdown items
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
                );

                lbl.setOpaque(true); // allow custom bg
                lbl.setForeground(Color.WHITE); // white text
                lbl.setBackground(isSelected ? DARK_BG_SELECTED : DARK_BG);
                lbl.setFont(UIFonts.TEXT); // set font
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));

                return lbl;
            }
        });
    }

}


