# Domain Management Service — Developer API Guide

**Base URL:** `https://dms.vps3.auctionhacker.com`  
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
| 6a | `POST` | `/rr/domains/{domainName}/lock` | Lock domain for transfer (adds `CLIENT_TRANSFER_PROHIBITED`) |
| 6b | `POST` | `/rr/domains/{domainName}/unlock` | Unlock domain for transfer (removes `CLIENT_TRANSFER_PROHIBITED`) |
| 7 | `POST` | `/rr/domains/{domainName}/renew` | Renew a domain |
| 8 | `POST` | `/rr/domains/{domainName}/transfer` | Transfer a domain in |
| 9 | `GET` | `/rr/domains/{domainName}/transfer/{processId}` | Get transfer status |
| 10 | `POST` | `/rr/domains/{domainName}/transfer/{processId}/{action}` | Approve or reject a transfer |
| 11 | `GET` | `/rr/domains/{domainName}/check` | Check domain availability & price |
| 12 | `POST` | `/rr/domains/{domainName}/restore` | Restore a domain from redemption |
| 13 | `POST` | `/rr/domains/{domainName}/push` | Push domain to another RTR registrar |
| 14 | `GET` | `/rr/domains/{domainName}/zone` | Get DNS zone records |
| 15 | `POST` | `/rr/domains/{domainName}/zone/update` | Update DNS zone records |
| 16 | `DELETE` | `/rr/domains/{domainName}` | Delete a domain |
| 17 | `GET` | `/rr/customers/{customer}/notifications` | List notifications |
| 18 | `POST` | `/rr/customers/{customer}/notifications/{notificationId}/ack` | Acknowledge a notification |
| 19 | `GET` | `/rr/customers/{customer}/contacts` | List contacts with filters & pagination |
| 20 | `GET` | `/rr/customers/{customer}/contacts/{handle}` | Get single contact |
| 21 | `POST` | `/rr/customers/{customer}/contacts/bulk-info` | Get details for multiple contacts at once |
| 22 | `POST` | `/rr/customers/{customer}/contacts/{handle}` | Create a contact |
| 23 | `POST` | `/rr/customers/{customer}/contacts/{handle}/update` | Update a contact |
| 24 | `DELETE` | `/rr/customers/{customer}/contacts/{handle}` | Delete a contact |
| 25 | `GET` | `/rr/processes/{processId}` | Get async process status |
| 26 | `GET` | `/rr/customers/{customer}/pricelist` | Get TLD price list |
| 27 | `GET` | `/rr/tlds/{tld}` | Get TLD info & capabilities |

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
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?limit=10&offset=0"

# Search by name
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?search=example&limit=25"

# Sort by expiry date, newest last
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?order=expiryDate&limit=50"

# Sort by expiry date, soonest first (expiring soon)
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?order=-expiryDate&limit=50"

# Specific fields only
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?fields=domainName,expiryDate,status&limit=100"

# Export all (no pagination)
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?export=true"

# Count only
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains?limit=0"
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
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com"
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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/bulk-info" \
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
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com/authcode"
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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com" \
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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.ai" \
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

Updates domain settings. Only fields you include are changed. To remove an optional field, specify it as empty (`""`, `[]`, or `false`).

**Request body fields (all optional):**

| Field | Type | Description | Restrictions |
|-------|------|-------------|--------------|
| `registrant` | String | New registrant contact handle | 3–40 chars, `[a-zA-Z0-9\-_@\.]+` |
| `privacyProtect` | Boolean | Enable/disable WHOIS privacy | |
| `authcode` | String | Set auth/EPP code. Pass `""` to generate a random one. | Max 64 chars |
| `autoRenew` | Boolean | Enable/disable auto-renewal | |
| `autoRenewPeriod` | Integer | Auto-renewal period in months | Registry-specific |
| `ns` | Array\<String\> | Replace nameservers. Pass `[]` to remove all. | Max 10 items, 4–255 chars each |
| `status` | Array\<Enum\> | Add/remove CLIENT_* statuses. IRTPC_TRANSFER_PROHIBITED can only be removed. | See status values below |
| `designatedAgent` | Enum | Acting as designated agent for: `NONE`, `OLD`, `NEW`, `BOTH` | Requires DESIGNATED_AGENT permission |
| `zone` | Object | DNS zone config | See zone fields below |
| `contacts` | Array | Replace ADMIN / TECH / BILLING contact handles | See contacts fields below |
| `keyData` | Array | DNSSEC key data. Pass `[]` to remove all. | See keyData fields below |
| `dsData` | Array | DNSSEC DS data — removal only, pass `[]` to remove all. New DS records not accepted. | See dsData fields below |
| `billables` | Array | Billing acknowledgment required by some operations | See billables fields below |

**`zone` object fields:**

| Field | Type | Description |
|-------|------|-------------|
| `service` | Enum | `BASIC` or `PREMIUM`. Defaults to `BASIC` for new zones. |
| `template` | String | Template name to apply (removes existing records/master) |
| `link` | Boolean | Link zone to template. Default: `true` |
| `master` | String | IP of hidden master (removes existing records/template) |
| `dnssec` | Boolean | Enable DNSSEC signing |
| `managed` | Boolean | Set `false` to convert a managed zone to unmanaged |

> To remove an existing zone, omit `zone` and set `"ns": []`.

**`contacts` array item fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `role` | Enum | **Required** | `ADMIN`, `BILLING`, or `TECH` |
| `handle` | String | **Required** | Contact handle (3–40 chars) |

**`keyData` array item fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `protocol` | Integer | **Required** | Must be `3` |
| `flags` | Integer | **Required** | `256` (ZSK) or `257` (KSK) |
| `algorithm` | Integer | **Required** | Algorithm ID: `3`, `5`, `6`, `7`, `8`, `10`, `12`, `13`, `14`, `15`, `16`, `17`, `23` |
| `publicKey` | String | **Required** | Base64-encoded public key |

**`dsData` array item fields (removal only):**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `keyTag` | Integer | **Required** | DNSSEC keyTag (0–65536) |
| `algorithm` | Integer | **Required** | Algorithm ID (same values as keyData) |
| `digestType` | Integer | **Required** | `1` (SHA-1), `2` (SHA-256), `3` (GOST), `4` (SHA-384) |
| `digest` | String | **Required** | Digest of the public key |

**`billables` array item fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `product` | String | **Required** | Product code (e.g. `DOMAIN_COM`) |
| `action` | Enum | **Required** | `CREATE`, `TRANSFER`, `RENEW`, `RESTORE`, `UPDATE`, `REGISTRANT_CHANGE`, `PRIVACY_PROTECT`, `REGISTRY_LOCK`, etc. |
| `quantity` | Integer | Optional | Max quantity. Default: `1` |

**`status` possible values:**

`CLIENT_HOLD`, `CLIENT_DELETE_PROHIBITED`, `CLIENT_UPDATE_PROHIBITED`, `CLIENT_RENEW_PROHIBITED`, `CLIENT_TRANSFER_PROHIBITED` — these are the only ones you can add/remove. All other statuses (`SERVER_*`, `PENDING_*`, etc.) must remain unchanged.

```bash
# Toggle auto-renew on
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "autoRenew": true }'

# Change nameservers
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "ns": ["ns1.namekart.com", "ns2.namekart.com"] }'

# Remove all nameservers
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "ns": [] }'

# Regenerate auth code
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "authcode": "" }'

# Set specific auth code
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "authcode": "myNewAuthCode123!" }'

# Lock domain (prevent transfers and updates)
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "status": ["CLIENT_TRANSFER_PROHIBITED", "CLIENT_UPDATE_PROHIBITED"] }'

# Enable privacy + change contacts
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "privacyProtect": true,
    "contacts": [
      { "role": "ADMIN",   "handle": "NK-REG-001" },
      { "role": "TECH",    "handle": "NK-REG-001" },
      { "role": "BILLING", "handle": "NK-REG-001" }
    ]
  }'

# Add DNSSEC key (algorithm 13 = ECDSA P-256 / SHA-256)
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "keyData": [
      { "protocol": 3, "flags": 257, "algorithm": 13, "publicKey": "base64encodedkey==" }
    ]
  }'

# Remove all DNSSEC keys
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{ "keyData": [] }'

# Apply DNS zone template
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/update" \
  -H "Content-Type: application/json" \
  -d '{
    "zone": { "service": "BASIC", "template": "my-template", "link": true }
  }'
```

**Response:** `200 OK` (updated immediately) or `202 Accepted` (async).

---

## 6a. Lock Domain

`POST /rr/domains/{domainName}/lock`

Locks a domain against outbound transfers by adding `CLIENT_TRANSFER_PROHIBITED` to its status list. Fetches the current domain status first, then calls the RTR update API with the merged status list. Idempotent — safe to call if the domain is already locked.

**Request body:** none

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/lock"
```

**Response `200 OK`** — locked immediately. **`202 Accepted`** — async (poll notifications).

**Domain status after lock:**
```json
{ "status": ["OK", "CLIENT_TRANSFER_PROHIBITED"] }
```

---

## 6b. Unlock Domain

`POST /rr/domains/{domainName}/unlock`

Unlocks a domain for outbound transfer by removing `CLIENT_TRANSFER_PROHIBITED` from its status list. Fetches the current domain status first, removes the status, then calls the RTR update API. Idempotent — safe to call if the domain is already unlocked.

**Request body:** none

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/unlock"
```

**Response `200 OK`** — unlocked immediately. **`202 Accepted`** — async (poll notifications).

**Domain status after unlock:**
```json
{ "status": ["OK"] }
```

> After unlocking, use endpoint **#4** (`/authcode`) to retrieve the EPP auth code needed to initiate a transfer at the receiving registrar.

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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/renew" \
  -H "Content-Type: application/json" \
  -d '{
    "period": "12",
    "billables": [
      { "product": "DOMAIN_COM", "action": "RENEW", "quantity": 1 }
    ]
  }'

# Renew for 24 months
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/renew" \
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
| `authcode` | String | Optional | EPP/auth code from current registrar |
| `privacyProtect` | Boolean | Optional | Enable WHOIS privacy after transfer |
| `period` | Integer | Optional | Transfer period in months (registry minimum if omitted) |
| `autoRenew` | Boolean | Optional | Auto-renewal after transfer. Default: `false` |
| `ns` | Array\<String\> | Optional | Nameservers to set after transfer. Omit to keep existing. |
| `transferContacts` | Array\<Enum\> | Optional | Preferred FOA contacts: `REGISTRANT`, `ADMIN` |
| `designatedAgent` | Enum | Optional | `NONE`, `OLD`, `NEW`, `BOTH` |
| `zone` | Object | Optional | DNS zone config to provision on transfer (see zone fields in §6) |
| `contacts` | Array | Optional | ADMIN / TECH / BILLING contact handles |
| `keyData` | Array | Optional | DNSSEC key data (see keyData fields in §6) |
| `billables` | Array | Optional | Required by some TLDs |

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/transfer" \
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

## 9. Get Transfer Status

`GET /rr/domains/{domainName}/transfer/{processId}`

Returns the current status of an inbound or outbound transfer, including a full status history log.

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com/transfer/98765"
```

**Response `200 OK`:**
```json
{
  "domainName": "example.com",
  "registrar": null,
  "status": "pending",
  "requestedDate": "2025-01-01T00:00:00Z",
  "actionDate": "2025-01-08T00:00:00Z",
  "expiryDate": null,
  "type": "IN",
  "processId": 98765,
  "log": [
    { "date": "2025-01-01T10:00:00Z", "status": "pendingfoa", "message": "Waiting for registrant approval" },
    { "date": "2025-01-01T11:00:00Z", "status": "pending",    "message": "Transfer approved by registrant" }
  ]
}
```

> `registrar` is only present on outgoing (`OUT`) transfers.

---

## 10. Approve or Reject Transfer

`POST /rr/domains/{domainName}/transfer/{processId}/{action}`

Approves or rejects a pending transfer. `{action}` must be `approve` or `reject`. No request body.

> Restricted to **gateway accounts** only.

```bash
# Approve
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/transfer/98765/approve"

# Reject
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/transfer/98765/reject"
```

**Response `200 OK`** — no body.

---

## 11. Check Domain Availability

`GET /rr/domains/{domainName}/check`

Checks whether a domain name is available for registration, and optionally returns pricing.

**Query parameters:**

| Param | Type | Description |
|-------|------|-------------|
| `renewPrice` | Boolean | If `true`, include the renewal price in the response |

```bash
# Basic availability check
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com/check"

# Include renewal price
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com/check?renewPrice=true"
```

**Response `200 OK` — available:**
```json
{
  "available": true,
  "premium": false,
  "price": null,
  "renewPrice": null,
  "currency": null,
  "reason": null
}
```

**Response `200 OK` — premium domain:**
```json
{
  "available": true,
  "premium": true,
  "price": 50000,
  "renewPrice": 50000,
  "currency": "USD",
  "reason": null
}
```

**Response `200 OK` — not available:**
```json
{
  "available": false,
  "reason": "Domain is already registered",
  "premium": false,
  "price": null,
  "renewPrice": null,
  "currency": null
}
```

> Prices are in **cents** — divide by 100 for display (e.g. `1099` → `$10.99`).

---

## 12. Restore Domain

`POST /rr/domains/{domainName}/restore`

Restores a domain that has entered the redemption grace period after expiry.

**Query parameters:**

| Param | Type | Description |
|-------|------|-------------|
| `quote` | Boolean | If `true`, returns a price quote without executing the restore |

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `reason` | String | **Required** | Reason for restoring the domain |
| `billables` | Array | Optional | Required if RTR returns `BillableAcknowledgmentNeededException` |

```bash
# Dry-run — get price quote
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/restore?quote=true" \
  -H "Content-Type: application/json" \
  -d '{ "reason": "Accidental deletion" }'

# Execute restore
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/restore" \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "Accidental deletion",
    "billables": [
      { "product": "DOMAIN_COM", "action": "RESTORE", "quantity": 1 }
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

## 13. Push Domain (Internal Transfer)

`POST /rr/domains/{domainName}/push`

Pushes a domain to another registrar within the RTR network.

**Request body fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `recipient` | String | **Required** | Target registrar handle in the RTR network |

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/push" \
  -H "Content-Type: application/json" \
  -d '{ "recipient": "target-registrar-handle" }'
```

**Response:** `200 OK` or `202 Accepted` — no body.

---

## 14. Get DNS Zone

`GET /rr/domains/{domainName}/zone`

Returns the full DNS zone for a domain, including all DNS records.

> Internally this fetches the domain to get its zone ID, then fetches the zone from `/v2/dns/zones/{id}`. The domain must have an active DNS zone provisioned.

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/domains/example.com/zone"
```

**Response `200 OK`:**
```json
{
  "id": 54321,
  "name": "example.com",
  "managed": true,
  "service": "BASIC",
  "dnssec": false,
  "hostMaster": "hostmaster@example.com",
  "refresh": 3600,
  "retry": 900,
  "expire": 1209600,
  "ttl": 3600,
  "records": [
    { "name": "example.com",     "type": "A",     "content": "1.2.3.4",      "ttl": 3600, "prio": null },
    { "name": "www.example.com", "type": "CNAME", "content": "example.com.", "ttl": 3600, "prio": null },
    { "name": "example.com",     "type": "MX",    "content": "mail.example.com.", "ttl": 3600, "prio": 10 }
  ],
  "defaultRecords": []
}
```

**Error — domain has no zone:**
```json
{ "error": "Domain example.com has no DNS zone" }
```

---

## 15. Update DNS Zone

`POST /rr/domains/{domainName}/zone/update`

Replaces DNS records for a domain's zone. All fields are optional — only the fields you send are changed.

> **Important:** The `records` field **replaces the entire record set** — send all records you want, not just the changed ones.

> Internally this fetches the domain to get its zone ID, then calls `/v2/dns/zones/{id}/update`.

**Request body fields (all optional):**

| Field | Type | Description |
|-------|------|-------------|
| `records` | Array | Full set of DNS records — replaces existing records |
| `ttl` | Integer | Default TTL in seconds (min 60, max 2419200, default 3600) |
| `hostMaster` | String | Hostmaster email address |
| `refresh` | Integer | Zone refresh interval in seconds (min 1) |
| `retry` | Integer | Zone retry interval in seconds (default 3600) |
| `expire` | Integer | Zone expiry in seconds (default 1209600) |
| `template` | String | Apply a named DNS template |
| `link` | Boolean | Link zone to template (default `true`) |
| `master` | String | IP of hidden master (removes records and template) |
| `ns` | Array\<String\> | Zone nameservers (max 10) |
| `dnssec` | Boolean | Enable DNSSEC signing |

**`records` array item fields:**

| Field | Type | Required | Description |
|-------|------|:--------:|-------------|
| `name` | String | **Required** | Full record name (e.g. `www.example.com`) |
| `type` | Enum | **Required** | `A`, `AAAA`, `CNAME`, `MX`, `TXT`, `NS`, `SRV`, `CAA`, `TLSA`, `ALIAS`, `SOA`, etc. |
| `content` | String | **Required** | Record value |
| `ttl` | Integer | Optional | Record-specific TTL (inherits zone default if omitted) |
| `prio` | Integer | Optional | Priority — required for `MX` and `SRV` records |

```bash
# Set A + CNAME + MX records (replaces all existing records)
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/zone/update" \
  -H "Content-Type: application/json" \
  -d '{
    "records": [
      { "name": "example.com",     "type": "A",     "content": "1.2.3.4",           "ttl": 3600 },
      { "name": "www.example.com", "type": "CNAME", "content": "example.com.",      "ttl": 3600 },
      { "name": "example.com",     "type": "MX",    "content": "mail.example.com.", "ttl": 3600, "prio": 10 }
    ]
  }'

# Update only the default TTL (no record changes)
curl -X POST "https://dms.vps3.auctionhacker.com/rr/domains/example.com/zone/update" \
  -H "Content-Type: application/json" \
  -d '{ "ttl": 300 }'
```

**Response `200 OK`** — no body.

---

## 16. Delete Domain

`DELETE /rr/domains/{domainName}`

Deletes a domain or places it into pending delete (depending on registry). No request body.

```bash
curl -X DELETE "https://dms.vps3.auctionhacker.com/rr/domains/example.com"
```

**Response `200 OK`** — deleted immediately:
```json
{ "domainName": "example.com" }
```

**Response `202 Accepted`** — placed in pending delete queue.

---

## 17. List Notifications

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
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/notifications?limit=25"

# Check status of a specific domain operation
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/notifications?q=processIdentifier:example.com&limit=10"
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

## 18. Acknowledge Notification

`POST /rr/customers/{customer}/notifications/{notificationId}/ack`

Marks a notification as acknowledged. Get the `id` from the List Notifications response.

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/notifications/12345678/ack"
```

**Response `200 OK`** — no body.

---

## 19. List Contacts

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
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts?limit=10"

# Search by name
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts?search=john&limit=25"

# Export all
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts?export=true"
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

## 20. Get Contact

`GET /rr/customers/{customer}/contacts/{handle}`

Returns full details for a single contact.

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/NK-REG-001"
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

## 21. Bulk Get Contacts

`POST /rr/customers/{customer}/contacts/bulk-info`

Fetches details for a list of contact handles. Each contact is fetched individually server-side and results are returned as an ordered array. Failures appear in-place — one failure does not abort the rest.

**Request body:** JSON array of contact handle strings.

```bash
curl -X POST "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/bulk-info" \
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

## 22. Create Contact

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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/NK-REG-001" \
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

## 23. Update Contact

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
curl -X POST "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/NK-REG-001/update" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newemail@example.com",
    "voice": "+1.2125559999"
  }'

# Update address
curl -X POST "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/NK-REG-001/update" \
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

## 24. Delete Contact

`DELETE /rr/customers/{customer}/contacts/{handle}`

Deletes a contact. The contact must not be in use on any active domain.

```bash
curl -X DELETE "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/contacts/NK-REG-001"
```

**Response `200 OK`** — no body.

---

## 25. Get Process Status

`GET /rr/processes/{processId}`

Returns the status and details of any async operation (registration, transfer, renew, update, restore). Get the `processId` from the original operation's response.

**Query parameters:**

| Param | Type | Description |
|-------|------|-------------|
| `fields` | String | Comma-separated list of fields to return (optimization) |

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/processes/98765"
```

**Response `200 OK`:**
```json
{
  "id": 98765,
  "user": "NK-12345/admin",
  "customer": "NK-12345",
  "status": "COMPLETED",
  "statusDetail": null,
  "createdDate": "2025-01-01T10:00:00Z",
  "updatedDate": "2025-01-01T10:05:00Z",
  "startedDate": "2025-01-01T10:00:01Z",
  "type": "domain",
  "identifier": "example.com",
  "action": "create",
  "command": {},
  "error": null,
  "resumeTypes": [],
  "billables": [
    { "product": "DOMAIN_COM", "action": "CREATE", "quantity": 1 }
  ]
}
```

**`status` values:**

| Value | Meaning |
|-------|---------|
| `NEW` | Just created, not yet started |
| `VALIDATED` | Validated, waiting to run |
| `RUNNING` | In progress |
| `COMPLETED` | Finished successfully |
| `FAILED` | Failed — check `error` field |
| `IN_DOUBT` | Completed but outcome uncertain |
| `CANCELLED` | Cancelled |
| `INVALID` | Invalid input |
| `SCHEDULED` | Scheduled for future execution |
| `SUSPENDED` | Suspended, waiting for manual action |

---

## 26. Get Price List

`GET /rr/customers/{customer}/pricelist`

Returns registration, renewal, and transfer prices for all supported TLDs.

**Query parameters:**

| Param | Type | Description |
|-------|------|-------------|
| `currency` | String | Currency code. E.g. `USD`. Defaults to account currency. |

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/customers/NK-12345/pricelist?currency=USD"
```

**Response `200 OK`:**
```json
[
  { "product": "DOMAIN_COM", "action": "CREATE", "price": 1099, "currency": "USD" },
  { "product": "DOMAIN_COM", "action": "RENEW",  "price": 1099, "currency": "USD" },
  { "product": "DOMAIN_NET", "action": "CREATE", "price": 1199, "currency": "USD" },
  { "product": "DOMAIN_AI",  "action": "CREATE", "price": 8900, "currency": "USD" }
]
```

> Prices are in **cents** — divide by 100 for display.

---

## 27. Get TLD Info

`GET /rr/tlds/{tld}`

Returns detailed info for a TLD: minimum/maximum registration period, supported operations, required contact fields, available billable products, and registry-specific constraints.

```bash
curl -X GET "https://dms.vps3.auctionhacker.com/rr/tlds/com"
curl -X GET "https://dms.vps3.auctionhacker.com/rr/tlds/ai"
```

**Response `200 OK`** — returns a complex nested object from RTR directly. Key top-level fields include `tld`, `registryProducts`, `periods`, `operations`, and `requiredContactFields`.

---

## Async operation flow

Many operations (register, update, transfer, renew, restore) may return `202 Accepted`. Two ways to track:

**Option A — Notifications (recommended):**
```
POST /rr/domains/example.com          → 202 { processId: 98765 }
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

**Option B — Process status (direct):**
```
POST /rr/domains/example.com          → 202 { processId: 98765 }
         ↓
GET  /rr/processes/98765
         ↓
Check status:
  COMPLETED  → done ✅
  FAILED     → check error field ❌
  RUNNING    → wait and retry 🔄
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
curl https://dms.vps3.auctionhacker.com/health
# → "Domain Management Service is healthy!"
```
