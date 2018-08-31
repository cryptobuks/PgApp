package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/9/4.
 */

public class AppUpdate implements Serializable {

        @Expose
        private  String version;
        @Expose
        private  boolean mustUpdate;
        @Expose
        private  int versionCode;
        @Expose
        private  String updateContent;
        @Expose
        private  String updateUrl;

        public boolean isMustUpdate() {
                return mustUpdate;
        }

        public void setMustUpdate(boolean mustUpdate) {
                this.mustUpdate = mustUpdate;
        }

        public String getVersion() {
                return version;
        }

        public void setVersion(String version) {
                this.version = version;
        }

        public String getDownLoad() {
                return updateUrl;
        }

        public void setDownLoad(String downLoad) {
                this.updateUrl = downLoad;
        }

        public int getVersionCode() {
                return versionCode;
        }

        public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
        }

        public String getUpdateContent() {
                return updateContent;
        }

        public void setUpdateContent(String updateContent) {
                this.updateContent = updateContent;
        }
}
