package uk.co.codecritical.asrsdemo.common;

import java.util.Objects;

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

    public int getProductId() {
        return product.getId();
    }

    //endregion

    //region BI

    public void incAmount(int inc) {
        amount += inc;
    }

    //endregion

    //region toString

    @Override
    public String toString() {
        return "Tote{" +
                "id=" + id +
                ", productId=" + product.getId() +
                ", productName=" + product.getName() +
                ", amount=" + amount +
                '}';
    }

    public String niceProduct() {
        return String.format("%03d,%s,%d",
                id,
                getProduct().getName(),
                amount);
    }

    public static String niceProduct(Tote pa) {
        if (pa == null)
            return "";
        else
            return pa.niceProduct();
    }

    //endregion

    //region Comparison

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tote tote = (Tote) o;
        return id == tote.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    //endregion

    //region Static Code

    private static int nextId = 1;
    private synchronized static int getNextId() {
        return nextId++;
    }

    //endregion

}
