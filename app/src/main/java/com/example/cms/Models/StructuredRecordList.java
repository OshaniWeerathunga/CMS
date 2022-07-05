package com.example.cms.Models;

import java.io.Serializable;

public class StructuredRecordList implements Serializable {

    public String from;
    public String to;
    public String installment;

    public StructuredRecordList() {
    }

    public StructuredRecordList(String from, String to, String installment) {
        this.from = from;
        this.to = to;
        this.installment = installment;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }
}
