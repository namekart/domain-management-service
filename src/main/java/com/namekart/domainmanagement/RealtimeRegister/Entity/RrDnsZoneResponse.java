package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrDnsZoneResponse {
    private Integer id;
    private String name;
    private Boolean managed;
    private String service;
    private Boolean dnssec;
    private String hostMaster;
    private Integer refresh;
    private Integer retry;
    private Integer expire;
    private Integer ttl;
    private List<RrDnsZoneRecord> records;
    private List<RrDnsZoneRecord> defaultRecords;
}
