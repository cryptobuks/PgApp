package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class MiningStatus implements Serializable {

    @Expose
    private int onLineBox;
    @Expose
    private int allBox;
    @Expose
    private double allTodayCoins = 0;
    @Expose
    private double allYesterdayCoins = 0;
    @Expose
    private double totalMiningCoin = 0;
    @Expose
    private double remainMiningCoin = 0;

    public double getRemainMiningCoin() {
        return remainMiningCoin;
    }

    public void setRemainMiningCoin(double remainMiningCoin) {
        this.remainMiningCoin = remainMiningCoin;
    }

    public int getOnLineBox() {
        return onLineBox;
    }

    public void setOnLineBox(int onLineBox) {
        this.onLineBox = onLineBox;
    }

    public int getAllBox() {
        return allBox;
    }

    public void setAllBox(int allBox) {
        this.allBox = allBox;
    }

    public double getAllYesterdayCoins() {
        return allYesterdayCoins;
    }

    public void setAllYesterdayCoins(double allYesterdayCoins) {
        this.allYesterdayCoins = allYesterdayCoins;
    }

    public double getTotalMiningCoin() {
        return totalMiningCoin;
    }

    public void setTotalMiningCoin(double totalMiningCoin) {
        this.totalMiningCoin = totalMiningCoin;
    }

    public double getAllTodayCoins() {
        return allTodayCoins;
    }

    public void setAllTodayCoins(double allTodayCoins) {
        this.allTodayCoins = allTodayCoins;
    }
}
