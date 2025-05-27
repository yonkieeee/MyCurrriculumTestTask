package org.example.mycurrriculumtesttask.controllers.lerningUnitControllers;

import org.example.mycurrriculumtesttask.dto.LessonRequestDTO;
import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;
import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.LessonRepository;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.ModuleRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.LessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllLessons() {
        try{
           return ResponseEntity.ok(lessonRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addModule(@RequestBody LessonRequestDTO dto) {
        try{
            LearnModule module = moduleRepository.findById(dto.getModuletId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getModuletId()));

            Lesson lesson = new Lesson();
            lesson.setLearnModule(module);
            lesson.setDescription(dto.getDescription());
            lesson.setName(dto.getName());

            return ResponseEntity.ok(lessonRepository.save(lesson));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

}
