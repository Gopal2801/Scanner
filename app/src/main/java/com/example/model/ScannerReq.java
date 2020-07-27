package com.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScannerReq {
    @SerializedName("uniqueId")
    @Expose
    String uniqueId = "";
    @SerializedName("productId")
    @Expose
    String productId = "";
    @SerializedName("productName")
    @Expose
    String productName = "";
    @SerializedName("timeZone")
    @Expose
    String timeZone = "";

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }


}
