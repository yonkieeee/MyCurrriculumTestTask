package org.example.mycurrriculumtesttask.controllers.lerningUnitControllers;

import org.example.mycurrriculumtesttask.models.learningUnit.Subject;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.SubjectRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.SubjectProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    private SubjectProgressRepository subjectProgressRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllSubjects() {
        try{
            return ResponseEntity.ok(subjectRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("USER: " + auth.getName());
        System.out.println("ROLES: " + auth.getAuthorities());

        try {
            return ResponseEntity.ok(subjectRepository.save(subject));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
