package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrContactDetails {
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
}
