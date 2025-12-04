package com.client.screens;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.client.components.BottomNavigationBar;
import com.client.constants.Constants;
import com.client.constants.UIFonts;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.Currency;
import com.client.utils.HttpClient;

public class SettingsScreen extends BasePanel {

    private final BottomNavigationBar bottomNav;

    private final JButton categoriesBtn;
    private final JButton logoutBtn;
    private final JButton deleteAccountBtn;

    private JComboBox<String> currencyDropdown;
    private JLabel currencyLabel;

    public SettingsScreen() {
        setLayout(null);
        setOpaque(false);

        // page title
        JLabel title = new JLabel("Settings");

        title.setFont(UIFonts.TITLE);
        title.setForeground(new Color(245, 245, 255));
        title.setBounds(20, 20, 400, 40);
        
        add(title);



        // currency label
        currencyLabel = new JLabel("Default Currency");
        currencyLabel.setFont(UIFonts.TEXT_BOLD);
        currencyLabel.setForeground(Color.WHITE);

        add(currencyLabel);

        // currency dropdown
        List<Currency> list = AppState.getInstance().getCurrencies();
        String[] dropdownItems = list.stream()
                .map(c -> c.getCode() + " " + c.getSymbol())
                .toArray(String[]::new);

        currencyDropdown = new JComboBox<>(dropdownItems);

        currencyDropdown.setFont(UIFonts.TEXT);
        currencyDropdown.setFocusable(false);
        currencyDropdown.setEditable(false);
        currencyDropdown.setBackground(new Color(35, 45, 65));
        currencyDropdown.setForeground(Color.WHITE);
        currencyDropdown.setBorder(BorderFactory.createEmptyBorder());

        // CUSTOM UI FOR DROPDOWN with custom arrow button
        currencyDropdown.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(35, 45, 65));
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusPainted(false);
                return btn;
            }

            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                g.setColor(new Color(35, 45, 65));
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                super.paint(g, c);
            }
        });

        // CUSTOM RENDERER FOR DROPDOWN ITEMSd
        currencyDropdown.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                lbl.setOpaque(true);
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(isSelected ? new Color(60, 70, 90)
                                             : new Color(35, 45, 65));
                lbl.setFont(UIFonts.TEXT);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));

                return lbl;
            }
        });

        // Restore saved currency
        String saved = AppState.getInstance().getCurrencyCode();
        for (int i = 0; i < dropdownItems.length; i++) {
            if (dropdownItems[i].startsWith(saved)) {
                currencyDropdown.setSelectedIndex(i);
                break;
            }
        }

        // Save when changed
        currencyDropdown.addActionListener(e -> {
            String item = (String) currencyDropdown.getSelectedItem();
            if (item != null) {
                String code = item.split(" ")[0];
                AppState.getInstance().setCurrencyCode(code);
            }
        });

        add(currencyDropdown);

        // manage categories button
        categoriesBtn = createButton("Manage Categories");
        categoriesBtn.addActionListener(e -> ScreenManager.show(new ManageCategories()));
        add(categoriesBtn);

        // logout button
        logoutBtn = createButton("Logout");
        logoutBtn.setBackground(new Color(220, 80, 80));
        logoutBtn.addActionListener(e -> {
            AppState.getInstance().reset();
            ScreenManager.show(new LoginScreen());
        });
        add(logoutBtn);

        // delete account button
        deleteAccountBtn = createButton("Delete Account");
        deleteAccountBtn.setBackground(new Color(150, 50, 50));
        deleteAccountBtn.addActionListener(e -> confirmDeleteAccount());
        add(deleteAccountBtn);

        // bottom nav
        bottomNav = new BottomNavigationBar("Settings");
        add(bottomNav);
    }

    // button builder
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIFonts.TEXT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40, 50, 70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    // delete account handling
    private void confirmDeleteAccount() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete your account?\nThis action cannot be undone.",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opt == JOptionPane.YES_OPTION) {
            deleteAccountAsync();
        }
    }

    // delete account in a separate thread
    private void deleteAccountAsync() {
        new Thread(() -> {
            try {
                String username = AppState.getInstance().getUsername();
                String token = AppState.getInstance().getJwtToken();   // 🔥 GET JWT

                // 🔥 AUTHORIZED DELETE REQUEST
                String result = HttpClient.deleteAuthorized(
                    Constants.BASE_URL + "/auth/" + username,
                    token
                );

                if ("OK".equals(result)) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                            this,
                            "Your account has been deleted.",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        AppState.getInstance().reset();
                        ScreenManager.show(new LoginScreen());
                    });
                } else {
                    javax.swing.SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete account. Try again later.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        )
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    // layout
    @Override
    public void doLayout() {
        super.doLayout();

        int w = getWidth();
        int padding = 20;
        int btnWidth = w - padding * 2;
        int btnHeight = 45;

        int y = 80;  // start below the title

        // Currency Label
        currencyLabel.setBounds(padding, y, btnWidth, 25);
        y += 35;

        // Currency Dropdown
        currencyDropdown.setBounds(padding, y, btnWidth, 40);
        y += 60;

        // Manage Categories Button
        categoriesBtn.setBounds(padding, y, btnWidth, btnHeight);
        y += btnHeight + 20;

        // Logout Button
        logoutBtn.setBounds(padding, y, btnWidth, btnHeight);
        y += btnHeight + 15;

        // Delete Account Button
        deleteAccountBtn.setBounds(padding, y, btnWidth, btnHeight);

        // Bottom Navigation
        bottomNav.setBounds(0, getHeight() - 60, w, 60);
        bottomNav.doLayout();
    }


    // background (gradient)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setPaint(new GradientPaint(
                0, 0, new Color(75, 108, 183),
                0, getHeight(), new Color(24, 40, 72)
        ));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
