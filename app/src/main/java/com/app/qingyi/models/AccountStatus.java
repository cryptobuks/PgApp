package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/9/4.
 */

public class AccountStatus implements Serializable {

        @Expose
        private  String address;
        @Expose
        private  double balance;
        @Expose
        private  String account;
        @Expose
        private  boolean isSuccess;

        public String getAddress() {
                return address;
        }

        public double getBalance() {
                return balance;
        }

        public String getAccount() {
                return account;
        }

        public boolean getIsSuccess() {
                return isSuccess;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public void setBalance(double balance) {
                this.balance = balance;
        }

        public void setAccount(String account) {
                this.account = account;
        }

        public void setIsSuccess(boolean isSuccess) {
                this.isSuccess = isSuccess;
        }
}
