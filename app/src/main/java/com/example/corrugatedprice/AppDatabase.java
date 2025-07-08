package com.example.corrugatedprice;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Raw.class, Mark.class, CostFunds.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract RawDao rawDao();
    public abstract MarcDao markDao();
    public abstract CostFundsDao costFundsDao();

}
