package com.osb.mobiledev.kpi.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract HistoryDAO historyDAO();
}
