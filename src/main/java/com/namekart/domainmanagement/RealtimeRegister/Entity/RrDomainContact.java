package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RrDomainContact {
    private String role;
    private String handle;

    public RrDomainContact(String role, String handle) {
        this.role = role;
        this.handle = handle;
    }
}
