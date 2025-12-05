package com.client.screens.settings;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.client.components.BottomNavigationBar;
import com.client.components.settings.CurrencyDropdownUI;
import com.client.components.settings.SettingsComponents;
import com.client.constants.UIFonts;
import com.client.core.AppState;
import com.client.model.Currency;

public class SettingsUIBuilder {

    public void build(SettingsScreen scr) {

        // Title
        scr.titleLabel = new JLabel("Settings");
        scr.titleLabel.setFont(UIFonts.TITLE);
        scr.titleLabel.setForeground(new Color(245,245,255));
        scr.add(scr.titleLabel);

        // Currency label
        scr.currencyLabel = new JLabel("Default Currency");
        scr.currencyLabel.setFont(UIFonts.TEXT_BOLD);
        scr.currencyLabel.setForeground(Color.WHITE);
        scr.add(scr.currencyLabel);

        // Currency dropdown
        java.util.List<Currency> list = AppState.getInstance().getCurrencies();
        String[] items = list.stream()
                .map(c -> c.getCode() + " " + c.getSymbol())
                .toArray(String[]::new);

        scr.currencyDropdown = new JComboBox<>(items);
        scr.currencyDropdown.setFont(UIFonts.TEXT);
        scr.currencyDropdown.setFocusable(false);
        scr.currencyDropdown.setEditable(false);

        CurrencyDropdownUI.apply(scr.currencyDropdown);

        restoreSavedCurrency(scr, items);

        scr.add(scr.currencyDropdown);

        // Buttons
        scr.categoriesBtn    = SettingsComponents.primaryButton("Manage Categories");
        scr.logoutBtn        = SettingsComponents.dangerButton("Logout");
        scr.deleteAccountBtn = SettingsComponents.dangerButton2("Delete Account");

        scr.add(scr.categoriesBtn);
        scr.add(scr.logoutBtn);
        scr.add(scr.deleteAccountBtn);

        // Bottom nav
        scr.bottomNav = new BottomNavigationBar("Settings");
        scr.add(scr.bottomNav);
    }

    private void restoreSavedCurrency(SettingsScreen scr, String[] items) {
        String saved = AppState.getInstance().getCurrencyCode();

        for (int i = 0; i < items.length; i++) {
            if (items[i].startsWith(saved)) {
                scr.currencyDropdown.setSelectedIndex(i);
                break;
            }
        }
    }

    public void layout(SettingsScreen scr) {

        int w = scr.getWidth();
        int padding = 20;
        int btnW = w - padding * 2;
        int btnH = 45;

        int y = 80;

        scr.titleLabel.setBounds(20,20,300,40);

        scr.currencyLabel.setBounds(padding, y, btnW, 25);
        y += 35;

        scr.currencyDropdown.setBounds(padding, y, btnW, 40);
        y += 60;

        scr.categoriesBtn.setBounds(padding, y, btnW, btnH);
        y += btnH + 20;

        scr.logoutBtn.setBounds(padding, y, btnW, btnH);
        y += btnH + 15;

        scr.deleteAccountBtn.setBounds(padding, y, btnW, btnH);

        scr.bottomNav.setBounds(0, scr.getHeight() - 60, w, 60);
        scr.bottomNav.doLayout();
    }
}
