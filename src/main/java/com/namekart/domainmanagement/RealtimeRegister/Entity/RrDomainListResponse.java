package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrDomainListResponse {
    private Integer total;
    private List<RrDomainDetails> entities;
}
