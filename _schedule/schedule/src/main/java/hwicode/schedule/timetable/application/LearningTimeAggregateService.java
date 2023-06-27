package hwicode.schedule.timetable.application;

import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.infra.limited_repository.LearningTimeFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
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
        LearningTime learningTime = learningTimeFindRepository.findById(learningTimeId);

        return learningTime.deleteSubject();
    }

    @Transactional
    public String changeSubject(Long learningTimeId, String subject) {
        LearningTime learningTime = learningTimeFindRepository.findById(learningTimeId);

        return learningTime.changeSubject(subject);
    }

    @Transactional
    public String changeSubjectOfTask(Long learningTimeId, Long subjectOfTaskId) {
        LearningTime learningTime = learningTimeFindRepository.findById(learningTimeId);
        SubjectOfTask subjectOfTask = subjectOfTaskFindRepository.findById(subjectOfTaskId);

        return learningTime.changeSubjectOfTask(subjectOfTask);
    }

    @Transactional
    public String changeSubjectOfSubTask(Long learningTimeId, Long subjectOfSubTaskId) {
        LearningTime learningTime = learningTimeFindRepository.findById(learningTimeId);
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskFindRepository.findById(subjectOfSubTaskId);

        return learningTime.changeSubjectOfSubTask(subjectOfSubTask);
    }
}
