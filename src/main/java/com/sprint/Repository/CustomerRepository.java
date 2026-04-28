package com.sprint.Repository;

import com.sprint.Entities.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;          
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Customer entity);

    @Override
    @RestResource(exported = false)
    <S extends Customer> S save(S entity);

    List<Customer> findByFirstNameAndLastName(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName);

    Optional<Customer> findByEmail(@Param("email") String email);

    Page<Customer> findByActive(@Param("active") Boolean active, Pageable pageable);
}
