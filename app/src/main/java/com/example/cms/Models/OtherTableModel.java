package com.example.cms.Models;

public class OtherTableModel {

    String id,customer,asset,product,financeAmt,rate,tenor,status,userId,topic;

    public OtherTableModel(String id, String customer, String asset, String product, String financeAmt, String rate, String tenor, String status, String userId, String topic) {
        this.id = id;
        this.customer = customer;
        this.asset = asset;
        this.product = product;
        this.financeAmt = financeAmt;
        this.rate = rate;
        this.tenor = tenor;
        this.status = status;
        this.userId = userId;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getAsset() {
        return asset;
    }

    public String getProduct() {
        return product;
    }

    public String getFinanceAmt() {
        return financeAmt;
    }

    public String getRate() {
        return rate;
    }

    public String getTenor() {
        return tenor;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getTopic() {
        return topic;
    }
}
