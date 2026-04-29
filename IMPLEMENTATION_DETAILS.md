# Implementation Details - Code Changes

## 1. LanguageRepository.java
**Location**: `src/main/java/com/sprint/Repository/LanguageRepository.java`

**Change**: Added `@Validated` annotation

```java
import org.springframework.validation.annotation.Validated;

@Validated  // ← ADDED THIS
@RepositoryRestResource(collectionResourceRel = "languages", path = "languages", excerptProjection = LanguageProjection.class)
public interface LanguageRepository extends JpaRepository<Language, Long> {
    // ... rest of code
}
```

**Why**: Enables method-level validation on repository operations using Jakarta Bean Validation.

---

## 2. LanguageEventHandler.java (NEW FILE)
**Location**: `src/main/java/com/sprint/config/LanguageEventHandler.java`

**Created with**:
```java
package com.sprint.config;

import com.sprint.Entities.Language;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

@Component
@RepositoryEventHandler(Language.class)
public class LanguageEventHandler {

    @Autowired
    private Validator validator;

    @HandleBeforeCreate
    public void validateBeforeCreate(Language language) {
        validateLanguage(language);
    }

    @HandleBeforeSave
    public void validateBeforeSave(Language language) {
        validateLanguage(language);
    }

    private void validateLanguage(Language language) {
        Set<ConstraintViolation<Language>> violations = validator.validate(language);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
```

**Why**: 
- Intercepts Spring Data REST events before entity persistence
- Manually runs Jakarta Validator on the entity
- Throws `ConstraintViolationException` if validation fails
- This exception is caught by RestExceptionHandler and returns 400 error

---

## 3. RestExceptionHandler.java
**Location**: `src/main/java/com/sprint/config/RestExceptionHandler.java`

**Changes**: Enhanced to handle multiple validation exceptions

```java
package com.sprint.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Validation failed");
        body.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Validation failed");
        body.put("message", ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Invalid input"));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Invalid request");
        body.put("message", "Malformed JSON request body");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
```

**Why**:
- `handleConstraintViolation`: Catches validation errors from LanguageEventHandler
- `handleMethodArgumentNotValid`: Catches Spring's built-in request body validation
- `handleHttpMessageNotReadable`: Catches JSON parsing errors
- All return proper HTTP 400 with JSON formatted error messages

---

## 4. Language Entity Reference
**Location**: `src/main/java/com/sprint/Entities/Language.java`

**Validation Annotations** (Already present, used by EventHandler):
```java
@NotBlank(message = "Language name is required")
@Size(min = 1, max = 20, message = "Language name must be between 1 and 20 characters")
@Column(name = "name", length = 20, nullable = false)
private String name;

@NotNull(message = "Last update is required")
@Column(name = "last_update")
private Timestamp lastUpdate;
```

---

## Validation Flow Summary

```
Client Request
    ↓
Spring DispatcherServlet
    ↓
RepositoryEntityController (Spring Data REST)
    ↓
LanguageRepository.save(language)
    ↓
EventListenerFactory discovers events
    ↓
LanguageEventHandler.@HandleBeforeCreate/@HandleBeforeSave
    ↓
validator.validate(language)
    ↓
   ┌─ Constraints satisfied?
   │
   ├─ YES → Continue to database save → Return 201/204
   │
   └─ NO  → Throw ConstraintViolationException
             ↓
             RestExceptionHandler.handleConstraintViolation()
             ↓
             Return 400 Bad Request + JSON Error
```

---

## Test Case Mapping

| Test | Request | Expected | Now Gets |
|------|---------|----------|----------|
| test05_post_validLanguage | POST with valid data | 201 + body | ✅ 201 + body |
| test06_post_blankLanguageName | POST name="" | 4xx | ✅ 400 |
| test07_post_missingLanguageName | POST no name | 4xx | ✅ 400 |
| test08_post_missingLastUpdate | POST no timestamp | 4xx | ✅ 400 |
| test09_post_languageNameTooLong | POST name too long | 4xx | ✅ 400 |
| test10_put_success | PUT with valid data | 204 | ✅ 204 |
| test11_put_notFound | PUT id 999999 | 4xx | ✅ 404 |
| test12_put_blankLanguageName | PUT name="" | 4xx | ✅ 400 |
| test13_put_missingLastUpdate | PUT no timestamp | 4xx | ✅ 400 |
| test14_put_languageNameTooLong | PUT name too long | 4xx | ✅ 400 |

---

## Dependencies Used

All dependencies are already in `pom.xml`:
- `spring-boot-starter-data-jpa` - Spring Data REST support
- `spring-boot-starter-validation` - Jakarta Bean Validation
- `spring-boot-starter-web` - Spring Web MVC

No new dependencies need to be added!
