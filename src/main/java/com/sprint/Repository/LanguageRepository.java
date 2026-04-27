package com.sprint.Repository;

import com.sprint.Entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "languages", path = "languages")
public interface LanguageRepository extends JpaRepository<Language, Long> {
    
    List<Language> findByNameContainingIgnoreCase(String name);
    
    @RestResource(exported = false)
    void deleteById(Long id);
    
    @RestResource(exported = false)
    void delete(Language language);
}
