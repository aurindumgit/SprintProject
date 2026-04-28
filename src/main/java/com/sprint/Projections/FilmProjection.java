package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;
import com.sprint.Entities.Film;

@Projection(name = "filmDetail", types = Film.class)
public interface FilmProjection {
    
    Long getFilmId();
    
    String getTitle();
    
    String getReleaseYear();
    
    String getRating();
}
