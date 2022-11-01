package uk.co.codecritical.asrsdemo.common;

public class Tote {

    private final int id;
    private final Product product;
    private int amount;

    public Tote(Product product, int amount) {
        this.id = getNextId();
        this.product = product;
        this.amount = amount;
    }

    public Tote(int productId, String productName, int amount) {
        this.id = getNextId();
        this.product = new Product(productId, productName);
        this.amount = amount;
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

    public int getId() {
        return id;
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

    //region Static Code

    private static int nextId = 1;
    private synchronized static int getNextId() {
        return nextId++;
    }

    //endregion

}
