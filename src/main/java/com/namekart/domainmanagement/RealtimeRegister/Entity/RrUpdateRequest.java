package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrUpdateRequest {
    private String registrant;
    private Boolean privacyProtect;
    private String authcode;
    private Boolean autoRenew;
    private Integer autoRenewPeriod;
    private List<String> ns;
    private List<String> status;
    private String designatedAgent;
    private RrDomainZone zone;
    private List<RrDomainContact> contacts;
    private List<RrKeyData> keyData;
    private List<RrDsData> dsData;
    private List<RrBillableItem> billables;
}
