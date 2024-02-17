import java.io.Serializable;

abstract class Product implements Serializable {
    private String productID;
    private String productName;
    private int availableQty;
    private double price;
}
