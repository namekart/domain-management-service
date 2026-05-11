# Domain Management Service — API cURL Reference

> Two curls per endpoint:
> - **DMS** — call your DMS instance (Postman-friendly, no auth header needed)
> - **Direct RR** — call RealtimeRegister API directly from VPS3 terminal (only VPS3 IP is whitelisted)

---

## Variable reference

Replace these placeholders before running:

| Placeholder | Where it comes from | Example |
|-------------|---------------------|---------|
| `YOUR_RR_API_KEY` | env var `RR_API_KEY` on server — only server-side config needed | `abc123xyz` |
| `YOUR_CUSTOMER_HANDLE` | **provided by caller in request body** — not configured on server | e.g. `NK-12345` |
| `YOUR_REGISTRANT_HANDLE` | **provided by caller in request body** — used for all 3 contacts (ADMIN/TECH/BILLING) | e.g. `NK-REG-001` |

**Set once on VPS3 terminal to avoid retyping:**
```bash
export RR_KEY="YOUR_RR_API_KEY"
export RR_CUST="YOUR_CUSTOMER_HANDLE"
export RR_REG="YOUR_REGISTRANT_HANDLE"
```

---

## How AMP builds register requests (reference)

From `RealtimeRegisterService.java` — this is exactly what AMP sends:

```java
template.setCustomer(customerHandle);       // RR_CUSTOMER_HANDLE
template.setRegistrant(registrantHandle);   // RR_REGISTRANT_HANDLE
template.setAutoRenew(false);
template.setPrivacyProtect(false);
template.setPeriod(12);                     // 24 for .ai domains
template.setBillables(List.of(new RrBillableItem("DOMAIN_COM", "CREATE")));
template.setContacts(List.of(
    new RrDomainContact("ADMIN",   registrantHandle),
    new RrDomainContact("TECH",    registrantHandle),
    new RrDomainContact("BILLING", registrantHandle)
));
```

> `ns` is NOT set — RR uses default nameservers.  
> All 3 contact roles point to the same `registrantHandle`.

---

## 1. List Domains

Lists all domains with optional filtering and pagination. All params are passed straight through to the RR API.

**DMS:**
```bash
# Basic — first 10
curl -X GET "http://localhost:91/rr/domains?limit=10&offset=0"

# Search by name
curl -X GET "http://localhost:91/rr/domains?search=example&limit=25"

# Sort by expiry date descending
curl -X GET "http://localhost:91/rr/domains?order=-expiryDate&limit=50"

# Get all (no pagination)
curl -X GET "http://localhost:91/rr/domains?export=true"

# Count only (no entities returned)
curl -X GET "http://localhost:91/rr/domains?limit=0"
```

**Direct RR (VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/domains?limit=10&offset=0" \
  -H "Authorization: ApiKey $RR_KEY"

# Search
curl -X GET "https://api.yoursrs.com/v2/domains?search=example&limit=25" \
  -H "Authorization: ApiKey $RR_KEY"

# Sort by expiry descending
curl -X GET "https://api.yoursrs.com/v2/domains?order=-expiryDate&limit=50" \
  -H "Authorization: ApiKey $RR_KEY"

# Get all
curl -X GET "https://api.yoursrs.com/v2/domains?export=true" \
  -H "Authorization: ApiKey $RR_KEY"
```

**Response:**
```json
{
  "total": 100,
  "entities": [ { "domainName": "example.com", "expiryDate": "...", ... } ]
}
```

**Supported query params:**

| Param | Description |
|-------|-------------|
| `limit` | Results per page. Default `10`. Use `0` for count-only. |
| `offset` | Pagination offset. |
| `order` | Sort field. Prefix `-` for descending (e.g. `-expiryDate`, `-createdDate`). |
| `search` | Generic search across all fields. |
| `fields` | Comma-separated fields to return (e.g. `domainName,expiryDate,status`). |
| `export` | `true` returns all records — cannot combine with `limit`/`offset`. |
| `noTotal` | `true` omits total count for better performance. |

---

## 2. Bulk Get Domain Info (custom DMS route)

Custom DMS-only endpoint — accepts a list of domain names, calls `GET /v2/domains/{domainName}` for each one sequentially, returns an array of full domain detail objects in the same order. Failed domains appear in-place with only `domainName` and `error` set.

**No Direct RR equivalent** — call `GET /v2/domains/{domainName}` individually on VPS3 for each domain.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/domains/bulk-info" \
  -H "Content-Type: application/json" \
  -d '["example.com", "test.net", "hello.org"]'
```

**Response:**
```json
[
  { "domainName": "example.com", "status": ["OK"], "expiryDate": "2026-01-01T00:00:00Z", ... },
  { "domainName": "test.net",    "status": ["OK"], "expiryDate": "2025-08-15T00:00:00Z", ... },
  { "domainName": "hello.org",   "error": "[404 Not Found] Domain not found" }
]
```

> Failures appear in-place with `domainName` + `error` only — a single failed domain does not abort the rest.

---

## 3. Get Domain Info

**DMS:**
```bash
curl -X GET "http://localhost:91/rr/domains/example.com"
```

**Direct RR (VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/domains/example.com" \
  -H "Authorization: ApiKey $RR_KEY"
```

---

## 4. Register Domain

Matches exactly what AMP sends.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/domains/example.com" \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "YOUR_CUSTOMER_HANDLE",
    "registrant": "YOUR_REGISTRANT_HANDLE",
    "autoRenew": false,
    "privacyProtect": false,
    "period": 12,
    "contacts": [
      { "role": "ADMIN",   "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "TECH",    "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "BILLING", "handle": "YOUR_REGISTRANT_HANDLE" }
    ],
    "billables": [
      { "product": "DOMAIN_COM", "action": "CREATE", "quantity": 1 }
    ]
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/domains/example.com" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"customer\":\"$RR_CUST\",\"registrant\":\"$RR_REG\",\"autoRenew\":false,\"privacyProtect\":false,\"period\":12,\"contacts\":[{\"role\":\"ADMIN\",\"handle\":\"$RR_REG\"},{\"role\":\"TECH\",\"handle\":\"$RR_REG\"},{\"role\":\"BILLING\",\"handle\":\"$RR_REG\"}],\"billables\":[{\"product\":\"DOMAIN_COM\",\"action\":\"CREATE\",\"quantity\":1}]}"
```

> **HTTP 201** = registered immediately.  
> **HTTP 202** = async — domain queued, poll notifications endpoint to confirm.

**For `.ai` domains** — change `"period": 24` (AMP hardcodes 24 months for .ai):
```bash
curl -X POST "https://api.yoursrs.com/v2/domains/example.ai" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"customer\":\"$RR_CUST\",\"registrant\":\"$RR_REG\",\"autoRenew\":false,\"privacyProtect\":false,\"period\":24,\"contacts\":[{\"role\":\"ADMIN\",\"handle\":\"$RR_REG\"},{\"role\":\"TECH\",\"handle\":\"$RR_REG\"},{\"role\":\"BILLING\",\"handle\":\"$RR_REG\"}],\"billables\":[{\"product\":\"DOMAIN_AI\",\"action\":\"CREATE\",\"quantity\":1}]}"
```

---

## 5. Update Domain

> AMP does NOT set `customer` or `registrant` on update requests. Only these fields.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "registrant": "YOUR_REGISTRANT_HANDLE",
    "privacyProtect": false,
    "autoRenew": false,
    "contacts": [
      { "role": "ADMIN",   "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "TECH",    "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "BILLING", "handle": "YOUR_REGISTRANT_HANDLE" }
    ]
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/domains/example.com/update" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"registrant\":\"$RR_REG\",\"privacyProtect\":false,\"autoRenew\":false,\"contacts\":[{\"role\":\"ADMIN\",\"handle\":\"$RR_REG\"},{\"role\":\"TECH\",\"handle\":\"$RR_REG\"},{\"role\":\"BILLING\",\"handle\":\"$RR_REG\"}]}"
```

---

## 6. Renew Domain

> AMP does NOT set `customer` or `registrant` on renew requests.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/domains/example.com/renew" \
  -H "Content-Type: application/json" \
  -d '{
    "period": "12",
    "billables": [
      { "product": "DOMAIN_COM", "action": "RENEW", "quantity": 1 }
    ]
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/domains/example.com/renew" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d '{"period":"12","billables":[{"product":"DOMAIN_COM","action":"RENEW","quantity":1}]}'
```

---

## 7. Delete Domain

> AMP does NOT set `customer` or `registrant` on delete.

**DMS:**
```bash
curl -X DELETE "http://localhost:91/rr/domains/example.com"
```

**Direct RR (VPS3):**
```bash
curl -X DELETE "https://api.yoursrs.com/v2/domains/example.com" \
  -H "Authorization: ApiKey $RR_KEY"
```

---

## 8. Transfer Domain

Matches AMP's `bulkTransfer` — same customer/registrant/contacts pattern as register.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/domains/example.com/transfer" \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "YOUR_CUSTOMER_HANDLE",
    "registrant": "YOUR_REGISTRANT_HANDLE",
    "authcode": "TRANSFER-AUTH-CODE",
    "autoRenew": false,
    "contacts": [
      { "role": "ADMIN",   "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "TECH",    "handle": "YOUR_REGISTRANT_HANDLE" },
      { "role": "BILLING", "handle": "YOUR_REGISTRANT_HANDLE" }
    ],
    "billables": [
      { "product": "DOMAIN_COM", "action": "TRANSFER", "quantity": 1 }
    ]
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/domains/example.com/transfer" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d "{\"customer\":\"$RR_CUST\",\"registrant\":\"$RR_REG\",\"authcode\":\"TRANSFER-AUTH-CODE\",\"autoRenew\":false,\"contacts\":[{\"role\":\"ADMIN\",\"handle\":\"$RR_REG\"},{\"role\":\"TECH\",\"handle\":\"$RR_REG\"},{\"role\":\"BILLING\",\"handle\":\"$RR_REG\"}],\"billables\":[{\"product\":\"DOMAIN_COM\",\"action\":\"TRANSFER\",\"quantity\":1}]}"
```

---

## 9. List Notifications

**All pending (DMS):**
```bash
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/notifications?limit=25&offset=0"
```

**Filter by domain — check status of a specific domain (DMS):**
```bash
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/notifications?q=processIdentifier:example.com&limit=10"
```

**All pending (Direct RR, VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/customers/$RR_CUST/notifications?limit=25&offset=0" \
  -H "Authorization: ApiKey $RR_KEY"
```

**Filter by domain (Direct RR, VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/customers/$RR_CUST/notifications?q=processIdentifier:example.com&limit=10" \
  -H "Authorization: ApiKey $RR_KEY"
```

---

## 10. Acknowledge Notification

Get the `id` from the notification list response first.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/notifications/12345678/ack"
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/customers/$RR_CUST/notifications/12345678/ack" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d '{}'
```

---

## 11. Get Auth Code

Returns `domainName` + `authcode` for a domain. Calls `GET /v2/domains/{domainName}` internally and extracts just those two fields.

**DMS:**
```bash
curl -X GET "http://localhost:91/rr/domains/example.com/authcode"
```

**Response:**
```json
{
  "domainName": "example.com",
  "authcode": "abc123XYZ!"
}
```

> There is no separate RR endpoint for auth codes — they're part of the full domain response. Call `GET /v2/domains/{domainName}` directly on VPS3 and read the `authcode` field.

---

## 12. List Contacts

**DMS:**
```bash
# Basic — first 10
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts?limit=10&offset=0"

# Search by name
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts?search=john&limit=25"

# Export all
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts?export=true"
```

**Direct RR (VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/customers/$RR_CUST/contacts?limit=10&offset=0" \
  -H "Authorization: ApiKey $RR_KEY"
```

**Response:**
```json
{
  "total": 5,
  "entities": [ { "handle": "NK-REG-001", "name": "John Doe", "email": "john@example.com", ... } ]
}
```

---

## 13. Get Contact

**DMS:**
```bash
curl -X GET "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts/NK-REG-001"
```

**Direct RR (VPS3):**
```bash
curl -X GET "https://api.yoursrs.com/v2/customers/$RR_CUST/contacts/NK-REG-001" \
  -H "Authorization: ApiKey $RR_KEY"
```

**Response:**
```json
{
  "customer": "NK-12345",
  "handle": "NK-REG-001",
  "brand": "default",
  "name": "John Doe",
  "organization": "Namekart",
  "addressLine": ["123 Main St"],
  "postalCode": "10001",
  "city": "New York",
  "state": "NY",
  "country": "US",
  "email": "john@example.com",
  "voice": "+1.2125551234",
  "fax": null,
  "createdDate": "2024-01-01T00:00:00Z"
}
```

---

## 14. Bulk Get Contacts

Custom DMS-only endpoint — accepts a list of contact handles, calls `GET /v2/customers/{customer}/contacts/{handle}` for each one sequentially.

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts/bulk-info" \
  -H "Content-Type: application/json" \
  -d '["NK-REG-001", "NK-REG-002", "NK-REG-999"]'
```

**Response:**
```json
[
  { "handle": "NK-REG-001", "name": "John Doe", "email": "john@example.com", ... },
  { "handle": "NK-REG-002", "name": "Jane Smith", "email": "jane@example.com", ... },
  { "handle": "NK-REG-999", "error": "[404 Not Found] Contact not found" }
]
```

---

## 15. Create Contact

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts/NK-REG-001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "addressLine": ["123 Main St"],
    "postalCode": "10001",
    "city": "New York",
    "country": "US",
    "email": "john@example.com",
    "voice": "+1.2125551234",
    "organization": "Namekart",
    "state": "NY"
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/customers/$RR_CUST/contacts/NK-REG-001" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","addressLine":["123 Main St"],"postalCode":"10001","city":"New York","country":"US","email":"john@example.com","voice":"+1.2125551234"}'
```

> **HTTP 201** = contact created.

---

## 16. Update Contact

**DMS:**
```bash
curl -X POST "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts/NK-REG-001/update" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com",
    "voice": "+1.2125559999"
  }'
```

**Direct RR (VPS3):**
```bash
curl -X POST "https://api.yoursrs.com/v2/customers/$RR_CUST/contacts/NK-REG-001/update" \
  -H "Authorization: ApiKey $RR_KEY" \
  -H "Content-Type: application/json" \
  -d '{"email":"newemail@example.com","voice":"+1.2125559999"}'
```

> Only fields you include are changed. All fields are optional.

---

## 17. Delete Contact

**DMS:**
```bash
curl -X DELETE "http://localhost:91/rr/customers/YOUR_CUSTOMER_HANDLE/contacts/NK-REG-001"
```

**Direct RR (VPS3):**
```bash
curl -X DELETE "https://api.yoursrs.com/v2/customers/$RR_CUST/contacts/NK-REG-001" \
  -H "Authorization: ApiKey $RR_KEY"
```

---

## 11. Health & Metrics

```bash
# Health
curl http://localhost:91/health

# Prometheus metrics
curl http://localhost:91/actuator/prometheus

# Swagger UI
open http://localhost:91/swagger-ui.html
```

---

## Notification subjectStatus reference

| Status | Meaning |
|--------|---------|
| `OK` / `COMPLETED` | Operation succeeded |
| `FAILED` | Failed — check `reason` and `statusDetail` fields |
| `PENDING` / `NEW` | Still processing |
| `WARNING` | Done with warnings — check `statusDetail` |

---

## billables product codes

| TLD | product |
|-----|---------|
| .com | `DOMAIN_COM` |
| .net | `DOMAIN_NET` |
| .org | `DOMAIN_ORG` |
| .io  | `DOMAIN_IO` |
| .ai  | `DOMAIN_AI` |
| .co  | `DOMAIN_CO` |

> AMP hardcodes `DOMAIN_COM` for all domains regardless of TLD — you may want to make this dynamic in DMS based on the domain extension.
