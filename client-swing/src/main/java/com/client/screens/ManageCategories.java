package com.client.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.client.components.BottomNavigationBar;
import com.client.components.CustomScrollBar;
import com.client.constants.Constants;
import com.client.constants.UIFonts;
import com.client.core.AppState;
import com.client.core.BasePanel;
import com.client.model.ExpenseCategory;
import com.client.utils.HttpClient;

public class ManageCategories extends BasePanel {
    // base url for categories API
    private static final String BASE_URL = Constants.BASE_URL + "/categories";

    private final BottomNavigationBar bottomNav;
    private final JScrollPane scrollPane;
    private final JButton addButton;
    private final JPanel listPanel;

    private final List<ExpenseCategory> categories = new ArrayList<>();

    public ManageCategories() {
        setLayout(null);
        setOpaque(false);

        Color textColor = new Color(245, 245, 255);  // bright white

        // page title
        JLabel title = new JLabel("Categories");
        
        title.setFont(UIFonts.TITLE);
        title.setForeground(textColor);
        title.setBounds(20, 20, 400, 40);
        
        add(title);

            // list panel inside scroll pane
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        add(scrollPane);

        // Floating + button to add new category
        addButton = new JButton() {
            // custom paint to draw circular button with plus sign
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int diameter = Math.min(w, h);

                // Background circular button
                g2.setColor(new Color(255, 100, 90)); // coral
                g2.fillOval(0, 0, diameter, diameter);

                // Draw crisp plus sign
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

        addButton.setPreferredSize(new Dimension(52, 52));
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setOpaque(false);


        add(addButton);

        addButton.addActionListener(e -> showAddDialog());

        bottomNav = new BottomNavigationBar("Settings");
        add(bottomNav);

        loadCategoriesAsync();
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int w = getWidth();
        int h = getHeight();
        int padding = 20;
        int navHeight = 60;

        getComponent(0).setBounds(20, 20, 400, 40);

        int listTop = 100;
        int listHeight = h - listTop - navHeight - 90;
        if (listHeight < 80) listHeight = 80;

        scrollPane.setBounds(padding, listTop, w - 2 * padding, listHeight);

        int fabSize = 52;
        addButton.setBounds(w - padding - fabSize, h - navHeight - padding - fabSize, fabSize, fabSize);

        bottomNav.setBounds(0, h - navHeight, w, navHeight);
        bottomNav.doLayout();
    }

    // to load categories from backend, GET /categories/{username}
    private void loadCategoriesAsync() {
        new Thread(() -> {
            try {
                String username = AppState.getInstance().getUsername();
                String json = HttpClient.get(BASE_URL + "/" + username);
                
                if (json == null) {
                    return;
                }

                List<ExpenseCategory> list = HttpClient.fromJsonList(json, ExpenseCategory.class);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    categories.clear();
                    categories.addAll(list);

                    // KEEP APP STATE SYNCED WITH BACKEND
                    AppState.getInstance().setCategories(new ArrayList<>(categories));

                    rebuildList();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void rebuildList() {
        listPanel.removeAll();

        if (categories.isEmpty()) {
            JLabel empty = new JLabel("No categories yet. Tap + to add.", SwingConstants.CENTER);

            empty.setForeground(Color.WHITE);
            empty.setFont(UIFonts.TEXT);
            empty.setBorder(new EmptyBorder(10, 10, 10, 10));
            empty.setAlignmentX(LEFT_ALIGNMENT);
            
            listPanel.add(empty);
        } else {
            for (ExpenseCategory cat : categories) {
                CategoryRow row = new CategoryRow(cat);
                row.setAlignmentX(LEFT_ALIGNMENT);
            
                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // Create category, dialog to get name and icon
    private void showAddDialog() {
        JTextField nameField = new JTextField();
        JTextField iconField = new JTextField();

        Object[] message = { "Name:", nameField, "Icon (emoji):", iconField };

        // show dialog
        int option = JOptionPane.showConfirmDialog(
            this, message, "Add Category",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        // if user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            String username = AppState.getInstance().getUsername();
            String icon = iconField.getText().trim();
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Name is required.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            createCategoryAsync(name, icon.isEmpty() ? "💸" : icon, username);
        }
    }

    // we will run this in a separate thread to avoid blocking UI
    private void createCategoryAsync(String name, String icon, String username) {
        new Thread(() -> {
            try {
                // create json body
                String jsonBody = String.format(
                    "{\"name\":\"%s\",\"icon\":\"%s\",\"username\":\"%s\"}",
                    escape(name), escape(icon), escape(username)
                );

                String res = HttpClient.post(BASE_URL, jsonBody);

                if ("OK".equalsIgnoreCase(res)) {
                    loadCategoriesAsync();
                    // success
                    // we reload categories from backend to keep in sync
                } else {
                    javax.swing.SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(
                                this, "Category already exists.",
                                "Error", JOptionPane.ERROR_MESSAGE
                            ));
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    // update category,
    // we will run this in a separate thread to avoid blocking UI
    private void updateCategoryAsync(ExpenseCategory cat) {
        new Thread(() -> {
            try {
                // create json body
                String username = AppState.getInstance().getUsername();

                String json = String.format(
                        "{\"name\":\"%s\",\"icon\":\"%s\",\"username\":\"%s\"}",
                        escape(cat.getName()),
                        escape(cat.getIcon()),
                        escape(username)
                );
                // send PUT request to update category
                String res = HttpClient.put(BASE_URL + "/" + cat.getId(), json);

                if ("OK".equalsIgnoreCase(res)) {
                    // success
                    // reload categories from backend to keep in sync
                    loadCategoriesAsync();
                } else {
                    javax.swing.SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                            this,
                            "Update failed.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        ));
                }

            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }


    private static String escape(String s) { 
        // simply escape double quotes for JSON strings
        return s.replace("\"", "\\\""); 
    }

    // UI component for each category row
    public class CategoryRow extends JPanel {
        private final ExpenseCategory category;
        private final JButton editBtn;

        CategoryRow(ExpenseCategory cat) {
            this.category = cat;

            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(8, 12, 8, 12));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

            JPanel left = new JPanel();
            left.setOpaque(false);
            left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

            JLabel iconLabel = new JLabel(cat.getIcon());
            iconLabel.setFont(UIFonts.TITLE);
            iconLabel.setForeground(Color.WHITE);

            JLabel nameLabel = new JLabel("  " + cat.getName());
            nameLabel.setFont(UIFonts.TEXT_BOLD);
            nameLabel.setForeground(Color.WHITE);

            left.add(iconLabel);
            left.add(nameLabel);

            JPanel right = new JPanel();
            right.setOpaque(false);
            right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));

            editBtn = smallButton("Edit");
            right.add(editBtn);

            add(left, BorderLayout.WEST);
            add(right, BorderLayout.EAST);

            setButtonsVisible(false);

            var hover = new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { setButtonsVisible(true); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { setButtonsVisible(false); }
            };
            this.addMouseListener(hover);
            left.addMouseListener(hover);
            right.addMouseListener(hover);
            editBtn.addMouseListener(hover);

            editBtn.addActionListener(e -> showEditDialog());
        }

        private JButton smallButton(String text) {
            JButton btn = new JButton(text);

            btn.setFont(UIFonts.TEXT);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(60, 70, 90));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setPreferredSize(new Dimension(70, 28));
            
            return btn;
        }

        private void setButtonsVisible(boolean visible) { editBtn.setVisible(visible); }

        private void showEditDialog() {
            JTextField nameField = new JTextField(category.getName());
            JTextField iconField = new JTextField(category.getIcon());

            Object[] message = { "Name:", nameField, "Icon (emoji):", iconField };

            int option = JOptionPane.showConfirmDialog(
                    ManageCategories.this, message, "Edit Category",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                category.setName(nameField.getText().trim());
                category.setIcon(iconField.getText().trim());

                updateCategoryAsync(category);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 30, 45, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
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
