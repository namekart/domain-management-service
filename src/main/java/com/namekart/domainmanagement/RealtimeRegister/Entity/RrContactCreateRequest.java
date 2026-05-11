package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrContactCreateRequest {
    private String name;
    private List<String> addressLine;
    private String postalCode;
    private String city;
    private String country;
    private String email;
    private String voice;
    private String organization;
    private String state;
    private String fax;
    private String brand;
}
