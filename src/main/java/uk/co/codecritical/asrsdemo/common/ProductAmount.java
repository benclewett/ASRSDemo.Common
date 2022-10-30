package uk.co.codecritical.asrsdemo.common;

public class ProductAmount {

    private final Product product;
    private int amount;

    public ProductAmount(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public ProductAmount(int productId, String productName, int amount) {
        this.product = new Product(productId, productName);
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductAmount{" +
                "product=" + product +
                ", amount=" + amount +
                '}';
    }

    public static String niceProduct(ProductAmount pa) {
        if (pa == null)
            return "";
        else
            return String.format("%s,%d", pa.getProduct().getName(), pa.getAmount());
    }

}
