package com.namekart.domainmanagement.Controller;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final Tracer tracer;

    @Value("${server.port:91}")
    private int serverPort;

    @GetMapping("/health")
    public String healthCheck() {
        log.info("Health check called");
        return "Domain Management Service is healthy!";
    }

    @GetMapping("/trace-test")
    public String traceTest() {
        log.info("trace-test: parent span started");
        String result = doChildWork("step-one") + " | " + doChildWork("step-two");
        Span current = tracer.currentSpan();
        String traceId = current != null ? current.context().traceId() : "none";
        log.info("trace-test: complete — traceId={}", traceId);
        return "Trace complete. traceId=" + traceId + " | steps: " + result;
    }

    private String doChildWork(String stepName) {
        Span child = tracer.nextSpan().name("child-work/" + stepName).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(child)) {
            log.info("trace-test: child span — step={}", stepName);
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            return stepName + ":ok";
        } finally {
            child.end();
        }
    }
}
