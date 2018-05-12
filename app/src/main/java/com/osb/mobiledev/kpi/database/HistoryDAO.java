package com.osb.mobiledev.kpi.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HistoryDAO {
    @Query("SELECT * FROM history where currency_code=:r030")
    List<History> loadAllByCurrencyCode(Long r030);

    @Insert
    void insert(History hist);
}
