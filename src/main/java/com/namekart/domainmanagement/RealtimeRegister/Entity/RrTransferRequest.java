package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrTransferRequest {
    private String customer;
    private String registrant;
    private String authcode;
    private Boolean autoRenew;
    private List<String> ns;
    private List<RrDomainContact> contacts;
    private List<RrBillableItem> billables;
}
