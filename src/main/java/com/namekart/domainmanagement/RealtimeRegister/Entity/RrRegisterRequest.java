package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrRegisterRequest {
    private String customer;
    private String registrant;
    private Boolean privacyProtect;
    private Integer period;
    private Boolean autoRenew;
    private List<String> ns;
    private List<RrDomainContact> contacts;
    private List<RrBillableItem> billables;
}
