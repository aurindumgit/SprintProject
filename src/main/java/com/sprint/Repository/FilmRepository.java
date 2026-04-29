package com.sprint.Repository;

import com.sprint.Entities.Film;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "films-custom")
public interface FilmRepository extends JpaRepository<Film, Long> {

    // 🔍 Filter by release year
    Page<Film> findByReleaseYear(
            @Param("releaseYear") String releaseYear,
            Pageable pageable
    );

    // 🔍 Search by title
    Page<Film> findByTitleContainingIgnoreCase(
            @Param("title") String title,
            Pageable pageable
    );

    // 🔍 Combined filter + search (VERY IMPORTANT)
    Page<Film> findByTitleContainingIgnoreCaseAndReleaseYear(
            @Param("title") String title,
            @Param("releaseYear") String releaseYear,
            Pageable pageable
    );
}


