package org.example.mycurrriculumtesttask.repository.learningUnitRepository;

import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;
import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;
import org.example.mycurrriculumtesttask.models.learningUnit.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<LearnModule, Long> {
}
