package com.sprint.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull(message = "Store is required")
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name must not exceed 45 characters")
    @Column(name = "first_name", length = 45)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name must not exceed 45 characters")
    @Column(name = "last_name", length = 45)
    private String lastName;

    @Email(message = "Email must be a valid email address")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Column(name = "email", length = 50, nullable = true)
    private String email;

    @NotNull(message = "Address is required")
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @NotNull(message = "Active status is required")
    @Column(name = "active")
    private Boolean active;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) this.createDate = LocalDate.now();
        if (this.lastUpdate == null) this.lastUpdate = new Timestamp(System.currentTimeMillis());
        if (this.active == null) this.active = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdate = new Timestamp(System.currentTimeMillis());
    }
}