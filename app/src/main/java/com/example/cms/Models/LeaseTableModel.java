package com.example.cms.Models;

public class LeaseTableModel {

    String userid,leaseApplicationNo,leaseCustomer,leaseProduct,leaseAsset,leaseAction,leaseStatus;

    public LeaseTableModel(String userid, String leaseApplicationNo, String leaseCustomer, String leaseProduct, String leaseAsset, String leaseAction, String leaseStatus) {
        this.userid = userid;
        this.leaseApplicationNo = leaseApplicationNo;
        this.leaseCustomer = leaseCustomer;
        this.leaseProduct = leaseProduct;
        this.leaseAsset = leaseAsset;
        this.leaseAction = leaseAction;
        this.leaseStatus = leaseStatus;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLeaseApplicationNo() {
        return leaseApplicationNo;
    }

    public void setLeaseApplicationNo(String leaseApplicationNo) {
        this.leaseApplicationNo = leaseApplicationNo;
    }

    public String getLeaseCustomer() {
        return leaseCustomer;
    }

    public void setLeaseCustomer(String leaseCustomer) {
        this.leaseCustomer = leaseCustomer;
    }

    public String getLeaseProduct() {
        return leaseProduct;
    }

    public void setLeaseProduct(String leaseProduct) {
        this.leaseProduct = leaseProduct;
    }

    public String getLeaseAsset() {
        return leaseAsset;
    }

    public void setLeaseAsset(String leaseAsset) {
        this.leaseAsset = leaseAsset;
    }

    public String getLeaseAction() {
        return leaseAction;
    }

    public void setLeaseAction(String leaseAction) {
        this.leaseAction = leaseAction;
    }

    public String getLeaseStatus() {
        return leaseStatus;
    }

    public void setLeaseStatus(String leaseStatus) {
        this.leaseStatus = leaseStatus;
    }
}
