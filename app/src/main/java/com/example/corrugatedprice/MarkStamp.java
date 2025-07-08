package com.example.corrugatedprice;

import androidx.room.ColumnInfo;

public class MarkStamp {

    String firstLay;

    String secondLay;

    String thirdLay;

    public MarkStamp(String firstLay, String secondLay, String thirdLay) {
        this.firstLay = firstLay;
        this.secondLay = secondLay;
        this.thirdLay = thirdLay;
    }
}
