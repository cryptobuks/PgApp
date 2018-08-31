package com.app.qingyi.models;

import com.app.qingyi.utils.Utils;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class CapitalFlow implements Serializable {

    @Expose
    private ArrayList<capitalFlowItem> list;
    @Expose
    private  int total ;
    @Expose
    private  int count ;

    public void setList(ArrayList<capitalFlowItem> list) {
        this.list = list;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<capitalFlowItem> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public class capitalFlowItem implements Serializable{
        @Expose
        private  int id;
        @Expose
        private  String account;
        @Expose
        private  double amount = 0;
        @Expose
        private  double minerfee= 0;
        @Expose
        private  double actualAmount= 0;
        @Expose
        private  String status;
        @Expose
        private  String canReceiveAddress;
        @Expose
        private  String hash;
        @Expose
        private  String inOrOut;
        @Expose
        private  String tokenName;
        @Expose
        private Date createdAt;
        @Expose
        private Date updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setMinerfee(double minerfee) {
            this.minerfee = minerfee;
        }

        public void setActualAmount(double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCanReceiveAddress(String canReceiveAddress) {
            this.canReceiveAddress = canReceiveAddress;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public void setInOrOut(String inOrOut) {
            this.inOrOut = inOrOut;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getAccount() {
            return account;
        }

        public double getAmount() {
            return amount;
        }

        public double getMinerfee() {
            return minerfee;
        }

        public double getActualAmount() {
            return actualAmount;
        }

        public String getStatus() {
            return status;
        }

        public String getCanReceiveAddress() {
            return canReceiveAddress;
        }

        public String getHash() {
            return hash;
        }

        public String getInOrOut() {
            return inOrOut;
        }

        public String getTokenName() {
            return tokenName;
        }

        public String getCreatedAt() {
            return Utils.dateToStringFormat(createdAt);
        }

        public String getUpdatedAt() {
            return Utils.dateToStringFormat(updatedAt);
        }
    }

}
