package com.sprint.Repository;

import com.sprint.Entities.FilmActor;
import com.sprint.Entities.FilmActorId;
import com.sprint.Projections.FilmActorWithFilmProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RepositoryRestResource(collectionResourceRel = "filmActors", path = "filmActors", excerptProjection = FilmActorWithFilmProjection.class)
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {

    Page<FilmActor> findByActorActorId(@Param("actorId") Long actorId, Pageable pageable);

    @RestResource(path = "findByActorFirstName", rel = "findByActorFirstName")
    Page<FilmActor> findByActorFirstNameContainingIgnoreCase(@Param("firstName") String firstName, Pageable pageable);

    @RestResource(path = "findByActorName", rel = "findByActorName")
    Page<FilmActor> findByActorFirstNameIgnoreCaseAndActorLastNameIgnoreCase(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName, Pageable pageable);
}