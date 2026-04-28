package com.sprint.Repository;

import com.sprint.Entities.Film;
import com.sprint.Projections.FilmLanguageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "films", path = "films", excerptProjection = FilmLanguageProjection.class)
public interface FilmLanguageRepository extends JpaRepository<Film, Long> {
    
    List<Film> findByLanguage_LanguageId(Long languageId);
    
    @RestResource(exported = false)
    void deleteById(Long id);
    
    @RestResource(exported = false)
    void delete(Film film);
}
