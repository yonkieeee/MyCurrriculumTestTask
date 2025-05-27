package org.example.mycurrriculumtesttask.services;

import org.example.mycurrriculumtesttask.models.learningUnit.Lesson;
import org.example.mycurrriculumtesttask.models.learningUnit.LearnModule;
import org.example.mycurrriculumtesttask.models.learningUnit.Subject;
import org.example.mycurrriculumtesttask.models.progress.LessonProgress;
import org.example.mycurrriculumtesttask.models.progress.ModuleProgress;
import org.example.mycurrriculumtesttask.models.progress.SubjectProgress;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.LessonRepository;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.ModuleRepository;
import org.example.mycurrriculumtesttask.repository.learningUnitRepository.SubjectRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.LessonProgressRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.ModuleProgressRepository;
import org.example.mycurrriculumtesttask.repository.progressRepository.SubjectProgressRepository;
import org.example.mycurrriculumtesttask.repository.userReposiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgresService {
    private final LessonProgressRepository lessonProgressRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final SubjectProgressRepository subjectProgressRepository;
    private final UserRepository userRepository;

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ProgresService(LessonProgressRepository lessonProgressRepository,
                          ModuleProgressRepository moduleProgressRepository,
                          SubjectProgressRepository subjectProgress, UserRepository userRepository,
                          LessonRepository lessonRepository,
                          ModuleRepository moduleRepository,
                          SubjectRepository subjectRepository) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.moduleProgressRepository = moduleProgressRepository;
        this.subjectProgressRepository = subjectProgress;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
        this.subjectRepository = subjectRepository;
    }

    public LessonProgress markLessonCompleted(Long lessonProgressId, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LessonProgress lessonProgress = lessonProgressRepository.findLessonProgressByUserAndId(user, lessonProgressId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson Progress Not Found"));

        lessonProgress.setCompleted(true);
        lessonProgress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(lessonProgress);

        return lessonProgress;
    }

    public void checkAndUpdateModuleProgress(LearnModule learnModule, UserEntity user) {
        ModuleProgress moduleProgress = moduleProgressRepository.findByLearnModuleAndUser(learnModule, user)
                .orElseThrow(() -> new RuntimeException("LearnModule Progress Not Found"));

        boolean allLessonsCompleted = learnModule.getLessons().stream().allMatch(lesson ->
               lessonProgressRepository.findLessonProgressByUserAndLesson(user, lesson)
                        .map(LessonProgress::isCompleted)
                        .orElse(false)
        );
        if (allLessonsCompleted && !moduleProgress.isCompleted()) {
            moduleProgress.setCompleted(true);
            moduleProgress.setCompletedAt(LocalDateTime.now());
            moduleProgressRepository.save(moduleProgress);
            checkAndUpdateSubjectProgress(moduleProgress.getLearnModule().getSubject(), user);
        }
    }

    public void checkAndUpdateSubjectProgress(Subject subject, UserEntity user) {
        SubjectProgress subjectProgress = (SubjectProgress) subjectProgressRepository.findBySubjectAndUser(subject, user)
                .orElseThrow(() -> new RuntimeException("LearnModule Progress Not Found"));

        boolean allModulesCompleted = subject.getLearnModules().stream().allMatch(module->
                moduleProgressRepository.findByLearnModuleAndUser(module , user)
                        .map(ModuleProgress::isCompleted)
                        .orElse(false)
        );
        if (allModulesCompleted && !subjectProgress.isCompleted()) {
            subjectProgress.setCompleted(true);
            subjectProgress.setCompletedAt(LocalDateTime.now());
            subjectProgressRepository.save(subjectProgress);
        }
    }

    public SubjectProgress startSubject(Long subjectId, UserEntity user) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (subjectProgressRepository.findBySubjectAndUser(subject, user).isPresent()) {
            throw new IllegalStateException("Subject already started");
        }

        SubjectProgress subjectProgress = new SubjectProgress();
        subjectProgress.setUser(user);
        subjectProgress.setSubject(subject);
        subjectProgress.setCompleted(false);

        subjectProgressRepository.save(subjectProgress);

        List<ModuleProgress> moduleProgressList = new ArrayList<>();

        for (LearnModule learnModule : subject.getLearnModules()) {
            ModuleProgress moduleProgress = new ModuleProgress();
            moduleProgress.setUser(user);
            moduleProgress.setLearnModule(learnModule);
            moduleProgress.setCompleted(false);
            moduleProgress.setSubjectProgress(subjectProgress);

            moduleProgressRepository.save(moduleProgress);

            List<LessonProgress> lessonProgressList = new ArrayList<>();

            for (Lesson lesson : learnModule.getLessons()) {
                LessonProgress lessonProgress = new LessonProgress();
                lessonProgress.setUser(user);
                lessonProgress.setLesson(lesson);
                lessonProgress.setCompleted(false);
                lessonProgress.setModuleProgress(moduleProgress);

                lessonProgressRepository.save(lessonProgress);
                lessonProgressList.add(lessonProgress);
            }

            moduleProgress.setLessonsProgress(lessonProgressList); // <-- Прив'язка уроків до модуля
            moduleProgressList.add(moduleProgress);
        }

        subjectProgress.setModuleProgressList(moduleProgressList); // <-- Прив'язка модулів до сабджекта

        return subjectProgress;
    }
}
