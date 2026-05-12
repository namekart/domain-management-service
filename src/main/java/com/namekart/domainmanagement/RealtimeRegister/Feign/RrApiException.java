package com.namekart.domainmanagement.RealtimeRegister.Feign;

public class RrApiException extends RuntimeException {

    private final String type;
    private final String description;
    private final String httpStatus;
    private final boolean recordError;
    private final String rawBody;

    public RrApiException(String type, String description, String httpStatus, boolean recordError, String rawBody) {
        super(httpStatus + " " + type + (description.isBlank() ? "" : ": " + description));
        this.type = type;
        this.description = description;
        this.httpStatus = httpStatus;
        this.recordError = recordError;
        this.rawBody = rawBody;
    }

    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getHttpStatus() { return httpStatus; }
    public boolean isRecordError() { return recordError; }
    public String getRawBody() { return rawBody; }
}
