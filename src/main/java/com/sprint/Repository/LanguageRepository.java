package com.sprint.Repository;

import com.sprint.Entities.Language;
import com.sprint.Projections.LanguageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "languages", path = "languages", excerptProjection = LanguageProjection.class)
public interface LanguageRepository extends JpaRepository<Language, Long> {
    
    @RestResource(exported = false)
    void deleteById(Long id);
    
    @RestResource(exported = false)
    void delete(Language language);
}
