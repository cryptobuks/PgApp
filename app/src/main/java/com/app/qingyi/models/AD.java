package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/9/4.
 */

public class AD implements Serializable {

        @Expose
        private  String title;
        @Expose
        private  String content;

        @Expose
        private  String imgUrl;

        @Expose
        private  String adType;

        @Expose
        private  String httpUrl;

        @Expose
        private  String createUser;

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public String getImgUrl() {
                return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
        }

        public String getAdType() {
                return adType;
        }

        public void setAdType(String adType) {
                this.adType = adType;
        }

        public String getHttpUrl() {
                return httpUrl;
        }

        public void setHttpUrl(String httpUrl) {
                this.httpUrl = httpUrl;
        }

        public String getCreateUser() {
                return createUser;
        }

        public void setCreateUser(String createUser) {
                this.createUser = createUser;
        }
}
