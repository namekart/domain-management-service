package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;

@Data
public class RrNotificationResponse {
    private Long id;
    private String fireDate;
    private String readDate;
    private String acknowledgeDate;
    private String message;
    private String reason;
    private String customer;
    private Long process;
    private String eventType;
    private String notificationType;
    private String subjectStatus;
    private String statusDetail;
    private String processIdentifier;
    private String processType;
    private Boolean isAsync;
}
