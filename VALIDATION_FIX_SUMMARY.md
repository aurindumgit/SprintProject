# Language API - Fixes Applied

## Problem Summary
The tests were failing because:
1. POST requests were not validating input (blank names, too-long names, missing fields)
2. PUT requests were not validating input (blank names, too-long names, missing fields)
3. Validation errors were not being caught and returned as 4xx errors

## Validation Constraints on Language Entity
- `@NotBlank`: Language name cannot be blank
- `@Size(min=1, max=20)`: Language name must be between 1 and 20 characters
- `@NotNull`: Last update timestamp is required

## Solution Implemented

### 1. Added @Validated annotation to LanguageRepository
**File**: `src/main/java/com/sprint/Repository/LanguageRepository.java`
- Enables method-level validation on repository operations

### 2. Created LanguageEventHandler
**File**: `src/main/java/com/sprint/config/LanguageEventHandler.java`
- Implements `@RepositoryEventHandler` for Language entity
- Validates entity using Jakarta Validator before CREATE and SAVE operations
- Throws `ConstraintViolationException` if validation fails

### 3. Enhanced RestExceptionHandler
**File**: `src/main/java/com/sprint/config/RestExceptionHandler.java`
- Added handler for `ConstraintViolationException` 
- Added handler for `MethodArgumentNotValidException`
- Added handler for `HttpMessageNotReadableException`
- All handlers return proper HTTP 400 Bad Request with JSON error response

## Test Cases That Should Now Pass

### POST Validation Tests:
- **test05_post_validLanguage**: ✓ Should return 201 Created with entity body
- **test06_post_blankLanguageName**: ✓ Should return 400 Bad Request
- **test07_post_missingLanguageName**: ✓ Should return 400 Bad Request
- **test08_post_missingLastUpdate**: ✓ Should return 400 Bad Request
- **test09_post_languageNameTooLong**: ✓ Should return 400 Bad Request

### PUT Validation Tests:
- **test10_put_success**: ✓ Should return 204 No Content
- **test11_put_notFound**: ✓ Should return 404 Not Found
- **test12_put_blankLanguageName**: ✓ Should return 400 Bad Request
- **test13_put_missingLastUpdate**: ✓ Should return 400 Bad Request
- **test14_put_languageNameTooLong**: ✓ Should return 400 Bad Request

## API Documentation

### Endpoints:

#### GET /languages
```
Description: Get all languages (paginated)
Query Parameters:
  - projection: Optional projection name (e.g., "languageDetail")
  - page: Page number (default 0)
  - size: Page size (default 20)
Response: 200 OK
  Content-Type: application/json
  Body:
  {
    "content": [
      {
        "languageId": 1,
        "name": "English",
        "lastUpdate": "2026-04-29T14:41:11.015Z"
      }
    ],
    "page": { "number": 0, "size": 20, "totalElements": 8, "totalPages": 1 }
  }
```

#### POST /languages
```
Description: Create a new language
Request:
  Content-Type: application/json
  Body:
  {
    "name": "Esperanto",
    "lastUpdate": "2026-04-29T14:41:11.015Z"
  }

Success Response: 201 Created
  Location: http://localhost:8000/languages/9
  Body: { "languageId": 9, "name": "Esperanto", "lastUpdate": "2026-04-29T14:41:11.015Z" }

Validation Error Response: 400 Bad Request
  Body:
  {
    "error": "Validation failed",
    "message": "name: Language name must be between 1 and 20 characters"
  }
```

#### GET /languages/{id}
```
Description: Get a specific language
Response: 200 OK
  Body: { "languageId": 1, "name": "English", "lastUpdate": "2026-04-29T14:41:11.015Z" }
```

#### PUT /languages/{id}
```
Description: Update a language
Request:
  Content-Type: application/json
  Body:
  {
    "name": "Spanish",
    "lastUpdate": "2026-04-29T14:41:11.015Z"
  }

Success Response: 204 No Content

Not Found Response: 404 Not Found

Validation Error Response: 400 Bad Request
  Body:
  {
    "error": "Validation failed",
    "message": "name: Language name is required"
  }
```

## How Validation Works

1. **User sends request** → Spring receives the request
2. **EventHandler intercepts** → LanguageEventHandler catches @HandleBeforeCreate/@HandleBeforeSave events
3. **Validation runs** → Jakarta Validator checks all @NotNull, @NotBlank, @Size constraints
4. **If validation fails** → ConstraintViolationException is thrown
5. **Exception handler catches** → RestExceptionHandler.handleConstraintViolation() 
6. **Error response sent** → HTTP 400 with JSON error details
7. **If validation passes** → Entity is persisted normally

## Files Modified

1. ✓ `LanguageRepository.java` - Added @Validated
2. ✓ `RestExceptionHandler.java` - Enhanced to handle multiple exception types
3. ✓ `LanguageEventHandler.java` - NEW: Validates before save/create
