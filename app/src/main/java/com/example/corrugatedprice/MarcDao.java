package com.example.corrugatedprice;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarcDao {
    @Insert
    long insert(Raw raw);

    @Query("SELECT * FROM marktable")
    List<Raw> getAll();

    @Query("DELETE FROM marktable")
        //"DROP TABLE mytable"
    int clearAll();
}
