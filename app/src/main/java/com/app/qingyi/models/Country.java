package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class Country implements Serializable {

    @Expose
    private String name = "";
    @Expose
    private String english_name = "";
    @Expose
    private String name_code = "";
    @Expose
    private String phone_code = "";

    public String getName() {
        return name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public String getName_code() {
        return name_code;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
}
