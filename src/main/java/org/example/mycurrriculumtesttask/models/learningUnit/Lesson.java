package org.example.mycurrriculumtesttask.models.learningUnit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "lessons") @NoArgsConstructor
public class Lesson {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private LearnModule learnModule;
}
