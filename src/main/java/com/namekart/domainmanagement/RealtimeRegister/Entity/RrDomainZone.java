package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrDomainZone {
    private Integer id;
    private String service;
    private String template;
    private Boolean dnssec;
    private String master;
}
