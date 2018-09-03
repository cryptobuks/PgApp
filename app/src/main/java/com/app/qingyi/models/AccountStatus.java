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
        private  String balance;
        @Expose
        private  String account;
        @Expose
        private  boolean isSuccess;
        @Expose
        private  String ethToDo;
        @Expose
        private  String minEth;

        public String getAddress() {
                return address;
        }

        public String getBalance() {
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

        public void setBalance(String balance) {
                this.balance = balance;
        }

        public void setAccount(String account) {
                this.account = account;
        }

        public void setIsSuccess(boolean isSuccess) {
                this.isSuccess = isSuccess;
        }

        public void setSuccess(boolean success) {
                isSuccess = success;
        }

        public void setEthToDo(String ethToDo) {
                this.ethToDo = ethToDo;
        }

        public void setMinEth(String minEth) {
                this.minEth = minEth;
        }

        public boolean isSuccess() {
                return isSuccess;
        }

        public String getEthToDo() {
                return ethToDo;
        }

        public String getMinEth() {
                return minEth;
        }
}
