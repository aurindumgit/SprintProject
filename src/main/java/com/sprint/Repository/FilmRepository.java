package com.sprint.Repository;

import com.sprint.Entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource
public interface FilmRepository extends JpaRepository<Film, Long> {

    @RestResource(path = "byReleaseYear")
    Page<Film> findByReleaseYear(
            @Param("releaseYear") String releaseYear,
            Pageable pageable
    );

    @RestResource(path = "byTitle")
    Page<Film> findByTitleContainingIgnoreCase(
            @Param("title") String title,
            Pageable pageable
    );

    @RestResource(path = "byTitleAndYear")
    Page<Film> findByTitleContainingIgnoreCaseAndReleaseYear(
            @Param("title") String title,
            @Param("releaseYear") String releaseYear,
            Pageable pageable
    );
}