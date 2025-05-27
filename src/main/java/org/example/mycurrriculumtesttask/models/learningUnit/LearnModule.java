    package org.example.mycurrriculumtesttask.models.learningUnit;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Table(name = "modules")
    @Data
    public class LearnModule {
        @Id
        @GeneratedValue
        private long id;

        private String name;
        private String description;

        @OneToMany(mappedBy = "learnModule", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Lesson> lessons = new ArrayList<>();

        @ManyToOne
        @JoinColumn(name = "subject_id")
        @JsonBackReference
        private Subject subject;
    }
