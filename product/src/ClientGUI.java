import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientGUI extends JFrame {
    private WestminsterShoppingManager shoppingManager;
    private String username;
    private JLabel titleLabel;
    private JComboBox<String> categoryComboBox;
    private JTextField productIdField;
    private JButton addButton;
    private JButton shoppingCartButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsTextArea;

    // Shopping Cart Interface
    public class ShoppingCartInterface extends JPanel {
        private JTextArea cartTextArea;
        private Map<String, JSpinner> quantitySpinners;
        private Map<String, JLabel> priceLabels;

        public ShoppingCartInterface() {
            setLayout(new BorderLayout());
            quantitySpinners = new HashMap<>();
            priceLabels = new HashMap<>();

            // Initialize components for the shopping cart interface
            cartTextArea = new JTextArea();
            cartTextArea.setEditable(false);

            JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

            add(new JLabel("Shopping Cart"), BorderLayout.NORTH);
            add(cartScrollPane, BorderLayout.CENTER);
        }

        // Method to display selected products in the shopping cart
        public void displaySelectedProducts(int[] selectedRows) {
            JPanel productsPanel = new JPanel(new GridLayout(selectedRows.length, 1));

            // Iterate through selected rows and create a panel for each product
            for (int row : selectedRows) {
                String productID = (String) tableModel.getValueAt(row, 0);
                String productName = (String) tableModel.getValueAt(row, 1);
                double price = (double) tableModel.getValueAt(row, 3);

                // Create a label to display product information
                JLabel productLabel = new JLabel("Product: " + productName + ", Price: $" + price);
                productsPanel.add(productLabel);

                JPanel productDetailsPanel = new JPanel(new BorderLayout());

                // Create a spinner for selecting the quantity
                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
                quantitySpinner.addChangeListener(changeEvent -> updateTotalPrice(productID));
                quantitySpinners.put(productID, quantitySpinner);

                // Create a label to display the total price for the product
                JLabel priceLabel = new JLabel("Total Price: $" + price);
                priceLabels.put(productID, priceLabel);

                // Create a panel to display quantity spinner and total price label
                JPanel quantityPricePanel = new JPanel(new GridLayout(1, 2));
                quantityPricePanel.add(quantitySpinner);
                quantityPricePanel.add(priceLabel);

                // Add the quantity and price panel to the product details panel
                productDetailsPanel.add(quantityPricePanel, BorderLayout.SOUTH);
                productsPanel.add(productDetailsPanel);
            }

            // Clear existing text and set the layout to display products panel
            cartTextArea.setText("");
            cartTextArea.setLayout(new BorderLayout());
            cartTextArea.add(productsPanel, BorderLayout.NORTH);
        }

        // Method to update the total price label when quantity is changed
        private void updateTotalPrice(String productID) {
            int quantity = (int) quantitySpinners.get(productID).getValue();
            double price = (double) tableModel.getValueAt(getRowIndex(productID), 3);

            double totalPrice = quantity * price;
            priceLabels.get(productID).setText("Total Price: $" + totalPrice);
        }

        // Helper method to get the row index of a product in the table model
        private int getRowIndex(String productID) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(productID)) {
                    return i;
                }
            }
            return -1;
        }
    }

    // Client GUI constructor
    public ClientGUI(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;

        if (!showLoginDialog()) {
            System.exit(0);
        }

        setTitle("Client Interface - Welcome " + username);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Westminster Shopping Center");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Add "Shopping Cart" button to the top-east corner
        shoppingCartButton = new JButton("Shopping Cart");
        topPanel.add(shoppingCartButton, BorderLayout.EAST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "All"});
        categoryComboBox.setSelectedIndex(2);
        categoryComboBox.setFont(categoryComboBox.getFont().deriveFont(Font.PLAIN, 20));
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductTable();
            }
        });
        productIdField = new JTextField();

        // Initialize the JTable with the table model
        productTable = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Available Quantity");
        tableModel.addColumn("Product Price");
        tableModel.addColumn("Product Info");
        productTable.setModel(tableModel);

        // Set up sorting for the JTable
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);

        // Add ListSelectionListener to update details when rows are selected
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedRows = productTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    displayProductDetails(selectedRows);
                }
            }
        });

        // Add components to the center panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel categoryLabel = new JLabel("Select Product Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
        centerPanel.add(inputPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(productTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Add "Add to Shopping Cart" button to the center panel and center it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add to Shopping Cart");
        buttonPanel.add(addButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Initialize the JTextArea for details
        detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);

        // Add the details pane to the right of the productTable
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JLabel("Product Details:"), BorderLayout.NORTH);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        centerPanel.add(detailsPanel, BorderLayout.EAST);

        // Initialize the shopping cart interface
        ShoppingCartInterface shoppingCartInterface = new ShoppingCartInterface();

        // Add ActionListener to the "Add to Shopping Cart" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = productTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    shoppingCartInterface.displaySelectedProducts(selectedRows);
                    openShoppingCartInterface(shoppingCartInterface);
                }
            }
        });

        // Add ActionListener to the "Shopping Cart" button
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openShoppingCartInterface(shoppingCartInterface);
            }
        });

        add(centerPanel, BorderLayout.CENTER);

        // Update the product table and make the GUI visible
        updateProductTable();
        setVisible(true);
    }

    // Method to open the shopping cart interface
    private void openShoppingCartInterface(ShoppingCartInterface shoppingCartInterface) {
        JFrame shoppingCartFrame = new JFrame("Shopping Cart");
        shoppingCartFrame.setSize(400, 300);
        shoppingCartFrame.setLayout(new BorderLayout());
        shoppingCartFrame.setLocationRelativeTo(null);

        // Add the shopping cart interface to the frame
        shoppingCartFrame.add(shoppingCartInterface, BorderLayout.CENTER);

        // Add a "Place Order" button to the shopping cart interface
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder(shoppingCartInterface);
            }
        });

        shoppingCartFrame.add(placeOrderButton, BorderLayout.SOUTH);

        shoppingCartFrame.setVisible(true);
    }

    // Method to place an order based on the selected products and quantities
    private void placeOrder(ShoppingCartInterface shoppingCartInterface) {
        // Implement logic to place the order using the selected products and quantities
        // You can access the selected products, quantities, and total prices from the shoppingCartInterface
        // For demonstration purposes, this method simply displays a confirmation message
        JOptionPane.showMessageDialog(null, "Order Placed Successfully!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);

        // Clear the shopping cart interface after placing the order
        shoppingCartInterface.removeAll();
        shoppingCartInterface.repaint();
    }

    // Method to update the product table
    public void updateProductTable(Product product, String productInfo) {
        // Get product details
        String productID = product.getProductID();
        String productName = product.getProductName();
        int availableQty = product.getAvailableQty();
        double price = product.getPrice();

        // Add the product details to the table model
        tableModel.addRow(new Object[]{productID, productName, availableQty, price, productInfo});
    }

    // Method to update the product table based on the selected category
    public void updateProductTable() {
        // Clear the existing rows
        tableModel.setRowCount(0);

        // Get selected category
        String selectedCategory = (String) categoryComboBox.getSelectedItem();

        // Get the products based on the selected category
        List<Product> products;
        if (selectedCategory.equals("Electronics")) {
            products = shoppingManager.getAllProducts()
                    .stream()
                    .filter(p -> p instanceof Electronics)
                    .collect(Collectors.toList());
        } else if (selectedCategory.equals("Clothing")) {
            products = shoppingManager.getAllProducts()
                    .stream()
                    .filter(p -> p instanceof Clothing)
                    .collect(Collectors.toList());
        } else {
            products = shoppingManager.getAllProducts();
        }

        // Update the table with product details
        for (Product product : products) {
            var productInfo = getString(product);
            updateProductTable(product, productInfo);
        }
    }

    // Method to display product details in the JTextArea
    private void displayProductDetails(int[] selectedRows) {
        // Display details in the JTextArea
        StringBuilder details = new StringBuilder();
        for (int row : selectedRows) {
            String productID = (String) tableModel.getValueAt(row, 0);
            String productName = (String) tableModel.getValueAt(row, 1);
            int availableQty = (int) tableModel.getValueAt(row, 2);
            double price = (double) tableModel.getValueAt(row, 3);
            String productInfo = (String) tableModel.getValueAt(row, 4);

            details.append("Product ID: ").append(productID).append("\n");
            details.append("Product Name: ").append(productName).append("\n");
            details.append("Available Quantity: ").append(availableQty).append("\n");
            details.append("Price: $").append(price).append("\n");
            details.append("Additional Info: ").append(productInfo).append("\n");
            details.append("\n");
        }

        detailsTextArea.setText(details.toString());
    }

    // Method to show the login dialog and authenticate the user
    // Method to show the login dialog and authenticate the user
    private boolean showLoginDialog() {
        JPanel panel = new JPanel();
        JLabel userLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField userField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        panel.setLayout(new GridLayout(2, 2));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            username = userField.getText();
            String password = new String(passwordField.getPassword());

            // Create an instance of the User class with the provided username and password
            User user = new User(username, password);

            // You can now use the 'user' object where needed in your code

            return username.equals("w1985637") && password.equals("12345");
        }
        return false;
    }

    // Method to get additional information about a product
    private static String getString(Product product) {
        String productInfo;
        if (product instanceof Electronics) {
            Electronics electronics = (Electronics) product;
            productInfo = "Brand: " + electronics.getBrand() + ", Warranty: " + electronics.getWarranty() + " months";
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            productInfo = "Size: " + clothing.getSize() + ", Color: " + clothing.getColor();
        } else {
            productInfo = "";
        }
        return productInfo;
    }
}