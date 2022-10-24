package com.example.cms.Models;

public class LoanTableModel {

    String userId,loanNo,proposal,loanCustomer,loanProduct,lastAction,status;

    public LoanTableModel(String userId, String loanNo, String proposal, String loanCustomer, String loanProduct, String lastAction, String status) {
        this.userId = userId;
        this.loanNo = loanNo;
        this.proposal = proposal;
        this.loanCustomer = loanCustomer;
        this.loanProduct = loanProduct;
        this.lastAction = lastAction;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getLoanCustomer() {
        return loanCustomer;
    }

    public void setLoanCustomer(String loanCustomer) {
        this.loanCustomer = loanCustomer;
    }

    public String getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(String loanProduct) {
        this.loanProduct = loanProduct;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
