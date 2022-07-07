package com.example.desihand;

public class GetHelpOut {
    public String getToplace1() {
        return toplace1;
    }

    public void setToplace1(String toplace1) {
        this.toplace1 = toplace1;
    }

    public String getFromplace1() {
        return fromplace1;
    }

    public void setFromplace1(String fromplace1) {
        this.fromplace1 = fromplace1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String toplace1;
    String fromplace1;
    String date;
    public GetHelpOut(String toplace1, String fromplace1, String date) {
        this.toplace1 = toplace1;
        this.fromplace1 = fromplace1;
        this.date = date;
    }



    public GetHelpOut() {
    }
}
