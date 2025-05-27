package org.example.mycurrriculumtesttask.repository.learningUnitRepository;

import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
