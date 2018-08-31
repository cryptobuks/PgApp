package com.app.qingyi.models;

import com.app.qingyi.utils.Utils;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class Information implements Serializable {
    @Expose
    private ArrayList<inforItem> data;
    @Expose
    private  String success ;
    @Expose
    private  String msg ;

    public void setData(ArrayList<inforItem> data) {
        this.data = data;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<inforItem> getData() {
        return data;
    }

    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public class inforItem implements Serializable{
        @Expose
        private  String name;
        @Expose
        private  String brief;
        @Expose
        private  String imgurl = "";
        @Expose
        private  String httpurl = "";
        @Expose
        private Date createdAt;

        public void setName(String name) {
            this.name = name;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public void setHttpurl(String httpurl) {
            this.httpurl = httpurl;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Expose

        private Date updatedAt;

        public String getName() {
            return name;
        }

        public String getBrief() {
            return brief;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getHttpurl() {
            return httpurl;
        }

        public String getCreatedAt() {
            return Utils.dateToStringFormat(createdAt);
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }
    }

}
