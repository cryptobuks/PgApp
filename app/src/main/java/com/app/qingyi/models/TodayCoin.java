package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class TodayCoin implements Serializable {
    @Expose
    private String today;
    @Expose
    private int sum = 0;

    public String getToday() {
        return today.substring(5, today.length());
    }

    public int getSum() {
        return sum;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
