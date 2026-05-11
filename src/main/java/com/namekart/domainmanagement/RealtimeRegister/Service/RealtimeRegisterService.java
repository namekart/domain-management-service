package com.namekart.domainmanagement.RealtimeRegister.Service;

import com.namekart.domainmanagement.RealtimeRegister.Entity.*;
import com.namekart.domainmanagement.RealtimeRegister.Feign.RealtimeRegisterFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeRegisterService {

    private final RealtimeRegisterFeign rrFeign;

    public RrDomainListResponse listDomains(Map<String, Object> params) {
        log.info("listDomains params={}", params);
        return rrFeign.listDomains(params);
    }

    public RrDomainDetails getDomain(String domainName) {
        log.info("getDomain domain={}", domainName);
        return rrFeign.getDomain(domainName);
    }

    public List<RrBulkDomainResult> bulkGetDomains(List<String> domainNames) {
        log.info("bulkGetDomains count={}", domainNames.size());
        List<RrBulkDomainResult> results = new ArrayList<>();
        for (String domain : domainNames) {
            try {
                results.add(RrBulkDomainResult.from(rrFeign.getDomain(domain)));
            } catch (Exception e) {
                log.warn("bulkGetDomains failed for domain={} error={}", domain, e.getMessage());
                results.add(RrBulkDomainResult.error(domain, e.getMessage()));
            }
        }
        return results;
    }

    public ResponseEntity<RrRegisterResponse> registerDomain(String domainName, RrRegisterRequest request) {
        log.info("registerDomain domain={}", domainName);
        return rrFeign.registerDomain(domainName, request);
    }

    public ResponseEntity<Void> updateDomain(String domainName, RrUpdateRequest request) {
        log.info("updateDomain domain={}", domainName);
        return rrFeign.updateDomain(domainName, request);
    }

    public ResponseEntity<Void> renewDomain(String domainName, RrRenewRequest request) {
        log.info("renewDomain domain={}", domainName);
        return rrFeign.renewDomain(domainName, request);
    }

    public ResponseEntity<Void> deleteDomain(String domainName) {
        log.info("deleteDomain domain={}", domainName);
        return rrFeign.deleteDomain(domainName);
    }

    public ResponseEntity<RrTransferResponse> transferDomain(String domainName, RrTransferRequest request) {
        log.info("transferDomain domain={}", domainName);
        return rrFeign.transferDomain(domainName, request);
    }

    public RrNotificationListResponse listNotifications(String customer, Map<String, Object> params) {
        log.info("listNotifications customer={} params={}", customer, params);
        return rrFeign.listNotifications(customer, params);
    }

    public void ackNotification(String customer, Long notificationId) {
        log.info("ackNotification customer={} notificationId={}", customer, notificationId);
        rrFeign.ackNotification(customer, notificationId, Map.of());
    }

    public RrContactListResponse listContacts(String customer, Map<String, Object> params) {
        log.info("listContacts customer={} params={}", customer, params);
        return rrFeign.listContacts(customer, params);
    }

    public RrContactDetails getContact(String customer, String handle) {
        log.info("getContact customer={} handle={}", customer, handle);
        return rrFeign.getContact(customer, handle);
    }

    public List<RrBulkContactResult> bulkGetContacts(String customer, List<String> handles) {
        log.info("bulkGetContacts customer={} count={}", customer, handles.size());
        List<RrBulkContactResult> results = new ArrayList<>();
        for (String handle : handles) {
            try {
                results.add(RrBulkContactResult.from(rrFeign.getContact(customer, handle)));
            } catch (Exception e) {
                log.warn("bulkGetContacts failed for handle={} error={}", handle, e.getMessage());
                results.add(RrBulkContactResult.error(handle, e.getMessage()));
            }
        }
        return results;
    }

    public ResponseEntity<Void> createContact(String customer, String handle, RrContactCreateRequest request) {
        log.info("createContact customer={} handle={}", customer, handle);
        return rrFeign.createContact(customer, handle, request);
    }

    public ResponseEntity<Void> updateContact(String customer, String handle, RrContactUpdateRequest request) {
        log.info("updateContact customer={} handle={}", customer, handle);
        return rrFeign.updateContact(customer, handle, request);
    }

    public ResponseEntity<Void> deleteContact(String customer, String handle) {
        log.info("deleteContact customer={} handle={}", customer, handle);
        return rrFeign.deleteContact(customer, handle);
    }

    public Map<String, String> getAuthCode(String domainName) {
        log.info("getAuthCode domain={}", domainName);
        RrDomainDetails domain = rrFeign.getDomain(domainName);
        Map<String, String> result = new LinkedHashMap<>();
        result.put("domainName", domain.getDomainName());
        result.put("authcode", domain.getAuthcode());
        return result;
    }
}
