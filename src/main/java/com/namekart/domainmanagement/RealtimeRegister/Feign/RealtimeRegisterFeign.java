package com.namekart.domainmanagement.RealtimeRegister.Feign;

import com.namekart.domainmanagement.RealtimeRegister.Entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
