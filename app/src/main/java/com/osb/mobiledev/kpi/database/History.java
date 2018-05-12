package com.osb.mobiledev.kpi.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "currency_code")
    private Long r030;

    @ColumnInfo(name = "exchange_rate")
    private Double rate;

    @ColumnInfo(name = "exchange_date")
    private String date;

    public History(Long r030, Double rate, String date) {
        this.r030 = r030;
        this.date = date;
        this.rate = rate;
    }
}
