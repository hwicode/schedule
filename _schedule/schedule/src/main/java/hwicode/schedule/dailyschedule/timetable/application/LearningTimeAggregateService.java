package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetable.LearningTimeNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.LearningTimeFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LearningTimeAggregateService {

    private final LearningTimeFindRepository learningTimeFindRepository;
    private final SubjectOfTaskFindRepository subjectOfTaskFindRepository;
    private final SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository;

    @Transactional
    public boolean deleteSubject(Long learningTimeId) {
        LearningTime learningTime = findLearningTimeById(learningTimeId);

        return learningTime.deleteSubject();
    }

    @Transactional
    public String changeSubject(Long learningTimeId, String subject) {
        LearningTime learningTime = findLearningTimeById(learningTimeId);

        return learningTime.changeSubject(subject);
    }

    @Transactional
    public String changeSubjectOfTask(Long learningTimeId, Long subjectOfTaskId) {
        LearningTime learningTime = findLearningTimeById(learningTimeId);
        SubjectOfTask subjectOfTask = SubjectFindService.findSubjectOfTask(subjectOfTaskFindRepository, subjectOfTaskId);

        return learningTime.changeSubjectOfTask(subjectOfTask);
    }

    @Transactional
    public String changeSubjectOfSubTask(Long learningTimeId, Long subjectOfSubTaskId) {
        LearningTime learningTime = findLearningTimeById(learningTimeId);
        SubjectOfSubTask subjectOfSubTask = SubjectFindService.findSubjectOfSubTask(subjectOfSubTaskFindRepository, subjectOfSubTaskId);

        return learningTime.changeSubjectOfSubTask(subjectOfSubTask);
    }

    private LearningTime findLearningTimeById(Long learningTimeId) {
        return learningTimeFindRepository.findById(learningTimeId)
                .orElseThrow(LearningTimeNotFoundException::new);
    }
}
