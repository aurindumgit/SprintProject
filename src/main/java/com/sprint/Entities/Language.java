package com.sprint.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "language")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long languageId;

    @NotBlank(message = "Language name is required")
    @Size(min = 1, max = 20, message = "Language name must be between 1 and 20 characters")
    @Column(name = "name", length = 20, nullable = false)
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Last update is required")
    @Column(name = "last_update")
    @JsonProperty("lastUpdate")
    private Timestamp lastUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private List<Film> films;

    @JsonIgnore
    @OneToMany(mappedBy = "originalLanguage")
    private List<Film> originalLanguageFilms;
}