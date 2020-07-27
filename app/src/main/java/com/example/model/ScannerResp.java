package com.example.model;

public class ScannerResp {

    String uniqueId = "";
    String ProductId = "";
    String ProductName = "";
    String timeZone = "";
    String Scancount = "";

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        this.ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getScancount() {
        return Scancount;
    }

    public void setScancount(String scancount) {
        Scancount = scancount;
    }
}
