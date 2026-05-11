package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrUpdateRequest {
    private String registrant;
    private Boolean privacyProtect;
    private Boolean autoRenew;
    private List<String> ns;
    private List<RrDomainContact> contacts;
    private List<RrBillableItem> billables;
}
