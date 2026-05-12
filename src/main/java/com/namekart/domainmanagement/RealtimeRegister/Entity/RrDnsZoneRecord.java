package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrDnsZoneRecord {
    private String name;
    private String type;
    private String content;
    private Integer ttl;
    private Integer prio;
}
