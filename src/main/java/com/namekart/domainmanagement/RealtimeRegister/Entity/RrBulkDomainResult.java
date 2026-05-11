package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrBulkDomainResult {
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
    private String error;

    public static RrBulkDomainResult from(RrDomainDetails d) {
        RrBulkDomainResult r = new RrBulkDomainResult();
        r.domainName = d.getDomainName();
        r.registry = d.getRegistry();
        r.registryAccount = d.getRegistryAccount();
        r.customer = d.getCustomer();
        r.registrant = d.getRegistrant();
        r.privacyProtect = d.getPrivacyProtect();
        r.status = d.getStatus();
        r.authcode = d.getAuthcode();
        r.languageCode = d.getLanguageCode();
        r.autoRenew = d.getAutoRenew();
        r.autoRenewPeriod = d.getAutoRenewPeriod();
        r.ns = d.getNs();
        r.childHosts = d.getChildHosts();
        r.createdDate = d.getCreatedDate();
        r.updatedDate = d.getUpdatedDate();
        r.expiryDate = d.getExpiryDate();
        r.premium = d.getPremium();
        r.gateway = d.getGateway();
        r.roid = d.getRoid();
        r.premiumCategory = d.getPremiumCategory();
        r.privacyContactId = d.getPrivacyContactId();
        r.lastErrpNotificationDate = d.getLastErrpNotificationDate();
        r.lastWdrpNotificationDate = d.getLastWdrpNotificationDate();
        r.zone = d.getZone();
        r.contacts = d.getContacts();
        r.keyData = d.getKeyData();
        r.dsData = d.getDsData();
        return r;
    }

    public static RrBulkDomainResult error(String domainName, String errorMessage) {
        RrBulkDomainResult r = new RrBulkDomainResult();
        r.domainName = domainName;
        r.error = errorMessage;
        return r;
    }
}
