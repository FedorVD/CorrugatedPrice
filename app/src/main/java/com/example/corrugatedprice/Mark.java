package com.example.corrugatedprice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "marktable")
public class Mark {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "firstLay")
    String firstLay;
    @ColumnInfo(name = "secondLay")
    String secondLay;
    @ColumnInfo(name = "thirdLay")
    String thirdLay;

    public Mark(String name, String firstLay, String secondLay, String thirdLay) {
        this.name = name;
        this.firstLay = firstLay;
        this.secondLay = secondLay;
        this.thirdLay = thirdLay;
    }
}
