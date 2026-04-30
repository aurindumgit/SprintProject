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
