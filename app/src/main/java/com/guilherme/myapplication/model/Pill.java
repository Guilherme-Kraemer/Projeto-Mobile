package com.guilherme.myapplication.model;

public class Pill {

    int imgPill;
    String pillName;
    String pillDescription;
    String pilltime;

    public Pill(int imgPill, String pillName, String pillDescription, String pilltime) {
        this.imgPill = imgPill;
        this.pillName = pillName;
        this.pillDescription = pillDescription;
        this.pilltime = pilltime;
    }

    public int getImgPill() {
        return imgPill;
    }

    public String getPillName() {
        return pillName;
    }

    public String getPillDescription() {
        return pillDescription;
    }

    public String getPilltime() {
        return pilltime;
    }
}