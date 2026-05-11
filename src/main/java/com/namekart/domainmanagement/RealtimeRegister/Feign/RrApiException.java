package com.namekart.domainmanagement.RealtimeRegister.Feign;

public class RrApiException extends RuntimeException {

    private final String type;
    private final String description;
    private final String httpStatus;

    public RrApiException(String type, String description, String httpStatus) {
        super(httpStatus + " " + type + (description.isBlank() ? "" : ": " + description));
        this.type = type;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getHttpStatus() { return httpStatus; }
}
