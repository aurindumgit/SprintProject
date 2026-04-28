package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "actor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Long actorId;
    
    @Column(name = "first_name", length = 45)
    private String firstName;
    
    @Column(name = "last_name", length = 45)
    private String lastName;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;
    
    @JsonIgnore
    @OneToMany(mappedBy = "actor")
    private List<FilmActor> filmActors;
}
