# RealtimeRegister API — Error Reference

All errors return `application/json`. The `type` field in every response maps to one of the error types below.

---

## Generic Errors

These can occur on any API call.

| Error Type | HTTP Status | RTR Message | Response Fields | User-Facing Message | Record Error |
|---|---|---|---|---|---|
| `AuthenticationError` | 401 | "Authentication failed" | `type`, `message` | "Service authentication failed. Please contact support." | `false` |
| `AuthorizationError` | 403 | "The user does not have permission to execute this request" | `type`, `message` | "You don't have permission to perform this action." | `True` |
| `ConstraintViolationException` | 400 | "The request parameters did not meet validation" | `type`, `message`, `violations[]` → `field`, `message`, `value` | "One or more fields are invalid: {field} — {message}" | `false` |
| `InsufficientCreditException` | 400 | "Insufficient credit available for action" | `type`, `message` | "An internal error occurred. Please try again or contact support" | `True` |
| `InternalSRSError` | 500 | Generic internal error | `type`, `message` | "An internal error occurred. Please try again or contact support." | `false` |
| `InvalidMessage` | 400 | "Request message could not be parsed, e.g. invalid JSON" | `type`, `message` | "The request could not be processed due to a formatting error." | `false` |
| `NoContractException` | 400 | "No contract active for product" | `type`, `message` | "No active contract for this product. Please contact support." | `false` |
| `ObjectDoesNotExist` | 400 | "A referenced object does not exist" | `type`, `message`, `objectType`, `identifier` | "Item does not exist." | `True` |
| `ObjectExists` | 400 | "An object with the same name already exists" | `type`, `message`, `objectType`, `identifier` | "Item already exists." | `True` |
| `ObjectStatusProhibitsOperation` | 400 | "Operation blocked by object's current status" | `type`, `message` | "This action cannot be performed because the resource is in a state that does not allow it." | `True` |
| `ProcessError` | 400 | "An unrecoverable error was encountered during the execution" | `type`, `message` | "The operation failed during processing. Please try again or contact support." | `True` |
| `ProviderConnectionError` | 502 | Provider connection failure | `type`, `message` | "An internal error occurred. Please retry in a few moments." | `false` |
| `ProviderUnavailable` | 503 | "The provider for the TLD is not available at this moment" | `type`, `message` | "The provider for the TLD is not available at this moment." | `false` |
| `TooManyRequests` | 429 | Rate limiting applied | `type`, `message` | "Too many requests. Please slow down and try again shortly." | `false` |
| `UnrecognizedPropertyException` | 400 | Unrecognized or invalid field in request | `type`, `message` | "The request contained an unrecognized field. Please check your input." | `false` |
| `UnsupportedTld` | 400 | "TLD is not supported" | `type`, `message` | "This TLD is not supported." | `false` |
| `ValidationError` | 400 | Request syntax valid but fails specific validation rules | `type`, `message` | "Validation failed: {message}" | `false` |

---

## Specific Errors

These apply only to certain operations.

| Error Type | HTTP Status | Applies To | RTR Message | Response Fields | User-Facing Message | Record Error |
|---|---|---|---|---|---|---|
| `AuthCodeInvalidError` | 400 | Domain transfer | "Invalid authorization code" | `type`, `message` | "The authorization code is invalid. Please verify and try again." | `false` |
| `BillableAcknowledgmentNeededException` | 400 | Transfer, Renew, Update, Restore | Acknowledgment required for billable action | `type`, `message` | "This action requires explicit cost acknowledgment. Please confirm the billable action and retry." | `True` |
| `ContactUpdateValidationError` | 400 | Update contact | "Contact update is not allowed by constraints laid down by one or more providers" | `type`, `message`, `errors` (Map\<String, String\> provider → reason) | "Contact update was rejected please try again or contact support." | `false` |
| `DnsConfigurationException` | 400 | DNS zone update | "The DNS records contain errors" | `type`, `message` | "The DNS configuration is invalid." | `false` |

---

## Error Response JSON Shapes

Errors with fields beyond the base `type` + `message` pair:

### `ConstraintViolationException`

```json
{
  "type": "ConstraintViolationException",
  "message": "The request parameters did not meet validation",
  "violations": [
    {
      "field": "email",
      "message": "must be a valid email address",
      "value": "bad-email"
    }
  ]
}
```

- `violations` is a list; there may be multiple entries, one per failing field.
- `value` is optional — it echoes back the submitted value that failed.

### `ObjectDoesNotExist` / `ObjectExists`

```json
{
  "type": "ObjectDoesNotExist",
  "message": "A referenced object does not exist",
  "objectType": "Contact",
  "identifier": "my-handle"
}
```

- `objectType`: e.g. `Contact`, `Domain`, `Host`
- `identifier`: the handle / domain name / host name that was not found (or already exists)

### `ContactUpdateValidationError`

```json
{
  "type": "ContactUpdateValidationError",
  "message": "Contact update is not allowed by constraints laid down by one or more providers using the contact.",
  "errors": {
    "provider-a": "Field 'name' cannot be changed after registration",
    "provider-b": "Registrant update requires FOA"
  }
}
```

- `errors` is a `Map<String, String>` keyed by provider name, value is the provider's rejection reason.

---

## How Errors Flow Through This Service

```
RTR API response (4xx/5xx)
        ↓
RealtimeRegisterErrorDecoder.java
  — parses JSON, extracts type + message
  — returns RrApiException(type, description, httpStatus)
        ↓
RealtimeRegisterController.java
  — catch block calls logError() → logs exception class + message
  — rethrows up to caller
```

The `type` field in the RTR JSON response maps 1-to-1 to the error types in this document.

Relevant files:
- `src/main/java/com/namekart/domainmanagement/RealtimeRegister/Feign/RealtimeRegisterErrorDecoder.java`
- `src/main/java/com/namekart/domainmanagement/RealtimeRegister/Feign/RrApiException.java`
- `src/main/java/com/namekart/domainmanagement/RealtimeRegister/Controller/RealtimeRegisterController.java`
