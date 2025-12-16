package com.client.screens.dashboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.components.ArrowButton;
import com.client.components.BottomNavigationBar;
import com.client.components.CustomScrollBar;
import com.client.components.dashboard.DashboardRowFactory;
import com.client.components.dashboard.DayGroup;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;
import com.client.screens.addentry.AddDataEntryScreen;

public class DashboardScreen extends BasePanel {

    private JButton addButton;
    private JButton prevMonthButton;
    private JButton nextMonthButton;

    private BottomNavigationBar bottomNavigationBar;

    private JScrollPane scrollPane;
    private JPanel listPanel;

    private JLabel monthLabel;
    private JComboBox<Integer> yearDropdown;

    private LocalDate currentDate = LocalDate.now();
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMM yyyy");
    private static final DateTimeFormatter DATE_IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_OUT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");

    public DashboardScreen() {
        setLayout(null);
        setOpaque(false);

        createComponents();
        refreshList();
    }

    private void createComponents() {

        //  Transaction list panel
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 12));

        add(scrollPane);

        // Month navigation
        prevMonthButton = new ArrowButton(true);
        nextMonthButton = new ArrowButton(false);

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(UIFonts.TEXT_BOLD);
        monthLabel.setForeground(Color.WHITE);

        prevMonthButton.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            refreshList();
        });

        nextMonthButton.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            refreshList();
        });

        add(prevMonthButton);
        add(nextMonthButton);
        add(monthLabel);

        // Year Dropdown
        int currentYear = LocalDate.now().getYear();
        List<Integer> years = new ArrayList<>();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) years.add(y);

        yearDropdown = new JComboBox<>(years.toArray(new Integer[0]));
        yearDropdown.setFont(UIFonts.TEXT);
        UIStyle.styleDarkDropdown(yearDropdown);
        yearDropdown.setFocusable(false);
        yearDropdown.setSelectedItem(currentYear);

        yearDropdown.addActionListener(e -> {
            int selectedYear = (int) yearDropdown.getSelectedItem();
            currentDate = YearMonth.of(selectedYear, currentDate.getMonth()).atDay(1);
            refreshList();
        });

        add(yearDropdown);

        // Floating Add Button
        addButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int diameter = Math.min(w, h);

                g2.setColor(new Color(255, 100, 90));
                g2.fillOval(0, 0, diameter, diameter);

                g2.setColor(Color.WHITE);
                int cx = w / 2;
                int cy = h / 2;

                int lineLength = (int) (diameter * 0.45);
                int thickness = 4;

                g2.fillRoundRect(cx - lineLength / 2, cy - thickness / 2, lineLength, thickness, 4, 4);
                g2.fillRoundRect(cx - thickness / 2, cy - lineLength / 2, thickness, lineLength, 4, 4);

                g2.dispose();
            }
        };

        addButton.setBorderPainted(false);
        addButton.setOpaque(false);
        addButton.setContentAreaFilled(false);
        addButton.setFocusPainted(false);

        addButton.addActionListener(e -> ScreenManager.show(new AddDataEntryScreen()));

        add(addButton);
        setComponentZOrder(addButton, 0);

        bottomNavigationBar = new BottomNavigationBar("Dashboard");
        add(bottomNavigationBar);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int w = getWidth();
        int h = getHeight();

        int padding = 20;
        int bottomNavigationBarHeight = 60;

        int btnSize         = 60;
        int btnBottomMargin = 80;

        int filterY = 10;
        int arrowW = 40;
        int filterHeight = 35;
        int monthW = 150;

        int filterX = (w - monthW) / 2;

        // Month label + arrows
        monthLabel.setBounds(filterX, filterY, monthW, filterHeight);
        prevMonthButton.setBounds(filterX - arrowW, filterY, arrowW, filterHeight);
        nextMonthButton.setBounds(filterX + monthW, filterY, arrowW, filterHeight);

        // Year dropdown
        yearDropdown.setBounds(filterX + monthW + arrowW + 20, filterY, 100, filterHeight);

        // List area
        int listTop = filterY + filterHeight + 10;
        int listHeight = h - listTop - (bottomNavigationBarHeight + padding);
        if (listHeight < 100) listHeight = 100;

        scrollPane.setBounds(padding, listTop, w - 2 * padding, listHeight);

        // Floating Add Button
        int btnX = w - btnSize - padding;
        int btnY = h - btnBottomMargin - btnSize;
        addButton.setBounds(btnX, btnY, btnSize, btnSize);

        bottomNavigationBar.setBounds(0, h - bottomNavigationBarHeight, w, bottomNavigationBarHeight);
        bottomNavigationBar.doLayout();
    }

    private void refreshList() {
        listPanel.removeAll();

        List<DataEntry> entries = AppState.getInstance().getEntries();
        if (entries == null) entries = Collections.emptyList();

        YearMonth selected = YearMonth.from(currentDate);
        int selectedYear = (int) yearDropdown.getSelectedItem();
        currentDate = YearMonth.of(selectedYear, currentDate.getMonth()).atDay(1);

        monthLabel.setText(selected.format(MONTH_FMT));

        // Filter by month
        List<DataEntry> filtered = new ArrayList<>();
        for (DataEntry e : entries) {
            if (e == null || e.getDate() == null) continue;
            try {
                LocalDate d = LocalDate.parse(e.getDate(), DATE_IN);
                if (YearMonth.from(d).equals(selected)) filtered.add(e);
            } catch (Exception ignored) {}
        }

        if (filtered.isEmpty()) {
            JLabel lbl = new JLabel("No transactions for this month.");
            lbl.setFont(UIFonts.TEXT);
            lbl.setForeground(Color.WHITE);
            lbl.setBorder(new EmptyBorder(20, 10, 20, 10));
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(10));
            listPanel.add(lbl);
            listPanel.add(Box.createVerticalGlue());
            listPanel.revalidate();
            listPanel.repaint();
            return;
        }

        Map<LocalDate, DayGroup> map = new TreeMap<>(Collections.reverseOrder());

        for (DataEntry e : filtered) {
            try {
                LocalDate d = LocalDate.parse(e.getDate(), DATE_IN);
                DayGroup g = map.computeIfAbsent(d, k -> new DayGroup(k));

                if ("Income".equalsIgnoreCase(e.getType()))
                    g.totalIncome += e.getAmount();
                else
                    g.totalExpense += e.getAmount();

                g.entries.add(e);
            } catch (Exception ignored) {}
        }

        for (DayGroup g : map.values()) {
            listPanel.add(DashboardRowFactory.createDayHeaderRow(g));
            for (DataEntry e : g.entries) {
                listPanel.add(DashboardRowFactory.createTransactionRow(e));
            }
            listPanel.add(Box.createVerticalStrut(8));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

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
