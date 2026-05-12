package com.namekart.domainmanagement.RealtimeRegister.Controller;

import com.namekart.domainmanagement.RealtimeRegister.Entity.*;
import com.namekart.domainmanagement.RealtimeRegister.Service.RealtimeRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/domains")
    public RrDomainListResponse listDomains(@RequestParam Map<String, Object> params) {
        return rrService.listDomains(params);
    }

    @GetMapping("/domains/{domainName}")
    public RrDomainDetails getDomain(@PathVariable String domainName) {
        return rrService.getDomain(domainName);
    }

    @PostMapping("/domains/bulk-info")
    public List<RrBulkDomainResult> bulkGetDomains(@RequestBody List<String> domainNames) {
        return rrService.bulkGetDomains(domainNames);
    }

    @PostMapping("/domains/{domainName}")
    public ResponseEntity<RrRegisterResponse> registerDomain(
            @PathVariable String domainName,
            @RequestBody RrRegisterRequest request) {
        return rrService.registerDomain(domainName, request);
    }

    @PostMapping("/domains/{domainName}/update")
    public ResponseEntity<Void> updateDomain(
            @PathVariable String domainName,
            @RequestBody RrUpdateRequest request) {
        return rrService.updateDomain(domainName, request);
    }

    @PostMapping("/domains/{domainName}/lock")
    public ResponseEntity<Void> lockDomain(@PathVariable String domainName) {
        return rrService.lockDomain(domainName);
    }

    @PostMapping("/domains/{domainName}/unlock")
    public ResponseEntity<Void> unlockDomain(@PathVariable String domainName) {
        return rrService.unlockDomain(domainName);
    }

    @PostMapping("/domains/{domainName}/renew")
    public ResponseEntity<Void> renewDomain(
            @PathVariable String domainName,
            @RequestBody RrRenewRequest request) {
        return rrService.renewDomain(domainName, request);
    }

    @DeleteMapping("/domains/{domainName}")
    public ResponseEntity<Void> deleteDomain(@PathVariable String domainName) {
        return rrService.deleteDomain(domainName);
    }

    @PostMapping("/domains/{domainName}/transfer")
    public ResponseEntity<RrTransferResponse> transferDomain(
            @PathVariable String domainName,
            @RequestBody RrTransferRequest request) {
        return rrService.transferDomain(domainName, request);
    }

    @GetMapping("/customers/{customer}/notifications")
    public RrNotificationListResponse listNotifications(
            @PathVariable String customer,
            @RequestParam Map<String, Object> params) {
        return rrService.listNotifications(customer, params);
    }

    @PostMapping("/customers/{customer}/notifications/{notificationId}/ack")
    public ResponseEntity<Void> ackNotification(
            @PathVariable String customer,
            @PathVariable Long notificationId) {
        rrService.ackNotification(customer, notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customers/{customer}/contacts")
    public RrContactListResponse listContacts(
            @PathVariable String customer,
            @RequestParam Map<String, Object> params) {
        return rrService.listContacts(customer, params);
    }

    @GetMapping("/customers/{customer}/contacts/{handle}")
    public RrContactDetails getContact(
            @PathVariable String customer,
            @PathVariable String handle) {
        return rrService.getContact(customer, handle);
    }

    @PostMapping("/customers/{customer}/contacts/bulk-info")
    public List<RrBulkContactResult> bulkGetContacts(
            @PathVariable String customer,
            @RequestBody List<String> handles) {
        return rrService.bulkGetContacts(customer, handles);
    }

    @PostMapping("/customers/{customer}/contacts/{handle}")
    public ResponseEntity<Void> createContact(
            @PathVariable String customer,
            @PathVariable String handle,
            @RequestBody RrContactCreateRequest request) {
        return rrService.createContact(customer, handle, request);
    }

    @PostMapping("/customers/{customer}/contacts/{handle}/update")
    public ResponseEntity<Void> updateContact(
            @PathVariable String customer,
            @PathVariable String handle,
            @RequestBody RrContactUpdateRequest request) {
        return rrService.updateContact(customer, handle, request);
    }

    @DeleteMapping("/customers/{customer}/contacts/{handle}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable String customer,
            @PathVariable String handle) {
        return rrService.deleteContact(customer, handle);
    }

    @GetMapping("/domains/{domainName}/authcode")
    public Map<String, String> getAuthCode(@PathVariable String domainName) {
        return rrService.getAuthCode(domainName);
    }

    @GetMapping("/domains/{domainName}/check")
    public ResponseEntity<RrDomainCheckResponse> checkDomain(
            @PathVariable String domainName,
            @RequestParam(required = false) Boolean renewPrice) {
        return rrService.checkDomain(domainName, renewPrice);
    }

    @GetMapping("/domains/{domainName}/transfer/{processId}")
    public ResponseEntity<RrTransferResponse> getTransferStatus(
            @PathVariable String domainName,
            @PathVariable Integer processId) {
        return rrService.getTransferStatus(domainName, processId);
    }

    @PostMapping("/domains/{domainName}/transfer/{processId}/{action}")
    public ResponseEntity<Void> authorizeTransfer(
            @PathVariable String domainName,
            @PathVariable Integer processId,
            @PathVariable String action) {
        return rrService.authorizeTransfer(domainName, processId, action);
    }

    @PostMapping("/domains/{domainName}/restore")
    public ResponseEntity<RrRestoreResponse> restoreDomain(
            @PathVariable String domainName,
            @RequestParam(required = false) Boolean quote,
            @RequestBody RrRestoreRequest request) {
        return rrService.restoreDomain(domainName, quote, request);
    }

    @PostMapping("/domains/{domainName}/push")
    public ResponseEntity<Void> pushTransferDomain(
            @PathVariable String domainName,
            @RequestBody RrPushTransferRequest request) {
        return rrService.pushTransferDomain(domainName, request);
    }

    @GetMapping("/domains/{domainName}/zone")
    public ResponseEntity<RrDnsZoneResponse> getDnsZone(@PathVariable String domainName) {
        return rrService.getDnsZone(domainName);
    }

    @PostMapping("/domains/{domainName}/zone/update")
    public ResponseEntity<Void> updateDnsZone(
            @PathVariable String domainName,
            @RequestBody RrDnsZoneUpdateRequest request) {
        return rrService.updateDnsZone(domainName, request);
    }

    @GetMapping("/processes/{processId}")
    public ResponseEntity<RrProcessResponse> getProcess(
            @PathVariable Integer processId,
            @RequestParam(required = false) String fields) {
        return rrService.getProcess(processId, fields);
    }

    @GetMapping("/customers/{customer}/pricelist")
    public ResponseEntity<List<RrPriceListItem>> getPriceList(
            @PathVariable String customer,
            @RequestParam(required = false) String currency) {
        return rrService.getPriceList(customer, currency);
    }

    @GetMapping("/tlds/{tld}")
    public ResponseEntity<Map<String, Object>> getTldInfo(@PathVariable String tld) {
        return rrService.getTldInfo(tld);
    }
}
