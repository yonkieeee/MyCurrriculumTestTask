package org.example.mycurrriculumtesttask.models.progress;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles", "username"})
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private boolean completed;

    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "module_progress_id")
    @JsonBackReference
    private ModuleProgress moduleProgress;
}
