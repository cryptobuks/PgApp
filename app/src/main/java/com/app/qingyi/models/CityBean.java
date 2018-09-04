package com.app.qingyi.models;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class CityBean  implements Serializable {

    @Expose
    private String note;
    @Expose
    private List<Data> data;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data implements Serializable{
        @Expose
        private String name;
        @Expose
        private List<City> city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<City> getCity() {
            return city;
        }

        public void setCity(List<City> city) {
            this.city = city;
        }

        public static class City implements Serializable{
            @Expose
            private String name;
            @Expose
            private List<String> county;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getCounty() {
                return county;
            }

            public void setCounty(List<String> county) {
                this.county = county;
            }
        }
    }
}