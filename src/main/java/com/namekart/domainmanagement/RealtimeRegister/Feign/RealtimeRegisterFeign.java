package com.namekart.domainmanagement.RealtimeRegister.Feign;

import com.namekart.domainmanagement.RealtimeRegister.Entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "RealtimeRegisterFeign", url = "${realtimeregister.api.url}", configuration = RealtimeRegisterFeignConfig.class)
public interface RealtimeRegisterFeign {

    @GetMapping("/v2/domains")
    RrDomainListResponse listDomains(@RequestParam Map<String, Object> params);

    @GetMapping("/v2/domains/{domainName}")
    RrDomainDetails getDomain(@PathVariable("domainName") String domainName);

    @PostMapping("/v2/domains/{domainName}")
    ResponseEntity<RrRegisterResponse> registerDomain(
            @PathVariable("domainName") String domainName,
            @RequestBody RrRegisterRequest request);

    @PostMapping("/v2/domains/{domainName}/update")
    ResponseEntity<Void> updateDomain(
            @PathVariable("domainName") String domainName,
            @RequestBody RrUpdateRequest request);

    @PostMapping("/v2/domains/{domainName}/renew")
    ResponseEntity<Void> renewDomain(
            @PathVariable("domainName") String domainName,
            @RequestBody RrRenewRequest request);

    @DeleteMapping("/v2/domains/{domainName}")
    ResponseEntity<Void> deleteDomain(@PathVariable("domainName") String domainName);

    @PostMapping("/v2/domains/{domainName}/transfer")
    ResponseEntity<RrTransferResponse> transferDomain(
            @PathVariable("domainName") String domainName,
            @RequestBody RrTransferRequest request);

    @GetMapping("/v2/customers/{customer}/notifications")
    RrNotificationListResponse listNotifications(
            @PathVariable("customer") String customer,
            @RequestParam Map<String, Object> params);

    @PostMapping("/v2/customers/{customer}/notifications/{notificationId}/ack")
    void ackNotification(
            @PathVariable("customer") String customer,
            @PathVariable("notificationId") Long notificationId,
            @RequestBody Map<String, Object> body);

    @GetMapping("/v2/customers/{customer}/contacts")
    RrContactListResponse listContacts(
            @PathVariable("customer") String customer,
            @RequestParam Map<String, Object> params);

    @GetMapping("/v2/customers/{customer}/contacts/{handle}")
    RrContactDetails getContact(
            @PathVariable("customer") String customer,
            @PathVariable("handle") String handle);

    @PostMapping("/v2/customers/{customer}/contacts/{handle}")
    ResponseEntity<Void> createContact(
            @PathVariable("customer") String customer,
            @PathVariable("handle") String handle,
            @RequestBody RrContactCreateRequest request);

    @PostMapping("/v2/customers/{customer}/contacts/{handle}/update")
    ResponseEntity<Void> updateContact(
            @PathVariable("customer") String customer,
            @PathVariable("handle") String handle,
            @RequestBody RrContactUpdateRequest request);

    @DeleteMapping("/v2/customers/{customer}/contacts/{handle}")
    ResponseEntity<Void> deleteContact(
            @PathVariable("customer") String customer,
            @PathVariable("handle") String handle);

    @GetMapping("/v2/domains/{domainName}/check")
    ResponseEntity<RrDomainCheckResponse> checkDomain(
            @PathVariable("domainName") String domainName,
            @RequestParam(value = "renewPrice", required = false) Boolean renewPrice);

    @GetMapping("/v2/domains/{domainName}/transfer/{processId}")
    ResponseEntity<RrTransferResponse> getTransferStatus(
            @PathVariable("domainName") String domainName,
            @PathVariable("processId") Integer processId);

    @PostMapping("/v2/domains/{domainName}/transfer/{processId}/{action}")
    ResponseEntity<Void> authorizeTransfer(
            @PathVariable("domainName") String domainName,
            @PathVariable("processId") Integer processId,
            @PathVariable("action") String action);

    @PostMapping("/v2/domains/{domainName}/restore")
    ResponseEntity<RrRestoreResponse> restoreDomain(
            @PathVariable("domainName") String domainName,
            @RequestParam(value = "quote", required = false) Boolean quote,
            @RequestBody RrRestoreRequest request);

    @PostMapping("/v2/domains/{domainName}/transfer/push")
    ResponseEntity<Void> pushTransferDomain(
            @PathVariable("domainName") String domainName,
            @RequestBody RrPushTransferRequest request);

    @GetMapping("/v2/dns/zones/{zoneId}")
    ResponseEntity<RrDnsZoneResponse> getDnsZone(
            @PathVariable("zoneId") Integer zoneId);

    @PostMapping("/v2/dns/zones/{zoneId}/update")
    ResponseEntity<Void> updateDnsZone(
            @PathVariable("zoneId") Integer zoneId,
            @RequestBody RrDnsZoneUpdateRequest request);

    @GetMapping("/v2/processes/{processId}")
    ResponseEntity<RrProcessResponse> getProcess(
            @PathVariable("processId") Integer processId,
            @RequestParam(value = "fields", required = false) String fields);

    @GetMapping("/v2/customers/{customer}/pricelist")
    ResponseEntity<List<RrPriceListItem>> getPriceList(
            @PathVariable("customer") String customer,
            @RequestParam(value = "currency", required = false) String currency);

    @GetMapping("/v2/tlds/{tld}/info")
    ResponseEntity<Map<String, Object>> getTldInfo(
            @PathVariable("tld") String tld);
}
