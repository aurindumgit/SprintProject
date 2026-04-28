package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;
import com.sprint.Entities.Customer;

import java.time.LocalDate;
import java.sql.Timestamp;
// import java.util.List;

@Projection(name = "customerDetail", types = Customer.class)
public interface CustomerProjection {

    // -------------------------------------------------------
    // Direct fields from Customer entity
    // -------------------------------------------------------

    String getFirstName();

    String getLastName();

    // nullable — N flag in schema
    String getEmail();

    Boolean getActive();

    LocalDate getCreateDate();

    Timestamp getLastUpdate();

    // -------------------------------------------------------
    // Nested projection for Address
    // Returns address, address2, phone flat inside response
    // instead of a HAL link
    // -------------------------------------------------------
    AddressProjection getAddress();

    // -------------------------------------------------------
    // Rentals — List because one customer can have many rentals
    // Each rental shows rentalDate and returnDate
    // Note: Rental entity must have a @ManyToOne back to Customer
    // and Customer entity must have @OneToMany List<Rental> rentals
    // for this to work
    // -------------------------------------------------------
    // List<RentalProjection> getRentals();
}
