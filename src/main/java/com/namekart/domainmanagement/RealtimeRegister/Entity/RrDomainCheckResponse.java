package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrDomainCheckResponse {
    private Boolean available;
    private String reason;
    private Boolean premium;
    private String currency;
    private Integer price;
    private Integer renewPrice;
}
