import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class Product implements Serializable {
    private String productID;
    private String productName;
    private int availableQty;
    private double price;

    public Product (String productID, String productName, int availableQty, double price) {
        this.productID = productID;
        this.productName = productName;
        this.availableQty = availableQty;
        this.price = price;
    }

    // Getters and Setters
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract void displayDetails();
}

class Electronics extends Product implements Serializable { // Using Serializable indicate that instances of these classes can be serialized.
    private String brand;
    private int warranty;

    public Electronics(String productID, String productName, int availableQty, double price, String brand, int warranty) {
        super(productID, productName, availableQty, price);
        this.brand = brand;
        this.warranty = warranty;
    }

    // Getters and Setters for Electronics-specific attributes
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public String toString() {
        return super.toString() +
                "\nBrand: " + brand +
                "\nWarranty Period: " + warranty + "months";
    }

    public void displayDetails() {
        System.out.println("Clothing Details: ");
        System.out.println("Product ID: " + getProductID());
        System.out.println("ProductName: " + getProductName());
        System.out.println("Available Quantity: " + getAvailableQty());
        System.out.println("Price: $" + getPrice());
        System.out.println("Brand: " + getBrand());
        System.out.println("Warranty: " + getWarranty() + "months");
        System.out.println("--------------------------------------------");
    }
}

class Clothing extends Product implements Serializable { // Using Serializable indicate that instances of these classes can be serialized.
    private String size;
    private String color;

    public Clothing (String productID, String productName, int availableQty, double price, String size, String color){
        super (productID, productName,availableQty,price);
        this.size = size;
        this.color = color;
    }

    // Getters and Setters for Clothing - Specific attributes
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toString() {
        return super.toString() +
                "\nSize: " + size +
                "\nColor: " + color;
    }

    public void displayDetails() {
        System.out.println("Clothing Details: ");
        System.out.println("Product ID: " + getProductID());
        System.out.println("ProductName: " + getProductName());
        System.out.println("Available Quantity: " + getAvailableQty());
        System.out.println("Price: $" + getPrice());
        System.out.println("Size: " + getSize());
        System.out.println("Color: " + getColor());
        System.out.println("--------------------------------------------");
    }
}

class User implements Serializable { // Using Serializable indicate that instances of these classes can be serialized.
    private String username;
    private String password;
    private final ShoppingCart shoppingCart;

    public User (String username, String password){
        this.username = "W1985637";
        this.password = "12345";
        this.shoppingCart = new ShoppingCart();
    }

    // Getters for User attributes
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}

class ShoppingCart implements Serializable {
    private final List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        // Implement logic to add the products to the cart
        products.add(product);
        System.out.println("Product added to the cart: " + product.getProductName());
    }

    public void removeProduct (Product product) {
        // Implement logic to remove the products from the cart
        if (products.contains(product)) {
            products.remove(product);
            System.out.println("Product removed from the cart: " + product.getProductName());
        } else {
            System.out.println("Product not found in the cart: " + product.getProductName());
        }
    }

    public double calculateTotalCost() {
        //Implement logic to calculate the total cost of the product in the cart
        double totalCost = 0.0;
        for (Product product : products) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }

    public Iterable<Product> getProducts() {
        return null;
    }
}

interface ShoppingManagerInterface {
    void displayProductDetails (Product product);       // Displays the details of a specific product
    List<Product> getAllProducts();                     // Retrieves a list of all products available in the System
    List<Product> searchProducts(String keyword);       // Searches for products based on a keyword.
    void addToCart (User user, Product product);        // Adds a product to the shopping cart for a Specific User
    void removeFromCart (User user, Product product);   // Removes a product from the shopping cart for a specific user
    double calculateTotalCost (User user);              // Calculates the total cost of products in the user's shopping cart
    boolean processPayment(User user, double amount);   // Processes payment for the user's shopping cart
    void displayReceipt(User user);                     // Displays a receipt for the users purchase
}

class WestminsterShoppingManager implements ShoppingManagerInterface {
    private static final String FILE_NAME = "products.dat";
    private List<Product> productList;
    private static final Logger LOGGER = Logger.getLogger(WestminsterShoppingManager.class.getName());

    // Constants for menu choices
    private static final int ADD_PRODUCT = 1;
    private static final int DELETE_PRODUCT = 2;
    private static final int PRINT_PRODUCTS = 3;
    private static final int SAVE_TO_FILE = 4;
    private static final int LOAD_FROM_FILE = 5;
    private static final int EXIT = 0;
    private ClientGUI clientGUI;

    public WestminsterShoppingManager() {
        // Initializing the product list
        this.productList = new ArrayList<>();

        // Initialize the ClientGUI Instance
        this.clientGUI = new ClientGUI(this);
        this.clientGUI.setVisible(true); // Set the GUI visible
    }

    public void displayMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            int choice;

            do {
                displayMainMenu();

                System.out.print("Enter your choice: ");
                try {
                    choice = scanner.nextInt();
                    handleMenuChoice(choice);
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the buffer
                    choice = -1; // Set an invalid choice to continue the loop
                }

            } while (choice != EXIT);
        }
    }

    private void displayMainMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("***** Westminster Shopping Manager Menu *****");
        System.out.println("---------------------------------------------");
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print list of products");
        System.out.println("4. Save products to file");
        System.out.println("5. Load Products from file");
        System.out.println("0. Exit");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case ADD_PRODUCT -> addProduct();
            case DELETE_PRODUCT -> deleteProduct();
            case PRINT_PRODUCTS -> printProducts();
            case SAVE_TO_FILE -> saveToFile();
            case LOAD_FROM_FILE -> loadFromFile();
            case EXIT -> System.out.println("Exiting Westminster Shopping Manager. Goodbye!");
            default -> System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    public void addProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("------------------");
        System.out.println("Add a new product:");
        System.out.println("------------------");
        System.out.println("1. Electronics");
        System.out.println("2. Clothing");
        System.out.print("Choose product type (1 or 2): ");

        int productType;
        while (true) {
            if (scanner.hasNextInt()) {
                productType = scanner.nextInt();
                if (productType == 1 || productType == 2) {
                    break; // valid input
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // consume invalid input
            }
        }

        // Input Common attributes
        System.out.print("Enter product ID: ");
        scanner.nextLine(); // Consume the newline character left by nextInt()
        String productID = scanner.nextLine();

        // Validate product ID format (alphanumeric)
        if (!productID.matches("^[a-zA-Z0-9]+$")) {
            System.out.println("Invalid product ID format. Please use alphanumeric characters only.");
            return;
        }

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        // Check if the product name is empty
        while (productName.trim().isEmpty()) {
            System.out.println("\u001B[31mError: Please enter the product name.\u001B[0m"); // ANSI escape code for red text
            System.out.print("Enter product name: ");
            productName = scanner.nextLine();
        }

        // Input available quantity with validation
        int availableQty;
        while (true) {
            System.out.print("Enter available quantity: ");
            if (scanner.hasNextInt()) {
                availableQty = scanner.nextInt();
                if (availableQty <= 50) {
                    break; // valid input
                } else {
                    System.out.println("Error: Available quantity cannot be more than 50.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // consume invalid input
            }
        }

        // Input price with validation
        double price;
        while (true) {
            System.out.print("Enter price: ");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                break; // valid input
            } else {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next(); // consume invalid input
            }
        }

        // Input specific attributes based on product type
        Product newProduct = null;
        if (productType == 1) {
            // Electronics
            System.out.print("Enter brand: ");
            String brand = scanner.next();

            // Set the warranty period to 12 months by default for all Electronics
            int warranty = 12;

            // Create and add the Electronics object to the productList
            newProduct = new Electronics(productID, productName, availableQty, price, brand, warranty);
        } else if (productType == 2) {
            // Clothing
            System.out.print("Enter size: ");
            String size = scanner.next();
            System.out.print("Enter color: ");
            String color = scanner.next();

            // Create and add the clothing object to the productList
            newProduct = new Clothing(productID, productName, availableQty, price, size, color);
        } else {
            System.out.println("Invalid product type.");
            return;
        }

        // Add the new product to the productList
        productList.add(newProduct);

        // Update the GUI with the new product
        clientGUI.updateProductTable();
    }


    public void deleteProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product ID to delete: ");
        String productIDToDelete = scanner.next();

        // Find the product in the productList based on the ID
        for (Product product : productList) {
            if (product.getProductID().equals(productIDToDelete)) {

                // Display information about the deleted product
                System.out.println("Deleted product information: ");
                displayProductDetails(product);

                if (product instanceof Electronics electronics) {
                    System.out.println("Product Type: Electronics");
                    System.out.println("Brand: " + electronics.getBrand());
                    System.out.println("Warranty Period: " + electronics.getWarranty() + " months");

                } else if (product instanceof Clothing clothing) {
                    System.out.println("Product Type: Clothing");
                    System.out.println("Size: " + clothing.getSize());
                    System.out.println("Color: " + clothing.getColor());
                }

                // Remove the product from the productList
                productList.remove(product);

                // Update the GUI after removing the product
                clientGUI.updateProductTable();

                System.out.println("Total number of products left: " + productList.size());
                return;
            }
        }

        // If the product with the given ID is not found
        System.out.println("Product with ID " + productIDToDelete + " not found.");
    }

    public void printProducts() {
        // Sort the productList alphabetically by product ID
        Collections.sort(productList, Comparator.comparing(Product::getProductID));

        System.out.println("--------------------------------");
        System.out.println("List of products in the system: ");
        System.out.println("--------------------------------");
        for (Product product : productList) {

            if (product instanceof Electronics electronics) {

                // Print Electronics-specific details
                System.out.println("-------------------------");
                System.out.println("Product Type: Electronics");
                System.out.println("-------------------------");
                System.out.println("Product ID: " + electronics.getProductID());
                System.out.println("Product Name: " + electronics.getProductName());
                System.out.println("Available Quantity: " + electronics.getAvailableQty());
                System.out.println("Price: " + electronics.getPrice());
                System.out.println("Brand: " + electronics.getBrand());
                System.out.println("Warranty Period: " + electronics.getWarranty() + " months");

            } else if (product instanceof Clothing clothing) {

                // Print Clothing-specific details
                System.out.println("----------------------");
                System.out.println("Product Type: Clothing");
                System.out.println("----------------------");
                System.out.println("Product ID: " + clothing.getProductID());
                System.out.println("Product Name: " + clothing.getProductName());
                System.out.println("Available Quantity: " + clothing.getAvailableQty());
                System.out.println("Price: " + clothing.getPrice());
                System.out.println("Size: " + clothing.getSize());
                System.out.println("Color: " + clothing.getColor());
            }

            System.out.println("-------------------------------------------------");
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            Collections.sort(productList, Comparator.comparing(Product::getProductID));
            objectOutputStream.writeObject(productList);
            System.out.println("Product list saved to file.");

            // Update the GUI after saving the product list to file
            clientGUI.updateProductTable();

            LOGGER.info("Product list saved to file: " + FILE_NAME);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving product to file: " + e.getMessage(), e);
            System.out.println("Failed to save product list to file. Please check the logs for details.");
        }
    }


    public void loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            productList = (List<Product>) objectInputStream.readObject();
            System.out.println("Product list loaded from file.");

            // Update the GUI after loading the product list from file
            clientGUI.updateProductTable();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading products from file: " + e.getMessage(), e);
            System.out.println("Failed to load product list from file. Please check the logs for details.");
        }
    }



    public void displayProductDetails(Product product) {
        // Implementing logic to display product details
        System.out.println("Product Details: ");
        System.out.println("Product ID: " + product.getProductID());
        System.out.println("Product Name: " + product.getProductName());
        System.out.println("Available Quantity: " + product.getAvailableQty());
        System.out.println("Price: $" + product.getPrice());
        System.out.println("---------------------------------------------------");
    }

    public List<Product> getAllProducts() {
        // Implementing logic to retrieve all products
        return new ArrayList<>(productList);
    }

    public List<Product> searchProducts (String keyword) {
        // Implementing logic to search for products based on a keyword
        List<Product> searchResults = new ArrayList<>();
        for (Product product: productList) {
            if (product.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(product);
            }
        }
        return searchResults;
    }

    public void addToCart(User user, Product product) {
        // Implementing logic to add a product to the user's shopping cart
        user.getShoppingCart().addProduct(product);
        System.out.println(product.getProductName() + " added to the cart for " + user.getUsername());

        // Update the GUI after adding the product to the cart
        clientGUI.updateProductTable();
    }


    @Override
    public void removeFromCart(User user, Product product) {
        // Implementing logic to remove a product from the user's shopping cart
        user.getShoppingCart().removeProduct(product);
        System.out.println(product.getProductName() + " removed from the cart for " + user.getUsername());

        // Update the GUI after removing the product from the cart
        clientGUI.updateProductTable();
    }


    @Override
    public double calculateTotalCost(User user) {
        // Implementing logic to calculate the total cost of products in the user's shopping cart
        return user.getShoppingCart().calculateTotalCost();
    }

    @Override
    public boolean processPayment(User user, double amount) {
        // Implementing logic to process the payment for the user's Shopping cart
        double totalCost = calculateTotalCost(user);
        if (amount >= totalCost) {
            System.out.println("Payment processed successfully for " + user.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public void displayReceipt(User user) {
        // Implementing logic to display a receipt for the user's purchase
        System.out.println("Receipt for " + user.getUsername());
        System.out.println("---------------------------------");
        user.getShoppingCart().getProducts().forEach(this::displayProductDetails);
        System.out.println("Total Cost: $" + calculateTotalCost(user));
        System.out.println("---------------------------------");
    }

    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        shoppingManager.displayMenu();
        LOGGER.info("Westminster Shopping Manager application exited.");
    }
}