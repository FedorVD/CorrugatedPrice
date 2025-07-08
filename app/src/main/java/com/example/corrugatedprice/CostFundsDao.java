package com.example.corrugatedprice;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CostFundsDao {

    @Insert
    long insert(Raw raw);

    @Query("SELECT * FROM costfundstable")
    List<Raw> getAll();

    @Query("DELETE FROM costfundstable")
        //"DROP TABLE mytable"
    int clearAll();

}
