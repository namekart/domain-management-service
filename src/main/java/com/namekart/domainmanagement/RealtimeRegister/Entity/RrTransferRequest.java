package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrTransferRequest {
    private String customer;
    private String registrant;
    private Boolean privacyProtect;
    private Integer period;
    private String authcode;
    private Boolean autoRenew;
    private List<String> ns;
    private List<String> transferContacts;
    private String designatedAgent;
    private RrZoneConfig zone;
    private List<RrDomainContact> contacts;
    private List<RrKeyData> keyData;
    private List<RrBillableItem> billables;
}
