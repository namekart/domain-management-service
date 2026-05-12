package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrTransferResponse {
    private String domainName;
    private String registrar;
    private String status;
    private String requestedDate;
    private String actionDate;
    private String expiryDate;
    private String type;
    private Integer processId;
    private List<RrTransferLogEntry> log;
}
