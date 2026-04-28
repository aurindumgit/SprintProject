package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Inventory;

@Projection(name = "inventoryProjection", types = Inventory.class)
public interface InventoryProjection {

    Long getInventoryId();

    FilmProjection getFilm();
    StoreProjection getStore();

    interface FilmProjection {
        Long getFilmId();
        String getTitle();
    }

    interface StoreProjection {
        Long getStoreId();
    }
}
