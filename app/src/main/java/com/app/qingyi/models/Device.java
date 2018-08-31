package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class Device implements Serializable {
    @Expose
    private ArrayList<deviceItem> list;
    @Expose
    private  int total = 0;
    @Expose
    private  int count = 0;

    public ArrayList<deviceItem> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public void setList(ArrayList<deviceItem> list) {
        this.list = list;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public class deviceItem implements Serializable{
        @Expose
        private  int id = -1;
        @Expose
        private  String boxSN;
        @Expose
        private  String boxName = "--";
        @Expose
        private  int status = -1;
        @Expose
        private  boolean isMining;
        @Expose
        private  String boxIp;
        @Expose
        private  double uplinkBandwidth;
        @Expose
        private  double storageSize;
        @Expose
        private  double allTodayCoins;
        @Expose
        private  boolean currentWifi = false;
        @Expose
        private  boolean isExpandable = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBoxSN() {
            return boxSN;
        }

        public void setBoxSN(String boxSN) {
            this.boxSN = boxSN;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isMining() {
            return isMining;
        }

        public void setMining(boolean mining) {
            isMining = mining;
        }

        public String getBoxIp() {
            return boxIp;
        }

        public void setBoxIp(String boxIp) {
            this.boxIp = boxIp;
        }

        public double getUplinkBandwidth() {
            return uplinkBandwidth;
        }

        public void setUplinkBandwidth(double uplinkBandwidth) {
            this.uplinkBandwidth = uplinkBandwidth;
        }

        public double getStorageSize() {
            return storageSize;
        }

        public void setStorageSize(double storageSize) {
            this.storageSize = storageSize;
        }

        public double getAllTodayCoins() {
            return allTodayCoins;
        }

        public void setAllTodayCoins(double allTodayCoins) {
            this.allTodayCoins = allTodayCoins;
        }

        public boolean isExpandable() {
            return isExpandable;
        }

        public void setExpandable(boolean expandable) {
            isExpandable = expandable;
        }

        public String getBoxName() {
            return boxName.equals("") ? "--": boxName;
        }

        public void setBoxName(String boxName) {
            this.boxName = boxName;
        }

        public boolean isCurrentWifi() {
            return currentWifi;
        }

        public void setCurrentWifi(boolean currentWifi) {
            this.currentWifi = currentWifi;
        }
    }

}
