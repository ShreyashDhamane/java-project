package com.client.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.components.ArrowButton;
import com.client.components.BottomNavigationBar;
import com.client.components.CustomScrollBar;
import com.client.components.StatsScreenComponents;
import com.client.components.StatsScreenComponents.CategoryLegendItem;
import com.client.components.StatsScreenComponents.EmojiMapper;
import com.client.components.StatsScreenComponents.PieSlice;
import com.client.components.StatsScreenComponents.PieChartPanel;
import com.client.components.TimeSeriesChartPanel;
import com.client.constants.StatsScreenConstants;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.model.DataEntry;

public class StatsScreen extends BasePanel {
    // bottom navigation bar
    private BottomNavigationBar bottomNav;

    // mode selector, we hvae dropdown for Month/Year/Total
    private JComboBox<String> timePeriodDropdown;

    // month/year selector
    private JButton prevPeriodBtn;
    private JButton nextPeriodBtn;
    private JLabel periodLabel;

    // income/expense toggle
    private JToggleButton incomeToggleButton;
    private JToggleButton expenseToggleButton;

    // chart + legend
    private PieChartPanel pieChartPanel;
    private JPanel legendListPanel;
    private JScrollPane legendScrollPane;

    // timeseries chart panel
    private TimeSeriesChartPanel timeSeriesChartPanel;

    // state
    private StatsScreenConstants.AggregationMode mode = StatsScreenConstants.AggregationMode.MONTH;
    private LocalDate currentDate = LocalDate.now(); // drives month/year
    private boolean showIncome = false; // or expense, false is default, == expense

    // top tabs
    private JLabel statsTabLabel;
    private JLabel timeSeriesTabLabel;
    private boolean showTimeSeries = false; // default tab -> Stats

    public StatsScreen() {
        setLayout(null);
        setOpaque(false);
        // 
        createComponents();
        refreshData();
    }

    // create and setup all components
    private void createComponents() {

        // top tabs stats and timeseries
        statsTabLabel = StatsScreenComponents.createTopTabLabel("Stats", true); // default active
        timeSeriesTabLabel = StatsScreenComponents.createTopTabLabel("Timeline", false);

        // event listeners for tabs
        statsTabLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showTimeSeries = false;
                // update UI for tab
                updateTabStyles();
                // update view visibility ,that is show/hide charts and legends
                updateViewVisibility();
            }
        });

        timeSeriesTabLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showTimeSeries = true;
                // update UI for tab
                updateTabStyles();
                // update
                updateViewVisibility();
            }
        });
        // 
        add(statsTabLabel);
        add(timeSeriesTabLabel);

        // dropdwn to change time period mode
        timePeriodDropdown = new JComboBox<>(new String[]{"Month", "Year", "Total"});
        timePeriodDropdown.setFont(UIFonts.TEXT);
        // default style
        UIStyle.styleDarkDropdown(timePeriodDropdown);

        timePeriodDropdown.setFocusable(false);
        timePeriodDropdown.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        timePeriodDropdown.setBackground(new Color(35, 45, 65)); // dark bg
        timePeriodDropdown.setForeground(Color.WHITE); // white text

        // remove default editor border, we want transparent bg
        ((JTextField) timePeriodDropdown.getEditor().getEditorComponent()).setOpaque(false);
        ((JTextField) timePeriodDropdown.getEditor().getEditorComponent()).setBorder(null);

        // Better rendering for dropdown items,
        // list of items in dropdown
        timePeriodDropdown.setRenderer(new javax.swing.DefaultListCellRenderer() {
            // component to render each item of the list for dropdown
            @Override
            public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {

                // default label from super
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
                );

                // customize colors + font + padding
                lbl.setOpaque(true);
                lbl.setForeground(Color.WHITE);
                // selected item bg different color
                lbl.setBackground(isSelected ? new Color(60, 70, 90) : new Color(35, 45, 65));
                lbl.setFont(UIFonts.TEXT);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));

                return lbl;
            }
        });

        // event listener for mode change for dropdown item selection
        timePeriodDropdown.addActionListener(e -> {
            String selected = (String) timePeriodDropdown.getSelectedItem();

            switch (selected) {
                case "Month" -> mode = StatsScreenConstants.AggregationMode.MONTH;
                case "Year"  -> mode = StatsScreenConstants.AggregationMode.YEAR;
                case "Total" -> mode = StatsScreenConstants.AggregationMode.TOTAL;
            }

            refreshData();
        });

        add(timePeriodDropdown);

        // period selector left and right arrows
        prevPeriodBtn = new ArrowButton(true);
        nextPeriodBtn = new ArrowButton(false);

        periodLabel = new JLabel("", SwingConstants.CENTER);
        periodLabel.setFont(UIFonts.TEXT_BOLD);
        periodLabel.setForeground(Color.WHITE);

        add(prevPeriodBtn);
        add(nextPeriodBtn);
        add(periodLabel);

        // defcrease or increase month/year based on mode
        prevPeriodBtn.addActionListener(e -> {
            switch (mode) {
                case MONTH -> currentDate = currentDate.minusMonths(1);
                case YEAR  -> currentDate = currentDate.minusYears(1);
                case TOTAL -> { /* no-op */ }
            }
            refreshData();
        });

        nextPeriodBtn.addActionListener(e -> {
            switch (mode) {
                case MONTH -> currentDate = currentDate.plusMonths(1);
                case YEAR  -> currentDate = currentDate.plusYears(1);
                case TOTAL -> { /* no-op */ }
            }
            refreshData();
        });

        // toggle button to switch between income and expense data
        incomeToggleButton = StatsScreenComponents.createToggleButton("Income");
        expenseToggleButton = StatsScreenComponents.createToggleButton("Expense");
        // create button group so only one is selected at a time
        ButtonGroup typeGroup = new ButtonGroup();

        typeGroup.add(incomeToggleButton);
        typeGroup.add(expenseToggleButton);

        // default: Expense 
        expenseToggleButton.setSelected(true);
        showIncome = false;
        updateTypeToggleStyles();

        // action listeners
        incomeToggleButton.addActionListener(e -> {
            showIncome = true;
            updateTypeToggleStyles();
            refreshData();
        });
        expenseToggleButton.addActionListener(e -> {
            showIncome = false;
            updateTypeToggleStyles();
            refreshData();
        });

        add(incomeToggleButton);
        add(expenseToggleButton);

        // pie chart panel
        pieChartPanel = new PieChartPanel();
        add(pieChartPanel);

        // time-series chart panel
        timeSeriesChartPanel = new TimeSeriesChartPanel();
        add(timeSeriesChartPanel);

        // legend list panel at bottom
        legendListPanel = new JPanel();
        legendListPanel.setOpaque(false);
        legendListPanel.setLayout(new BoxLayout(legendListPanel, BoxLayout.Y_AXIS));

        legendScrollPane = new JScrollPane(legendListPanel);
        legendScrollPane.setBorder(null);
        legendScrollPane.setOpaque(false);
        legendScrollPane.getViewport().setOpaque(false);
        legendScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        legendScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        legendScrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

        add(legendScrollPane);

        // bottom navigation bar
        bottomNav = new BottomNavigationBar("Charts");
        add(bottomNav);

        // initial visibility/state
        updateTabStyles();
        updateViewVisibility();
    }

    private void updateTypeToggleStyles() {
        Color selectedColor = new Color(200, 200, 200);
        
        if (showIncome) {
            incomeToggleButton.setForeground(Color.WHITE);
            expenseToggleButton.setForeground(selectedColor);
        } else {
            expenseToggleButton.setForeground(Color.WHITE);
            incomeToggleButton.setForeground(selectedColor);
        }
    }

    private void updateTabStyles() {
        Color activeBg = new Color(255, 100, 90);
        Color inactiveBg = new Color(30, 35, 48);
        Color inactiveFg = new Color(180, 180, 180);

        if (!showTimeSeries) {
            // Stats active
            statsTabLabel.setBackground(activeBg);
            statsTabLabel.setForeground(Color.WHITE);

            timeSeriesTabLabel.setBackground(inactiveBg);
            timeSeriesTabLabel.setForeground(inactiveFg);
        } else {
            // Time Series active
            timeSeriesTabLabel.setBackground(activeBg);
            timeSeriesTabLabel.setForeground(Color.WHITE);

            statsTabLabel.setBackground(inactiveBg);
            statsTabLabel.setForeground(inactiveFg);
        }
    }

    // show/hide pie chart + legend or time-series chart based on selected tab
    private void updateViewVisibility() {
        boolean statsMode = !showTimeSeries;

        pieChartPanel.setVisible(statsMode);
        legendScrollPane.setVisible(statsMode);

        timeSeriesChartPanel.setVisible(!statsMode);

        revalidate();
        repaint();
    }

    // place and size all components
    @Override
    public void doLayout() {
        super.doLayout();
        int w = getWidth();
        int h = getHeight();

        int padding = 20;
        int topBarHeight = 40;
        int modeWidth = 120;
        int toggleHeight = 30;
        int navHeight = 60;

        // Top segmented tabs (left side)
        int tabsY = padding;
        int tabHeight = 30;
        int tabWidth = 90;
        int tabGap = 8;
        int tabsX = padding + 150;

        statsTabLabel.setBounds(tabsX, tabsY, tabWidth, tabHeight);
        timeSeriesTabLabel.setBounds(tabsX + tabWidth + tabGap, tabsY, tabWidth + 20, tabHeight);

        // Mode combo (M/Y/T) at top-right
        timePeriodDropdown.setBounds(w - padding - modeWidth, tabsY, modeWidth, tabHeight);

        // Period selector row
        int periodY = tabsY + tabHeight + 15;
        int arrowBtnWidth = 40;
        int periodWidth = 150;

        int periodX = (w - periodWidth) / 2;
        periodLabel.setBounds(periodX, periodY, periodWidth, topBarHeight);

        prevPeriodBtn.setBounds(periodX - arrowBtnWidth, periodY, arrowBtnWidth, topBarHeight);
        nextPeriodBtn.setBounds(periodX + periodWidth, periodY, arrowBtnWidth, topBarHeight);

        // Income / Expense toggle row
        int typeY = periodY + topBarHeight + 5;
        int typeWidth = 120;
        int gap = 40;
        int centerX = w / 2;

        incomeToggleButton.setBounds(centerX - typeWidth - gap / 2, typeY, typeWidth, toggleHeight);
        expenseToggleButton.setBounds(centerX + gap / 2, typeY, typeWidth, toggleHeight);

        // Chart area
        int chartTop = typeY + toggleHeight + 5;
        int chartHeight = (int) (h * 0.40);
        int chartSize = Math.min(w - 2 * padding, chartHeight);

        int labelPadding = 120; // extra space for labels around pie
        int chartPanelHeight = chartSize + labelPadding;

        int chartX = (w - chartSize) / 2;

        int horizontalLabelPadding = 220;   // increased to avoid clipping
        int verticalLabelPadding   = 120;   // keep this as-is

        pieChartPanel.setBounds(
                chartX - horizontalLabelPadding / 2,
                chartTop,
                chartSize + horizontalLabelPadding,
                chartSize + verticalLabelPadding
        );

        // Legend area starts after pie panel
        int legendTop = chartTop + chartPanelHeight - 60;
        int legendHeight = h - legendTop - navHeight - 20;
        if (legendHeight < 80) legendHeight = 80;

        legendScrollPane.setBounds(
                padding,
                legendTop,
                w - 2 * padding,
                legendHeight
        );

        // Time-series chart shares the vertical chart area
        int tsHeight = h - chartTop - navHeight - 20;  
        if (tsHeight < 200) tsHeight = 200; // minimum safe height

        timeSeriesChartPanel.setBounds(
                padding,
                chartTop,
                w - 2 * padding,
                tsHeight
        );

        // Bottom nav
        bottomNav.setBounds(0, h - navHeight, w, navHeight);
        bottomNav.doLayout();
    }

    // ==========================================================
    // DATA AGGREGATION
    // ==========================================================
    private void refreshData() {
        List<DataEntry> all = AppState.getInstance().getEntries();
        if (all == null) all = Collections.emptyList();

        // filter by mode + type
        Map<String, Long> categoryTotals = new LinkedHashMap<>();

        YearMonth selectedMonth = YearMonth.from(currentDate);
        int selectedYear = currentDate.getYear();

        for (DataEntry e : all) {
            if (e == null || e.getDate() == null) continue;

            // type filter
            boolean isIncome = "Income".equalsIgnoreCase(e.getType());
            if (showIncome && !isIncome) continue;
            if (!showIncome && isIncome) continue;

            LocalDate d;
            try {
                d = LocalDate.parse(e.getDate(), StatsScreenConstants.DATE_IN);
            } catch (Exception ex) {
                continue;
            }

            // mode filter
            boolean matches = switch (mode) {
                case MONTH -> YearMonth.from(d).equals(selectedMonth);
                case YEAR  -> d.getYear() == selectedYear;
                case TOTAL -> true;
            };
            if (!matches) continue;

            String category = e.getCategory() != null ? e.getCategory() : "Other";
            long amount = e.getAmount();

            categoryTotals.merge(category, amount, Long::sum);
        }

        // sort categories by amount desc
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(categoryTotals.entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        long totalAmount = 0;
        for (Map.Entry<String, Long> e : sorted) {
            totalAmount += e.getValue();
        }

        // prepare slices for chart
        List<PieSlice> slices = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, Long> e : sorted) {
            double percent = (totalAmount == 0) ? 0 : (e.getValue() * 1.0 / totalAmount);
            Color color = PieChartPanel.SLICE_COLORS[colorIndex % PieChartPanel.SLICE_COLORS.length];
            String emoji = EmojiMapper.getEmojiForCategory(e.getKey());
            slices.add(new PieSlice(e.getKey(), emoji, e.getValue(), percent, color));
            colorIndex++;
        }

        // update pie chart
        String centerLabel = (showIncome ? "Income" : "Exp.") + " " + StatsScreenConstants.AMOUNT_FMT.format(totalAmount);
        pieChartPanel.setData(slices, centerLabel);

        // update legend list
        rebuildLegendList(slices, totalAmount);

        // update period label text
        switch (mode) {
            case MONTH -> periodLabel.setText(selectedMonth.format(StatsScreenConstants.MONTH_OUT));
            case YEAR  -> periodLabel.setText(currentDate.format(StatsScreenConstants.YEAR_OUT));
            case TOTAL -> periodLabel.setText("Total");
        }

        // enable/disable arrows in TOTAL mode
        boolean arrowsEnabled = (mode != StatsScreenConstants.AggregationMode.TOTAL);
        prevPeriodBtn.setEnabled(arrowsEnabled);
        nextPeriodBtn.setEnabled(arrowsEnabled);
        prevPeriodBtn.setForeground(arrowsEnabled ? Color.WHITE : new Color(100, 100, 100));
        nextPeriodBtn.setForeground(arrowsEnabled ? Color.WHITE : new Color(100, 100, 100));

        // ==================================================
        // TIME SERIES DATA (driven by SAME filters)
        // ==================================================
        Map<String, Long> seriesData = new LinkedHashMap<>();

        for (DataEntry e : all) {
            if (e == null || e.getDate() == null) continue;

            boolean isIncome = "Income".equalsIgnoreCase(e.getType());
            if (showIncome && !isIncome) continue;
            if (!showIncome && isIncome) continue;

            LocalDate d;
            try {
                d = LocalDate.parse(e.getDate(), StatsScreenConstants.DATE_IN);
            } catch (Exception ex) {
                continue;
            }

            boolean matches = switch (mode) {
                case MONTH -> YearMonth.from(d).equals(YearMonth.from(currentDate));
                case YEAR  -> d.getYear() == currentDate.getYear();
                case TOTAL -> true;
            };
            if (!matches) continue;

            String key;
            if (mode == StatsScreenConstants.AggregationMode.MONTH) {
                // group by day
                key = String.valueOf(d.getDayOfMonth());
            } else if (mode == StatsScreenConstants.AggregationMode.YEAR) {
                // group by month
                key = d.getMonth().name().substring(0, 3);
            } else {
                // TOTAL -> group by year
                key = String.valueOf(d.getYear());
            }

            seriesData.merge(key, (long) e.getAmount(), Long::sum);
        }

        // Title text for time-series chart
        String typeText = showIncome ? "Income" : "Expense";
        String periodText = switch (mode) {
            case MONTH -> selectedMonth.format(StatsScreenConstants.MONTH_OUT); // "Nov 2025"
            case YEAR  -> currentDate.format(StatsScreenConstants.YEAR_OUT);    // "2025"
            case TOTAL -> "All Years";
        };
        String titleText = typeText + " \u2013 " + periodText;

        timeSeriesChartPanel.setData(seriesData, titleText);

        // keep tabs + visibility in sync with current mode
        updateTabStyles();
        updateViewVisibility();
    }

    private void rebuildLegendList(List<PieSlice> slices, long totalAmount) {
        legendListPanel.removeAll();

        if (slices.isEmpty() || totalAmount == 0) {
            // no data, keep it empty
        } else {
            for (PieSlice slice : slices) {
                double percent = slice.percent;
                String percentText = StatsScreenConstants.PERCENT_FMT.format(percent * 100.0);
                String amountText  = StatsScreenConstants.AMOUNT_FMT.format(slice.amount);
                CategoryLegendItem item = new CategoryLegendItem(
                        slice.color,
                        percentText,
                        slice.emoji,
                        slice.category,
                        amountText
                );
                legendListPanel.add(item);
                legendListPanel.add(Box.createVerticalStrut(4));
            }
        }

        legendListPanel.revalidate();
        legendListPanel.repaint();
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
