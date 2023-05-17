package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
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
