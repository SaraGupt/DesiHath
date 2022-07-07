package com.example.desihand;

public class GetRequestItem {
    String  toplace;

    public GetRequestItem(String toplace, String fromplace, String date, String desc, String name) {
        this.toplace = toplace;
        this.fromplace = fromplace;
        this.date = date;
        this.desc = desc;
        this.name = name;
    }

    String fromplace;
    String date;
    String desc;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public GetRequestItem() {
    }

    public String getToplace() {
        return toplace;
    }

    public void setToplace(String toplace) {
        this.toplace = toplace;
    }

    public String getFromplace() {
        return fromplace;
    }

    public void setFromplace(String fromplace) {
        this.fromplace = fromplace;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }




}