package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrKeyData {
    private Integer protocol;
    private Integer flags;
    private Integer algorithm;
    private String publicKey;
}
