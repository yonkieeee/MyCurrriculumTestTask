package org.example.mycurrriculumtesttask.models.progress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.models.learningUnit.Subject;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "subject_progress")
public class SubjectProgress {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles", "username"})
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private boolean completed;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "subjectProgress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ModuleProgress> moduleProgressList;
}
