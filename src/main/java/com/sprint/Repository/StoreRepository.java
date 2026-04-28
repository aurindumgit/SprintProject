package com.sprint.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.sprint.Entities.Store;
import com.sprint.Projections.StoreProjection;

@RepositoryRestResource(excerptProjection = StoreProjection.class)
public interface StoreRepository extends JpaRepository<Store, Long> {
    @EntityGraph(attributePaths = {
            "address", 
            "address.city"
        }
    )
    Page<Store> findByAddress_City_CityIgnoreCase(String city, Pageable pageable);
}   
