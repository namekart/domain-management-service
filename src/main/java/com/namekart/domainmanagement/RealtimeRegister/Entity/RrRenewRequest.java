package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class RrRenewRequest {
    private String period;
    private List<RrBillableItem> billables;

    public RrRenewRequest(String period) {
        this.period = period;
    }
}
