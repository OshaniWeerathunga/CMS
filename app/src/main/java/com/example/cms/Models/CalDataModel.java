package com.example.cms.Models;

public class CalDataModel {

    String capital,outstanding,intrest,month,installment;

    public CalDataModel(String capital, String outstanding, String month, String intrest, String installment) {
        this.capital = capital;
        this.outstanding = outstanding;
        this.intrest = intrest;
        this.month = month;
        this.installment = installment;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getIntrest() {
        return intrest;
    }

    public void setIntrest(String intrest) {
        this.intrest = intrest;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }
}
