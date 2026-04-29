package com.sprint.Projections;

import com.sprint.Entities.Film;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "page2", types = Film.class)
public interface FilmPage2Projection {

    Long getFilmId();
    String getTitle();
    String getDescription();
    String getReleaseYear();
    String getRating();
}