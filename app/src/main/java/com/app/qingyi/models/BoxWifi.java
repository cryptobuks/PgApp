package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class BoxWifi implements Serializable {

    @Expose
    private boolean allCanWifi = false;

    public boolean isAllCanWifi() {
        return allCanWifi;
    }

    public void setAllCanWifi(boolean allCanWifi) {
        this.allCanWifi = allCanWifi;
    }
}
