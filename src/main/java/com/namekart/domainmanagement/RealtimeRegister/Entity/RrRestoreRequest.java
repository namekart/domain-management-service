package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrRestoreRequest {
    private String reason;
    private List<RrBillableItem> billables;
}
