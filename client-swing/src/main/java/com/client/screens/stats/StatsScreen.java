package com.client.screens.stats;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import com.client.components.BottomNavigationBar;
import com.client.components.stats.LegendBuilder;
import com.client.components.stats.PieChartPanel;
import com.client.components.stats.StatsAggregator;
import com.client.components.stats.StatsLayout;
import com.client.components.stats.StatsViewRefresher;
import com.client.components.timeseries.TimeSeriesChartPanel;
import com.client.core.BasePanel;

public class StatsScreen extends BasePanel {

    // Bottom navigation bar
    public BottomNavigationBar bottomNav;

    // Mode selector dropdown (Month / Year / Total)
    public JComboBox<String> timePeriodDropdown;

    // Month / Year period selector arrows + label
    public JButton prevPeriodBtn;
    public JButton nextPeriodBtn;
    public JLabel periodLabel;

    // Income / Expense toggle buttons
    public JToggleButton incomeToggleButton;
    public JToggleButton expenseToggleButton;

    // Pie chart + legend
    public PieChartPanel pieChartPanel;
    public JPanel legendListPanel;
    public JScrollPane legendScrollPane;

    // Time series chart
    public TimeSeriesChartPanel timeSeriesChartPanel;

    // Top tabs (Stats / Timeline)
    public JLabel statsTabLabel;
    public JLabel timeSeriesTabLabel;

    // State + logic helpers
    private final StatsState state;
    private final StatsViewRefresher refresher;

    public StatsScreen() {
        setLayout(null);
        setOpaque(false);

        this.state = new StatsState();

        StatsAggregator aggregator = new StatsAggregator();
        LegendBuilder legendBuilder = new LegendBuilder();
        this.refresher = new StatsViewRefresher(this, state, aggregator, legendBuilder);

        StatsUIBuilder uiBuilder = new StatsUIBuilder();
        uiBuilder.build(this, state);

        StatsController controller = new StatsController(this, state, refresher);
        controller.wireEvents();

        // Initial refresh to render charts & legend
        refresher.refresh();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        new StatsLayout().layout(this);
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
