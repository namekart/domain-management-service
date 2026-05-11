package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrBulkContactResult {
    private String customer;
    private String handle;
    private String brand;
    private String name;
    private String organization;
    private List<String> addressLine;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String email;
    private String voice;
    private String fax;
    private String createdDate;
    private String error;

    public static RrBulkContactResult from(RrContactDetails d) {
        RrBulkContactResult r = new RrBulkContactResult();
        r.customer = d.getCustomer();
        r.handle = d.getHandle();
        r.brand = d.getBrand();
        r.name = d.getName();
        r.organization = d.getOrganization();
        r.addressLine = d.getAddressLine();
        r.postalCode = d.getPostalCode();
        r.city = d.getCity();
        r.state = d.getState();
        r.country = d.getCountry();
        r.email = d.getEmail();
        r.voice = d.getVoice();
        r.fax = d.getFax();
        r.createdDate = d.getCreatedDate();
        return r;
    }

    public static RrBulkContactResult error(String handle, String errorMessage) {
        RrBulkContactResult r = new RrBulkContactResult();
        r.handle = handle;
        r.error = errorMessage;
        return r;
    }
}
