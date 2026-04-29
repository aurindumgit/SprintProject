package com.sprint.Projections;



import com.sprint.Entities.Film;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "noProj", types = Film.class)
public interface FilmNoProjection {
}