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
}
