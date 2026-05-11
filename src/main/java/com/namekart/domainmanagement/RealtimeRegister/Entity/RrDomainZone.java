package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrDomainZone {
    private Integer id;
    private String service;
    private String template;
    private Boolean link;
    private String master;
    private Boolean dnssec;
    private Boolean managed;
}
