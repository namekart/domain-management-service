package com.namekart.domainmanagement.RealtimeRegister.Feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RealtimeRegisterErrorDecoder implements ErrorDecoder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        String status = "[" + response.status() + " " + response.reason() + "]";

        if (body != null && !body.isBlank()) {
            try {
                JsonNode root = MAPPER.readTree(body);
                String type = text(root, "type");
                String message = text(root, "message");

                if (type != null) {
                    return new RrApiException(type, message != null ? message : "", status);
                }
                if (message != null) return new RuntimeException(status + " " + message);
            } catch (Exception ignored) {}
            String truncated = body.length() > 200 ? body.substring(0, 200) + "…" : body;
            return new RuntimeException(status + " " + truncated);
        }

        return new RuntimeException(status);
    }

    private static String readBody(Response response) {
        if (response.body() == null) return null;
        try (InputStream is = response.body().asInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            return null;
        }
    }

    private static String text(JsonNode node, String field) {
        JsonNode f = node.get(field);
        return (f != null && !f.isNull() && f.isTextual()) ? f.asText() : null;
    }
}
