package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(name = "name", length = 25)
    private String name;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<FilmCategory> filmCategories;
}
