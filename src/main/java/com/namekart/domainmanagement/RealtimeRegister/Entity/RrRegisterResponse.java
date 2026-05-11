package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrRegisterResponse {
    private String domainName;
    private String expiryDate;
    private List<String> status;
}
