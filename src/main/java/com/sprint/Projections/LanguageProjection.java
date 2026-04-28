package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;
import com.sprint.Entities.Language;

@Projection(name = "languageDetail", types = Language.class)
public interface LanguageProjection {
    
    Long getLanguageId();
    
    String getName();
}
