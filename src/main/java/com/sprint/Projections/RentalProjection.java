package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;
import com.sprint.Entities.Rental;
import java.sql.Timestamp;

// Nested projection — used inside CustomerProjection
// to show rentalDate and returnDate per rental record
@Projection(name = "rentalDetail", types = Rental.class)
public interface RentalProjection {

    Timestamp getRentalDate();

    Timestamp getReturnDate();
}
