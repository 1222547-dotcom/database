package phase3;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final StringProperty brand;
    private final StringProperty model;
    private final StringProperty color;
    private final StringProperty ram;
    private final StringProperty storage;
    private final DoubleProperty lastPurchasePrice;
    private final DoubleProperty sellingPrice;
    private final IntegerProperty supplierId;
    private final StringProperty supplierName;

    public Product() {
        this.productId = new SimpleIntegerProperty();
        this.productName = new SimpleStringProperty();
        this.brand = new SimpleStringProperty();
        this.model = new SimpleStringProperty();
        this.color = new SimpleStringProperty();
        this.ram = new SimpleStringProperty();
        this.storage = new SimpleStringProperty();
        this.lastPurchasePrice = new SimpleDoubleProperty();
        this.sellingPrice = new SimpleDoubleProperty();
        this.supplierId = new SimpleIntegerProperty();
        this.supplierName = new SimpleStringProperty();
    }

    public int getProductId() { return productId.get(); }
    public String getProductName() { return productName.get(); }
    public String getBrand() { return brand.get(); }
    public String getModel() { return model.get(); }
    public String getColor() { return color.get(); }
    public String getRam() { return ram.get(); }
    public String getStorage() { return storage.get(); }
    public double getLastPurchasePrice() { return lastPurchasePrice.get(); }
    public double getSellingPrice() { return sellingPrice.get(); }
    public int getSupplierId() { return supplierId.get(); }
    public String getSupplierName() { return supplierName.get(); }

    public void setProductId(int v) { productId.set(v); }
    public void setProductName(String v) { productName.set(v); }
    public void setBrand(String v) { brand.set(v); }
    public void setModel(String v) { model.set(v); }
    public void setColor(String v) { color.set(v); }
    public void setRam(String v) { ram.set(v); }
    public void setStorage(String v) { storage.set(v); }
    public void setLastPurchasePrice(double v) { lastPurchasePrice.set(v); }
    public void setSellingPrice(double v) { sellingPrice.set(v); }
    public void setSupplierId(int v) { supplierId.set(v); }
    public void setSupplierName(String v) { supplierName.set(v); }

    public IntegerProperty productIdProperty() { return productId; }
    public StringProperty productNameProperty() { return productName; }
    public StringProperty brandProperty() { return brand; }
    public StringProperty modelProperty() { return model; }
    public StringProperty colorProperty() { return color; }
    public DoubleProperty sellingPriceProperty() { return sellingPrice; }
    public StringProperty supplierNameProperty() { return supplierName; }
}