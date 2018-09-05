package com.app.qingyi.models;

import com.app.qingyi.utils.Utils;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class Goods implements Serializable {

    @Expose
    private ArrayList<GoodsItem> list;
    @Expose
    private int total;
    @Expose
    private int count;

    public void setList(ArrayList<GoodsItem> list) {
        this.list = list;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<GoodsItem> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public static class GoodsItem implements Serializable {
        @Expose
        private int id;
        @Expose
        private String title;
        @Expose
        private String name;
        @Expose
        private String age;
        @Expose
        private String country;
        @Expose
        private String province;
        @Expose
        private String city;
        @Expose
        private String area;
        @Expose
        private String seePrice;
        @Expose
        private String brief;
        @Expose
        private String price;
        @Expose
        private String serviceTime;
        @Expose
        private String service;
        @Expose
        private String describe;
        @Expose
        private String qq;
        @Expose
        private String face;
        @Expose
        private String tel;
        @Expose
        private String wechat;
        @Expose
        private String[] pictures;
        @Expose
        private int visitors;

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getCity() {
            return city;
        }

        public String getArea() {
            return area;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setSeePrice(String seePrice) {
            this.seePrice = seePrice;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setServiceTime(String serviceTime) {
            this.serviceTime = serviceTime;
        }

        public void setService(String service) {
            this.service = service;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public void setPictures(String[] pictures) {
            this.pictures = pictures;
        }

        public void setVisitors(int visitors) {
            this.visitors = visitors;
        }

        public String getVisitors() {
            return visitors >= 10000 ? division(visitors,10000) +"w" : visitors+"";
        }

        public String division(int a ,int b){
            String result = "";
            float num =(float)a/b;
            DecimalFormat df = new DecimalFormat("0.0");
            result = df.format(num);
            return result;
        }
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getSeePrice() {
            return seePrice;
        }

        public String getBrief() {
            return brief;
        }

        public String getPrice() {
            return price;
        }

        public String getServiceTime() {
            return serviceTime;
        }

        public String getService() {
            return service;
        }

        public String getDescribe() {
            return describe;
        }

        public String getQq() {
            return qq;
        }

        public String getTel() {
            return tel;
        }

        public String getWechat() {
            return wechat;
        }

        public String[] getPictures() {
            return pictures;
        }

        public GoodsItem(int id, String title, String[] pictures, int visitors, String price) {
            this.id = id;
            this.title = title;
            this.pictures = pictures;
            this.price = price;
            this.visitors = visitors;
        }
    }

}
