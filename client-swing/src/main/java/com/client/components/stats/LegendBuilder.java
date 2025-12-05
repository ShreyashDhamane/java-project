package com.client.components.stats;

import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

public class LegendBuilder {

    public void rebuildLegendList(JPanel legendPanel, List<PieSlice> slices, long totalAmount) {
        legendPanel.removeAll();

        if (slices.isEmpty() || totalAmount == 0) {
            // keep empty, no data
        } else {
            for (PieSlice slice : slices) {
                String percentText = com.client.constants.StatsScreenConstants.PERCENT_FMT
                        .format(slice.percent * 100.0);
                String amountText = com.client.constants.StatsScreenConstants.AMOUNT_FMT
                        .format(slice.amount);

                CategoryLegendItem item = new CategoryLegendItem(
                        slice.color,
                        percentText,
                        slice.emoji,
                        slice.category,
                        amountText
                );
                legendPanel.add(item);
                legendPanel.add(Box.createVerticalStrut(4));
            }
        }

        legendPanel.revalidate();
        legendPanel.repaint();
    }
}
