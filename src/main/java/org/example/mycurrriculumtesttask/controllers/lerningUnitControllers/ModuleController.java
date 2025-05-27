package org.example.mycurrriculumtesttask.controllers.lerningUnitControllers;

import org.example.mycurrriculumtesttask.dto.ModuleRequestDTO;
import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;
import org.example.mycurrriculumtesttask.models.learningUnit.Subject;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.ModuleRepository;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.SubjectRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.ModuleProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    @Autowired
    private ModuleProgressRepository moduleProgressRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<?> getAllModules() {
        try{
            return ResponseEntity.ok(moduleRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<?> addModule(@RequestBody ModuleRequestDTO dto) {
        try{
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));

            LearnModule learnModule = new LearnModule();
            learnModule.setSubject(subject);
            learnModule.setDescription(dto.getDescription());
            learnModule.setName(dto.getName());

            return ResponseEntity.ok(moduleRepository.save(learnModule));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
