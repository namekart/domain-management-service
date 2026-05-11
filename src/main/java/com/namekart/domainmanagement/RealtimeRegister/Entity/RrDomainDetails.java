package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrDomainDetails {
    private String domainName;
    private String registry;
    private String registryAccount;
    private String customer;
    private String registrant;
    private Boolean privacyProtect;
    private List<String> status;
    private String authcode;
    private String languageCode;
    private Boolean autoRenew;
    private Integer autoRenewPeriod;
    private List<String> ns;
    private List<String> childHosts;
    private String createdDate;
    private String updatedDate;
    private String expiryDate;
    private Boolean premium;
    private Boolean gateway;
    private String roid;
    private String premiumCategory;
    private String privacyContactId;
    private String lastErrpNotificationDate;
    private String lastWdrpNotificationDate;
    private RrDomainZone zone;
    private List<RrDomainContact> contacts;
    private List<RrKeyData> keyData;
    private List<RrDsData> dsData;
}
