import javax.swing.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.border.TitledBorder.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
import java.text.*;
import java.util.Collections;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;


public class Shop {
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DEEP_RED = new Color(150, 0, 24);
    private static final Color LIGHT_WOOD = new Color(205, 133, 63);
    
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 42);
    private static final Font BUTTON_FONT = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupModernLookAndFeel();
            showAuthenticationPage();
        });
    }

    private static void setupModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", BUTTON_FONT);
            UIManager.put("Panel.background", CREAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAuthenticationPage() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true); 
        frame.setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 30, 30));
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new GradientPanel(DARK_BROWN, LIGHT_WOOD);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("ARUP'S COFFEE SUITES", JLabel.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(GOLD);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        headerPanel.add(title);

        JLabel steamIcon = new JLabel(new ImageIcon("coffee_steam.gif")); 
        headerPanel.add(steamIcon);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton customerBtn = createModernButton("CUSTOMER ENTRY", DEEP_RED, GOLD);
        JButton adminBtn = createModernButton("ADMIN LOGIN", DARK_BROWN, GOLD);

        customerBtn.addActionListener(_ -> {
            frame.dispose();
            new CafeApp(false).setVisible(true);
        });

        adminBtn.addActionListener(_ -> {
            frame.dispose();
            showAdminLoginScreen();
        });

        buttonPanel.add(customerBtn);
        buttonPanel.add(adminBtn);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerText = new JLabel("Serving the finest coffee since 2025");
        footerText.setFont(LABEL_FONT);
        footerText.setForeground(GOLD);
        footerPanel.add(footerText);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        addWindowDragListener(frame);
    }

    private static JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        return button;
    }

    private static void addWindowDragListener(JFrame frame) {
        final Point[] dragOffset = new Point[1];
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragOffset[0] = e.getPoint();
            }
        });
        
        frame.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                frame.setLocation(currCoords.x - dragOffset[0].x, currCoords.y - dragOffset[0].y);
            }
        });
    }

    static class GradientPanel extends JPanel {
        private Color color1, color2;
        
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }
    }

    public static void showAdminLoginScreen() {
    JFrame frame = new JFrame();
    frame.setUndecorated(true);
    frame.setShape(new RoundRectangle2D.Double(0, 0, 500, 400, 30, 30));
    frame.setSize(500, 400);
    frame.setLocationRelativeTo(null);
    frame.setBackground(new Color(0, 0, 0, 0));

    JPanel mainPanel = new GradientPanel(DARK_BROWN, LIGHT_WOOD);
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    JPanel headerPanel = new JPanel();
    headerPanel.setOpaque(false);
    JLabel title = new JLabel("ADMIN PORTAL", JLabel.CENTER);
    title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 32));
    title.setForeground(GOLD);
    title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    headerPanel.add(title);

    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setOpaque(false);
    inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField usernameField = new JTextField(20);
    styleInputField(usernameField, "Username");
    gbc.gridx = 0;
    gbc.gridy = 0;
    inputPanel.add(usernameField, gbc);

    JPasswordField passwordField = new JPasswordField(20);
    styleInputField(passwordField, "Password");
    gbc.gridy = 1;
    inputPanel.add(passwordField, gbc);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
    buttonPanel.setOpaque(false);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

    JButton loginButton = createModernButton("SIGN IN", DEEP_RED, GOLD);
    JButton backButton = createModernButton("GO BACK", DARK_BROWN, GOLD);

    loginButton.addActionListener(_-> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("arup") && password.equals("3250")) {
            frame.dispose();
            showSuccessMessage();
            SwingUtilities.invokeLater(() -> new CafeApp(true).setVisible(true));
        } else {
            showShakeAnimation(frame);
            JOptionPane.showMessageDialog(frame, 
                "Invalid credentials", 
                "Access Denied", 
                JOptionPane.ERROR_MESSAGE);
        }
    });

    backButton.addActionListener(_ -> {
        frame.dispose();
        showAuthenticationPage();
    });

    buttonPanel.add(backButton);
    buttonPanel.add(loginButton);

    mainPanel.add(headerPanel, BorderLayout.NORTH);
    mainPanel.add(inputPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    frame.setContentPane(mainPanel);
    addWindowDragListener(frame);
    frame.setVisible(true);
}
private static void styleInputField(JComponent field, String placeholder) {
    field.setFont(new Font("Arial", Font.PLAIN, 16));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(GOLD, 2),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    field.setOpaque(false);
    field.setBackground(new Color(255, 255, 255, 100));
    field.setForeground(Color.WHITE);
    
    if (field instanceof JTextField) {
        ((JTextField)field).setHorizontalAlignment(JTextField.CENTER);
        ((JTextField)field).setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (((JTextField)field).getText().equals(placeholder)) {
                    ((JTextField)field).setText("");
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (((JTextField)field).getText().isEmpty()) {
                    ((JTextField)field).setText(placeholder);
                }
            }
        });
    }
}

private static void showShakeAnimation(JFrame frame) {
    final int SHAKE_DISTANCE = 10;
    final int SHAKE_SPEED = 5;
    
    try {
        Point originalLocation = frame.getLocation();
        for (int i = 0; i < 3; i++) {
            frame.setLocation(originalLocation.x + SHAKE_DISTANCE, originalLocation.y);
            Thread.sleep(SHAKE_SPEED);
            frame.setLocation(originalLocation.x - SHAKE_DISTANCE, originalLocation.y);
            Thread.sleep(SHAKE_SPEED);
        }
        frame.setLocation(originalLocation);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private static void showSuccessMessage() {
    JWindow splash = new JWindow();
    splash.setSize(300, 200);
    splash.setLocationRelativeTo(null);
    splash.setBackground(new Color(0, 0, 0, 0));

    JPanel panel = new GradientPanel(DEEP_RED, GOLD);
    panel.setLayout(new BorderLayout());
    
    JLabel message = new JLabel("ACCESS GRANTED", JLabel.CENTER);
    message.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 24));
    message.setForeground(Color.WHITE);
    
    JLabel icon = new JLabel(new ImageIcon("success_icon.png"));
    icon.setHorizontalAlignment(JLabel.CENTER);
    
    panel.add(icon, BorderLayout.CENTER);
    panel.add(message, BorderLayout.SOUTH);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    splash.setContentPane(panel);
    splash.setVisible(true);
    
    Timer timer = new Timer(2000, _-> {
        splash.dispose();
    });
    timer.setRepeats(false);
    timer.start();
}
}


class CafeApp extends JFrame {
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DEEP_RED = new Color(150, 0, 24);
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 42);
    private static final Font BUTTON_FONT = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
    
    public CafeApp(boolean isAdmin) {
        setupFrame();
        setupUI(isAdmin);
    }
    
    private void setupFrame() {
        setTitle("ARUP'S COFFEE SUITES");
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM);
        setLayout(new BorderLayout());
    }
    
    private void setupUI(boolean isAdmin) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_BROWN);
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("ARUP'S COFFEE SUITES");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD);
        headerPanel.add(titleLabel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CREAM);
        buttonPanel.setLayout(new GridLayout(isAdmin ? 6 : 4, 1, 15, 15));
        buttonPanel.setBorder(new EmptyBorder(40, 80, 40, 80));
        
        JButton menuButton = createModernButton("VIEW MENU", DEEP_RED, GOLD);
        JButton orderButton = createModernButton("TAKE ORDER", DEEP_RED, GOLD);
        JButton reviewButton = createModernButton("CUSTOMER REVIEWS", DEEP_RED, GOLD);
        
        menuButton.addActionListener(_ -> new MenuScreen().setVisible(true));
        orderButton.addActionListener(_ -> new OrderScreen().setVisible(true));
        reviewButton.addActionListener(_ -> new ReviewScreen().setVisible(true));
        
        buttonPanel.add(menuButton);
        buttonPanel.add(orderButton);
        buttonPanel.add(reviewButton);
        
        if (isAdmin) {
            JButton salesButton = createModernButton("SALES REPORT", DARK_BROWN, GOLD);
            JButton chartButton = createModernButton("SALES CHARTS", DARK_BROWN, GOLD);
            JButton logoutButton = createModernButton("LOGOUT", new Color(100, 30, 22), GOLD);
            
            salesButton.addActionListener(_ -> new SalesReport().setVisible(true));
            chartButton.addActionListener(_ -> new SalesChart().setVisible(true));
            logoutButton.addActionListener(_ -> {
                dispose();
                Shop.showAuthenticationPage();
            });
            
            buttonPanel.add(salesButton);
            buttonPanel.add(chartButton);
            buttonPanel.add(logoutButton);
        } else {
            JButton backButton = createModernButton("BACK TO LOGIN", DARK_BROWN, GOLD);
            backButton.addActionListener(_ -> {
                dispose();
                Shop.showAuthenticationPage();
            });
            buttonPanel.add(backButton);
        }
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(DARK_BROWN);
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel footerLabel = new JLabel("Serving the finest coffee since 2025");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(GOLD);
        footerPanel.add(footerLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        return button;
    }
}


class MenuScreen extends JFrame {
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 36);
    private static final Font BUTTON_FONT = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

    public MenuScreen() {
        setTitle("Cafe Menu");
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CREAM);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_BROWN);
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        JLabel titleLabel = new JLabel("ARUP'S COFFEE MENU");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD);
        headerPanel.add(titleLabel);

        ImageIcon icon = new ImageIcon("C:\\Users\\User\\Downloads\\Menu_card.png");
        Image image = icon.getImage().getScaledInstance(550, 600, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CREAM);
        scrollPane.setBackground(CREAM);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(CREAM);
        footerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton backButton = createModernButton("CLOSE", DARK_BROWN, GOLD);
        backButton.addActionListener(_ -> dispose());
        footerPanel.add(backButton);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };

        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        return button;
    }
}


class OrderScreen extends JFrame {
    private JTextArea orderSummary;
    private LinkedHashMap<String, Double> menu;
    private HashMap<String, Integer> order;
    private double totalBill;
    private static int orderCount = 0;
    private JPanel orderItemsPanel;

    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DEEP_RED = new Color(150, 0, 24);
    private static final Color LIGHT_WOOD = new Color(205, 133, 63);
    
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 32);
    private static final Font BUTTON_FONT = new Font("Arial Rounded MT Bold", Font.BOLD, 16);
    private static final Font ITEM_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font PRICE_FONT = new Font("Arial", Font.PLAIN, 14);

    public OrderScreen() {
        setTitle("ARUP'S COFFEE SUITES - Order System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(CREAM);

        menu = new LinkedHashMap<>();
        order = new HashMap<>();
        totalBill = 0.0;

        populateMenu();

        JPanel sandwichPanel = createMenuPanel("SANDWICHES & FOOD", 0, 5);
        JPanel coffeePanel = createMenuPanel("COFFEE & BEVERAGES", 5, menu.size());

        JPanel menuPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        menuPanel.setBackground(CREAM);
        menuPanel.add(createScrollPanel(sandwichPanel));
        menuPanel.add(createScrollPanel(coffeePanel));

orderItemsPanel = new JPanel();
orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
orderItemsPanel.setBackground(new Color(255, 230, 230));
orderItemsPanel.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(DARK_BROWN, 2),
    BorderFactory.createEmptyBorder(15, 15, 15, 15)));

JScrollPane orderScrollPane = new JScrollPane(orderItemsPanel);
orderScrollPane.setBorder(BorderFactory.createTitledBorder(
    BorderFactory.createEmptyBorder(), 
    "YOUR ORDER", 
    LEFT, 
    TOP, 
    BUTTON_FONT, 
    DARK_BROWN));
orderScrollPane.getViewport().setBackground(new Color(255, 230, 230));

        JPanel summaryPanel = new GradientPanel(LIGHT_WOOD, DARK_BROWN);
        summaryPanel.setLayout(new BorderLayout(10, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        orderSummary = new JTextArea(4, 30);
        styleOrderSummary();

        JButton orderButton = createModernButton("PLACE ORDER", DEEP_RED, GOLD);
        orderButton.addActionListener(_ -> placeOrder());

        summaryPanel.add(new JScrollPane(orderSummary), BorderLayout.CENTER);
        summaryPanel.add(orderButton, BorderLayout.SOUTH);

        add(menuPanel, BorderLayout.WEST);
        add(orderScrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JScrollPane createScrollPanel(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CREAM);
        return scrollPane;
    }

    private JPanel createMenuPanel(String title, int startIdx, int endIdx) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(CREAM);

        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setFont(TITLE_FONT.deriveFont(17f));
        label.setForeground(DARK_BROWN);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD),
            BorderFactory.createEmptyBorder(5, 0, 15, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (int i = startIdx; i < endIdx; i++) {
            String item = (String) menu.keySet().toArray()[i];
            double price = menu.get(item);
            
            panel.add(createMenuItemPanel(item, price));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panel;
    }

    private JPanel createMenuItemPanel(String item, double price) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BROWN, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel nameLabel = new JLabel(item);
        nameLabel.setFont(ITEM_FONT);
        nameLabel.setForeground(DARK_BROWN);

        JLabel priceLabel = new JLabel("$" + String.format("%.2f", price));
        priceLabel.setFont(PRICE_FONT);
        priceLabel.setForeground(DARK_BROWN);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(nameLabel, BorderLayout.WEST);
        infoPanel.add(priceLabel, BorderLayout.EAST);

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        
        itemPanel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                itemPanel.setBackground(GOLD);
                nameLabel.setForeground(Color.WHITE);
                priceLabel.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                itemPanel.setBackground(Color.WHITE);
                nameLabel.setForeground(DARK_BROWN);
                priceLabel.setForeground(DARK_BROWN);
            }
            public void mouseClicked(MouseEvent evt) {
                addToOrder(item);
            }
        });

        return itemPanel;
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        return button;
    }

    private void styleOrderSummary() {
        orderSummary.setEditable(false);
        orderSummary.setBackground(new Color(255, 230,230));
        orderSummary.setFont(new Font("Monospaced", Font.BOLD, 14));
        orderSummary.setForeground(DARK_BROWN);
        orderSummary.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    private void populateMenu() {
        menu.put("Turkey BLT", 8.50);
        menu.put("Classic Reuben", 9.25);
        menu.put("Philly Cheese Steak", 10.75);
        menu.put("Vegetarian Delight", 7.50);
        menu.put("Chicken Avocado", 8.95);
        
        menu.put("Espresso", 2.50);
        menu.put("Americano", 3.00);
        menu.put("Cappuccino", 3.75);
        menu.put("Latte", 4.25);
        menu.put("Flat White", 4.00);
        menu.put("Caramel Macchiato", 4.75);
        menu.put("Mocha", 4.50);
        menu.put("Vanilla Latte", 4.50);
        menu.put("Iced Coffee", 3.50);
        menu.put("Iced Latte", 4.25);
        menu.put("Cold Brew", 4.00);
        
        menu.put("English Breakfast Tea", 2.50);
        menu.put("Green Tea", 2.50);
        menu.put("Chai Latte", 3.75);
    }
    
    private void addToOrder(String item) {
        order.put(item, order.getOrDefault(item, 0) + 1);
        totalBill += menu.get(item);
        updateOrderDisplay();
        
        JLabel message = new JLabel(
            "<html><center><b>Added to Order:</b><br>" + item + 
            "<br><font color='" + String.format("#%02x%02x%02x", DARK_BROWN.getRed(), DARK_BROWN.getGreen(), DARK_BROWN.getBlue()) + 
            "'>$" + String.format("%.2f", menu.get(item)) + "</font></center></html>");
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        
    JButton okButton = new JButton("OK") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(GOLD.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(GOLD);
            } else {
                GradientPaint gp = new GradientPaint(
                    0, 0, DEEP_RED, 
                    getWidth(), getHeight(), LIGHT_WOOD);
                g2.setPaint(gp);
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();
            super.paintComponent(g);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
        }
    };
    
    okButton.setContentAreaFilled(false);
    okButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    okButton.setForeground(Color.WHITE);
    okButton.setFont(BUTTON_FONT);
    okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    okButton.setFocusPainted(false);
    okButton.addActionListener(e -> {
        Window window = SwingUtilities.getWindowAncestor((Component)e.getSource());
        window.dispose();
    });
    
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(CREAM);
    panel.add(message, BorderLayout.CENTER);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(CREAM);
    buttonPanel.add(okButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    JOptionPane optionPane = new JOptionPane(
        panel,
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.DEFAULT_OPTION,
        null,
        new Object[]{},
        null
    );
    
    JDialog dialog = optionPane.createDialog("Item Added");
    dialog.setVisible(true);
    }
    
    private void removeFromOrder(String item) {
        if (order.containsKey(item)) {
            int count = order.get(item);
            if (count > 1) {
                order.put(item, count - 1);
            } else {
                order.remove(item);
            }
            totalBill -= menu.get(item);
            updateOrderDisplay();
            
            orderItemsPanel.setBackground(new Color(255, 230, 230));
            Timer timer = new Timer(300, _ -> {
                orderItemsPanel.setBackground(Color.WHITE);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private void updateOrderDisplay() {
        orderItemsPanel.removeAll();
    
        if (order.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your order is empty. Click items to add them.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(DARK_BROWN);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            orderItemsPanel.add(emptyLabel);
        } else {
            for (Map.Entry<String, Integer> entry : order.entrySet()) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
                itemPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
                itemPanel.setBackground(CREAM);
    
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = menu.get(itemName) * quantity;
    
                JLabel itemLabel = new JLabel(
                    "<html><b>" + itemName + "</b> &times; " + quantity + 
                    " &nbsp;&nbsp;&nbsp; <font color='" + 
                    String.format("#%02x%02x%02x", 
                        DARK_BROWN.getRed(), 
                        DARK_BROWN.getGreen(), 
                        DARK_BROWN.getBlue()) + 
                    "'><b>$" + String.format("%.2f", itemPrice) + "</b></font></html>");
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    
                JButton removeButton = createModernButton("✕", DEEP_RED, GOLD);
                removeButton.setFont(new Font("Arial", Font.BOLD, 14));
                removeButton.setPreferredSize(new Dimension(40, 30));
                removeButton.addActionListener(_ -> removeFromOrder(entry.getKey()));
    
                itemPanel.add(itemLabel, BorderLayout.CENTER);
                itemPanel.add(removeButton, BorderLayout.EAST);
                orderItemsPanel.add(itemPanel);
                orderItemsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
    
        orderItemsPanel.revalidate();
        orderItemsPanel.repaint();
        updateOrderSummary();
    }
    
    private void updateOrderSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════ ORDER SUMMARY ═══════════╗\n");
        sb.append(String.format("║ %-20s %13s ║\n", "Items:", order.values().stream().mapToInt(Integer::intValue).sum()));
        sb.append(String.format("║ %-20s %13s ║\n", "Subtotal:", "$" + String.format("%.2f", totalBill)));
        sb.append(String.format("║ %-20s %13s ║\n", "Tax (10%):", "$" + String.format("%.2f", totalBill * 0.1)));
        sb.append(String.format("║ %-20s %13s ║\n", "Total:", "$" + String.format("%.2f", totalBill * 1.1)));
        sb.append("╚═══════════════════════════════════════╝");
        
        orderSummary.setText(sb.toString());
        orderSummary.setFont(new Font("Monospaced", Font.BOLD, 14));
        orderSummary.setForeground(DARK_BROWN);
    }
    
    private void placeOrder() {
        if (order.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please add items to your order before proceeding.", 
                "Empty Order", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        JPanel confirmPanel = new GradientPanel(LIGHT_WOOD, DARK_BROWN);
        confirmPanel.setLayout(new BorderLayout(10, 10));
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel confirmLabel = new JLabel("<html><center><font size='5'>Confirm Your Order?</font><br>" +
            "<font size='4'>Total with tax: $" + String.format("%.2f", totalBill * 1.1) + "</font></center></html>");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPanel.add(confirmLabel, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
    
        JButton yesButton = createModernButton("Confirm", DEEP_RED, GOLD);
        JButton noButton = createModernButton("Cancel", DARK_BROWN, GOLD);
    
        final int[] result = {JOptionPane.NO_OPTION};
        yesButton.addActionListener(_ -> {
            result[0] = JOptionPane.YES_OPTION;
            Window window = SwingUtilities.getWindowAncestor(yesButton);
            window.dispose();
        });
    
        noButton.addActionListener(_ -> {
            Window window = SwingUtilities.getWindowAncestor(noButton);
            window.dispose();
        });
    
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        confirmPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        JOptionPane optionPane = new JOptionPane(confirmPanel, 
            JOptionPane.PLAIN_MESSAGE, 
            JOptionPane.DEFAULT_OPTION, 
            null, new Object[]{}, null);
        
        JDialog dialog = optionPane.createDialog(this, "Order Confirmation");
        dialog.setVisible(true);
    
        if (result[0] != JOptionPane.YES_OPTION) {
            return;
        }
    
        // Process order
        orderCount++;
        saveOrderToFile();
        showReceipt();
        resetOrder();
    }
    
    private void saveOrderToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true))) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = formatter.format(new Date());
            
            writer.write(String.format(
                "Order #%d | %s | Items: %d | Subtotal: $%.2f | Tax: $%.2f | Total: $%.2f | Order: %s\n",
                orderCount,
                timestamp,
                order.values().stream().mapToInt(Integer::intValue).sum(),
                totalBill,
                totalBill * 0.1,
                totalBill * 1.1,
                order.toString()
            ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving order details. Please notify staff.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("╔══════════ ARUP'S COFFEE SUITES ══════════╗\n");
        receipt.append(String.format("║ %-20s Order #%-10d ║\n", " ", orderCount));
        receipt.append(String.format("║ %-20s %-17s ║\n", " ", 
            new SimpleDateFormat("MMM dd, yyyy hh:mm a").format(new Date())));
        receipt.append("╠══════════════════════════════════════════╣\n");
        
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            receipt.append(String.format("║ %-25s %2d × $%5.2f ║\n", 
                entry.getKey(), 
                entry.getValue(), 
                menu.get(entry.getKey())));
        }
        
        receipt.append("╠══════════════════════════════════════════╣\n");
        receipt.append(String.format("║ %-25s   $%6.2f ║\n", "Subtotal:", totalBill));
        receipt.append(String.format("║ %-25s   $%6.2f ║\n", "Tax (10%):", totalBill * 0.1));
        receipt.append(String.format("║ %-25s   $%6.2f ║\n", "Total:", totalBill * 1.1));
        receipt.append("╚══════════════════════════════════════════╝\n");
        receipt.append("       Thank you for your order!\n");
        receipt.append("       We hope to see you again soon!");
    
        JTextArea receiptArea = new JTextArea(receipt.toString());
        receiptArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        receiptArea.setEditable(false);
        receiptArea.setBackground(CREAM);
        receiptArea.setForeground(DARK_BROWN);
        receiptArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    JButton okButton = new JButton("GOT IT, THANKS!") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(GOLD.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(GOLD);
            } else {
                GradientPaint gp = new GradientPaint(
                    0, 0, DEEP_RED, 
                    getWidth(), getHeight(), LIGHT_WOOD);
                g2.setPaint(gp);
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();
            
            super.paintComponent(g);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
        }
    };
    okButton.addActionListener(e -> {
       resetOrder();
       Window window = SwingUtilities.getWindowAncestor((Component)e.getSource());
       window.dispose();
    });

    okButton.setContentAreaFilled(false);
    okButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    okButton.setForeground(Color.WHITE);
    okButton.setFont(BUTTON_FONT);
    okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    okButton.setFocusPainted(false);

    Object[] options = {okButton};
    JOptionPane pane = new JOptionPane(
        new JScrollPane(receiptArea),
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.DEFAULT_OPTION,
        null,
        options,
        options[0]
    );
    
    JDialog dialog = pane.createDialog(this, "Order #" + orderCount + " Confirmed");
    dialog.setVisible(true);
    }
    
    private void resetOrder() {
        order.clear();
    totalBill = 0.0;
    orderItemsPanel.removeAll();
    
    JLabel emptyLabel = new JLabel("Your order is empty. Click items to add them.");
    emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
    emptyLabel.setForeground(DARK_BROWN);
    emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
    orderItemsPanel.add(Box.createVerticalGlue());
    orderItemsPanel.add(emptyLabel);
    orderItemsPanel.add(Box.createVerticalGlue());
    
    orderItemsPanel.revalidate();
    orderItemsPanel.repaint();
    updateOrderSummary();
    }
    static class GradientPanel extends JPanel {
        private Color color1, color2;
        
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        }
    }
}


class SalesReport extends JFrame {
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color LIGHT_WOOD = new Color(205, 133, 63);
    
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 32);
    private static final Font CONTENT_FONT = new Font("Monospaced", Font.PLAIN, 14);

    public SalesReport() {
        setTitle("ARUP'S COFFEE SUITES - Sales Report");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new GradientPanel(DARK_BROWN, LIGHT_WOOD);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("SALES REPORT", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD);
        headerPanel.add(titleLabel);

        JTextArea reportArea = new JTextArea();
        styleReportArea(reportArea);
        Map<String, Object> salesStats = loadSalesData(reportArea);

        JPanel summaryPanel = createSummaryPanel(salesStats);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CREAM);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        contentPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void styleReportArea(JTextArea reportArea) {
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setFont(CONTENT_FONT);
        reportArea.setForeground(DARK_BROWN);
        reportArea.setBackground(new Color(255, 255, 255, 200));
        reportArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        reportArea.setCaretPosition(0);
    }

    private Map<String, Object> loadSalesData(JTextArea reportArea) {
        Map<String, Object> stats = new HashMap<>();
        double totalRevenue = 0;
        int totalOrders = 0;
        Map<String, Integer> itemCounts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
            StringBuilder reportBuilder = new StringBuilder();
            reportBuilder.append(String.format("%-10s %-20s %-8s %-10s %-10s %-10s\n", 
                "Order#", "Time", "Items", "Subtotal", "Tax", "Total"));
            reportBuilder.append("══════════════════════════════════════════════════════════════\n");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String orderNum = parts[0].trim().split("#")[1];
                    String time = parts[1].trim();
                    String items = parts[2].trim().split(":")[1].trim();
                    String subtotal = parts[3].trim().split("\\$")[1].trim();
                    String tax = parts[4].trim().split("\\$")[1].trim();
                    String total = parts[5].trim().split("\\$")[1].trim();

                    reportBuilder.append(String.format("%-10s %-20s %-8s $%-9s $%-9s $%-9s\n", 
                        orderNum, time, items, subtotal, tax, total));

                    totalRevenue += Double.parseDouble(total);
                    totalOrders++;
                    
                    if (parts.length > 6) {
                        String orderItems = parts[6].trim();
                        orderItems = orderItems.substring("Order: ".length(), orderItems.length() - 1);
                        String[] itemsArray = orderItems.split(", ");
                        for (String item : itemsArray) {
                            String[] itemParts = item.split("=");
                            if (itemParts.length == 2) {
                                String itemName = itemParts[0].trim();
                                int quantity = Integer.parseInt(itemParts[1].trim());
                                itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + quantity);
                            }
                        }
                    }
                }
            }
            
            if (totalOrders == 0) {
                reportArea.setText("No sales records found.");
            } else {
                reportArea.setText(reportBuilder.toString());
            }
            stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        
        String bestSeller = itemCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
        
        stats.put("bestSeller", bestSeller);
        } catch (IOException e) {
            reportArea.setText("Error loading sales data: " + e.getMessage());
        }
        return stats;
    }

    private JPanel createSummaryPanel(Map<String, Object> stats) {
        JPanel panel = new GradientPanel(LIGHT_WOOD, DARK_BROWN);
        panel.setLayout(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    int totalOrders = (int) stats.getOrDefault("totalOrders", 0);
    double totalRevenue = (double) stats.getOrDefault("totalRevenue", 0.0);
    String bestSeller = (String) stats.getOrDefault("bestSeller", "N/A");

    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    String revenueString = currencyFormat.format(totalRevenue);

        JLabel ordersLabel = createSummaryLabel("TOTAL ORDERS", 
        String.valueOf(totalOrders), GOLD);
        
        JLabel revenueLabel = createSummaryLabel("TOTAL REVENUE", 
        revenueString, GOLD);
        
        JLabel popularLabel = createSummaryLabel("BEST SELLER", 
        bestSeller, GOLD);

        panel.add(ordersLabel);
        panel.add(revenueLabel);
        panel.add(popularLabel);

        return panel;
    }

    private JLabel createSummaryLabel(String title, String value, Color color) {
        JLabel label = new JLabel("<html><center><font size=4>" + title + "</font><br>" +
                                 "<font size=5><b>" + value + "</b></font></center></html>");
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(color, 1));
        return label;
    }

    static class GradientPanel extends JPanel {
        private Color color1, color2;
        
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}


class ReviewScreen extends JFrame {
    private static final String[] STAR_SYMBOLS = {"\u2606", "\u2606", "\u2606", "\u2606", "\u2606", 
                                             "\u2605", "\u2605", "\u2605", "\u2605", "\u2605"};
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DEEP_RED = new Color(150, 0, 24);
    private static final Color LIGHT_WOOD = new Color(205, 133, 63);
    
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 32);
    private static final Font HEADER_FONT = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
    private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 14);
    private JComboBox<Integer> ratingDropdown;
    private JTextArea commentField;
    private JPanel imagePreviewPanel;
    private List<File> selectedImageFiles;
    private JPanel reviewsPanel;
    private List<Review> reviewsList;
    private JButton browseButton;

    public ReviewScreen() {
        setTitle("ARUP'S COFFEE SUITES - Customer Reviews");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(CREAM);

        reviewsList = new ArrayList<>();
        selectedImageFiles = new ArrayList<>();

        JPanel headerPanel = new GradientPanel(DARK_BROWN, LIGHT_WOOD);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("CUSTOMER REVIEWS", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        reviewsPanel.setBackground(CREAM);
        reviewsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(CREAM);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new GradientPanel(LIGHT_WOOD, DARK_BROWN);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ratingPanel.setOpaque(false);
        
        JLabel ratingLabel = new JLabel("Your Rating:");
        ratingLabel.setFont(HEADER_FONT);
        ratingLabel.setForeground(GOLD);
        ratingPanel.add(ratingLabel);
        
        ratingDropdown = new JComboBox<>(new Integer[] {1, 2, 3, 4, 5});
        ratingDropdown.setFont(HEADER_FONT);
        ratingDropdown.setBackground(Color.WHITE);
        ratingPanel.add(ratingDropdown);
        inputPanel.add(ratingPanel);

        commentField = new JTextArea(3, 40);
        commentField.setLineWrap(true);
        commentField.setWrapStyleWord(true);
        commentField.setFont(CONTENT_FONT);
        commentField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        JScrollPane commentScroll = new JScrollPane(commentField);
        commentScroll.setBorder(BorderFactory.createEmptyBorder());
        commentScroll.setOpaque(false);
        commentScroll.getViewport().setBackground(CREAM);
        inputPanel.add(commentScroll);

        JPanel imageUploadPanel = new JPanel(new BorderLayout());
        imageUploadPanel.setOpaque(false);
        
        browseButton = createModernButton("UPLOAD IMAGES", DARK_BROWN, GOLD);
        browseButton.addActionListener(this::browseImages);
        imageUploadPanel.add(browseButton, BorderLayout.NORTH);
        
        imagePreviewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        imagePreviewPanel.setBackground(CREAM);
        JScrollPane imageScroll = new JScrollPane(imagePreviewPanel);
        imageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        imageScroll.setPreferredSize(new Dimension(800, 150));
        imageScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(GOLD, 1), 
            "Selected Images", 
            LEFT, 
            TOP, 
            HEADER_FONT, 
            GOLD));
        imageUploadPanel.add(imageScroll, BorderLayout.CENTER);
        inputPanel.add(imageUploadPanel);

        JButton submitButton = createModernButton("SUBMIT REVIEW", DEEP_RED, GOLD);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(this::submitReview);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.SOUTH);

        loadReviews();
        displayReviews();
        setVisible(true);
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setForeground(Color.WHITE);
        button.setFont(HEADER_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        return button;
    }
    
    private void browseImages(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(dialog);
                    
                    customizeFileChooserButtons(dialog);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return dialog;
            }
        };
    
        fileChooser.setDialogTitle("SELECT IMAGES FOR REVIEW");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "IMAGE FILES (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.addChoosableFileFilter(filter);
    
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFiles.clear();
            imagePreviewPanel.removeAll();
    
            for (File file : fileChooser.getSelectedFiles()) {
                selectedImageFiles.add(file);
                try {
                    BufferedImage originalImage = ImageIO.read(file);
                    Image scaledImage = originalImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    
                    imageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 3, 1, GOLD),
                        BorderFactory.createEmptyBorder(5, 5, 2, 5)
                    ));
                    
                    imageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            imageLabel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(1, 1, 3, 1, DEEP_RED),
                                BorderFactory.createEmptyBorder(5, 5, 2, 5)
                            ));
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            imageLabel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(1, 1, 3, 1, GOLD),
                                BorderFactory.createEmptyBorder(5, 5, 2, 5)
                            ));
                        }
                    });
                    
                    imagePreviewPanel.add(imageLabel);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error loading image: " + ex.getMessage(),
                        "Image Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
    
            imagePreviewPanel.revalidate();
            imagePreviewPanel.repaint();
        }
    }
    
    private void customizeFileChooserButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText();
                
                if ("Open".equalsIgnoreCase(text) || "Select".equalsIgnoreCase(text)) {
                    styleButton(button, DEEP_RED, Color.WHITE);
                } else if ("Cancel".equalsIgnoreCase(text)) {
                    styleButton(button, DARK_BROWN, GOLD);
                }
            }
            if (comp instanceof Container) {
                customizeFileChooserButtons((Container) comp);
            }
        }
    }
    
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
        });
        
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    

    private void loadReviews() {
        reviewsList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("reviews.txt"))) {
            String line;
            Pattern pattern = Pattern.compile("\\[(.*?)\\] (★+☆*) - \"(.*?)\"( \\[Images: (.*?)\\])?");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);
                    String stars = matcher.group(2);
                    String comment = matcher.group(3);
                    String imagesPaths = matcher.group(5);

                    List<String> imagePathsList = new ArrayList<>();
                    if (imagesPaths != null) {
                        Collections.addAll(imagePathsList, imagesPaths.split(", "));
                    }

                    reviewsList.add(new Review(timestamp, stars, comment, imagePathsList));
                }
            }
        } catch (IOException e) {
        }
    }

    private void displayReviews() {
        reviewsPanel.removeAll();

        if (reviewsList.isEmpty()) {
            JLabel noReviewsLabel = new JLabel("<html><center><i>No reviews yet. Be the first to review!</i></center></html>", JLabel.CENTER);
            noReviewsLabel.setFont(HEADER_FONT.deriveFont(Font.ITALIC));
            noReviewsLabel.setForeground(DARK_BROWN);
            reviewsPanel.add(noReviewsLabel);
        } else {
            for (Review review : reviewsList) {
                JPanel reviewPanel = createReviewPanel(review);
                reviewsPanel.add(reviewPanel);
                reviewsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        reviewsPanel.revalidate();
        reviewsPanel.repaint();
    }

    private JPanel createReviewPanel(Review review) {
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        reviewPanel.setBackground(CREAM);
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        
        JLabel starsLabel = new JLabel(review.stars);
        starsLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        starsLabel.setForeground(GOLD);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.add(starsLabel); 
        
        JLabel timestampLabel = new JLabel(" • " + formatTimestamp(review.timestamp));
        timestampLabel.setFont(CONTENT_FONT);
        timestampLabel.setForeground(DARK_BROWN);
        headerPanel.add(timestampLabel);
        
        reviewPanel.add(headerPanel);
        reviewPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea commentArea = new JTextArea(review.comment);
        commentArea.setEditable(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBackground(CREAM);
        commentArea.setFont(CONTENT_FONT);
        commentArea.setBorder(BorderFactory.createEmptyBorder());
        reviewPanel.add(commentArea);

        if (!review.imagePaths.isEmpty()) {
            reviewPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            JLabel imagesLabel = new JLabel("Customer Photos:");
            imagesLabel.setFont(HEADER_FONT.deriveFont(Font.BOLD, 14));
            imagesLabel.setForeground(DARK_BROWN);
            reviewPanel.add(imagesLabel);
            reviewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            JPanel imagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            imagesPanel.setOpaque(false);

            for (String imagePath : review.imagePaths) {
                try {
                    BufferedImage img = ImageIO.read(new File(imagePath));
                    if (img != null) {
                        Image scaledImage = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                        imageLabel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(GOLD, 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                        imagesPanel.add(imageLabel);
                    }
                } catch (IOException e) {
                }
            }
            reviewPanel.add(imagesPanel);
        }

        return reviewPanel;
    }

    private String formatTimestamp(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
            return outputFormat.format(date);
        } catch (ParseException e) {
            return timestamp;
        }
    }

    private void submitReview(ActionEvent e) {
        int rating = (int) ratingDropdown.getSelectedItem();
        String comment = commentField.getText().trim();
        
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please write a comment before submitting.",
                "Incomplete Review",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String stars = "";
        for (int i = 0; i < 5; i++) {
            stars += (i < rating ? STAR_SYMBOLS[5+i] : STAR_SYMBOLS[i]);
        }
        StringBuilder reviewEntry = new StringBuilder();
        reviewEntry.append("[").append(timestamp).append("] ")
                  .append(stars).append(" - \"").append(comment).append("\"");

        List<String> savedImagePaths = saveReviewImages(timestamp);
        if (!savedImagePaths.isEmpty()) {
            reviewEntry.append(" [Images: ").append(String.join(", ", savedImagePaths)).append("]");
        }

        if (saveReviewToFile(reviewEntry.toString())) {
            reviewsList.add(new Review(timestamp, stars.toString(), comment, savedImagePaths));
            displayReviews();
            resetForm();
            showThankYouMessage();
        }
    }

    private List<String> saveReviewImages(String timestamp) {
        List<String> savedImagePaths = new ArrayList<>();
        if (!selectedImageFiles.isEmpty()) {
            try {
                File imagesDir = new File("reviews_images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdir();
                }

                for (File imageFile : selectedImageFiles) {
                    String ext = imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
                    String imageName = "review_" + timestamp.replaceAll("[: ]", "_") + "_" + 
                                     System.currentTimeMillis() + ext;
                    String imagePath = "reviews_images/" + imageName;

                    BufferedImage image = ImageIO.read(imageFile);
                    ImageIO.write(image, ext.substring(1), new File(imagePath));

                    savedImagePaths.add(imagePath);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving some images. Review was saved without them.",
                    "Image Save Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        return savedImagePaths;
    }

    private boolean saveReviewToFile(String reviewEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reviews.txt", true))) {
            writer.write(reviewEntry);
            writer.newLine();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving review. Please try again.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void resetForm() {
        commentField.setText("");
        imagePreviewPanel.removeAll();
        imagePreviewPanel.revalidate();
        imagePreviewPanel.repaint();
        selectedImageFiles.clear();
        ratingDropdown.setSelectedIndex(4); 
    }

    private void showThankYouMessage() {
        JPanel messagePanel = new JPanel(new BorderLayout(0, 20));
        messagePanel.setBackground(CREAM);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 15, 30));
    
        JLabel messageLabel = new JLabel(
            "<html><center><font size='6' color='" + 
            String.format("#%02x%02x%02x", DEEP_RED.getRed(), DEEP_RED.getGreen(), DEEP_RED.getBlue()) + 
            "'><b>Thank You!</b></font><br><br>" +
            "<font size='4' color='" + 
            String.format("#%02x%02x%02x", DARK_BROWN.getRed(), DARK_BROWN.getGreen(), DARK_BROWN.getBlue()) + 
            "'>We appreciate your feedback!</font></center></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
    
        JButton okButton = new JButton("OK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(DEEP_RED.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(DEEP_RED.brighter());
                } else {
                    g2.setColor(DEEP_RED);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.setContentAreaFilled(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(HEADER_FONT);
        okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        okButton.setFocusPainted(false);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CREAM);
        buttonPanel.add(okButton);
        messagePanel.add(buttonPanel, BorderLayout.SOUTH);
    
        JDialog dialog = new JDialog(this, "Review Submitted", true);
        dialog.setContentPane(messagePanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        okButton.addActionListener(_ -> dialog.dispose());
        dialog.setVisible(true);
    }

    private static class Review {
        String timestamp;
        String stars;
        String comment;
        List<String> imagePaths;

        public Review(String timestamp, String stars, String comment, List<String> imagePaths) {
            this.timestamp = timestamp;
            this.stars = stars;
            this.comment = comment;
            this.imagePaths = imagePaths != null ? imagePaths : new ArrayList<>();
        }
    }

    static class GradientPanel extends JPanel {
        private Color color1, color2;
        
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}


class SalesChart extends JFrame {
    private static final Color DARK_BROWN = new Color(60, 30, 10);
    private static final Color CREAM = new Color(255, 248, 220);
    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DEEP_RED = new Color(150, 0, 24);
    private static final Color LIGHT_WOOD = new Color(205, 133, 63);
    
    private static final Font TITLE_FONT = new Font("Brush Script MT", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 12);
    public SalesChart() {
        setTitle("ARUP'S COFFEE SUITES - Sales Analytics");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM);
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new GradientPanel(DARK_BROWN, LIGHT_WOOD);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("SALES ANALYTICS", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        showPieChart();
    }

    private void showPieChart() {
        HashMap<String, Integer> salesData = new HashMap<>();
        int totalSales = 0;
    
        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int orderStart = line.indexOf("Order: {");
                if (orderStart == -1) continue;
                
                String orderDetails = line.substring(orderStart + 8, line.indexOf("}", orderStart));
                String[] items = orderDetails.split(", ");
    
                for (String itemEntry : items) {
                    String[] parts = itemEntry.split("=");
                    if (parts.length == 2) {
                        String item = parts[0].trim();
                        int qty = Integer.parseInt(parts[1].trim());
                        salesData.put(item, salesData.getOrDefault(item, 0) + qty);
                        totalSales += qty;
                    }
                }
            }
        } catch (IOException e) {
            showMessage("No sales records found.", "Data Error", DEEP_RED);
            return;
        }
    
        if (salesData.isEmpty()) {
            showMessage("No valid sales data to display.", "Empty Data", DEEP_RED);
            return;
        }
    
        DefaultPieDataset dataset = createDataset(salesData);
        JFreeChart pieChart = createChart(dataset);
        styleChart(pieChart, dataset, totalSales);
        
        ChartPanel chartPanel = new ChartPanel(pieChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(750, 600);
            }
        };
        
        chartPanel.setBackground(CREAM);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(chartPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private DefaultPieDataset createDataset(HashMap<String, Integer> salesData) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        salesData.forEach((item, qty) -> dataset.setValue(item, qty));
        return dataset;
    }

    private JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            null,
            dataset,
            true,
            true,
            false
        );
        chart.setBackgroundPaint(CREAM);
        return chart;
    }

    private void styleChart(JFreeChart chart, DefaultPieDataset dataset, int totalSales) {
        PiePlot plot = (PiePlot) chart.getPlot();
        
        Paint[] paints = { GOLD, LIGHT_WOOD, DEEP_RED, DARK_BROWN, new Color(169, 113, 66) };
        
        int colorIndex = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable<?>) key, paints[colorIndex % paints.length]);
            colorIndex++;
        }
        
        plot.setBackgroundPaint(CREAM);
        plot.setOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setLabelBackgroundPaint(CREAM);
        plot.setLabelPaint(DARK_BROWN);
        plot.setLabelFont(LABEL_FONT);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelLinkPaint(DARK_BROWN);
        plot.setLabelLinkStroke(new BasicStroke(1.5f));
        
        plot.setLabelGenerator((PieSectionLabelGenerator) new StandardPieSectionLabelGenerator(
            "{0} ({1}, {2})", 
            new DecimalFormat("0"), 
            new DecimalFormat("0%")
        ) {
            @Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                Number value = dataset.getValue(key);
                double percent = value.doubleValue() / totalSales;
                return MessageFormat.format(
                    "{0} ({1}, {2})",
                    key.toString(),
                    value.toString(),
                    new DecimalFormat("0%").format(percent)
                );
            }
        });

        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setBackgroundPaint(CREAM);
            legend.setItemFont(LABEL_FONT);
            legend.setItemPaint(DARK_BROWN);
            legend.setBorder(0, 0, 0, 0);
            legend.setPosition(RectangleEdge.BOTTOM);
        }
    }

    private void showMessage(String message, String title, Color color) {
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(LABEL_FONT);
        label.setForeground(color);
        JOptionPane.showMessageDialog(this, label, title, JOptionPane.PLAIN_MESSAGE);
    }

    static class GradientPanel extends JPanel {
        private final Color color1, color2;
        
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}