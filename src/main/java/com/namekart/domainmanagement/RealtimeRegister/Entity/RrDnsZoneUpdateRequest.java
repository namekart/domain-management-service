package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrDnsZoneUpdateRequest {
    private List<RrDnsZoneRecord> records;
    private String template;
    private Boolean link;
    private String master;
    private List<String> ns;
    private Boolean dnssec;
    private String hostMaster;
    private Integer refresh;
    private Integer retry;
    private Integer expire;
    private Integer ttl;
}
