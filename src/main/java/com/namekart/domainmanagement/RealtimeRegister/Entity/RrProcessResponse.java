package com.namekart.domainmanagement.RealtimeRegister.Entity;

import lombok.Data;
import java.util.List;

@Data
public class RrProcessResponse {
    private Integer id;
    private String user;
    private String customer;
    private String status;
    private String statusDetail;
    private String createdDate;
    private String updatedDate;
    private String startedDate;
    private String type;
    private String identifier;
    private String action;
    private Object command;
    private Object error;
    private List<String> resumeTypes;
    private List<RrBillableItem> billables;
}
