package uk.co.codecritical.asrsdemo.common;

public class Tote {

    private final Product product;
    private int amount;
    private final int barcode;

    public Tote(Product product, int amount, int barcode) {
        this.product = product;
        this.amount = amount;
        this.barcode = barcode;
    }

    public Tote(int productId, String productName, int amount, int barcode) {
        this.product = new Product(productId, productName);
        this.amount = amount;
        this.barcode = barcode;
    }

    //region Get / Set
    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBarcode() {
        return barcode;
    }

    //endregion

    //region toString

    @Override
    public String toString() {
        return "Tote{" +
                "product=" + product +
                ", amount=" + amount +
                '}';
    }

    public String niceProduct() {
        return String.format("%s,%d", getProduct().getName(), getAmount());
    }

    public static String niceProduct(Tote pa) {
        if (pa == null)
            return "";
        else
            return pa.niceProduct();
    }

    //endregion


}
