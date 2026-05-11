package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;
import com.namekart.domainmanagement.RealtimeRegister.Entity.RrNotificationResponse;

@Data
public class RrNotificationListResponse {
    private Integer total;
    private List<RrNotificationResponse> entities;
}
