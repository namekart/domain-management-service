package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RrBillableItem {
    private String product;
    private String action;
    private int quantity = 1;

    public RrBillableItem(String product, String action) {
        this.product = product;
        this.action = action;
    }
}
