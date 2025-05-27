package org.example.mycurrriculumtesttask.controllers.userControllers;

import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.repository.userReposiories.UserRepository;
import org.example.mycurrriculumtesttask.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            if (authentication == null)
                return ResponseEntity.noContent().build();

            String username = authentication.getName();

            UserEntity user = userRepository.findByUsername(username).orElseThrow();

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
