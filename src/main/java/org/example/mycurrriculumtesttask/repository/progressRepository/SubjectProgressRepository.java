package org.example.mycurrriculumtesttask.repository.progressRepository;

import org.example.mycurrriculumtesttask.models.learningUnit.Subject;
import org.example.mycurrriculumtesttask.models.progress.SubjectProgress;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectProgressRepository extends JpaRepository<SubjectProgress, Long> {
    Optional<SubjectProgress> findBySubjectAndUser(Subject subject, UserEntity user);

    Optional<SubjectProgress> findByUser(UserEntity user);
}
