package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrContactListResponse {
    private Integer total;
    private List<RrContactDetails> entities;
}
