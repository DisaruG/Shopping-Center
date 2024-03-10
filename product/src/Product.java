import java.io.Serializable;

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
