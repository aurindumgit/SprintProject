package com.sprint.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

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
    private String name;

    @NotNull(message = "Last update is required")
    @Column(name = "last_update")
    private Timestamp lastUpdate;
}