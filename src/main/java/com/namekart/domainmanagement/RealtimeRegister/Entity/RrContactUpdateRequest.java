package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrContactUpdateRequest {
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
}
