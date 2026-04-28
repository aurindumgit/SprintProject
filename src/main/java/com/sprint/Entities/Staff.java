package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;
    
    @Column(name = "first_name", length = 45)
    private String firstName;
    
    @Column(name = "last_name", length = 45)
    private String lastName;
    
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    @Column(name = "email", length = 50, nullable = true)
    private String email;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @Column(name = "active")
    private Boolean active;
    
    @Column(name = "username", length = 16)
    private String username;
    
    @Column(name = "password", length = 40, nullable = true)
    private String password;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    private List<Rental> rentals;

    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    private List<Payment> payments;
}
