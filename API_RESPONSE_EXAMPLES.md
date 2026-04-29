# Language API - Response Examples

## Working Examples After Fixes

### ✅ Test 05: POST Valid Language (PASS)
```
REQUEST:
POST /languages HTTP/1.1
Content-Type: application/json

{
  "name": "Esperanto",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 201 CREATED
Location: http://localhost:8000/languages/9
Content-Type: application/json

{
  "languageId": 9,
  "name": "Esperanto",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}
```

---

### ❌ Test 06: POST Blank Name (PASS - Returns Error)
```
REQUEST:
POST /languages HTTP/1.1
Content-Type: application/json

{
  "name": "",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 400 BAD REQUEST
Content-Type: application/json

{
  "error": "Validation failed",
  "message": "Language name is required"
}
```

---

### ❌ Test 09: POST Name Too Long (PASS - Returns Error)
```
REQUEST:
POST /languages HTTP/1.1
Content-Type: application/json

{
  "name": "ThisLanguageNameIsWayTooLongForTheDatabase",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 400 BAD REQUEST
Content-Type: application/json

{
  "error": "Validation failed",
  "message": "Language name must be between 1 and 20 characters"
}
```

---

### ✅ Test 10: PUT Valid Language (PASS)
```
REQUEST:
PUT /languages/1 HTTP/1.1
Content-Type: application/json

{
  "name": "English",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 204 NO CONTENT
Location: http://localhost:8000/languages/1
```

---

### ❌ Test 12: PUT Blank Name (PASS - Returns Error)
```
REQUEST:
PUT /languages/1 HTTP/1.1
Content-Type: application/json

{
  "name": "",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 400 BAD REQUEST
Content-Type: application/json

{
  "error": "Validation failed",
  "message": "Language name is required"
}
```

---

### ❌ Test 13: PUT Missing LastUpdate (PASS - Returns Error)
```
REQUEST:
PUT /languages/1 HTTP/1.1
Content-Type: application/json

{
  "name": "Spanish"
}

RESPONSE: 400 BAD REQUEST
Content-Type: application/json

{
  "error": "Validation failed",
  "message": "Last update is required"
}
```

---

### ❌ Test 14: PUT Name Too Long (PASS - Returns Error)
```
REQUEST:
PUT /languages/1 HTTP/1.1
Content-Type: application/json

{
  "name": "ThisLanguageNameIsWayTooLongForTheDatabase",
  "lastUpdate": "2026-04-29T14:41:11.015Z"
}

RESPONSE: 400 BAD REQUEST
Content-Type: application/json

{
  "error": "Validation failed",
  "message": "Language name must be between 1 and 20 characters"
}
```

---

## Architecture Diagram

```
REQUEST
   ↓
Spring Web → LanguageEventHandler (validates) → ConstraintViolation? 
             ↓ (yes)
             throw ConstraintViolationException
                    ↓
              RestExceptionHandler
                    ↓
           Return 400 Bad Request + Error JSON
           
           (no)
             ↓
          Save to Database
                ↓
         Return 201/204 + Entity
```

## Key Components

1. **Language Entity** (Jakarta Validation Annotations)
   - `@NotBlank`: Cannot be empty or whitespace only
   - `@Size(min=1, max=20)`: Between 1-20 characters
   - `@NotNull`: Required field

2. **LanguageEventHandler** (Spring Data REST Events)
   - `@HandleBeforeCreate`: Validates before INSERT
   - `@HandleBeforeSave`: Validates before UPDATE
   - Uses Jakarta Validator to check constraints

3. **RestExceptionHandler** (Global Exception Handler)
   - Catches `ConstraintViolationException`
   - Returns HTTP 400 with formatted JSON error
   - Includes "error" and "message" fields

4. **LanguageRepository** (Spring Data REST)
   - `@Validated`: Enables method-level validation
   - Auto-creates `/languages` REST endpoint
   - Integrates with EventHandler for validation
