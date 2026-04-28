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
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
@Validated
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Customer entity);

    List<Customer> findByFirstNameAndLastName(
            @Param("firstName") @NotBlank String firstName,
            @Param("lastName") @NotBlank String lastName);

    Optional<Customer> findByEmail(@Param("email") String email);

    Page<Customer> findByActive(@Param("active") Boolean active, Pageable pageable);

   List<Customer> findByAddress_City_CityIgnoreCase(String city);
}
