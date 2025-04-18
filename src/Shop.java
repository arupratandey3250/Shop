import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.List;
public class Shop
{
    public static void main(String[] args)
    {
        showAuthenticationPage();
    }

    public static void showAuthenticationPage()
    {

        UIManager.put("OptionPane.background", new Color(173, 216, 230));
        UIManager.put("Panel.background", new Color(173, 216, 230));

        String[] options = {"User", "Admin"};
        int choice = JOptionPane.showOptionDialog(null, "Select Access Type", "Login",
                     JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                     null, options, options[0]);

        if (choice == 1)
        {
            showAdminLoginScreen();
        }
        else if (choice == 0)
        {
            SwingUtilities.invokeLater(() -> new CafeApp(false).setVisible(true));
        }
    }

    public static void showAdminLoginScreen()
    {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.addActionListener(_ ->
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("arup") && password.equals("3250"))
            {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                loginFrame.dispose();
                SwingUtilities.invokeLater(() -> new CafeApp(true).setVisible(true));
            }
            else
            {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials, try again.");
            }
        });


        backButton.addActionListener(_ ->
        {
            loginFrame.dispose();
            showAuthenticationPage();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        loginFrame.add(inputPanel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}

class CafeApp extends JFrame
{
    public CafeApp(boolean isAdmin)
    {
        setTitle("The Cafe Name");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 228, 225));

        setLayout(new GridLayout(isAdmin ? 6 : 4, 1, 10, 10));

        JButton menuButton = createStyledButton("View Menu", new Color(160, 0, 66));
        JButton orderButton = createStyledButton("Take Order", new Color(160, 0, 66));
        JButton reviewButton = createStyledButton("Customer Reviews", new Color(160, 0, 66));

        menuButton.addActionListener(_ -> new MenuScreen().setVisible(true));
        orderButton.addActionListener(_ -> new OrderScreen().setVisible(true));
        reviewButton.addActionListener(_ -> new ReviewScreen().setVisible(true));


        if (isAdmin)
        {
            JButton salesButton = createStyledButton("View Sales Report", new Color(160, 0, 66));
            JButton chartButton = createStyledButton("View Sales Chart", new Color(160, 0, 66));
            JButton logoutButton = createStyledButton("Logout", new Color(160, 0, 66));

            salesButton.addActionListener(_ -> new SalesReport().setVisible(true));
            chartButton.addActionListener(_ -> new SalesChart().setVisible(true));

            logoutButton.addActionListener(_ ->
            {
                dispose();
                Shop.showAuthenticationPage();
            });


            add(menuButton);
            add(orderButton);
            add(reviewButton);
            add(salesButton);
            add(chartButton);
            add(logoutButton);
        }
        else
        {

            add(menuButton);
            add(orderButton);
            add(reviewButton);


            JButton backToLoginButton = createStyledButton("Back to Login", new Color(160, 0, 66));
            backToLoginButton.addActionListener(_ ->
            {
                dispose();
                Shop.showAuthenticationPage();
            });

            add(backToLoginButton);
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }
}

class MenuScreen extends JFrame
{
    public MenuScreen()
    {
        setTitle("Cafe Menu");
        setSize(600, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 245, 238));

        JLabel menuCardImage = createImageLabel("C:\\Users\\User\\Cafe\\Cafe\\menu_card.jpg");
        add(menuCardImage, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createImageLabel(String path)
    {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(image));
    }
}

class OrderScreen extends JFrame
{
    private JTextArea orderSummary;
    private LinkedHashMap<String, Double> menu;
    private HashMap<String, Integer> order;
    private double totalBill;
    private static int orderCount = 0;
    private JPanel orderItemsPanel;

    public OrderScreen()
    {
        setTitle("Cafe Order");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 245, 238));

        menu = new LinkedHashMap<>();
        order = new HashMap<>();
        totalBill = 0.0;

        populateMenu();

        JScrollPane sandwichScroll = new JScrollPane(
            createMenuPanel("Sandwiches", 0, 5, new Color(255, 76, 76)));
        JScrollPane coffeeScroll = new JScrollPane(
            createMenuPanel("Coffee", 5, menu.size(), new Color(102, 51, 0)));

        sandwichScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        coffeeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sandwichScroll.setBorder(null);
        coffeeScroll.setBorder(null);

        JPanel menuPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        menuPanel.add(sandwichScroll);
        menuPanel.add(coffeeScroll);

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
        orderItemsPanel.setBackground(new Color(240, 255, 240));

        JScrollPane orderScrollPane = new JScrollPane(orderItemsPanel);
        orderScrollPane.setBorder(BorderFactory.createTitledBorder("Your Order"));
        orderScrollPane.setPreferredSize(new Dimension(300,0));

        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        orderSummary = new JTextArea(3, 30);
        orderSummary.setEditable(false);
        orderSummary.setBackground(new Color(240, 255, 240));
        orderSummary.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton orderButton = new JButton("Place Order");
        orderButton.setFont(new Font("Arial", Font.BOLD, 16));
        orderButton.setBackground(new Color(0, 100, 0));
        orderButton.setForeground(Color.WHITE);
        orderButton.addActionListener(_ -> placeOrder());

        summaryPanel.add(new JScrollPane(orderSummary), BorderLayout.CENTER);
        summaryPanel.add(orderButton, BorderLayout.SOUTH);

        add(menuPanel, BorderLayout.WEST);
        add(orderScrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    private JPanel createMenuPanel(String title, int startIdx, int endIdx, Color bgColor)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(255, 245, 238));

        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBackground(new Color(160, 0, 66));
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (int i = startIdx; i < endIdx; i++)
        {
            String item = (String) menu.keySet().toArray()[i];
            JButton itemButton = createMenuButton(item, bgColor);
            itemButton.addActionListener(_ -> addToOrder(item));
            itemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(itemButton);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        return panel;
    }

    private JButton createMenuButton(String text, Color bgColor)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        Dimension buttonSize = new Dimension(200, 50);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        return button;
    }

    private void populateMenu()
    {
        menu.put("Turkey BLAT", 8.0);
        menu.put("Reuben", 7.0);
        menu.put("Philly Cheese Steak", 11.0);
        menu.put("Hot Meatloaf Sandwich", 10.0);
        menu.put("Flat White", 7.0);
        menu.put("Brewed Coffee (Small)", 1.0);
        menu.put("Brewed Coffee (Medium)", 2.0);
        menu.put("Brewed Coffee (Large)", 3.0);
        menu.put("Coffee with a Shot (Small)", 2.0);
        menu.put("Coffee with a Shot (Medium)", 3.0);
        menu.put("Coffee with a Shot (Large)", 4.0);
        menu.put("Macchiato (Small)", 1.0);
        menu.put("Macchiato (Medium)", 2.0);
        menu.put("Macchiato (Large)", 3.0);
        menu.put("Cappuccino (Small)", 2.0);
        menu.put("Cappuccino (Medium)", 3.0);
        menu.put("Cappuccino (Large)", 4.0);
        menu.put("Latte (Small)", 3.0);
        menu.put("Latte (Medium)", 4.0);
        menu.put("Latte (Large)", 5.0);
        menu.put("Mocha (Small)", 3.0);
        menu.put("Mocha (Medium)", 4.0);
        menu.put("Mocha (Large)", 5.0);
        menu.put("Americano (Small)", 2.0);
        menu.put("Americano (Medium)", 3.0);
        menu.put("Americano (Large)", 4.0);
    }

    private void addToOrder(String item)
    {
        order.put(item, order.getOrDefault(item, 0) + 1);
        totalBill += menu.get(item);
        updateOrderDisplay();
    }

    private void removeFromOrder(String item)
    {
        if (order.containsKey(item))
        {
            int count = order.get(item);
            if (count > 1)
            {
                order.put(item, count - 1);
            }
            else
            {
                order.remove(item);
            }
            totalBill -= menu.get(item);
            updateOrderDisplay();
        }
    }

    private void updateOrderDisplay()
    {
        orderItemsPanel.removeAll();

        for (Map.Entry<String, Integer> entry : order.entrySet())
        {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel itemLabel = new JLabel(entry.getKey() + " (x" + entry.getValue() + ") - $" +
                                          String.format("%.2f", menu.get(entry.getKey()) * entry.getValue()));
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JButton removeButton = new JButton("Remove");
            removeButton.setBackground(new Color(255, 100, 100));
            removeButton.setForeground(Color.WHITE);
            removeButton.addActionListener(_ -> removeFromOrder(entry.getKey()));

            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(removeButton, BorderLayout.EAST);
            orderItemsPanel.add(itemPanel);
        }

        orderItemsPanel.revalidate();
        orderItemsPanel.repaint();
        updateOrderSummary();
    }

    private void updateOrderSummary()
    {
        StringBuilder sb = new StringBuilder("Order Summary:\n");
        sb.append("Total Items: ").append(order.values().stream().mapToInt(Integer::intValue).sum()).append("\n");
        sb.append("Total: $").append(String.format("%.2f", totalBill));
        orderSummary.setText(sb.toString());
    }

    private void placeOrder()
    {
        if (order.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "No items selected.");
            return;
        }

        orderCount++;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true)))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = formatter.format(new Date());
            writer.write("Order #" + orderCount + " - Time: " + timestamp + " Order: " + order.toString() + " Total: $" + totalBill + "\n");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Error saving order.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Order #" + orderCount + " placed successfully!\nTotal: $" + String.format("%.2f", totalBill));
        order.clear();
        totalBill = 0.0;
        orderItemsPanel.removeAll();
        orderItemsPanel.revalidate();
        updateOrderSummary();
    }
}

class SalesReport extends JFrame
{
    public SalesReport()
    {
        setTitle("Sales Report");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 245, 238));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setBackground(new Color(240, 255, 240));
        reportArea.setFont(new Font("Arial", Font.PLAIN, 14));

        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                reportArea.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            reportArea.setText("No sales records found.");
        }

        add(new JScrollPane(reportArea));
        setVisible(true);
    }
}

class ReviewScreen extends JFrame
{
    private JComboBox<Integer> ratingDropdown;
    private JTextArea commentField;
    private JPanel imagePreviewPanel;
    private List<File> selectedImageFiles;
    private JPanel reviewsPanel;
    private List<Review> reviewsList;
    private JButton browseButton;

    public ReviewScreen()
    {
        setTitle("Customer Reviews");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        reviewsList = new ArrayList<>();
        selectedImageFiles = new ArrayList<>();

        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        reviewsPanel.setBackground(new Color(255, 245, 238));

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topRow.add(new JLabel("Rating:"));
        ratingDropdown = new JComboBox<>(new Integer[] {1, 2, 3, 4, 5});
        topRow.add(ratingDropdown);

        browseButton = new JButton("Browse Images");
        browseButton.addActionListener(this::browseImages);
        topRow.add(browseButton);
        inputPanel.add(topRow);

        commentField = new JTextArea(3, 40);
        commentField.setLineWrap(true);
        inputPanel.add(new JScrollPane(commentField));

        JButton submitButton = new JButton("Submit Review");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(this::submitReview);
        inputPanel.add(submitButton);

        imagePreviewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JScrollPane imageScroll = new JScrollPane(imagePreviewPanel);
        imageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        imageScroll.setPreferredSize(new Dimension(800, 120));
        inputPanel.add(imageScroll);

        add(inputPanel, BorderLayout.SOUTH);
        loadReviews();
        displayReviews();
        setVisible(true);
    }

    private void browseImages(ActionEvent e)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Review Images");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            selectedImageFiles.clear();
            imagePreviewPanel.removeAll();

            for (File file : fileChooser.getSelectedFiles())
            {
                selectedImageFiles.add(file);
                try
                {
                    BufferedImage originalImage = ImageIO.read(file);
                    Image scaledImage = originalImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    imagePreviewPanel.add(imageLabel);
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage());
                }
            }

            imagePreviewPanel.revalidate();
            imagePreviewPanel.repaint();
        }
    }

    private void loadReviews()
    {
        reviewsList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("reviews.txt")))
        {
            String line;
            Pattern pattern = Pattern.compile("\\[(.*?)\\] (★+☆*) - \"(.*?)\"( \\[Images: (.*?)\\])?");

            while ((line = reader.readLine()) != null)
            {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find())
                {
                    String timestamp = matcher.group(1);
                    String stars = matcher.group(2);
                    String comment = matcher.group(3);
                    String imagesPaths = matcher.group(5);

                    List<String> imagePathsList = new ArrayList<>();
                    if (imagesPaths != null)
                    {
                        String[] paths = imagesPaths.split(", ");
                        for (String path : paths)
                        {
                            imagePathsList.add(path);
                        }
                    }

                    Review review = new Review(timestamp, stars, comment, imagePathsList);
                    reviewsList.add(review);
                }
            }
        }
        catch (IOException e)
        {
        }
    }

    private void displayReviews()
    {
        reviewsPanel.removeAll();

        for (Review review : reviewsList)
        {
            JPanel reviewPanel = createReviewPanel(review);
            reviewsPanel.add(reviewPanel);
            reviewsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        if (reviewsList.isEmpty())
        {
            JLabel noReviewsLabel = new JLabel("No reviews available yet.", JLabel.CENTER);
            noReviewsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            reviewsPanel.add(noReviewsLabel);
        }

        reviewsPanel.revalidate();
        reviewsPanel.repaint();
    }

    private JPanel createReviewPanel(Review review)
    {
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBorder(BorderFactory.createCompoundBorder(
                                  BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                                  BorderFactory.createEmptyBorder(15, 15, 15, 15)
                              ));
        reviewPanel.setBackground(Color.WHITE);
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(new JLabel(review.stars));
        headerPanel.add(new JLabel(" • " + review.timestamp));
        reviewPanel.add(headerPanel);

        JTextArea commentArea = new JTextArea(review.comment);
        commentArea.setEditable(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBackground(Color.WHITE);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        reviewPanel.add(commentArea);
        reviewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        if (!review.imagePaths.isEmpty())
        {
            JPanel imagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            imagesPanel.setBackground(Color.WHITE);

            for (String imagePath : review.imagePaths)
            {
                try
                {
                    BufferedImage img = ImageIO.read(new File(imagePath));
                    if (img != null)
                    {
                        Image scaledImage = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                        imagesPanel.add(imageLabel);
                    }
                }
                catch (IOException e)
                {
                }
            }

            reviewPanel.add(imagesPanel);
        }

        return reviewPanel;
    }

    private void submitReview(ActionEvent e)
    {
        int rating = (int) ratingDropdown.getSelectedItem();
        String comment = commentField.getText().trim();
        if (comment.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please write a comment.");
            return;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String stars = "★".repeat(rating) + "☆".repeat(5 - rating);
        StringBuilder reviewEntry = new StringBuilder();
        reviewEntry.append("[").append(timestamp).append("] ").append(stars).append(" - \"").append(comment).append("\"");

        List<String> savedImagePaths = new ArrayList<>();
        if (!selectedImageFiles.isEmpty())
        {
            try
            {
                File imagesDir = new File("reviews_images");
                if (!imagesDir.exists())
                {
                    imagesDir.mkdir();
                }

                for (File imageFile : selectedImageFiles)
                {
                    String imageName = "review_" + timestamp.replaceAll("[: ]", "_") + "_" +
                                       System.currentTimeMillis() +
                                       imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
                    String imagePath = "reviews_images/" + imageName;

                    BufferedImage image = ImageIO.read(imageFile);
                    ImageIO.write(image, imagePath.substring(imagePath.lastIndexOf(".") + 1),
                                  new File(imagePath));

                    savedImagePaths.add(imagePath);
                }

                if (!savedImagePaths.isEmpty())
                {
                    reviewEntry.append(" [Images: ").append(String.join(", ", savedImagePaths)).append("]");
                }
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "Error saving images: " + ex.getMessage());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reviews.txt", true)))
        {
            writer.write(reviewEntry.toString());
            writer.newLine();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, "Error saving review.");
            return;
        }

        reviewsList.add(new Review(timestamp, stars, comment, savedImagePaths));
        displayReviews();

        commentField.setText("");
        imagePreviewPanel.removeAll();
        imagePreviewPanel.revalidate();
        imagePreviewPanel.repaint();
        selectedImageFiles.clear();

        JOptionPane.showMessageDialog(this, "Thank you for your review!");
    }

    private static class Review
    {
        String timestamp;
        String stars;
        String comment;
        List<String> imagePaths;

        public Review(String timestamp, String stars, String comment, List<String> imagePaths)
        {
            this.timestamp = timestamp;
            this.stars = stars;
            this.comment = comment;
            this.imagePaths = imagePaths != null ? imagePaths : new ArrayList<>();
        }
    }
}

class SalesChart extends JFrame
{
    public SalesChart()
    {
        setTitle("Sales Chart");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        showPieChart();
    }

    private void showPieChart()
    {
        HashMap<String, Integer> salesData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.contains("Order: {")) continue;

                int orderStart = line.indexOf("Order: {") + 8;
                int orderEnd = line.indexOf("}", orderStart);
                if (orderStart == -1 || orderEnd == -1) continue;

                String orderDetails = line.substring(orderStart, orderEnd);
                String[] items = orderDetails.split(", ");

                for (String itemEntry : items)
                {
                    String[] parts = itemEntry.split("=");
                    if (parts.length == 2)
                    {
                        String item = parts[0].trim();
                        int qty = Integer.parseInt(parts[1].trim());

                        salesData.put(item, salesData.getOrDefault(item, 0) + qty);
                    }
                }
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "No sales records found.");
            return;
        }

        if (salesData.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "No valid sales data to display.");
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : salesData.entrySet())
        {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                                  "Sales Distribution",
                                  dataset,
                                  true,
                                  true,
                                  false
                              );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(new Color(255, 245, 238));
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setSimpleLabels(true);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(550, 500));
        setContentPane(chartPanel);
        setVisible(true);
    }
}

