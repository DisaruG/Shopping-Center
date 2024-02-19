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
}
