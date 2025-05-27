package org.example.mycurrriculumtesttask.controllers.lerningUnitControllers;

import org.example.mycurrriculumtesttask.models.progress.LessonProgress;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.repository.progressRepository.SubjectProgressRepository;
import org.example.mycurrriculumtesttask.repository.userReposiories.UserRepository;
import org.example.mycurrriculumtesttask.services.JwtUtil;
import org.example.mycurrriculumtesttask.services.ProgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private ProgresService progressService;

    @Autowired
    private SubjectProgressRepository subjectProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<?> createProgressForUser(@RequestParam(value = "subject-id") String subjectId,
                                                   @RequestParam(value = "user-id") String userId) {
        try{
            UserEntity user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(progressService.startSubject(Long.parseLong(subjectId), user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/complete-lesson")
    public ResponseEntity<?> completeLesson(@RequestParam(value = "lesson-id") String lessonProgressId
    , Authentication authentication) {
        try {
            String userId = String.valueOf(jwtUtil.getIdFromJwt(authentication.getDetails().toString()));
            LessonProgress updatedProgress = progressService.markLessonCompleted(Long.parseLong(lessonProgressId), Long.parseLong(userId));

            progressService.checkAndUpdateModuleProgress(
                    updatedProgress.getLesson().getLearnModule(),
                    updatedProgress.getUser()
            );

            return ResponseEntity.ok(updatedProgress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/get-progress")
    public ResponseEntity<?> getProgress(Authentication authentication) {
        try{
            String userId = String.valueOf(jwtUtil.getIdFromJwt(authentication.getDetails().toString()));

            UserEntity user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(subjectProgressRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Subject not found")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
