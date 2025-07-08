package com.example.corrugatedprice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "costfundstable")
public class CostFunds {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "cost")
    Double cost;

    public CostFunds(String name, Double cost) {
        this.name = name;
        this.cost = cost;
    }
}
