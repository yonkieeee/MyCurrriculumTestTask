package org.example.mycurrriculumtesttask.repository.userReposiories;

import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String fullName);
    Boolean existsByUsername(String fullName);
}
