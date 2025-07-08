package com.example.corrugatedprice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rawtable")
public class Raw {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name="name")
    String name;
    @ColumnInfo(name = "cost")
    Double cost;

    public Raw(String name, Double cost) {
        this.name = name;
        this.cost = cost;
    }
}
