package com.sprint.Repository;

import com.sprint.Entities.Inventory;
import com.sprint.Projections.InventoryProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "inventories", path = "inventories", excerptProjection = InventoryProjection.class)
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Override
    @EntityGraph(attributePaths = {
        "film",
        "film.language",
        "store",
        "store.address",
        "store.address.city"
    })
    Page<Inventory> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
        "film",
        "store", 
    })
    Page<Inventory> findByStore_StoreId(Long storeId, Pageable pageable);
}
