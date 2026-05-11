# Domain Management Service — Developer API Guide

**Base URL:** `https://domainmanagement.h.namekart.com`  
**Local:** `http://localhost:91`

All requests/responses are `application/json`.  
No authentication header required from the caller — the service handles RR API auth internally.

---

## Endpoints

| # | Method | Route | Description |
|---|--------|-------|-------------|
| 1 | `GET` | `/rr/domains` | List domains with filters & pagination |
| 2 | `GET` | `/rr/domains/{domainName}` | Get single domain details |
| 3 | `POST` | `/rr/domains/bulk-info` | Get details for multiple domains at once |
| 4 | `GET` | `/rr/domains/{domainName}/authcode` | Get domain auth/transfer code |
| 5 | `POST` | `/rr/domains/{domainName}` | Register a domain |
| 6 | `POST` | `/rr/domains/{domainName}/update` | Update domain settings |
| 7 | `POST` | `/rr/domains/{domainName}/renew` | Renew a domain |
| 8 | `POST` | `/rr/domains/{domainName}/transfer` | Transfer a domain in |
| 9 | `DELETE` | `/rr/domains/{domainName}` | Delete a domain |
| 10 | `GET` | `/rr/customers/{customer}/notifications` | List notifications |
| 11 | `POST` | `/rr/customers/{customer}/notifications/{notificationId}/ack` | Acknowledge a notification |
| 12 | `GET` | `/rr/customers/{customer}/contacts` | List contacts with filters & pagination |
| 13 | `GET` | `/rr/customers/{customer}/contacts/{handle}` | Get single contact |
| 14 | `POST` | `/rr/customers/{customer}/contacts/bulk-info` | Get details for multiple contacts at once |
| 15 | `POST` | `/rr/customers/{customer}/contacts/{handle}` | Create a contact |
| 16 | `POST` | `/rr/customers/{customer}/contacts/{handle}/update` | Update a contact |
| 17 | `DELETE` | `/rr/customers/{customer}/contacts/{handle}` | Delete a contact |

---

## 1. List Domains

`GET /rr/domains`

Returns a paginated list of all registered domains.

**Query parameters (all optional):**

| Param | Type | Description |
|-------|------|-------------|
| `limit` | Integer | Results per page. Default: `10`. Use `0` for count only. |
| `offset` | Integer | Pagination offset. Default: `0`. |
| `order` | String | Sort field. Prefix `-` for descending. E.g. `-expiryDate`, `domainName`. |
| `search` | String | Search across all domain fields. |
| `fields` | String | Comma-separated fields to return. E.g. `domainName,expiryDate,status`. |
| `export` | Boolean | `true` returns all records. Cannot combine with `limit`/`offset`. |
| `noTotal` | Boolean | `true` omits the total count (faster). |

```bash
# First 10 domains
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?limit=10&offset=0"

# Search by name
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?search=example&limit=25"

# Sort by expiry date, newest last
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?order=expiryDate&limit=50"

# Sort by expiry date, soonest first (expiring soon)
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?order=-expiryDate&limit=50"

# Specific fields only
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?fields=domainName,expiryDate,status&limit=100"

# Export all (no pagination)
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?export=true"

# Count only
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains?limit=0"
```

**Response `200 OK`:**
```json
{
  "total": 150,
  "entities": [
    {
      "domainName": "example.com",
      "registry": "verisign",
      "customer": "NK-12345",
      "registrant": "NK-REG-001",
      "status": ["OK"],
      "expiryDate": "2026-01-01T00:00:00Z",
      "autoRenew": false,
      "privacyProtect": false,
      "premium": false,
      "createdDate": "2024-01-01T00:00:00Z",
      "updatedDate": "2024-06-01T00:00:00Z",
      "ns": ["ns1.example.com", "ns2.example.com"],
      "contacts": [
        { "role": "ADMIN",   "handle": "NK-REG-001" },
        { "role": "TECH",    "handle": "NK-REG-001" },
        { "role": "BILLING", "handle": "NK-REG-001" }
      ]
    }
  ]
}
```

---

## 2. Get Domain

`GET /rr/domains/{domainName}`

Returns full details for a single domain.

```bash
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains/example.com"
```

**Response `200 OK`:**
```json
{
  "domainName": "example.com",
  "registry": "verisign",
  "customer": "NK-12345",
  "registrant": "NK-REG-001",
  "status": ["OK"],
  "expiryDate": "2026-01-01T00:00:00Z",
  "autoRenew": false,
  "privacyProtect": false,
  "premium": false,
  "createdDate": "2024-01-01T00:00:00Z",
  "updatedDate": "2024-06-01T00:00:00Z",
  "ns": ["ns1.example.com", "ns2.example.com"],
  "contacts": [
    { "role": "ADMIN",   "handle": "NK-REG-001" },
    { "role": "TECH",    "handle": "NK-REG-001" },
    { "role": "BILLING", "handle": "NK-REG-001" }
  ]
}
```

**Error — domain not found:**
```json
{ "error": "[404 Not Found] Domain not found" }
```

---

## 3. Bulk Get Domains

`POST /rr/domains/bulk-info`

Fetches details for a list of domain names in one call. Each domain is fetched individually server-side and results are returned as an ordered array matching the input. Failures appear in-place — one failure does not abort the rest.

**Request body:** JSON array of domain name strings.

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/bulk-info" \
  -H "Content-Type: application/json" \
  -d '["example.com", "test.net", "hello.org"]'
```

**Response `200 OK`:**
```json
[
  {
    "domainName": "example.com",
    "registry": "verisign",
    "customer": "NK-12345",
    "registrant": "NK-REG-001",
    "status": ["OK"],
    "expiryDate": "2026-01-01T00:00:00Z",
    "autoRenew": false,
    "privacyProtect": false,
    "premium": false,
    "createdDate": "2024-01-01T00:00:00Z",
    "updatedDate": "2024-06-01T00:00:00Z",
    "ns": ["ns1.example.com", "ns2.example.com"],
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ]
  },
  {
    "domainName": "test.net",
    "status": ["OK"],
    "expiryDate": "2025-08-15T00:00:00Z"
  },
  {
    "domainName": "hello.org",
    "error": "[404 Not Found] Domain not found"
  }
]
```

---

## 4. Get Auth Code

`GET /rr/domains/{domainName}/authcode`

Fetches a domain and returns only the domain name and its auth/EPP transfer code. Use this when you need the auth code to initiate an outbound transfer.

```bash
curl -X GET "https://domainmanagement.h.namekart.com/rr/domains/example.com/authcode"
```

**Response `200 OK`:**
```json
{
  "domainName": "example.com",
  "authcode": "abc123XYZ!"
}
```

> The auth code comes from the `authcode` field of the full domain response. If the domain has no auth code set, `authcode` will be `null`.

---

## 5. Register Domain

`POST /rr/domains/{domainName}`

Registers a new domain. Returns `201` if registered immediately, `202` if the operation is queued asynchronously (poll notifications to confirm).

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `customer` | String | **Required** | Your customer handle |
| `registrant` | String | **Required** | Registrant contact handle |
| `privacyProtect` | Boolean | Optional | Enable WHOIS privacy. Default: `false` |
| `period` | Integer | Optional | Registration period in months. Default: `12`. Use `24` for `.ai` domains |
| `autoRenew` | Boolean | Optional | Auto-renewal. Default: `false` |
| `ns` | Array\<String\> | Optional | Nameservers. Omit to use registrar defaults |
| `contacts` | Array | Optional | ADMIN / TECH / BILLING contact handles |
| `billables` | Array | Optional | Required by some TLDs to acknowledge billing |

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com" \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "NK-12345",
    "registrant": "NK-REG-001",
    "privacyProtect": false,
    "period": 12,
    "autoRenew": false,
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ],
    "billables": [
      { "product": "DOMAIN_COM", "action": "CREATE", "quantity": 1 }
    ]
  }'
```

**For `.ai` domains** (24-month minimum):
```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.ai" \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "NK-12345",
    "registrant": "NK-REG-001",
    "privacyProtect": false,
    "period": 24,
    "autoRenew": false,
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ],
    "billables": [
      { "product": "DOMAIN_AI", "action": "CREATE", "quantity": 1 }
    ]
  }'
```

**Response `201 Created`** — registered immediately:
```json
{
  "domainName": "example.com",
  "expiryDate": "2026-01-01T00:00:00Z",
  "status": ["OK"]
}
```

**Response `202 Accepted`** — async, poll notifications:
```json
{
  "domainName": "example.com",
  "expiryDate": null,
  "status": []
}
```

> When you receive `202`, use endpoint **#10** to poll notifications filtered by `processIdentifier:example.com` until `subjectStatus` is `OK` or `FAILED`.

---

## 6. Update Domain

`POST /rr/domains/{domainName}/update`

Updates domain settings. Only fields you include are changed.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `registrant` | String | Optional | New registrant handle |
| `privacyProtect` | Boolean | Optional | Enable/disable WHOIS privacy |
| `autoRenew` | Boolean | Optional | Enable/disable auto-renewal |
| `ns` | Array\<String\> | Optional | Replace nameservers. Empty array removes all. |
| `contacts` | Array | Optional | Replace ADMIN / TECH / BILLING contacts |
| `billables` | Array | Optional | Required by some TLDs |

```bash
# Toggle auto-renew on
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "autoRenew": true
  }'

# Change nameservers
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "ns": ["ns1.namekart.com", "ns2.namekart.com"]
  }'

# Enable privacy + change contacts
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "privacyProtect": true,
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ]
  }'
```

**Response:** `200 OK` (updated immediately) or `202 Accepted` (async).

---

## 7. Renew Domain

`POST /rr/domains/{domainName}/renew`

Renews a domain for an additional period.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `period` | String | **Required** | Renewal period in months as a string. E.g. `"12"` |
| `billables` | Array | Optional | Required by some TLDs |

```bash
# Renew for 12 months
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/renew" \
  -H "Content-Type: application/json" \
  -d '{
    "period": "12",
    "billables": [
      { "product": "DOMAIN_COM", "action": "RENEW", "quantity": 1 }
    ]
  }'

# Renew for 24 months
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/renew" \
  -H "Content-Type: application/json" \
  -d '{
    "period": "24",
    "billables": [
      { "product": "DOMAIN_COM", "action": "RENEW", "quantity": 1 }
    ]
  }'
```

**Response `200 OK`:**
```json
{
  "domainName": "example.com",
  "expiryDate": "2027-01-01T00:00:00Z"
}
```

---

## 8. Transfer Domain

`POST /rr/domains/{domainName}/transfer`

Initiates an inbound domain transfer. Returns `200` if completed immediately, `202` if async.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `customer` | String | **Required** | Your customer handle |
| `registrant` | String | **Required** | Registrant contact handle |
| `authcode` | String | **Required** | Transfer authorization/EPP code from current registrar |
| `autoRenew` | Boolean | Optional | Auto-renewal after transfer. Default: `false` |
| `ns` | Array\<String\> | Optional | Nameservers to set after transfer. Omit to keep existing. |
| `contacts` | Array | Optional | ADMIN / TECH / BILLING contact handles |
| `billables` | Array | Optional | Required by some TLDs |

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/domains/example.com/transfer" \
  -H "Content-Type: application/json" \
  -d '{
    "customer": "NK-12345",
    "registrant": "NK-REG-001",
    "authcode": "abc123XYZ!",
    "autoRenew": false,
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ],
    "billables": [
      { "product": "DOMAIN_COM", "action": "TRANSFER", "quantity": 1 }
    ]
  }'
```

**Response `200 OK`** — completed immediately:
```json
{
  "domainName": "example.com",
  "status": "completed",
  "requestedDate": "2025-01-01T00:00:00Z",
  "expiryDate": "2027-01-01T00:00:00Z",
  "type": "IN",
  "processId": 98765
}
```

**Response `202 Accepted`** — async:
```json
{
  "domainName": "example.com",
  "status": "pending",
  "requestedDate": "2025-01-01T00:00:00Z",
  "actionDate": "2025-01-08T00:00:00Z",
  "type": "IN",
  "processId": 98765
}
```

> Transfer status values: `pendingwhois` → `pendingfoa` → `pendingvalidation` → `pending` → `completed` / `rejected` / `failed`.

---

## 9. Delete Domain

`DELETE /rr/domains/{domainName}`

Deletes a domain or places it into pending delete (depending on registry). No request body.

```bash
curl -X DELETE "https://domainmanagement.h.namekart.com/rr/domains/example.com"
```

**Response `200 OK`** — deleted immediately:
```json
{ "domainName": "example.com" }
```

**Response `202 Accepted`** — placed in pending delete queue.

---

## 10. List Notifications

`GET /rr/customers/{customer}/notifications`

Lists pending and historical notifications for a customer. Use this to check the status of async operations (register, transfer, renew, update).

**Path parameter:**

| Param | Description |
|-------|-------------|
| `customer` | Your customer handle (e.g. `NK-12345`) |

**Query parameters (all optional):**

| Param | Description |
|-------|-------------|
| `limit` | Results per page. Default: `10`. |
| `offset` | Pagination offset. |
| `q` | Filter query. Use `processIdentifier:example.com` to check a specific domain. |

```bash
# All pending notifications
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/notifications?limit=25"

# Check status of a specific domain operation
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/notifications?q=processIdentifier:example.com&limit=10"
```

**Response `200 OK`:**
```json
{
  "total": 3,
  "entities": [
    {
      "id": 12345678,
      "fireDate": "2025-01-01T10:00:00Z",
      "message": "Domain registration completed",
      "subjectStatus": "OK",
      "processIdentifier": "example.com",
      "processType": "DOMAIN_REGISTRATION",
      "isAsync": true
    }
  ]
}
```

**`subjectStatus` values:**

| Value | Meaning |
|-------|---------|
| `OK` / `COMPLETED` | Operation succeeded |
| `FAILED` | Failed — check `reason` and `statusDetail` fields |
| `PENDING` / `NEW` | Still in progress |
| `WARNING` | Completed with warnings |

---

## 11. Acknowledge Notification

`POST /rr/customers/{customer}/notifications/{notificationId}/ack`

Marks a notification as acknowledged. Get the `id` from the List Notifications response.

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/notifications/12345678/ack"
```

**Response `200 OK`** — no body.

---

## 12. List Contacts

`GET /rr/customers/{customer}/contacts`

Returns a paginated list of contacts for a customer.

**Query parameters (all optional):**

| Param | Type | Description |
|-------|------|-------------|
| `limit` | Integer | Results per page. Default: `10`. Use `0` for count only. |
| `offset` | Integer | Pagination offset. Default: `0`. |
| `order` | String | Sort field. Prefix `-` for descending. E.g. `-createdDate`. |
| `search` | String | Search across contact fields. |
| `fields` | String | Comma-separated fields to return. |
| `export` | Boolean | `true` returns all records. Cannot combine with `limit`/`offset`. |
| `noTotal` | Boolean | `true` omits total count (faster). |

```bash
# First 10 contacts
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts?limit=10"

# Search by name
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts?search=john&limit=25"

# Export all
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts?export=true"
```

**Response `200 OK`:**
```json
{
  "total": 5,
  "entities": [
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
  ]
}
```

---

## 13. Get Contact

`GET /rr/customers/{customer}/contacts/{handle}`

Returns full details for a single contact.

```bash
curl -X GET "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/NK-REG-001"
```

**Response `200 OK`:**
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

`POST /rr/customers/{customer}/contacts/bulk-info`

Fetches details for a list of contact handles. Each contact is fetched individually server-side and results are returned as an ordered array. Failures appear in-place — one failure does not abort the rest.

**Request body:** JSON array of contact handle strings.

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/bulk-info" \
  -H "Content-Type: application/json" \
  -d '["NK-REG-001", "NK-REG-002", "NK-REG-999"]'
```

**Response `200 OK`:**
```json
[
  {
    "customer": "NK-12345",
    "handle": "NK-REG-001",
    "name": "John Doe",
    "email": "john@example.com",
    "country": "US"
  },
  {
    "customer": "NK-12345",
    "handle": "NK-REG-002",
    "name": "Jane Smith",
    "email": "jane@example.com",
    "country": "US"
  },
  {
    "handle": "NK-REG-999",
    "error": "[404 Not Found] Contact not found"
  }
]
```

---

## 15. Create Contact

`POST /rr/customers/{customer}/contacts/{handle}`

Creates a new contact. The `{handle}` in the URL is the identifier you assign to this contact.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `name` | String | **Required** | Full name (1–255 chars) |
| `addressLine` | Array\<String\> | **Required** | 1–3 address lines (1–255 chars each) |
| `postalCode` | String | **Required** | Postal/ZIP code (1–16 chars) |
| `city` | String | **Required** | City (1–255 chars) |
| `country` | String | **Required** | ISO 3166-1 alpha-2 country code (e.g. `US`, `GB`) |
| `email` | String | **Required** | Email address |
| `voice` | String | **Required** | Phone in E.164 format (e.g. `+1.2125551234`) |
| `organization` | String | Optional | Organization/company name |
| `state` | String | Optional | State or province |
| `fax` | String | Optional | Fax in E.164 format |
| `brand` | String | Optional | Brand name. Default: `"default"` |

```bash
curl -X POST "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/NK-REG-001" \
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

**Response `201 Created`** — no body.

---

## 16. Update Contact

`POST /rr/customers/{customer}/contacts/{handle}/update`

Updates an existing contact. Only fields you include are changed — all fields are optional.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `name` | String | Optional | Full name |
| `addressLine` | Array\<String\> | Optional | 1–3 address lines |
| `postalCode` | String | Optional | Postal/ZIP code |
| `city` | String | Optional | City |
| `country` | String | Optional | ISO 3166-1 alpha-2 country code |
| `email` | String | Optional | Email address |
| `voice` | String | Optional | Phone in E.164 format |
| `organization` | String | Optional | Organization/company name |
| `state` | String | Optional | State or province |
| `fax` | String | Optional | Fax in E.164 format |

```bash
# Update email and phone only
curl -X POST "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/NK-REG-001/update" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com",
    "voice": "+1.2125559999"
  }'

# Update address
curl -X POST "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/NK-REG-001/update" \
  -H "Content-Type: application/json" \
  -d '{
    "addressLine": ["456 New Ave", "Suite 100"],
    "city": "Los Angeles",
    "state": "CA",
    "postalCode": "90001"
  }'
```

**Response:** `200 OK` (updated immediately) or `202 Accepted` (async).

---

## 17. Delete Contact

`DELETE /rr/customers/{customer}/contacts/{handle}`

Deletes a contact. The contact must not be in use on any active domain.

```bash
curl -X DELETE "https://domainmanagement.h.namekart.com/rr/customers/NK-12345/contacts/NK-REG-001"
```

**Response `200 OK`** — no body.

---

## Async operation flow

Many operations (register, update, transfer, renew) may return `202 Accepted`. Follow this pattern:

```
POST /rr/domains/example.com          → 202
         ↓
GET  /rr/customers/{customer}/notifications?q=processIdentifier:example.com
         ↓
Check subjectStatus:
  OK / COMPLETED  → done ✅
  FAILED          → check reason field ❌
  PENDING / NEW   → wait and retry 🔄
         ↓
POST /rr/customers/{customer}/notifications/{id}/ack   → acknowledge
```

---

## billables product codes

Only needed for TLDs that require billing acknowledgment. If the API returns `BillableAcknowledgmentNeededException`, add the relevant billable to your request.

| TLD | product |
|-----|---------|
| `.com` | `DOMAIN_COM` |
| `.net` | `DOMAIN_NET` |
| `.org` | `DOMAIN_ORG` |
| `.io` | `DOMAIN_IO` |
| `.ai` | `DOMAIN_AI` |
| `.co` | `DOMAIN_CO` |

---

## Health check

```bash
curl https://domainmanagement.h.namekart.com/health
# → "Domain Management Service is healthy!"
```
