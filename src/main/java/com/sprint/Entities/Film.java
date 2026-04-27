package com.sprint.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.math.BigDecimal;

@Entity
@Table(name = "film")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long filmId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Pattern(regexp = "\\d{4}", message = "Release year must be a 4 digit year")
    @Column(name = "release_year", length = 4)
    private String releaseYear;

    @NotNull(message = "Language is required")
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @NotNull(message = "Rental duration is required")
    @Min(value = 1, message = "Rental duration must be at least 1 day")
    @Max(value = 365, message = "Rental duration must not exceed 365 days")
    @Column(name = "rental_duration")
    private Integer rentalDuration;

    @NotNull(message = "Rental rate is required")
    @DecimalMin(value = "0.00", message = "Rental rate must be positive")
    @DecimalMax(value = "99.99", message = "Rental rate must not exceed 99.99")
    @Digits(integer = 2, fraction = 2, message = "Rental rate must have at most 2 decimal places")
    @Column(name = "rental_rate", precision = 4, scale = 2)
    private BigDecimal rentalRate;

    @Min(value = 1, message = "Length must be at least 1 minute")
    @Max(value = 9999, message = "Length must not exceed 9999 minutes")
    @Column(name = "length")
    private Integer length;

    @NotNull(message = "Replacement cost is required")
    @DecimalMin(value = "0.00", message = "Replacement cost must be positive")
    @DecimalMax(value = "999.99", message = "Replacement cost must not exceed 999.99")
    @Digits(integer = 3, fraction = 2, message = "Replacement cost must have at most 2 decimal places")
    @Column(name = "replacement_cost", precision = 5, scale = 2)
    private BigDecimal replacementCost;

    @Pattern(regexp = "G|PG|PG-13|R|NC-17", message = "Rating must be G, PG, PG-13, R or NC-17")
    @Column(name = "rating", length = 10)
    private String rating;

    @Size(max = 100, message = "Special features must not exceed 100 characters")
    @Column(name = "special_features", columnDefinition = "VARCHAR(100)")
    private String specialFeatures;

    @NotNull(message = "Last update is required")
    @Column(name = "last_update")
    private Timestamp lastUpdate;
}