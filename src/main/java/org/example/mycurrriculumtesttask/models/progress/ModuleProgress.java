package org.example.mycurrriculumtesttask.models.progress;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;
import org.example.mycurrriculumtesttask.models.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "modul_progress")
public class ModuleProgress {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles", "username"})
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private LearnModule learnModule;

    private boolean completed;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "moduleProgress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LessonProgress> lessonsProgress;

    @ManyToOne
    @JoinColumn(name = "subject_progress_id")
    @JsonBackReference
    private SubjectProgress subjectProgress;
}

