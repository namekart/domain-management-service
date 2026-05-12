package com.namekart.domainmanagement.RealtimeRegister.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namekart.domainmanagement.RealtimeRegister.Entity.*;
import com.namekart.domainmanagement.RealtimeRegister.Service.RealtimeRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rr")
@RequiredArgsConstructor
public class RealtimeRegisterController {

    private final RealtimeRegisterService rrService;

    @Autowired
    private ObjectMapper objectMapper;

    private void logReq(String endpoint, Object... params) {
        StringBuilder sb = new StringBuilder("[RR][REQ] ").append(endpoint);
        for (int i = 0; i + 1 < params.length; i += 2)
            sb.append(" ").append(params[i]).append("=").append(params[i + 1]);
        log.info(sb.toString());
    }

    private void logResp(String endpoint, Object response) {
        try {
            log.info("[RR][RESP] {} -> {}", endpoint, objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            log.info("[RR][RESP] {} -> {}", endpoint, response);
        }
    }

    private void logError(String endpoint, Exception e) {
        log.error("[RR][ERROR] {} -> {} {}", endpoint, e.getClass().getSimpleName(), e.getMessage());
    }

    @GetMapping("/domains")
    public RrDomainListResponse listDomains(@RequestParam Map<String, Object> params) {
        logReq("GET /rr/domains", "params", params);
        try {
            RrDomainListResponse resp = rrService.listDomains(params);
            logResp("GET /rr/domains", resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/domains", e); throw e; }
    }

    @GetMapping("/domains/{domainName}")
    public RrDomainDetails getDomain(@PathVariable String domainName) {
        logReq("GET /rr/domains/{domainName}", "domainName", domainName);
        try {
            RrDomainDetails resp = rrService.getDomain(domainName);
            logResp("GET /rr/domains/" + domainName, resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/domains/" + domainName, e); throw e; }
    }

    @PostMapping("/domains/bulk-info")
    public List<RrBulkDomainResult> bulkGetDomains(@RequestBody List<String> domainNames) {
        logReq("POST /rr/domains/bulk-info", "count", domainNames.size(), "domains", domainNames);
        try {
            List<RrBulkDomainResult> resp = rrService.bulkGetDomains(domainNames);
            logResp("POST /rr/domains/bulk-info", resp);
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/bulk-info", e); throw e; }
    }

    @PostMapping("/domains/{domainName}")
    public ResponseEntity<RrRegisterResponse> registerDomain(
            @PathVariable String domainName,
            @RequestBody RrRegisterRequest request) {
        logReq("POST /rr/domains/{domainName}", "domainName", domainName, "body", request);
        try {
            ResponseEntity<RrRegisterResponse> resp = rrService.registerDomain(domainName, request);
            logResp("POST /rr/domains/" + domainName, resp.getBody());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName, e); throw e; }
    }

    @PostMapping("/domains/{domainName}/update")
    public ResponseEntity<Void> updateDomain(
            @PathVariable String domainName,
            @RequestBody RrUpdateRequest request) {
        logReq("POST /rr/domains/{domainName}/update", "domainName", domainName, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.updateDomain(domainName, request);
            logResp("POST /rr/domains/" + domainName + "/update", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/update", e); throw e; }
    }

    @PostMapping("/domains/{domainName}/lock")
    public ResponseEntity<Void> lockDomain(@PathVariable String domainName) {
        logReq("POST /rr/domains/{domainName}/lock", "domainName", domainName);
        try {
            ResponseEntity<Void> resp = rrService.lockDomain(domainName);
            logResp("POST /rr/domains/" + domainName + "/lock", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/lock", e); throw e; }
    }

    @PostMapping("/domains/{domainName}/unlock")
    public ResponseEntity<Void> unlockDomain(@PathVariable String domainName) {
        logReq("POST /rr/domains/{domainName}/unlock", "domainName", domainName);
        try {
            ResponseEntity<Void> resp = rrService.unlockDomain(domainName);
            logResp("POST /rr/domains/" + domainName + "/unlock", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/unlock", e); throw e; }
    }

    @PostMapping("/domains/{domainName}/renew")
    public ResponseEntity<Void> renewDomain(
            @PathVariable String domainName,
            @RequestBody RrRenewRequest request) {
        logReq("POST /rr/domains/{domainName}/renew", "domainName", domainName, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.renewDomain(domainName, request);
            logResp("POST /rr/domains/" + domainName + "/renew", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/renew", e); throw e; }
    }

    @DeleteMapping("/domains/{domainName}")
    public ResponseEntity<Void> deleteDomain(@PathVariable String domainName) {
        logReq("DELETE /rr/domains/{domainName}", "domainName", domainName);
        try {
            ResponseEntity<Void> resp = rrService.deleteDomain(domainName);
            logResp("DELETE /rr/domains/" + domainName, resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("DELETE /rr/domains/" + domainName, e); throw e; }
    }

    @PostMapping("/domains/{domainName}/transfer")
    public ResponseEntity<RrTransferResponse> transferDomain(
            @PathVariable String domainName,
            @RequestBody RrTransferRequest request) {
        logReq("POST /rr/domains/{domainName}/transfer", "domainName", domainName, "body", request);
        try {
            ResponseEntity<RrTransferResponse> resp = rrService.transferDomain(domainName, request);
            logResp("POST /rr/domains/" + domainName + "/transfer", resp.getBody());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/transfer", e); throw e; }
    }

    @GetMapping("/customers/{customer}/notifications")
    public RrNotificationListResponse listNotifications(
            @PathVariable String customer,
            @RequestParam Map<String, Object> params) {
        logReq("GET /rr/customers/{customer}/notifications", "customer", customer, "params", params);
        try {
            RrNotificationListResponse resp = rrService.listNotifications(customer, params);
            logResp("GET /rr/customers/" + customer + "/notifications", resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/customers/" + customer + "/notifications", e); throw e; }
    }

    @PostMapping("/customers/{customer}/notifications/{notificationId}/ack")
    public ResponseEntity<Void> ackNotification(
            @PathVariable String customer,
            @PathVariable Long notificationId) {
        logReq("POST /rr/customers/{customer}/notifications/{notificationId}/ack",
                "customer", customer, "notificationId", notificationId);
        try {
            rrService.ackNotification(customer, notificationId);
            log.info("[RR][RESP] POST /rr/customers/{}/notifications/{}/ack -> 200 OK", customer, notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) { logError("POST /rr/customers/" + customer + "/notifications/" + notificationId + "/ack", e); throw e; }
    }

    @GetMapping("/customers/{customer}/contacts")
    public RrContactListResponse listContacts(
            @PathVariable String customer,
            @RequestParam Map<String, Object> params) {
        logReq("GET /rr/customers/{customer}/contacts", "customer", customer, "params", params);
        try {
            RrContactListResponse resp = rrService.listContacts(customer, params);
            logResp("GET /rr/customers/" + customer + "/contacts", resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/customers/" + customer + "/contacts", e); throw e; }
    }

    @GetMapping("/customers/{customer}/contacts/{handle}")
    public RrContactDetails getContact(
            @PathVariable String customer,
            @PathVariable String handle) {
        logReq("GET /rr/customers/{customer}/contacts/{handle}", "customer", customer, "handle", handle);
        try {
            RrContactDetails resp = rrService.getContact(customer, handle);
            logResp("GET /rr/customers/" + customer + "/contacts/" + handle, resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/customers/" + customer + "/contacts/" + handle, e); throw e; }
    }

    @PostMapping("/customers/{customer}/contacts/bulk-info")
    public List<RrBulkContactResult> bulkGetContacts(
            @PathVariable String customer,
            @RequestBody List<String> handles) {
        logReq("POST /rr/customers/{customer}/contacts/bulk-info", "customer", customer, "count", handles.size(), "handles", handles);
        try {
            List<RrBulkContactResult> resp = rrService.bulkGetContacts(customer, handles);
            logResp("POST /rr/customers/" + customer + "/contacts/bulk-info", resp);
            return resp;
        } catch (Exception e) { logError("POST /rr/customers/" + customer + "/contacts/bulk-info", e); throw e; }
    }

    @PostMapping("/customers/{customer}/contacts/{handle}")
    public ResponseEntity<Void> createContact(
            @PathVariable String customer,
            @PathVariable String handle,
            @RequestBody RrContactCreateRequest request) {
        logReq("POST /rr/customers/{customer}/contacts/{handle}",
                "customer", customer, "handle", handle, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.createContact(customer, handle, request);
            logResp("POST /rr/customers/" + customer + "/contacts/" + handle, resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/customers/" + customer + "/contacts/" + handle, e); throw e; }
    }

    @PostMapping("/customers/{customer}/contacts/{handle}/update")
    public ResponseEntity<Void> updateContact(
            @PathVariable String customer,
            @PathVariable String handle,
            @RequestBody RrContactUpdateRequest request) {
        logReq("POST /rr/customers/{customer}/contacts/{handle}/update",
                "customer", customer, "handle", handle, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.updateContact(customer, handle, request);
            logResp("POST /rr/customers/" + customer + "/contacts/" + handle + "/update", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/customers/" + customer + "/contacts/" + handle + "/update", e); throw e; }
    }

    @DeleteMapping("/customers/{customer}/contacts/{handle}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable String customer,
            @PathVariable String handle) {
        logReq("DELETE /rr/customers/{customer}/contacts/{handle}", "customer", customer, "handle", handle);
        try {
            ResponseEntity<Void> resp = rrService.deleteContact(customer, handle);
            logResp("DELETE /rr/customers/" + customer + "/contacts/" + handle, resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("DELETE /rr/customers/" + customer + "/contacts/" + handle, e); throw e; }
    }

    @GetMapping("/domains/{domainName}/authcode")
    public Map<String, String> getAuthCode(@PathVariable String domainName) {
        logReq("GET /rr/domains/{domainName}/authcode", "domainName", domainName);
        try {
            Map<String, String> resp = rrService.getAuthCode(domainName);
            logResp("GET /rr/domains/" + domainName + "/authcode", resp);
            return resp;
        } catch (Exception e) { logError("GET /rr/domains/" + domainName + "/authcode", e); throw e; }
    }

    @GetMapping("/domains/{domainName}/check")
    public ResponseEntity<RrDomainCheckResponse> checkDomain(
            @PathVariable String domainName,
            @RequestParam(required = false) Boolean renewPrice) {
        logReq("GET /rr/domains/{domainName}/check", "domainName", domainName, "renewPrice", renewPrice);
        try {
            ResponseEntity<RrDomainCheckResponse> resp = rrService.checkDomain(domainName, renewPrice);
            logResp("GET /rr/domains/" + domainName + "/check", resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/domains/" + domainName + "/check", e); throw e; }
    }

    @GetMapping("/domains/{domainName}/transfer/{processId}")
    public ResponseEntity<RrTransferResponse> getTransferStatus(
            @PathVariable String domainName,
            @PathVariable Integer processId) {
        logReq("GET /rr/domains/{domainName}/transfer/{processId}", "domainName", domainName, "processId", processId);
        try {
            ResponseEntity<RrTransferResponse> resp = rrService.getTransferStatus(domainName, processId);
            logResp("GET /rr/domains/" + domainName + "/transfer/" + processId, resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/domains/" + domainName + "/transfer/" + processId, e); throw e; }
    }

    @PostMapping("/domains/{domainName}/transfer/{processId}/{action}")
    public ResponseEntity<Void> authorizeTransfer(
            @PathVariable String domainName,
            @PathVariable Integer processId,
            @PathVariable String action) {
        logReq("POST /rr/domains/{domainName}/transfer/{processId}/{action}",
                "domainName", domainName, "processId", processId, "action", action);
        try {
            ResponseEntity<Void> resp = rrService.authorizeTransfer(domainName, processId, action);
            logResp("POST /rr/domains/" + domainName + "/transfer/" + processId + "/" + action, resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/transfer/" + processId + "/" + action, e); throw e; }
    }

    @PostMapping("/domains/{domainName}/restore")
    public ResponseEntity<RrRestoreResponse> restoreDomain(
            @PathVariable String domainName,
            @RequestParam(required = false) Boolean quote,
            @RequestBody RrRestoreRequest request) {
        logReq("POST /rr/domains/{domainName}/restore", "domainName", domainName, "quote", quote, "body", request);
        try {
            ResponseEntity<RrRestoreResponse> resp = rrService.restoreDomain(domainName, quote, request);
            logResp("POST /rr/domains/" + domainName + "/restore", resp.getBody());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/restore", e); throw e; }
    }

    @PostMapping("/domains/{domainName}/push")
    public ResponseEntity<Void> pushTransferDomain(
            @PathVariable String domainName,
            @RequestBody RrPushTransferRequest request) {
        logReq("POST /rr/domains/{domainName}/push", "domainName", domainName, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.pushTransferDomain(domainName, request);
            logResp("POST /rr/domains/" + domainName + "/push", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/push", e); throw e; }
    }

    @GetMapping("/domains/{domainName}/zone")
    public ResponseEntity<RrDnsZoneResponse> getDnsZone(@PathVariable String domainName) {
        logReq("GET /rr/domains/{domainName}/zone", "domainName", domainName);
        try {
            ResponseEntity<RrDnsZoneResponse> resp = rrService.getDnsZone(domainName);
            logResp("GET /rr/domains/" + domainName + "/zone", resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/domains/" + domainName + "/zone", e); throw e; }
    }

    @PostMapping("/domains/{domainName}/zone/update")
    public ResponseEntity<Void> updateDnsZone(
            @PathVariable String domainName,
            @RequestBody RrDnsZoneUpdateRequest request) {
        logReq("POST /rr/domains/{domainName}/zone/update", "domainName", domainName, "body", request);
        try {
            ResponseEntity<Void> resp = rrService.updateDnsZone(domainName, request);
            logResp("POST /rr/domains/" + domainName + "/zone/update", resp.getStatusCode());
            return resp;
        } catch (Exception e) { logError("POST /rr/domains/" + domainName + "/zone/update", e); throw e; }
    }

    @GetMapping("/processes/{processId}")
    public ResponseEntity<RrProcessResponse> getProcess(
            @PathVariable Integer processId,
            @RequestParam(required = false) String fields) {
        logReq("GET /rr/processes/{processId}", "processId", processId, "fields", fields);
        try {
            ResponseEntity<RrProcessResponse> resp = rrService.getProcess(processId, fields);
            logResp("GET /rr/processes/" + processId, resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/processes/" + processId, e); throw e; }
    }

    @GetMapping("/customers/{customer}/pricelist")
    public ResponseEntity<List<RrPriceListItem>> getPriceList(
            @PathVariable String customer,
            @RequestParam(required = false) String currency) {
        logReq("GET /rr/customers/{customer}/pricelist", "customer", customer, "currency", currency);
        try {
            ResponseEntity<List<RrPriceListItem>> resp = rrService.getPriceList(customer, currency);
            logResp("GET /rr/customers/" + customer + "/pricelist", resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/customers/" + customer + "/pricelist", e); throw e; }
    }

    @GetMapping("/tlds/{tld}")
    public ResponseEntity<Map<String, Object>> getTldInfo(@PathVariable String tld) {
        logReq("GET /rr/tlds/{tld}", "tld", tld);
        try {
            ResponseEntity<Map<String, Object>> resp = rrService.getTldInfo(tld);
            logResp("GET /rr/tlds/" + tld, resp.getBody());
            return resp;
        } catch (Exception e) { logError("GET /rr/tlds/" + tld, e); throw e; }
    }
}
