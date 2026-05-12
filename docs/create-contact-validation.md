# Create Contact — Field Validation Reference

**Endpoint:** `POST /v2/customers/{customer}/contacts/{handle}`  
**Content-Type:** `application/json`

---

## URL Path Fields

| Field | Type | Min Length | Max Length | Regex |
|-------|------|-----------|-----------|-------|
| `customer` | String | 3 | 40 | `[a-zA-Z0-9\-_@\.]+` |
| `handle` | String | 3 | 40 | `[a-zA-Z0-9\-_@\.]+` |

---

## Request Body Fields

| Field | Type | Required | Min | Max | Notes / Restrictions |
|-------|------|----------|-----|-----|----------------------|
| `brand` | String | Optional | 3 | 40 | Regex: `[a-zA-Z0-9\-_@\.]+`. Defaults to `"default"` |
| `organization` | String | Optional | 1 | 255 | Name of the organization |
| `name` | String | Required | 1 | 255 | Name of the person |
| `addressLine` | List\<String\> | Required | — | — | 1–3 items; each item: 1–255 chars |
| `postalCode` | String | Required | 1 | 16 | Postal code |
| `city` | String | Required | 1 | 255 | City |
| `state` | String | Optional | 1 | 255 | State / province |
| `country` | String | Required | 2 | 2 | ISO 3166-1 alpha-2 code (e.g. `IN`, `US`) |
| `email` | String | Required | 6 | 255 | Regex: `(?=.{1,64}@)[a-z0-9\-_]+(\.?[a-z0-9\-_]+)*@(?:[a-z0-9](?:[a-z0-9\-]{0,61}[a-z0-9])?\.)+[a-z]{2,6}` |
| `voice` | String | Required | 4 | 17 | E.164 format — e.g. `+31.384530759`. Regex: `\+[0-9]{1,3}\.[0-9]{1,14}` |
| `fax` | String | Optional | 4 | 17 | E.164 format — e.g. `+31.384530759`. Regex: `\+[0-9]{1,3}\.[0-9]{1,14}` |
| `disclosedFields` | List\<Enum\> | Optional | — | — | See enum values below |

---

## `disclosedFields` Enum Values

Fields to disclose publicly in RDAP. Country and state are always public.

| Value | Description |
|-------|-------------|
| `registryContactId` | Registry Contact ID (`contact:roid`) |
| `email` | Contact e-mail |
| `name` | Contact name |
| `organization` | Contact organization |
| `addressLine` | Contact address lines |
| `city` | Contact city or locality |
| `postalCode` | Contact postal code |
| `voice` | Contact phone number |
| `fax` | Contact fax number |
| `state` | Contact state |
| `country` | Contact country |

---

## Quick Validation Summary

```
customer / handle   → [a-zA-Z0-9\-_@\.]+ | len 3–40
name                → required | len 1–255
addressLine         → required | 1–3 items | each len 1–255
postalCode          → required | len 1–16
city                → required | len 1–255
country             → required | exactly 2 chars (ISO alpha-2)
email               → required | valid email regex | len 6–255
voice               → required | E.164: +[1-3 digits].[1-14 digits] | len 4–17
fax                 → optional | same format as voice
brand               → optional | [a-zA-Z0-9\-_@\.]+ | len 3–40
organization        → optional | len 1–255
state               → optional | len 1–255
disclosedFields     → optional | list of enum values above
```
