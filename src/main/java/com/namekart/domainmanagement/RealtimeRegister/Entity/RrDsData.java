package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrDsData {
    private Integer keyTag;
    private Integer algorithm;
    private Integer digestType;
    private String digest;
}
