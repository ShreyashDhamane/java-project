package com.client.components.stats;

import com.client.screens.stats.StatsScreen;

public class StatsLayout {

    public void layout(StatsScreen scr) {
        int w = scr.getWidth();
        int h = scr.getHeight();

        int padding = 20;
        int topBarHeight = 40;
        int modeWidth = 120;
        int toggleHeight = 30;
        int navHeight = 60;

        // Top tabs
        int tabsY = padding;
        int tabHeight = 30;
        int tabWidth = 90;
        int tabGap = 8;
        int tabsX = padding + 150;

        scr.statsTabLabel.setBounds(tabsX, tabsY, tabWidth, tabHeight);
        scr.timeSeriesTabLabel.setBounds(tabsX + tabWidth + tabGap, tabsY, tabWidth + 20, tabHeight);

        // Mode dropdown
        scr.timePeriodDropdown.setBounds(w - padding - modeWidth, tabsY, modeWidth, tabHeight);

        // Period selector
        int periodY = tabsY + tabHeight + 15;
        int arrowBtnWidth = 40;
        int periodWidth = 150;

        int periodX = (w - periodWidth) / 2;
        scr.periodLabel.setBounds(periodX, periodY, periodWidth, topBarHeight);

        scr.prevPeriodBtn.setBounds(periodX - arrowBtnWidth, periodY, arrowBtnWidth, topBarHeight);
        scr.nextPeriodBtn.setBounds(periodX + periodWidth, periodY, arrowBtnWidth, topBarHeight);

        // Income / Expense toggle row
        int typeY = periodY + topBarHeight + 5;
        int typeWidth = 120;
        int gap = 40;
        int centerX = w / 2;

        scr.incomeToggleButton.setBounds(centerX - typeWidth - gap / 2, typeY, typeWidth, toggleHeight);
        scr.expenseToggleButton.setBounds(centerX + gap / 2, typeY, typeWidth, toggleHeight);

        // Chart area
        int chartTop = typeY + toggleHeight + 5;
        int chartHeight = (int) (h * 0.40);
        int chartSize = Math.min(w - 2 * padding, chartHeight);

        int labelPadding = 120;
        int chartPanelHeight = chartSize + labelPadding;
        int chartX = (w - chartSize) / 2;

        int horizontalLabelPadding = 220;
        int verticalLabelPadding = 120;

        scr.pieChartPanel.setBounds(
                chartX - horizontalLabelPadding / 2,
                chartTop,
                chartSize + horizontalLabelPadding,
                chartSize + verticalLabelPadding
        );

        // Legend
        int legendTop = chartTop + chartPanelHeight - 60;
        int legendHeight = h - legendTop - navHeight - 20;
        if (legendHeight < 80) legendHeight = 80;

        scr.legendScrollPane.setBounds(
                padding,
                legendTop,
                w - 2 * padding,
                legendHeight
        );

        // Time-series chart
        int tsHeight = h - chartTop - navHeight - 20;
        if (tsHeight < 200) tsHeight = 200;

        scr.timeSeriesChartPanel.setBounds(
                padding,
                chartTop,
                w - 2 * padding,
                tsHeight
        );

        // Bottom nav
        scr.bottomNav.setBounds(0, h - navHeight, w, navHeight);
        scr.bottomNav.doLayout();
    }
}
