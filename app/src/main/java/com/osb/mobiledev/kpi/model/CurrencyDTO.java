package com.osb.mobiledev.kpi.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO implements Serializable{
    public String currencyTitle;
    public Double rate;
    public String date;
    public Long r030;
}
