package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/9/4.
 */

public class AccountStatus implements Serializable {

        //{"allTrue":true,"allBoxSize":5,"allBoxNeedCoins":1500,"remainMiningCoin":17840,"availableCoin":16340,"lockingCoins":11,"totalWaiMaiCoin":40000,"phone":"18810083326","email":"691424873@qq.com","fei":50,"isHaveCapitalPass":true}
        @Expose
        private  String account;
        @Expose
        private  String phone;
        @Expose
        private  String email = "";
        @Expose
        private  double remainMiningCoin = 0;
        @Expose
        private  String totalMiningCoin;
        @Expose
        private  String lockingCoins;
        @Expose
        private  String fei;
        @Expose
        private  boolean isHaveCapitalPass;
        @Expose
        private  int allBoxSize;
        @Expose
        private  double allBoxNeedCoins;
        @Expose
        private  double availableCoin;
        @Expose
        private  double totalWaiMaiCoin;

        public int getAllBoxSize() {
                return allBoxSize;
        }

        public void setAllBoxSize(int allBoxSize) {
                this.allBoxSize = allBoxSize;
        }

        public void setAllBoxNeedCoins(double allBoxNeedCoins) {
                this.allBoxNeedCoins = allBoxNeedCoins;
        }

        public void setAvailableCoin(double availableCoin) {
                this.availableCoin = availableCoin;
        }

        public void setTotalWaiMaiCoin(double totalWaiMaiCoin) {
                this.totalWaiMaiCoin = totalWaiMaiCoin;
        }

        public double getAllBoxNeedCoins() {
                return allBoxNeedCoins;
        }

        public double getAvailableCoin() {
                return availableCoin;
        }

        public double getTotalWaiMaiCoin() {
                return totalWaiMaiCoin;
        }

        public String getFei() {
                return fei;
        }

        public void setFei(String fei) {
                this.fei = fei;
        }

        public boolean isHaveCapitalPass() {
                return isHaveCapitalPass;
        }

        public void setHaveCapitalPass(boolean haveCapitalPass) {
                isHaveCapitalPass = haveCapitalPass;
        }

        public String getLockingCoins() {
                return lockingCoins;
        }

        public void setLockingCoins(String lockingCoins) {
                this.lockingCoins = lockingCoins;
        }

        public String getAccount() {
                return account;
        }

        public void setAccount(String account) {
                this.account = account;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public double getRemainMiningCoin() {
                return remainMiningCoin;
        }

        public void setRemainMiningCoin(double remainMiningCoin) {
                this.remainMiningCoin = remainMiningCoin;
        }

        public String getTotalMiningCoin() {
                return totalMiningCoin;
        }

        public void setTotalMiningCoin(String totalMiningCoin) {
                this.totalMiningCoin = totalMiningCoin;
        }
}
