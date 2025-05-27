package org.example.mycurrriculumtesttask.repository.progressRepository;

import org.example.mycurrriculumtesttask.models.progress.ModuleProgress;
import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;

import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {
    Optional<ModuleProgress> findByLearnModuleAndUser(LearnModule learnModule, UserEntity user);
}
