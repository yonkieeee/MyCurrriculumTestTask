package org.example.mycurrriculumtesttask.models.learningUnit;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @Table(name = "subjects")  @NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearnModule> learnModules = new ArrayList<>();

}
