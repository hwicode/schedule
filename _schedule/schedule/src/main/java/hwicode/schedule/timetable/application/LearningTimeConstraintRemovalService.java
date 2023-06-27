package hwicode.schedule.timetable.application;

import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LearningTimeConstraintRemovalService {

    private final LearningTimeRepository learningTimeRepository;

    @Transactional
    public void deleteSubjectOfTaskBelongingToLearningTime(Long subjectOfTaskId) {
        learningTimeRepository.deleteSubjectOfTaskBelongingToLearningTime(subjectOfTaskId);
    }

    @Transactional
    public void deleteSubjectOfSubTaskBelongingToLearningTime(Long subjectOfSubTaskId) {
        learningTimeRepository.deleteSubjectOfSubTaskBelongingToLearningTime(subjectOfSubTaskId);
    }
}
