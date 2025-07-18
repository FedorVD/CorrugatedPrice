package com.example.corrugatedprice;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RawDao {
    @Insert
    long insert(Raw raw);

    @Query("SELECT * FROM rawtable")
    List<Raw>getAll();

    @Query("SELECT * FROM rawtable WHERE name = :name")
    Raw getByName(String name);

    @Query("DELETE FROM rawtable")
        //"DROP TABLE mytable"
    int clearAll();

}
