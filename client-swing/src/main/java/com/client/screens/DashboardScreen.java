package com.client.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
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
import com.client.constants.UIColors;
import com.client.constants.UIFonts;
import com.client.constants.UIStyle;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.core.ScreenManager;
import com.client.model.DataEntry;


public class DashboardScreen extends BasePanel {

    private JButton addButton;
    private JButton prevMonthButton;
    private JButton nextMonthButton;

    private BottomNavigationBar bottomNavigationBar;
    
    private JScrollPane scrollPane;
    
    private JPanel listPanel;

    private static final DateTimeFormatter DATE_IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_OUT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");


    private JLabel monthLabel;

    private LocalDate currentDate = LocalDate.now();
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMM yyyy");

    private JComboBox<Integer> yearDropdown;


    // helper class to group entries by day, all transactions on same day will be grouped together
    private static class DayGroup {
        LocalDate date;

        int totalIncome = 0;
        int totalExpense = 0;
        
        List<DataEntry> entries = new ArrayList<>();
    }

    private static class ThreeColumnLayout implements LayoutManager2 {

        // Column width ratios (must sum to 1.0)
        // devidiing the container into 3 columns with given ratios
        private final double col1Ratio;
        private final double col2Ratio;
        private final double col3Ratio;

        ThreeColumnLayout() {
            this(0.50, 0.25, 0.25); // default ratios
        }

        ThreeColumnLayout(double col1Ratio, double col2Ratio, double col3Ratio) {
            this.col1Ratio = col1Ratio;
            this.col2Ratio = col2Ratio;
            this.col3Ratio = col3Ratio;
        }
        // we have to keep empty otherwise it gives error
        @Override
        public void addLayoutComponent(String name, java.awt.Component comp) {
            // keep empty, no named components needed            
        }

        @Override
        public void removeLayoutComponent(java.awt.Component comp) {
            // keep empty, no special handling needed
        }

        @Override
        public Dimension preferredLayoutSize(java.awt.Container parent) {
            Insets in = parent.getInsets();
            int width = 0;
            int height = 0;
            int count = parent.getComponentCount();

            for (int i = 0; i < count; i++) {
                java.awt.Component c = parent.getComponent(i);

                if (!c.isVisible()){
                    continue;
                }
                
                Dimension d = c.getPreferredSize();
                width = Math.max(width, d.width);
                height = Math.max(height, d.height);
            }
            // Width is flexible; height is driven by row panel
            return new Dimension(in.left + in.right + width, in.top + in.bottom + height);
        }

        @Override
        public Dimension minimumLayoutSize(java.awt.Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(java.awt.Container parent) {
            Insets in = parent.getInsets();
            int count = parent.getComponentCount();
            
            if (count == 0){
                
                return;
            }

            int totalWidth = parent.getWidth() - in.left - in.right;
            int totalHeight = parent.getHeight() - in.top - in.bottom;

            if (totalWidth <= 0 || totalHeight <= 0){
                return;
            }

            int x = in.left;
            int y = in.top;

            int col1Width = (int) Math.round(totalWidth * col1Ratio);
            int col2Width = (int) Math.round(totalWidth * col2Ratio);
            int col3Width = totalWidth - col1Width - col2Width; // remainder

            // We expect exactly 3 components: left, middle, right
            // If fewer/more, we guard with min(count, 3)
            int idx = 0;
            if (count > 0) {
                java.awt.Component c1 = parent.getComponent(idx++);
                if (c1.isVisible()) {
                    c1.setBounds(new Rectangle(x, y, col1Width, totalHeight));
                }
            }
            if (count > 1) {
                java.awt.Component c2 = parent.getComponent(idx++);
                if (c2.isVisible()) {
                    c2.setBounds(new Rectangle(x + col1Width, y, col2Width, totalHeight));
                }
            }
            if (count > 2) {
                java.awt.Component c3 = parent.getComponent(idx);
                if (c3.isVisible()) {
                    c3.setBounds(new Rectangle(x + col1Width + col2Width, y, col3Width, totalHeight));
                }
            }
        }

        // LayoutManager2 extra methods
        @Override
        public void addLayoutComponent(java.awt.Component comp, Object constraints) { }

        @Override
        public Dimension maximumLayoutSize(java.awt.Container target) {
            // allow stretching horizontally
            Dimension d = preferredLayoutSize(target);
            return new Dimension(Integer.MAX_VALUE, d.height);
        }

        @Override
        public float getLayoutAlignmentX(java.awt.Container target) {
            return 0.0f;
        }

        @Override
        public float getLayoutAlignmentY(java.awt.Container target) {
            return 0.0f;
        }

        @Override
        public void invalidateLayout(java.awt.Container target) { }
    }


    private static class BaseRowPanel extends JPanel {
        private final int fixedHeight;

        BaseRowPanel(int fixedHeight, boolean opaque, Color background) {
            super(new ThreeColumnLayout());
            this.fixedHeight = fixedHeight;
            setOpaque(opaque);
            if (background != null) {
                setBackground(background);
            }
            // Uniform padding so columns line up visually
            setBorder(new EmptyBorder(4, 10, 4, 10));
            setAlignmentX(LEFT_ALIGNMENT);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.height = fixedHeight;
            return d;
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension d = getPreferredSize();
            // allow width to grow to container width, fixed height
            d.width = Integer.MAX_VALUE;
            return d;
        }
    }

    public DashboardScreen() {
        setLayout(null);      // manual main layout
        setOpaque(false);     // gradient in paintComponent
        createComponents();
        refreshList();
    }

    private void createComponents() {

        // ------- Transaction list panel -------
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setBorder(null);;
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 12));
        add(scrollPane);

        prevMonthButton = new ArrowButton(true);   // same arrow as StatsScreen
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

        // ==== Year Filter Dropdown ====
        int currentYear = LocalDate.now().getYear();
        List<Integer> years = new ArrayList<>();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) {
            years.add(y);
        }

        yearDropdown = new JComboBox<>(years.toArray(new Integer[0]));
        yearDropdown.setFont(UIFonts.TEXT);
        UIStyle.styleDarkDropdown(yearDropdown);  // your custom dark rounded dropdown
        yearDropdown.setFocusable(false);
        yearDropdown.setSelectedItem(currentYear);

        yearDropdown.addActionListener(e -> {
            int selectedYear = (int) yearDropdown.getSelectedItem();
            // Adjust currentDate to stay within this year
            currentDate = YearMonth.of(selectedYear, currentDate.getMonth()).atDay(1);
            refreshList();
        });

        add(yearDropdown);



        addButton = new JButton() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int diameter = Math.min(w, h);

            // Coral/orange circular button — same as ManageCategories
            g2.setColor(new Color(255, 100, 90));
            g2.fillOval(0, 0, diameter, diameter);

            // White plus icon
            g2.setColor(Color.WHITE);
            int cx = w / 2;
            int cy = h / 2;

            int lineLength = (int) (diameter * 0.45);
            int thickness = 4;

            // Horizontal line
            g2.fillRoundRect(cx - lineLength / 2, cy - thickness / 2,
                    lineLength, thickness, 4, 4);

            // Vertical line
            g2.fillRoundRect(cx - thickness / 2, cy - lineLength / 2,
                    thickness, lineLength, 4, 4);

            g2.dispose();
        }

        
    };

    addButton.setBorderPainted(false);
    addButton.setFocusPainted(false);
    addButton.setContentAreaFilled(false);
    addButton.setOpaque(false);
        addButton.addActionListener(e -> {
            try {
                AddDataEntryScreen s = new AddDataEntryScreen();
                ScreenManager.show(s);
            } catch (Exception ex) {
                System.out.println(">>> ERROR WHILE CREATING AddDataEntryScreen");
                ex.printStackTrace();
            }
        });
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

        int listTop    = padding;
        int listBottom = bottomNavigationBarHeight + padding;
        int listHeight = h - listTop - listBottom;
        if (listHeight < 100) listHeight = 100;

        scrollPane.setBounds(padding, listTop, w - 2 * padding, listHeight);

        int filterY = 10;
        int arrowW = 40;
        int filterHeight = 35;
        int monthW = 150;

        int filterX = (w - monthW) / 2;

        monthLabel.setBounds(filterX, filterY, monthW, filterHeight);
        prevMonthButton.setBounds(filterX - arrowW, filterY, arrowW, filterHeight);
        nextMonthButton.setBounds(filterX + monthW, filterY, arrowW, filterHeight);

        // shift the list down below the month filter
        listTop = filterY + filterHeight + 10;
        listBottom = bottomNavigationBarHeight + padding;
        listHeight = h - listTop - listBottom;
        scrollPane.setBounds(padding, listTop, w - 2 * padding, listHeight);


        // Floating button
        int btnX = w - btnSize - padding;
        int btnY = h - btnBottomMargin - btnSize;
        addButton.setBounds(btnX, btnY, btnSize, btnSize);

        // Year dropdown on the right side of the month filter
        int yearDropdownWidth = 100;
        int yearDropdownHeight = filterHeight;

        yearDropdown.setBounds(
            filterX + monthW + arrowW + 20,   // a bit right of the next arrow
            filterY,
            yearDropdownWidth,
            yearDropdownHeight
        );


        // Bottom nav
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

        // filter by month
        List<DataEntry> filtered = new ArrayList<>();
        for (DataEntry e : entries) {
            if (e == null || e.getDate() == null) continue;
            try {
                LocalDate d = LocalDate.parse(e.getDate(), DATE_IN);
                if (YearMonth.from(d).equals(selected)) {
                    filtered.add(e);
                }
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

                DayGroup g = map.computeIfAbsent(d, k -> {
                    DayGroup dg = new DayGroup();
                    dg.date = k;
                    return dg;
                });

                if ("Income".equalsIgnoreCase(e.getType())) g.totalIncome += e.getAmount();
                else g.totalExpense += e.getAmount();

                g.entries.add(e);
            } catch (Exception ignored) {}
        }

        for (DayGroup g : map.values()) {
            listPanel.add(createDayHeaderRow(g));
            for (DataEntry e : g.entries) {
                listPanel.add(createTransactionRow(e));
            }
            listPanel.add(Box.createVerticalStrut(8));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }


    private JPanel createDayHeaderRow(DayGroup g) {
        Color bg = new Color(20, 28, 45, 220);
        BaseRowPanel row = new BaseRowPanel(
                34,                // fixed height
                true,              // opaque
                bg
        );

        // LEFT — date
        JLabel left = new JLabel(g.date.format(DATE_OUT));
        left.setFont(UIFonts.TEXT_BOLD);
        left.setForeground(Color.WHITE);
        left.setHorizontalAlignment(SwingConstants.LEFT);

        // MIDDLE — income
        JLabel mid = new JLabel("Income: " + g.totalIncome);
        mid.setFont(UIFonts.TEXT);
        mid.setForeground(UIColors.PRIMARY);
        mid.setHorizontalAlignment(SwingConstants.CENTER);

        // RIGHT — expense
        JLabel right = new JLabel("Expense: " + g.totalExpense);
        right.setFont(UIFonts.TEXT);
        right.setForeground(new Color(255, 80, 80));
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(left);
        row.add(mid);
        row.add(right);

        return row;
    }

    // ==========================================================
    // TRANSACTION ROW (Category | PaymentType | Amount)
    // ==========================================================
    
    private JPanel createTransactionRow(DataEntry e) {
        BaseRowPanel row = new BaseRowPanel(
                28,
                false,
                null
        );

        // ================================
        // DEFAULT + HOVER COLORS
        // ================================
        Color hoverColor = new Color(255, 255, 255, 30); // subtle white transparency
        Color normalColor = new Color(0, 0, 0, 0);       // fully transparent

        row.setBackground(normalColor);                 // ensure default is transparent
        row.setOpaque(false);                           // allow gradient below to show


        // LEFT — Category + note
        StringBuilder sb = new StringBuilder();
        if (e.getCategory() != null) sb.append(e.getCategory());
        if (e.getNote() != null && !e.getNote().isEmpty()) {
            if (sb.length() > 0) sb.append(" — ");
            sb.append(e.getNote());
        }

        JLabel left = new JLabel(sb.toString());
        left.setFont(UIFonts.TEXT);
        left.setForeground(Color.WHITE);
        left.setHorizontalAlignment(SwingConstants.LEFT);

        // MIDDLE — payment type
        JLabel mid = new JLabel(e.getPaymentType() != null ? e.getPaymentType() : "");
        mid.setFont(UIFonts.TEXT);
        mid.setForeground(new Color(200, 200, 200));
        mid.setHorizontalAlignment(SwingConstants.CENTER);

        // RIGHT — amount
        JLabel right = new JLabel(String.valueOf(e.getAmount()));
        right.setFont(UIFonts.TEXT_BOLD);
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        if ("Income".equalsIgnoreCase(e.getType())) {
            right.setForeground(UIColors.PRIMARY);
        } else {
            right.setForeground(new Color(255, 80, 80));
        }

        row.add(left);
        row.add(mid);
        row.add(right);

        // ================================
        // ADD HOVER EFFECT
        // ================================
        row.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent ev) {
                row.setOpaque(true);
                row.setBackground(hoverColor);       // light highlight
                row.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                row.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent ev) {
                row.setOpaque(false);
                row.setBackground(normalColor);
                row.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                row.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent ev) {
                ScreenManager.show(new AddDataEntryScreen(e));
            }
        });

        return row;
    }


    // =====================
    // BACKGROUND
    // =====================
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
