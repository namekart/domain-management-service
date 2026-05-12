package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrPriceListItem {
    private String product;
    private String action;
    private Integer price;
    private String currency;
}
