package com.sprint.Projections;

import com.sprint.Entities.Film;
import org.springframework.data.rest.core.config.Projection;
import com.sprint.Entities.FilmActor;

@Projection(name = "withFilm", types = { FilmActor.class })
public interface FilmActorWithFilmProjection {

    // Inlines the full Film object directly
    Film getFilm();

    // Inlines actor name fields
    ActorSummary getActor();

    interface ActorSummary {
        String getFirstName();
        String getLastName();
    }
}
