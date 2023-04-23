package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.exception.domain.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetable.LearningTimeNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LearningTimeService {

    private final LearningTimeRepository learningTimeRepository;
    private final SubjectOfTaskRepository subjectOfTaskRepository;
    private final SubjectOfSubTaskRepository subjectOfSubTaskRepository;

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

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.findById(subjectOfTaskId)
                .orElseThrow(SubjectOfTaskNotFoundException::new);

        return learningTime.changeSubjectOfTask(subjectOfTask);
    }

    @Transactional
    public String changeSubjectOfSubTask(Long learningTimeId, Long subjectOfSubTaskId) {
        LearningTime learningTime = findLearningTimeById(learningTimeId);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.findById(subjectOfSubTaskId)
                .orElseThrow(SubjectOfSubTaskNotFoundException::new);

        return learningTime.changeSubjectOfSubTask(subjectOfSubTask);
    }

    private LearningTime findLearningTimeById(Long learningTimeId) {
        return learningTimeRepository.findById(learningTimeId)
                .orElseThrow(LearningTimeNotFoundException::new);
    }
}
