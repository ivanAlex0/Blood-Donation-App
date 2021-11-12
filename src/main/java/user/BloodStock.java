package user;

public class BloodStock {
    private int quantity01Pos;
    private int quantity01Neg;
    private int quantityA2Pos;
    private int quantityA2Neg;
    private int quantityB3Pos;
    private int quantityB3Neg;
    private int quantityAB4Pos;
    private int quantityAB4Neg;

    public BloodStock(int quantity01Pos, int quantity01Neg, int quantityA2Pos, int quantityA2Neg, int quantityB3Pos, int quantityB3Neg, int quantityAB4Pos, int quantityAB4Neg) {
        this.quantity01Pos = quantity01Pos;
        this.quantity01Neg = quantity01Neg;
        this.quantityA2Pos = quantityA2Pos;
        this.quantityA2Neg = quantityA2Neg;
        this.quantityB3Pos = quantityB3Pos;
        this.quantityB3Neg = quantityB3Neg;
        this.quantityAB4Pos = quantityAB4Pos;
        this.quantityAB4Neg = quantityAB4Neg;
    }

    public int getQuantity01Pos() {
        return quantity01Pos;
    }

    public void setQuantity01Pos(int quantity01Pos) {
        this.quantity01Pos = quantity01Pos;
    }

    public int getQuantity01Neg() {
        return quantity01Neg;
    }

    public void setQuantity01Neg(int quantity01Neg) {
        this.quantity01Neg = quantity01Neg;
    }

    public int getQuantityA2Pos() {
        return quantityA2Pos;
    }

    public void setQuantityA2Pos(int quantityA2Pos) {
        this.quantityA2Pos = quantityA2Pos;
    }

    public int getQuantityA2Neg() {
        return quantityA2Neg;
    }

    public void setQuantityA2Neg(int quantityA2Neg) {
        this.quantityA2Neg = quantityA2Neg;
    }

    public int getQuantityB3Pos() {
        return quantityB3Pos;
    }

    public void setQuantityB3Pos(int quantityB3Pos) {
        this.quantityB3Pos = quantityB3Pos;
    }

    public int getQuantityB3Neg() {
        return quantityB3Neg;
    }

    public void setQuantityB3Neg(int quantityB3Neg) {
        this.quantityB3Neg = quantityB3Neg;
    }

    public int getQuantityAB4Pos() {
        return quantityAB4Pos;
    }

    public void setQuantityAB4Pos(int quantityAB4Pos) {
        this.quantityAB4Pos = quantityAB4Pos;
    }

    public int getQuantityAB4Neg() {
        return quantityAB4Neg;
    }

    public void setQuantityAB4Neg(int quantityAB4Neg) {
        this.quantityAB4Neg = quantityAB4Neg;
    }
}
