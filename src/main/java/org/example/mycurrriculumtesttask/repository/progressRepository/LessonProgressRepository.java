package org.example.mycurrriculumtesttask.repository.progressRepository;

import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;
import org.example.mycurrriculumtesttask.models.progress.LessonProgress;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findLessonProgressByUserAndLesson(UserEntity user, Lesson lesson);
    Optional<LessonProgress> findLessonProgressByUserAndId(UserEntity user, Long id);
}
